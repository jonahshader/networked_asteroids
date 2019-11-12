package jonahshader.networking

import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import jonahshader.Engine
import javax.swing.JFrame
import javax.swing.JOptionPane

class GameClient(val engine: Engine) {
    private val client = Client()

    init {
        client.start()
        client.connect(5000, JOptionPane.showInputDialog(JFrame(), "Enter IP:"), JOptionPane.showInputDialog(JFrame(), "Enter Port:").toInt())

        client.addListener(object : Listener() {
            override fun received(connection: Connection?, `object`: Any?) {
                if (`object` is AddPlayer) {
                    // register successful, create player

                }
            }
        })

        client.sendTCP(NewConnection())

    }
}