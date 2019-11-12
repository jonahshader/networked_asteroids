package jonahshader.client

import processing.core.PApplet

class MainApp : PApplet() {
    val screenWidth = 640
    val screenHeight = 480

    var screen: Screen? = MainMenu()
    var deltaTime = 1/60.0f
    var previousTimeNanos = System.nanoTime()

    override fun settings() {
        size(640, 480)
    }

    override fun setup() {
        frameRate(165f)
    }

    override fun draw() {
        screen?.draw(deltaTime, this)

        val currentTime = System.nanoTime()
        deltaTime = (currentTime.toDouble() * 1e-9 - previousTimeNanos.toDouble() * 1e-9).toFloat()
        previousTimeNanos = currentTime
    }
}