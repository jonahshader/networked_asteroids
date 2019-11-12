package jonahshader.networking

import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import jonahshader.Asteroid
import jonahshader.Engine
import jonahshader.Player
import jonahshader.client.Game
import jonahshader.networking.packets.*
import javax.swing.JFrame
import javax.swing.JOptionPane
import kotlin.concurrent.thread

class GameClient(val game: Game) {
    private val updateInterval = ((1/30f) * 1000).toLong()

    private val client = Client()

    fun start() {
        registerPackets(client.kryo)
        client.start()
        client.connect(5000, JOptionPane.showInputDialog(JFrame(), "Enter IP:"), JOptionPane.showInputDialog(JFrame(), "Enter Port:").toInt())

        client.addListener(object : Listener() {
            override fun received(connection: Connection?, `object`: Any?) {
                when (`object`) {
                    is AddPlayer -> {
                        // register successful, create player
                        val newPlayer = Player(`object`)
                        if (`object`.owner) {
                            game.clientPlayer = newPlayer
                        }
                        Engine.addObject(newPlayer, false)
                    }
                    is UpdatePlayer -> {
                        val player = Player(`object`)
                        if (`object`.id == game.clientPlayer?.id ?: -2) {
                            game.clientPlayer = player
                        }
                        Engine.replaceObject(player, false)
                    }
                    is AddAsteroid -> Engine.addObject(Asteroid(`object`), false)
                    is RemoveObject -> Engine.removeObject(`object`.id, false)
                }
            }
        })

        client.sendTCP(NewConnection())

        thread {
            updateLoop()
        }
    }

    private fun updateLoop() {
        while (true) {
            game.clientPlayer?.let{client.sendTCP(makeUpdatePlayerFromPlayer(game.clientPlayer!!))}
            Thread.sleep(updateInterval)
        }
    }
}