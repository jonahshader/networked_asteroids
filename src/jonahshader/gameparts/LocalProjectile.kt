package jonahshader.gameparts

import jonahshader.Engine
import jonahshader.client.Game
import jonahshader.client.MainApp
import jonahshader.networking.GameServer
import processing.core.PApplet
import processing.core.PGraphics
import kotlin.math.cos
import kotlin.math.sin

/**
 * local projectile performs collision detection and only runs on clients
 */
class LocalProjectile(x: Float, y: Float, baseXSpeed: Float, baseYSpeed: Float, direction: Float, val localId: Int, id: Int) : Projectile(x, y, baseXSpeed, baseYSpeed, direction, id) {
    var markedForRemoval = false
    var asteroidCollided: Asteroid? = null

    override fun run(dt: Float) {
        if (!markedForRemoval) {
            super.run(dt)

            if (x >= MainApp.screenWidth || x < 0 || y >= MainApp.screenHeight || y < 0)
                markedForRemoval = true

            asteroidCollided = getCollision()
            if (asteroidCollided != null) {
                markedForRemoval = true
            }
        }
    }

    override fun draw(graphics: PApplet) {
        if (!markedForRemoval)
            super.draw(graphics)
    }

    // checks collision with asteroids
    private fun getCollision() : Asteroid? {
        val xCenter = (x + xEnd) * 0.5f
        val yCenter = (y + yEnd) * 0.5f

        for (i in Engine.asteroids.indices) {
            // perform culling
            if (Engine.asteroids[i].checkCollision(xCenter, yCenter, length)) {
                for (j in 0..10) {
                    val xProgressed = interp(x, xEnd, j/10f)
                    val yProgressed = interp(y, yEnd, j/10f)
                    if (Engine.asteroids[i].checkCollision(xProgressed, yProgressed, 0f))
                        return Engine.asteroids[i]
                }
            }
        }
        return null
    }

    private fun interp(a: Float, b: Float, fade: Float) : Float = (a * (1-fade)) + (b * fade)
}