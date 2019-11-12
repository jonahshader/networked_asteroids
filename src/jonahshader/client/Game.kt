package jonahshader.client

import jonahshader.Asteroid
import jonahshader.Engine
import jonahshader.Player
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
        if (clientPlayer != null) {
            // Do player logic here
            val xKeyboard = (if (aPressed) -1 else 0) + (if (dPressed) 1 else 0)
            val yKeyboard = (if (sPressed) -1 else 0) + (if (wPressed) 1 else 0)

            if (xKeyboard != 0 || yKeyboard != 0) {
                val xCurrSpeed = cos(clientPlayer!!.direction) * clientPlayer!!.speed
                val yCurrSpeed = sin(clientPlayer!!.direction) * clientPlayer!!.speed
                val xNewSpeed = xCurrSpeed + xKeyboard * dt
                val yNewSpeed = yCurrSpeed + yKeyboard * dt

                val newDirection = atan2(yNewSpeed, xNewSpeed)
                val newMagnitude = sqrt(xNewSpeed.pow(2) + yNewSpeed.pow(2))

                // Update client player's movement
                clientPlayer!!.direction = newDirection
                clientPlayer!!.speed = newMagnitude
            }

            clientPlayer!!.run(dt)

            // Keep player in bounds
            clientPlayer!!.x %= graphics.width
            clientPlayer!!.y %= graphics.height

            if (clientPlayer!!.x < 0) clientPlayer!!.x += graphics.width
            if (clientPlayer!!.y < 0) clientPlayer!!.y += graphics.height
        }

        // Run players
        for (player in Engine.players) {
            if (clientPlayer != null) {
                if (player.id != clientPlayer!!.id) {
                    player.run(dt)
                } // else dont run because we already did above
            } else {
                player.run(dt)
            }
        }

        // Run asteroids
        for (asteroid in Engine.asteroids) {
            asteroid.run(dt)
        }
    }

    fun draw(graphics: PApplet) {
        // Draw asteroids
        for (asteroid in Engine.asteroids)
            drawAsteroid(asteroid, graphics)

        // Draw players
        for (player in Engine.players)
            drawPlayer(player, 0f, 0f, 255f, graphics)

        // Draw client player
        // TODO: currently draws over player that was in engine. fix that.
        clientPlayer?.let { drawPlayer(it, 255f, 0f, 0f, graphics) }

    }

    private fun drawPlayer(player: Player, r: Float, g: Float, b: Float, graphics: PApplet) {
        graphics.stroke(r, g, b)
        graphics.fill(255f, 255f, 255f)
        graphics.strokeWeight(1f)
        graphics.ellipseMode(CENTER)
        graphics.ellipse(player.x, player.y, player.diameter, player.diameter)
        graphics.line(player.x, player.y,
            player.x + cos(player.direction) * player.diameter,
            player.y + sin(player.direction) * player.diameter)
    }

    private fun drawAsteroid(asteroid: Asteroid, graphics: PApplet) {
        graphics.stroke(0)
        graphics.fill(200f, 200f, 200f)
        graphics.strokeWeight(1f)
        graphics.ellipseMode(CENTER)
        graphics.ellipse(asteroid.x, asteroid.y, asteroid.diameter, asteroid.diameter)
    }
}