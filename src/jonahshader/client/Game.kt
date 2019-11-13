package jonahshader.client

import jonahshader.gameparts.Asteroid
import jonahshader.Engine
import jonahshader.gameparts.Player
import jonahshader.networking.GameClient
import processing.core.PApplet
import processing.core.PConstants.CENTER
import kotlin.math.*

class Game {
    init {
        GameClient(this).start()
    }

    var clientPlayer: Player? = null

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
//            ' ' ->
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

    fun run(dt: Float) {
        if (clientPlayer != null) {
            // Do client player movement logic
            val xKeyboard = (if (aPressed) -1 else 0) + (if (dPressed) 1 else 0)
            val yKeyboard = ((if (sPressed) -1 else 0) + (if (wPressed) 1 else 0))

            // Update client player's movement
            if (xKeyboard != 0 || yKeyboard != 0)
                clientPlayer!!.turn(-xKeyboard, dt)
            clientPlayer!!.accelerating = yKeyboard > 0
        }

        // Run players
        for (i in Engine.players.indices)
            Engine.players[i].run(dt)

        // Run asteroids
        for (i in Engine.asteroids.indices)
            Engine.asteroids[i].run(dt)
    }

    fun draw(graphics: PApplet) {
        // make background black
        graphics.background(0)

        // Draw asteroids
        for (i in Engine.asteroids.indices)
            Engine.asteroids[i].draw(graphics)

        // Draw players
        for (i in Engine.players.indices)
            Engine.players[i].draw(graphics)
    }
}
