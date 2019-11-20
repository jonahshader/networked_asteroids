package jonahshader.launchers

import jonahshader.client.MainApp
import processing.core.PApplet

fun main() {
    val app = MainApp("0.0.0.0", 12345)
    PApplet.runSketch(arrayOf("MainApp"), app)
}