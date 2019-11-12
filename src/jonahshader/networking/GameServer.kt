package jonahshader.networking

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import jonahshader.Engine
import jonahshader.Player
import javax.swing.JFrame
import javax.swing.JOptionPane

class GameServer {
    private val server = Server()

    fun start() {
        server.start()
        server.bind(JOptionPane.showInputDialog(JFrame(), "Enter Port:").toInt())

        server.addListener(object : Listener() {
            override fun received(connection: Connection?, `object`: Any?) {
                when (`object`) {
                    is NewConnection -> {
                        // send client everything, and a new player
                    }
                }
            }
        })
    }

    /*
    TODO: where i left off:
    i need to modify engine to keep track of the highest object ID so that GameServer can create new
    objects with the highest ID and pass them to engine and to clients.

    OR:

    engine creates the objects upon server request and can give them directly to the server.
     */
    private fun createNewPlayer() : Player = Player()
}