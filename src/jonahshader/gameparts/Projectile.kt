package jonahshader.gameparts

import jonahshader.client.MainApp
import processing.core.PApplet
import kotlin.math.cos
import kotlin.math.sin

open class Projectile(var x: Float, var y: Float, var baseXSpeed: Float, var baseYSpeed: Float, var direction: Float, id: Int) : NetworkedObject(id) {
    private val speed = 210f // pixels per second
    val length = 14f // pixels
    var lifespan = 5f // exist for 5 seconds max

    private val xSpeedScale = cos(direction)
    private val ySpeedScale = sin(direction)

    var xEnd = x + xSpeedScale * length
    var yEnd = y + ySpeedScale * length

    override fun run(dt: Float) {
        // calculate new position
        x += (xSpeedScale * speed + baseXSpeed) * dt
        y += (ySpeedScale * speed + baseYSpeed) * dt

        // wrap around
        x %= MainApp.screenWidth
        y %= MainApp.screenHeight
        if (x < 0) x += MainApp.screenWidth
        if (y < 0) y += MainApp.screenHeight

        // calculate position of other end of the projectile
        xEnd = x + xSpeedScale * length
        yEnd = y + ySpeedScale * length

        // decrement lifespan
        lifespan -= dt
    }

    open fun alive() : Boolean = lifespan > 0

    open fun draw(graphics: PApplet) {
        graphics.stroke(255f)
        graphics.strokeWeight(1f)
        graphics.line(x, y, xEnd, yEnd)
    }
}