package jonahshader.client

import processing.core.PApplet
import javax.swing.JFrame
import javax.swing.JOptionPane

class MainMenu : Screen {
    var time = 0f

//    init {
//        // Immediately ask user for ip settings
//        val ipString = JOptionPane.showInputDialog(JFrame(), "Enter IP (without port):")
//        val portString = JOptionPane.showInputDialog(JFrame(), "Enter Port:")
//    }

    override fun draw(dt: Float, graphics: PApplet) {
        time += dt
        graphics.background(0)

    }

    override fun keyPressed(graphics: PApplet) {

    }

    override fun keyReleased(graphics: PApplet) {

    }

    override fun mousePressed(graphics: PApplet) {

    }

    override fun mouseReleased(graphics: PApplet) {

    }
}