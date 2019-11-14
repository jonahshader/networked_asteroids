package jonahshader.gameparts

import processing.core.PApplet
import kotlin.math.cos
import kotlin.math.sin

open class Projectile(var x: Float, var y: Float, var direction: Float, id: Int) : NetworkedObject(id) {
    private val speed = 20f // pixels per second
    val length = 8f // pixels

    private val xSpeedScale = cos(direction)
    private val ySpeedScale = sin(direction)

    var xEnd = x + xSpeedScale * length
    var yEnd = y + ySpeedScale * length

    override fun run(dt: Float) {
        x += xSpeedScale * speed * dt
        y += ySpeedScale * speed * dt

        xEnd = x + xSpeedScale * length
        yEnd = y + ySpeedScale * length
    }

    fun draw(graphics: PApplet) {
        graphics.stroke(255f)
        graphics.strokeWeight(1f)
        graphics.line(x, y, xEnd, yEnd)
    }
}