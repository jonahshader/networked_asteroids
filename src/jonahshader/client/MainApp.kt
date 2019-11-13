package jonahshader.client

import processing.core.PApplet

class MainApp : PApplet() {
    companion object {
        val screenWidth = 1280
        val screenHeight = 720
    }


    var screen: Screen? = GameScreen()
    var deltaTime = 1/60.0f
    var previousTimeNanos = System.nanoTime()

    override fun settings() {
        size(screenWidth, screenHeight)
    }

    override fun setup() {
        surface.setResizable(true)
        frameRate(165f)
        background(0)
    }

    override fun draw() {
        val xScale = screenWidth / width.toFloat()
        val yScale = screenHeight / height.toFloat()
        scale(1/max(xScale, yScale))

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