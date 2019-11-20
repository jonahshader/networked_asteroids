package jonahshader.networking

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import jonahshader.Engine
import jonahshader.ServerGameLogic
import jonahshader.gameparts.Player
import jonahshader.client.MainApp
import jonahshader.gameparts.Asteroid
import jonahshader.gameparts.Projectile
import jonahshader.networking.packets.*
import java.lang.Math.random

class GameServer {
    val server = Server()
    val playersConnected: Int
    get() = server.connections.size

    private val logic = ServerGameLogic(this)

    var score = 0

    fun start(port: Int) {
        registerPackets(server.kryo)
        println("Starting server on port $port")
        server.start()
        server.bind(port)

        server.addListener(object : Listener() {
            override fun received(connection: Connection?, `object`: Any?) {
                when (`object`) {
                    is NewConnection -> {
                        connection?.let { transferNewPlayerAndEverythingElse(it) }
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


        logic.start()
    }

    private fun createNewPlayer(playerInfo: AddPlayer) : Player {
        val newPlayer = Player(playerInfo)
        Engine.addObject(newPlayer)
        return newPlayer
    }

    fun reset() {
        score = 0
        server.sendToAllTCP(ResetGame())
        Thread.sleep(100) // sleep a little to make sure clients handle the reset command properly
        for (c in server.connections) {
            if (c != null)
                transferNewPlayerAndEverythingElse(c)
        }
    }

    fun resetNoResendData() {
        score = 0
    }

    private fun transferNewPlayerAndEverythingElse(c: Connection) {
        // create new player
        val newX = MainApp.screenWidth * random().toFloat()
        val newY = MainApp.screenHeight * random().toFloat()

        // send new player to clients. only the caller gets ownership.
        val playerInfoNotForClient = AddPlayer(newX, newY, 0f, 0f, 0f, Engine.nextId, false, false, true)
        val playerInfoForClient = AddPlayer(newX, newY, 0f, 0f, 0f, Engine.nextId, false, true, true)
        server.sendToAllExceptTCP(c.id, playerInfoNotForClient)
        c.sendTCP(playerInfoForClient)

        // add new player to engine
        createNewPlayer(playerInfoNotForClient)

        // send all players
        for (player in Engine.players) {
            // if this player isn't the new one we just created for the new client,
            if (player.id != playerInfoNotForClient.id) {
                // send it
                c.sendTCP(makeAddPlayerFromPlayer(player))
            }
        }

        // send all asteroids
        for (asteroid in Engine.asteroids) {
            c.sendTCP(makeAddAsteroidFromAsteroid(asteroid))
        }

        // send all projectiles
        for (projectile in Engine.projectiles) {
            c.sendTCP(AddProjectile(projectile.x, projectile.y, projectile.baseXSpeed, projectile.baseYSpeed, projectile.direction, projectile.id))
        }

        c.sendTCP(TimeTillStart(logic.timeTillGameStart))
    }
}