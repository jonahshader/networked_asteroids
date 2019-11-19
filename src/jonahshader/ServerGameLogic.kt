package jonahshader

import com.esotericsoftware.kryonet.Server
import jonahshader.client.MainApp
import jonahshader.gameparts.makeAsteroidFromVector
import jonahshader.networking.GameServer
import jonahshader.networking.makeAddAsteroidFromAsteroid
import jonahshader.networking.packets.RemoveObject

class ServerGameLogic(val gameServer: GameServer) {
    fun start() {
        var previousTime = System.nanoTime()
        var deltaTime = 1/150f
        var time = 0.0

        while (true){
            if (gameServer.playersConnected > 0) {
                // spawn random asteroids
                asteroidSpawner(gameServer.server, time, deltaTime)

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

                time += deltaTime
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

fun asteroidSpawner(server: Server, time: Double, dt: Float) {
//    val chanceToSpawn = ((time / 250) + 0.5).pow(1.0/dt.toDouble())
    val chanceToSpawn = ((time / 250) + 0.25) * dt
    if (Math.random() < chanceToSpawn) {
        spawnRandomAsteroid(server)
    }
}

fun spawnRandomAsteroid(server: Server) {
    // spawn some asteroids for testing
    val newAsteroid = makeAsteroidFromVector(
        MainApp.screenWidth * Math.random().toFloat(), MainApp.screenHeight * Math.random().toFloat(),
        (Math.random() * 30f).toFloat() + 25f, (Math.random() * Math.PI * 2).toFloat(), 16f, Engine.nextId
    )
    Engine.queueAddObject(newAsteroid)
    server.sendToAllTCP(makeAddAsteroidFromAsteroid(newAsteroid))
}