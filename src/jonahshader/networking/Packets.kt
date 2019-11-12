package jonahshader.networking

import com.esotericsoftware.kryo.Kryo
import jonahshader.Player

class NewConnection() // Requests everything to be sent from the server to the client

class UpdatePlayer(val x: Float, val y: Float, val speed: Float, val direction: Float, val id: Int) {
    constructor(player: Player) : this(player.x, player.y, player.speed, player.direction, player.id)
}
class RemoveObject(val id: Int)
class AddPlayer(val x: Float, val y: Float, val speed: Float, val direction: Float, val id: Int, val owner: Boolean)
class AddAsteroid(val x: Float, val y: Float, val speed: Float, val direction: Float, val diameter: Float, val id: Int)


fun registerPackets(kryo: Kryo) {
    kryo.register(NewConnection::class.java)
    kryo.register(UpdatePlayer::class.java)
    kryo.register(RemoveObject::class.java)
    kryo.register(AddPlayer::class.java)
    kryo.register(AddAsteroid::class.java)
}