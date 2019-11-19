package jonahshader.networking

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import jonahshader.Engine
import jonahshader.gameparts.Player
import jonahshader.client.MainApp
import jonahshader.gameparts.Asteroid
import jonahshader.gameparts.Projectile
import jonahshader.networking.packets.*
import java.lang.Math.random
import javax.swing.JFrame
import javax.swing.JOptionPane

class GameServer {
    val server = Server()
    val playersConnected: Int
    get() = server.connections.size

    var score = 0

    fun start() {
        registerPackets(server.kryo)
        server.start()
        server.bind(JOptionPane.showInputDialog(JFrame(), "Enter Port:").toInt())

        server.addListener(object : Listener() {
            override fun received(connection: Connection?, `object`: Any?) {
                when (`object`) {
                    is NewConnection -> {
                        // create new player
                        val newX = MainApp.screenWidth * random().toFloat()
                        val newY = MainApp.screenHeight * random().toFloat()

                        // send new player to clients. only the caller gets ownership.
                        val playerInfoNotForClient = AddPlayer(newX, newY, 0f, 0f, 0f, Engine.nextId, false, false, true)
                        val playerInfoForClient = AddPlayer(newX, newY, 0f, 0f, 0f, Engine.nextId, false, true, true)
                        server.sendToAllExceptTCP(connection!!.id, playerInfoNotForClient)
                        connection.sendTCP(playerInfoForClient)

                        // add new player to engine
                        createNewPlayer(playerInfoNotForClient)

                        // send all players
                        for (player in Engine.players) {
                            // if this player isn't the new one we just created for the new client,
                            if (player.id != playerInfoNotForClient.id) {
                                // send it
                                connection.sendTCP(makeAddPlayerFromPlayer(player))
                            }
                        }

                        // send all asteroids
                        for (asteroid in Engine.asteroids) {
                            connection.sendTCP(makeAddAsteroidFromAsteroid(asteroid))
                        }

                        // send all projectiles
                        for (projectile in Engine.projectiles) {
                            connection.sendTCP(AddProjectile(projectile.x, projectile.y, projectile.baseXSpeed, projectile.baseYSpeed, projectile.direction, projectile.id))
                        }
                    }

                    is UpdatePlayer -> {
                        Engine.updatePlayer(`object`)
                        server.sendToAllTCP(`object`)
                    }

                    is RequestCreateProjectile -> {
                        // send ProjectileIDReply to the client that sent this packet
                        val id = Engine.nextId
                        connection!!.sendTCP(ProjectileIDReply(id, `object`.localId))
                        server.sendToAllExceptTCP(connection.id,
                            AddProjectile(`object`.x, `object`.y, `object`.baseXSpeed, `object`.baseYSpeed, `object`.direction, id)
                        )
                        Engine.addObject(Projectile(`object`.x, `object`.y, `object`.baseXSpeed, `object`.baseYSpeed, `object`.direction, id))
                    }

                    is RequestRemoveProjectile -> {
                        if (Engine.idToObject[`object`.id] is Projectile) {
                            Engine.queueRemoveObject(`object`.id)
                            server.sendToAllTCP(RemoveObject(`object`.id))
                        }
                    }

                    is ReportCollision -> {
                        val asteroidCollided = Engine.idToObject[`object`.asteroidId]
                        val ownerPlayer = Engine.idToObject[`object`.playerId]
                        val projectile = Engine.idToObject[`object`.projectileId]

                        if (asteroidCollided is Asteroid && ownerPlayer is Player && projectile is Projectile) {
                            // for now, just remove asteroid and projectile
                            Engine.queueRemoveObject(asteroidCollided.id)
                            Engine.queueRemoveObject(projectile.id)
                            server.sendToAllTCP(RemoveObject(asteroidCollided.id))
                            server.sendToAllTCP(RemoveObject(projectile.id))

                            score++ // increment score by one, for now
                            server.sendToAllTCP(UpdateScore(score))
                        }
                    }
                    // TODO: this code might be used if pvp is added in the future
//                    is PlayerLife -> {
//                        val player = Engine.idToObject[`object`.id]
//                        if (player != null) {
//                            if (player is Player) {
//                                player.alive = `object`.alive
//                            }
//                        }
//                        server.sendToAllExceptTCP(connection!!.id, `object`)
//                    }
                }
            }
        })
    }

    private fun createNewPlayer(playerInfo: AddPlayer) : Player {
        val newPlayer = Player(playerInfo)
        Engine.addObject(newPlayer)
        return newPlayer
    }
}