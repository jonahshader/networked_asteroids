package jonahshader

import jonahshader.client.MainApp
import jonahshader.networking.GameServer

fun main() {
//    var asteroidDespawnMargin = 80

    val gameServer = GameServer()
    gameServer.start()
    var previousTime = System.nanoTime()
    var deltaTime = 1/60f
    while (true){
        for (i in Engine.asteroids.indices) {
            val asteroid = Engine.asteroids[i]
            asteroid.run(deltaTime)

            // keep asteroid in bound TODO: reduce redundancy by moving this code to a function
            asteroid.x %= MainApp.screenWidth
            asteroid.y %= MainApp.screenHeight

            if (asteroid.x < 0) asteroid.x += MainApp.screenWidth
            if (asteroid.y < 0) asteroid.y += MainApp.screenHeight
        }


        Thread.sleep(1) // sleep a lil
        val currentTime = System.nanoTime()
        deltaTime = (currentTime - previousTime) * (1e-9).toFloat()
        previousTime = currentTime
    }
}