package jonahshader

import processing.core.PApplet
import kotlin.math.cos
import kotlin.math.sin

open class NetworkedObject(val id: Int) {
    open fun run(dt: Float) {}
}

open class MovingCircle(var x: Float, var y: Float, private val speed: Float, private val direction: Float, private val diameter: Float,
                        id: Int
) : NetworkedObject(id) {
    override fun run(dt: Float) {
        x += speed * dt * cos(direction)
        y += speed * dt * sin(direction)
    }

    fun checkCollision(otherCircle: MovingCircle) : Boolean = (PApplet.dist(x, y, otherCircle.x, otherCircle.y) < ((diameter + otherCircle.diameter) / 2f))
}

class Asteroid(x: Float, y: Float, speed: Float, direction: Float, size: Float, id: Int) :
    MovingCircle(x, y, speed, direction, size, id) {
}

class Player(x: Float, y: Float, speed: Float, direction: Float, id: Int) :
    MovingCircle(x, y, speed, direction, 6.0f, id) {
}



class Engine{
    val idToObject = HashMap<Int, NetworkedObject>()
    val asteroids = mutableListOf<Asteroid>()
    val players = mutableListOf<Player>()

    fun removeObject(id: Int, client: Boolean) {
        if (client) {

        } else {
            val objToRemove = idToObject[id]
            asteroids.remove(objToRemove)
            players.remove(objToRemove)
            idToObject.remove(id)
        }
    }

    fun addObject(obj: NetworkedObject, client: Boolean) {
        if (client) {

        } else {
            if (obj is Player) {
                players.add(obj)
            } else if (obj is Asteroid) {
                asteroids.add(obj)
            }
            idToObject[obj.id] = obj
        }

    }

    fun replaceObject(obj: NetworkedObject, client: Boolean) {
        if (client) {

        } else {
            val objToReplace = idToObject[obj.id]

            if (obj is Player) {
                players.remove(objToReplace)
                players.add(obj)
            } else if (obj is Asteroid) {
                asteroids.remove(objToReplace)
                asteroids.add(obj)
            }

            idToObject[obj.id] = obj
        }
    }
}
