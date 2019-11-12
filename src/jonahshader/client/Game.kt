package jonahshader.client

import jonahshader.Player
import processing.core.PApplet
import kotlin.math.cos

class Game {
    val clientPlayer = Player(320f, 240f, 0f, 0f, -1)

    var wPressed = false
    var aPressed = false
    var sPressed = false
    var dPressed = false

    fun keyPressed(key: Char) {
        when (key.toLowerCase()) {
            'w' -> wPressed = true
            'a' -> aPressed = true
            's' -> sPressed = true
            'd' -> dPressed = true
        }
    }

    fun keyReleased(key: Char) {
        when (key.toLowerCase()) {
            'w' -> wPressed = false
            'a' -> aPressed = false
            's' -> sPressed = false
            'd' -> dPressed = false
        }
    }

    fun run(dt: Float, graphics: PApplet) {
        val xKeyboard = (if (aPressed) -1 else 0) + (if (dPressed) 1 else 0)
        val yKeyboard = (if (sPressed) -1 else 0) + (if (wPressed) 1 else 0)
        val xCurrent = cos(clientPlayer.direction) *
    }

    fun draw(graphics: PApplet) {

    }
}