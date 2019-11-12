package jonahshader.networking

import com.esotericsoftware.kryo.Kryo
import jonahshader.Asteroid
import jonahshader.Player
import jonahshader.networking.packets.*

//class NewConnection // Requests everything to be sent from the server to the client
//class UpdatePlayer(val x: Float, val y: Float, val speed: Float, val direction: Float, val id: Int)
//class RemoveObject(val id: Int)
//class AddPlayer(val x: Float, val y: Float, val speed: Float, val direction: Float, val id: Int, val owner: Boolean)
//class AddAsteroid(val x: Float, val y: Float, val speed: Float, val direction: Float, val diameter: Float, val id: Int)

fun makeAddPlayerFromPlayer(p: Player) : AddPlayer = AddPlayer(p.x, p.y, p.speed, p.direction, p.id, false)
fun makeAddAsteroidFromAsteroid(a: Asteroid) : AddAsteroid = AddAsteroid(a.x, a.y, a.speed, a.direction, a.diameter, a.id)
fun makeUpdatePlayerFromPlayer(p: Player) : UpdatePlayer = UpdatePlayer(p.x, p.y, p.speed, p.direction, p.id)

fun registerPackets(kryo: Kryo) {
    kryo.register(NewConnection::class.java)
    kryo.register(UpdatePlayer::class.java)
    kryo.register(RemoveObject::class.java)
    kryo.register(AddPlayer::class.java)
    kryo.register(AddAsteroid::class.java)
}