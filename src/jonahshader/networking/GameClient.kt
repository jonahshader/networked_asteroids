package jonahshader.networking

import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import jonahshader.gameparts.Asteroid
import jonahshader.Engine
import jonahshader.gameparts.Player
import jonahshader.client.Game
import jonahshader.gameparts.Projectile
import jonahshader.networking.packets.*
import javax.swing.JFrame
import javax.swing.JOptionPane
import kotlin.concurrent.thread

class GameClient(val game: Game) {
    private val updateInterval = ((1/30f) * 1000).toLong() // loop time for client to server update loop

    val client = Client() // kryonet client network component

    fun start(ip: String, port: Int) {
        registerPackets(client.kryo)
        client.start()
        client.connect(6000, ip, port)

        client.addListener(object : Listener() {
            override fun received(connection: Connection?, `object`: Any?) {
                when (`object`) {
                    is AddPlayer -> {
                        // register successful, create player
                        val newPlayer = Player(`object`)
                        if (`object`.owner) {
                            game.clientPlayer = newPlayer
                        }
                        Engine.queueAddObject(newPlayer)
                    }
                    is UpdatePlayer -> Engine.updatePlayer(`object`)
                    is AddAsteroid -> Engine.queueAddObject(Asteroid(`object`))
                    is RemoveObject -> Engine.queueRemoveObject(`object`.id)
                    is ProjectileIDReply -> {
                        for (localp in game.clientProjectiles) {
                            if (localp.localId == `object`.localId)
                                localp.id = `object`.id
                        }
                    }
                    is AddProjectile -> Engine.queueAddObject(Projectile(`object`.x, `object`.y, `object`.baseXSpeed, `object`.baseYSpeed,`object`.direction, `object`.id))
                    is UpdateScore -> game.score = `object`.score
                    //TODO: idk if this code needs to be here lol. gotta think about this a little more.
//                    is PlayerLife -> {
//                        val id = `object`.id
//                        if (game.clientPlayer != null) {
//                            if (game.clientPlayer!!.id == id) {
//                                // update client player
//                                game.clientPlayer!!.alive = `object`.alive
//                            }
//                        }
//                        val player = Engine.idToObject[id]
//                        if (player != null) {
//                            if (player is Player) {
//                                player.alive = `object`.alive
//                            }
//                        }
//                    }
                    // ResetGame is called when a game match has ended, and the server is preparing to start another.
                    is ResetGame -> {
                        Engine.reset()
                        game.clientPlayer = null
                        game.score = 0
                    }
                }
            }
        })

        client.sendTCP(NewConnection())

        // start sending updates back to the server
        updateLoop()
    }

    /**
     * this function has an infinite loop that constantly sends updates to the server
     */
    private fun updateLoop() {
        thread {
            while (true) {
                game.clientPlayer?.let{client.sendTCP(makeUpdatePlayerFromPlayer(game.clientPlayer!!))}
                Thread.sleep(updateInterval)
            }
        }
    }
}