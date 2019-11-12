package jonahshader.client

import processing.core.PApplet

class MainApp : PApplet() {
    companion object {
        val screenWidth = 640
        val screenHeight = 480
    }


    var screen: Screen? = GameScreen()
    var deltaTime = 1/60.0f
    var previousTimeNanos = System.nanoTime()

    override fun settings() {
        size(screenWidth, screenHeight)
    }

    override fun setup() {
        frameRate(165f)
        background(0)
    }

    override fun draw() {
        screen?.draw(deltaTime, this)

        val currentTime = System.nanoTime()
        deltaTime = (currentTime.toDouble() * 1e-9 - previousTimeNanos.toDouble() * 1e-9).toFloat()
        previousTimeNanos = currentTime
    }

    override fun keyPressed() {
        screen?.keyPressed(this)
    }

    override fun keyReleased() {
        screen?.keyReleased(this)
    }
}