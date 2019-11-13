package jonahshader

import jonahshader.gameparts.Asteroid
import jonahshader.gameparts.NetworkedObject
import jonahshader.gameparts.Player
import jonahshader.networking.packets.UpdatePlayer

object Engine{
    private val idToObject = HashMap<Int, NetworkedObject>()
    val asteroids = mutableListOf<Asteroid>()
    val players = mutableListOf<Player>()
    var nextId = 0

    //TODO: make removeObject mark an object for removable (via a flag iin NetworkedObject) and then remove them all at once with removeIf()
    //TODO: also do the inverse by making addObject go into a queue before
    //TODO: add an atomic boolean that indicates whether engine is busy or not.
    // busy as in GameClient is currently modifying it.
    // when its not busy, set the flag to false which will allow Game to access it

    //TODO: instead of calling removeIf immediately after removeObject call, make Game call it right before iterating over
    // the objects. same with the add queue.

    fun removeObject(id: Int) {
        val objToRemove = idToObject[id]
        asteroids.remove(objToRemove)
        players.remove(objToRemove)
        idToObject.remove(id)
    }

    fun addObject(obj: NetworkedObject) {
        if (obj is Player) {
            players.add(obj)
        } else if (obj is Asteroid) {
            asteroids.add(obj)
        }
        idToObject[obj.id] = obj
        nextId++
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
}
