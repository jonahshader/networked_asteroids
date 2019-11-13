package jonahshader.networking

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import jonahshader.Asteroid
import jonahshader.Engine
import jonahshader.NetworkedObject
import jonahshader.Player
import jonahshader.client.MainApp
import jonahshader.networking.packets.AddPlayer
import jonahshader.networking.packets.NewConnection
import jonahshader.networking.packets.UpdatePlayer
import java.lang.Math.PI
import java.lang.Math.random
import javax.swing.JFrame
import javax.swing.JOptionPane

class GameServer {
    val server = Server()
    val playersConnected: Int
    get() = server.connections.size

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
                        val newSpeed = 0f
                        val newDirection = 0f

                        // send new player to clients. only the caller gets ownership.
                        server.sendToAllExceptTCP(connection!!.id, AddPlayer(newX, newY, newSpeed, newDirection, Engine.nextId, false))
                        connection.sendTCP(AddPlayer(newX, newY, newSpeed, newDirection, Engine.nextId, true))

                        val newPlayerInfo = AddPlayer(newX, newY, newSpeed, newDirection, Engine.nextId, false)
                        // add new player to engine
                        createNewPlayer(newPlayerInfo)

                        // send all players
                        for (player in Engine.players) {
                            // if this player isn't the new one we just created for the new client,
                            if (player.id != newPlayerInfo.id) {
                                // send it
                                connection.sendTCP(makeAddPlayerFromPlayer(player))
                            }
                        }

                        // send all asteroids
                        for (asteroid in Engine.asteroids) {
                            connection.sendTCP(makeAddAsteroidFromAsteroid(asteroid))
                        }
                    }

                    is UpdatePlayer -> {
                        Engine.replaceObject(Player(`object`), false)
                        server.sendToAllTCP(`object`)
                    }
                }
            }
        })
    }

    private fun createNewPlayer(playerInfo: AddPlayer) : Player {
        val newPlayer = Player(playerInfo)
        Engine.addObject(newPlayer, false)
        return newPlayer
    }
}