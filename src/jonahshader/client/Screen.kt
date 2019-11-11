package jonahshader.client

import processing.core.PApplet

interface Screen {
    fun draw(dt: Float, graphics: PApplet)
    fun keyPressed(graphics: PApplet)
    fun keyReleased(graphics: PApplet)
    fun mousePressed(graphics: PApplet)
    fun mouseReleased(graphics: PApplet)
}