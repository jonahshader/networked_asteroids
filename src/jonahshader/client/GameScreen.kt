package jonahshader.client

import processing.core.PApplet

class GameScreen : Screen {
    private val game = Game()

    override fun draw(dt: Float, graphics: PApplet) {
        game.run(dt)
        game.draw(graphics)
    }

    override fun keyPressed(graphics: PApplet) {
        game.keyPressed(graphics.key)
    }

    override fun keyReleased(graphics: PApplet) {
        game.keyReleased(graphics.key)
    }

    override fun mousePressed(graphics: PApplet) {

    }

    override fun mouseReleased(graphics: PApplet) {

    }
}