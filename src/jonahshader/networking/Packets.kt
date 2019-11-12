package jonahshader.networking

import com.esotericsoftware.kryo.Kryo

class NewConnection() // Requests everything to be sent from the server to the client

class UpdatePlayer(val x: Float, val y: Float, val speed: Float, val direction: Float, id: Int)
class RemoveObject(id: Int)
class AddPlayer(val x: Float, val y: Float, val speed: Float, val direction: Float, id: Int, owner: Boolean) //
class AddAsteroid(val x: Float, val y: Float, val speed: Float, val direction: Float, val diameter: Float, id: Int)


fun registerPackets(kryo: Kryo) {
    kryo.register(NewConnection::class.java)
    kryo.register(UpdatePlayer::class.java)
    kryo.register(RemoveObject::class.java)
    kryo.register(AddPlayer::class.java)
    kryo.register(AddAsteroid::class.java)
}