package jonahshader.gameparts

import jonahshader.Engine
import jonahshader.client.MainApp
import jonahshader.networking.packets.AddPlayer
import jonahshader.networking.packets.UpdatePlayer
import processing.core.PApplet
import processing.core.PConstants
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Player(x: Float, y: Float, xSpeed: Float, ySpeed: Float, var accelerating: Boolean, var direction: Float, id: Int) :
    MovingCircle(x, y, xSpeed, ySpeed, 12.0f, id) {
    private val playerAccelerationMultiplier = 90f
    private val playerTurnRate = (PI * 2f).toFloat()

    var alive = true

    var r = 0f
    var g = 0f
    var b = 255f

    constructor(playerInfo: AddPlayer) : this(playerInfo.x, playerInfo.y, playerInfo.xSpeed, playerInfo.ySpeed, playerInfo.accelerating, playerInfo.direction, playerInfo.id) {
        // change player color if the client owns it
        if (playerInfo.owner) {
            r = 255f
            g = 0f
            b = 0f
        }

        alive = playerInfo.alive
    }

    fun updatePlayer(info: UpdatePlayer) {
        this.x = info.x
        this.y = info.y
        this.xSpeed = info.xSpeed
        this.ySpeed = info.ySpeed
        this.direction = info.direction
        this.accelerating = info.accelerating
        this.alive = info.alive
    }

    override fun run(dt: Float) {
        if (alive) {
            if (accelerating) {
                xSpeed += cos(direction) * playerAccelerationMultiplier * dt
                ySpeed += sin(direction) * playerAccelerationMultiplier * dt
            }
            super.run(dt)

            // Keep player in bounds
            x %= MainApp.screenWidth
            y %= MainApp.screenHeight

            if (x < 0) x += MainApp.screenWidth
            if (y < 0) y += MainApp.screenHeight
        }
    }

    fun turn(polarity: Int, dt: Float) {
        if (alive) {
            direction -= polarity * playerTurnRate * dt
        }
    }

    fun applyForce(direction: Float, magnitude: Float) {
        if (alive) {
            xSpeed += cos(direction) * magnitude
            ySpeed += sin(direction) * magnitude
        }
    }

    fun draw(graphics: PApplet) {
        if (alive) {
            graphics.stroke(r, g, b)
            graphics.fill(255f, 255f, 255f)
            graphics.strokeWeight(1f)
            graphics.ellipseMode(PConstants.CENTER)
            graphics.ellipse(x, y, diameter, diameter)
            graphics.line(x, y,
                x + cos(direction) * diameter,
                y + sin(direction) * diameter)
        }
    }
}