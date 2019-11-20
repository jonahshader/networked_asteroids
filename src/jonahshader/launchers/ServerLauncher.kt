package jonahshader.launchers

import jonahshader.networking.GameServer
import javax.swing.JFrame
import javax.swing.JOptionPane

fun main() {
    val gameServer = GameServer()
    gameServer.start(JOptionPane.showInputDialog(JFrame(), "Enter Port:").toInt())
}

