package jonahshader

import jonahshader.gameparts.Asteroid
import jonahshader.gameparts.NetworkedObject
import jonahshader.gameparts.Player
import jonahshader.networking.packets.UpdatePlayer
import java.util.*
import kotlin.collections.HashMap

object Engine{
    private val idToObject = HashMap<Int, NetworkedObject>()
    val asteroids = Vector<Asteroid>()
    val players = Vector<Player>()

    val objAddQueue = Vector<NetworkedObject>()
    val objRemoveQueue = Vector<NetworkedObject>()

    var nextId = 0

    fun queueRemoveObject(id: Int) {
        val objToRemove = idToObject[id]
        objRemoveQueue.add(objToRemove)
    }

    fun queueAddObject(obj: NetworkedObject) {
        objAddQueue.add(obj)
        nextId++
    }

    // for server only
    fun addObject(obj: NetworkedObject) {
        if (obj is Player) {
            players.add(obj)
        } else if (obj is Asteroid) {
            asteroids.add(obj)
        }
        idToObject[obj.id] = obj
        nextId++
    }

    // for server only
    fun removeObject(id: Int) {
        val objToRemove = idToObject[id]
        players.remove(objToRemove)
        asteroids.remove(objToRemove)
        idToObject.remove(id)
    }

    fun updatePlayer(playerInfo: UpdatePlayer) {
        val objToUpdate = idToObject[playerInfo.id]
        val indexToUpdate = players.indexOf(objToUpdate)
        if (indexToUpdate >= 0)
            players[indexToUpdate].updatePlayer(playerInfo)
    }

    fun replaceObject(obj: NetworkedObject) {
        val objToReplace = idToObject[obj.id]

        if (obj is Player) {
            val indexToReplace = players.indexOf(objToReplace)
            if (indexToReplace >= 0)
                players[indexToReplace] = obj
        } else if (obj is Asteroid) {
            val indexToReplace = asteroids.indexOf(objToReplace)
            if (indexToReplace >= 0)
                asteroids[asteroids.indexOf(objToReplace)] = obj
        }

        idToObject[obj.id] = obj
    }

    /**
     * should be called by Game before the engine is used
     */
    fun updateEngine() {
        for (obj in objAddQueue) {
            if (obj is Player) {
                players.add(obj)
            } else if (obj is Asteroid) {
                asteroids.add(obj)
            }
            idToObject[obj.id] = obj
        }
        objAddQueue.clear()

        for (obj in objRemoveQueue) {
            asteroids.remove(obj)
            players.remove(obj)
            idToObject.remove(obj.id)
        }
        objRemoveQueue.clear()
    }
}
