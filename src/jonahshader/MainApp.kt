package jonahshader

import processing.core.PApplet

class MainApp : PApplet() {
    var screen: Screen? = null


    override fun settings() {
        size(640, 480)
    }

    override fun setup() {
        frameRate(165f)
    }

    override fun draw() {
        screen?.draw()
    }
}