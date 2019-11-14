package jonahshader

import jonahshader.gameparts.Asteroid
import jonahshader.gameparts.NetworkedObject
import jonahshader.gameparts.Player
import jonahshader.gameparts.Projectile
import jonahshader.networking.packets.UpdatePlayer
import java.util.*
import kotlin.collections.HashMap

object Engine{
    private val idToObject = HashMap<Int, NetworkedObject>()
    val asteroids = Vector<Asteroid>()
    val players = Vector<Player>()
    val projectiles = Vector<Projectile>()

    private val objAddQueue = Vector<NetworkedObject>()
    private val objRemoveQueue = Vector<NetworkedObject>()

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
        when (obj) {
            is Player -> players.add(obj)
            is Asteroid -> asteroids.add(obj)
            is Projectile -> projectiles.add(obj)
        }
        idToObject[obj.id] = obj
        nextId++
    }

    // for server only
    fun removeObject(id: Int) {
        val objToRemove = idToObject[id]
        players.remove(objToRemove)
        asteroids.remove(objToRemove)
        projectiles.remove(objToRemove)
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

        when (obj) {
            is Player -> {
                val indexToReplace = players.indexOf(objToReplace)
                if (indexToReplace >= 0)
                    players[indexToReplace] = obj
            }
            is Asteroid -> {
                val indexToReplace = asteroids.indexOf(objToReplace)
                if (indexToReplace >= 0)
                    asteroids[indexToReplace] = obj
            }
            is Projectile -> {
                val indexToReplace = projectiles.indexOf(objToReplace)
                if (indexToReplace >= 0)
                    projectiles[indexToReplace] = obj
            }
        }

        idToObject[obj.id] = obj
    }

    /**
     * should be called by Game before the engine is used. updates the add and remove queues
     */
    fun updateEngine() {
        for (obj in objAddQueue) {
            when (obj) {
                is Player -> players.add(obj)
                is Asteroid -> asteroids.add(obj)
                is Projectile -> projectiles.add(obj)
            }
            idToObject[obj.id] = obj
        }
        objAddQueue.clear()

        for (obj in objRemoveQueue) {
            asteroids.remove(obj)
            players.remove(obj)
            projectiles.remove(obj)
            idToObject.remove(obj.id)
        }
        objRemoveQueue.clear()
    }
}
