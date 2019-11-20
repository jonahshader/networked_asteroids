package jonahshader.client

import jonahshader.Engine
import jonahshader.gameparts.Player
import jonahshader.gameparts.LocalProjectile
import jonahshader.networking.GameClient
import jonahshader.networking.packets.RemoveObject
import jonahshader.networking.packets.ReportCollision
import jonahshader.networking.packets.RequestCreateProjectile
import jonahshader.networking.packets.RequestRemoveProjectile
import processing.core.PApplet
import processing.core.PConstants
import processing.core.PConstants.*
import java.util.*
import kotlin.math.PI

class Game(ip: String, port: Int) {
    var timeTillStart: Float = 30f
    private val gameClient = GameClient(this)
    var score = 0

    init {
        gameClient.start(ip, port)
    }

    var clientPlayer: Player? = null
    val clientProjectiles = Vector<LocalProjectile>()

    private var nextProjectileId = 0;

    private var wPressed = false
    private var aPressed = false
    private var sPressed = false
    private var dPressed = false

    fun keyPressed(key: Char) {
        when (key.toLowerCase()) {
            'w' -> wPressed = true
            'a' -> aPressed = true
            's' -> sPressed = true
            'd' -> dPressed = true
            ' ' -> {
                // if clientPlayer exists,
                if (clientPlayer != null) {
                    // if the clientplayer is alive and there are less than 4 projectiles,
                    if (clientPlayer!!.alive && clientProjectiles.size < 4) {
                        // apply backwards force to player
                        clientPlayer!!.applyForce(clientPlayer!!.direction + PConstants.PI, 5f)
                        // create a local projectile in clientProjectiles
                        clientProjectiles.add(LocalProjectile(clientPlayer!!.x, clientPlayer!!.y, clientPlayer!!.xSpeed, clientPlayer!!.ySpeed, clientPlayer!!.direction, nextProjectileId, -1))
                        // also send a request to create one on the server so that other clients can see it
                        gameClient.client.sendTCP(RequestCreateProjectile(clientPlayer!!.x, clientPlayer!!.y, clientPlayer!!.xSpeed, clientPlayer!!.ySpeed, clientPlayer!!.direction, nextProjectileId))
                        nextProjectileId++
                    }
                }
            }
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
        Engine.updateEngine()
        if (clientPlayer != null) {
            // Do client player movement logic
            val xKeyboard = (if (aPressed) -1 else 0) + (if (dPressed) 1 else 0)
            val yKeyboard = ((if (sPressed) -1 else 0) + (if (wPressed) 1 else 0))

            // Update client player's movement
            if (xKeyboard != 0 || yKeyboard != 0)
                clientPlayer!!.turn(-xKeyboard, dt)
            clientPlayer!!.accelerating = yKeyboard > 0

            // check if the client player got hit by an asteroid
            for (a in Engine.asteroids) {
                if (a.checkCollision(clientPlayer!!)) {
                    clientPlayer!!.alive = false
                }
            }
        }

        // Run local projectiles
        for (i in clientProjectiles.indices)
            clientProjectiles[i].run(dt)

        if (clientPlayer != null) {
            // try to remove projectiles that were marked for removal
            val pToRemove = mutableListOf<LocalProjectile>()
            for (p in clientProjectiles) {
                if (!p.alive() && p.id >= 0) {
                    if (p.asteroidCollided == null) {
                        gameClient.client.sendTCP(RequestRemoveProjectile(p.id))
                    } else {
                        gameClient.client.sendTCP(ReportCollision(clientPlayer!!.id, p.id, p.asteroidCollided!!.id))
                    }

                    pToRemove.add(p)
                }
            }
            clientProjectiles.removeAll(pToRemove)
            pToRemove.clear()
        }

        // Run other projectiles
        for (i in Engine.projectiles.indices)
            Engine.projectiles[i].run(dt)

        // Run players
        for (i in Engine.players.indices)
            Engine.players[i].run(dt)

        // Run asteroids
        for (i in Engine.asteroids.indices)
            Engine.asteroids[i].run(dt)

        // decrement timeTillStart
        timeTillStart -= dt
    }

    fun draw(graphics: PApplet) {
        // make background black
        graphics.background(0)

        // Draw asteroids
        for (i in Engine.asteroids.indices)
            Engine.asteroids[i].draw(graphics)

        // Draw other projectiles
        for (i in Engine.projectiles.indices)
            Engine.projectiles[i].draw(graphics)

        // Draw local projectiles
        for (i in clientProjectiles.indices)
            clientProjectiles[i].draw(graphics)

        // Draw players
        for (i in Engine.players.indices)
            Engine.players[i].draw(graphics)

        // draw score
        if (score > 0) {
            graphics.stroke(255f)
            graphics.fill(255f)
            graphics.textAlign(CENTER, TOP)
            graphics.textSize(20f)
            graphics.text("SCORE $score", MainApp.screenWidth / 2f, 0f)
        } else if (timeTillStart > 0) {
            graphics.stroke(255f)
            graphics.fill(255f)
            graphics.textAlign(CENTER, TOP)
            graphics.textSize(20f)
            graphics.text("STARTING IN: ${timeTillStart.toInt()}", MainApp.screenWidth / 2f, 0f)
        }
    }
}
