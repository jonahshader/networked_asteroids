package jonahshader

import com.esotericsoftware.kryonet.Server
import jonahshader.client.MainApp
import jonahshader.gameparts.makeAsteroidFromVector
import jonahshader.networking.GameServer
import jonahshader.networking.makeAddAsteroidFromAsteroid
import jonahshader.networking.packets.RemoveObject
import kotlin.concurrent.thread

class ServerGameLogic(private val gameServer: GameServer) {
    val TIME_TILL_GAME_START = 30f
    var previousTime = System.nanoTime()
    var deltaTime = 1/150f
    var gameTime = 0.0f
    var gameInProgress = false
    var timeTillGameStart = TIME_TILL_GAME_START
    var resetting = false

    fun start() {
        thread {
            while (true){
                if (resetting) {
                    reset()
                    resetting = false
                }
                if (gameServer.playersConnected > 0) {
                    if (gameInProgress) {
                        // spawn random asteroids
                        asteroidSpawner(gameServer.server, gameTime, deltaTime)

                        // run game logic for asteroids
                        for (i in Engine.asteroids.indices)
                            Engine.asteroids[i].run(deltaTime)

                        // run game logic for projectiles
                        for (i in Engine.projectiles.indices) {
                            Engine.projectiles[i].run(deltaTime)
                            // remove projectile if it is not alive
                            if (!Engine.projectiles[i].alive()) {
                                gameServer.server.sendToAllTCP(RemoveObject(Engine.projectiles[i].id))
                                Engine.queueRemoveObject(Engine.projectiles[i].id)
                            }
                        }

                        var allPlayersDead = true
                        for (player in Engine.players) {
                            if (player.alive) {
                                allPlayersDead = false
                            }
                        }
                        if (allPlayersDead) {
                            resetting = true
                        }
                        gameTime += deltaTime
                    } else {
                        if (timeTillGameStart <= 0) {
                            gameInProgress = true
                        } else {
                            timeTillGameStart -= deltaTime
                        }
                    }
                } else {
                    // partial reset
                    partialReset()
                }
                // update engine contents
                Engine.updateEngine()

                Thread.sleep(3) // sleep a lil
                val currentTime = System.nanoTime()
                deltaTime = (currentTime - previousTime) * (1e-9).toFloat()
                previousTime = currentTime
            }
        }
    }

    private fun reset() {
        gameTime = 0f
        gameInProgress = false
        timeTillGameStart = TIME_TILL_GAME_START
        Engine.queueReset()
        Engine.updateEngine()
        gameServer.reset()
    }

    private fun partialReset() {
        gameTime = 0f
        gameInProgress = false
        timeTillGameStart = TIME_TILL_GAME_START
        Engine.queueReset()
        gameServer.resetNoResendData()
    }

}


fun asteroidSpawner(server: Server, time: Float, dt: Float) {
//    val chanceToSpawn = ((time / 250) + 0.5).pow(1.0/dt.toDouble())
    val chanceToSpawn = ((time / 250) + 0.25) * dt
    if (Math.random() < chanceToSpawn) {
        spawnRandomAsteroid(server)
    }
}

fun spawnRandomAsteroid(server: Server) {
    val xOrY = Math.random() > 0.5
    // spawn some asteroids for testing
    val newAsteroid = if (xOrY) {
        makeAsteroidFromVector(
            MainApp.screenWidth * Math.random().toFloat(), 0f,
            (Math.random() * 30f).toFloat() + 25f, (Math.random() * Math.PI * 2).toFloat(), 16f, Engine.nextId
        )
    } else {
        makeAsteroidFromVector(
            0f, MainApp.screenHeight * Math.random().toFloat(),
            (Math.random() * 30f).toFloat() + 25f, (Math.random() * Math.PI * 2).toFloat(), 16f, Engine.nextId
        )
    }

    Engine.queueAddObject(newAsteroid)
    server.sendToAllTCP(makeAddAsteroidFromAsteroid(newAsteroid))
}