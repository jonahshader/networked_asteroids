package jonahshader.gameparts

import jonahshader.networking.packets.AddAsteroid
import processing.core.PApplet
import processing.core.PConstants
import kotlin.math.cos
import kotlin.math.sin

class Asteroid(x: Float, y: Float, xSpeed: Float, ySpeed: Float, diameter: Float, id: Int) :
    MovingCircle(x, y, xSpeed, ySpeed, diameter, id) {
    constructor(info: AddAsteroid) : this(info.x, info.y, info.xSpeed, info.direction, info.diameter, info.id)

    fun draw(graphics: PApplet) {
        graphics.stroke(200f)
        graphics.fill(200f, 200f, 200f, 100f)
        graphics.strokeWeight(1f)
        graphics.ellipseMode(PConstants.CENTER)
        graphics.ellipse(x, y, diameter, diameter)
    }
}

fun makeAsteroidFromVector(x: Float, y: Float, speed: Float, direction: Float, diameter: Float, id: Int) : Asteroid {
    return Asteroid(x, y, cos(direction) * speed, sin(direction) * speed, diameter, id)
}