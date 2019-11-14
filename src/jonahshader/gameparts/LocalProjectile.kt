package jonahshader.gameparts

import jonahshader.Engine
import jonahshader.client.Game
import jonahshader.client.MainApp
import jonahshader.networking.GameServer
import kotlin.math.cos
import kotlin.math.sin

class LocalProjectile(x: Float, y: Float, direction: Float, val ownerId: Int, id: Int) : Projectile(x, y, direction, id) {

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