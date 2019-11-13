package jonahshader.gameparts

import jonahshader.Engine
import kotlin.math.cos
import kotlin.math.sin

class Projectile(var x: Float, var y: Float, var direction: Float, ownerId: Int, id: Int) {
    private val speed = 20f
    private val length = 8f

    private val xSpeedScale = cos(direction)
    private val ySpeedScale = sin(direction)

    private var xEnd = x + xSpeedScale * length
    private var yEnd = y + ySpeedScale * length

    fun run(dt: Float, client: Boolean) {
        x += xSpeedScale * speed * dt
        y += ySpeedScale * speed * dt

        xEnd = x + xSpeedScale * length
        yEnd = y + ySpeedScale * length

        // if client, run collision calculations
        if (client) {
            val asteroidCollided = getCollision()
            if (asteroidCollided != null) {

            }
        }

        TODO("make projectile despawn when off screen + make player limit how many projectiles can be spawned")
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