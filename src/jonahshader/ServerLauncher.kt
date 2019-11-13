package jonahshader

import com.esotericsoftware.kryonet.Server
import jonahshader.client.MainApp
import jonahshader.networking.GameServer
import jonahshader.networking.makeAddAsteroidFromAsteroid
import jonahshader.networking.packets.AddAsteroid
import java.lang.Math.random
import kotlin.math.pow

fun main() {
//    var asteroidDespawnMargin = 80

    val gameServer = GameServer()
    gameServer.start()
    var previousTime = System.nanoTime()
    var deltaTime = 1/60f
    var time = 0.0

    while (true){
        if (gameServer.playersConnected > 0) {
            // spawn random asteroids
            asteroidSpawner(gameServer.server, time, deltaTime)

            // run game logic for asteroids
            for (i in Engine.asteroids.indices) {
                val asteroid = Engine.asteroids[i]
                asteroid.run(deltaTime)

                // keep asteroid in bound TODO: reduce redundancy by moving this code to a function
                asteroid.x %= MainApp.screenWidth
                asteroid.y %= MainApp.screenHeight

                if (asteroid.x < 0) asteroid.x += MainApp.screenWidth
                if (asteroid.y < 0) asteroid.y += MainApp.screenHeight
            }

            time += deltaTime
        }




        Thread.sleep(10) // sleep a lil
        val currentTime = System.nanoTime()
        deltaTime = (currentTime - previousTime) * (1e-9).toFloat()
        previousTime = currentTime
    }
}

fun asteroidSpawner(server: Server, time: Double, dt: Float) {
//    val chanceToSpawn = ((time / 250) + 0.5).pow(1.0/dt.toDouble())
    val chanceToSpawn = ((time / 250) + 0.25) * dt
    if (random() < chanceToSpawn) {
        spawnRandomAsteroid(server)
    }
}

fun spawnRandomAsteroid(server: Server) {
    // spawn some asteroids for testing
    val newAsteroid = Asteroid(MainApp.screenWidth * Math.random().toFloat(), MainApp.screenHeight * Math.random().toFloat(),
        (Math.random() * 30f).toFloat() + 25f, (Math.random() * Math.PI * 2).toFloat(), 16f, Engine.nextId)
    Engine.addObject(newAsteroid, false)
    server.sendToAllTCP(makeAddAsteroidFromAsteroid(newAsteroid))
}