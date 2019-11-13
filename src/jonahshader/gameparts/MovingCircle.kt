package jonahshader.gameparts

import jonahshader.client.MainApp
import processing.core.PApplet
import kotlin.math.cos
import kotlin.math.sin

open class MovingCircle(
    var x: Float, var y: Float, var xSpeed: Float, var ySpeed: Float, val diameter: Float, id: Int
) : NetworkedObject(id) {
    override fun run(dt: Float) {
        x += xSpeed * dt
        y += ySpeed * dt

        // keep asteroid in bounds
        x %= MainApp.screenWidth
        y %= MainApp.screenHeight

        if (x < 0) x += MainApp.screenWidth
        if (y < 0) y += MainApp.screenHeight
    }

    fun checkCollision(otherCircle: MovingCircle) : Boolean = PApplet.dist(x, y, otherCircle.x, otherCircle.y) < ((diameter + otherCircle.diameter) / 2f)

    fun checkCollision(x: Float, y: Float, diameter: Float) : Boolean = PApplet.dist(x, y, this.x, this.y) < ((diameter + this.diameter) / 2f)
}