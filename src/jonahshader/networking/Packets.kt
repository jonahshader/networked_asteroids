package jonahshader.networking

import com.esotericsoftware.kryo.Kryo
import jonahshader.gameparts.Asteroid
import jonahshader.gameparts.Player
import jonahshader.networking.packets.*

fun makeAddPlayerFromPlayer(p: Player) : AddPlayer = AddPlayer(p.x, p.y, p.xSpeed, p.ySpeed, p.direction, p.id, p.accelerating, false)
fun makeAddAsteroidFromAsteroid(a: Asteroid) : AddAsteroid = AddAsteroid(a.x, a.y, a.xSpeed, a.ySpeed, a.diameter, a.id)
fun makeUpdatePlayerFromPlayer(p: Player) : UpdatePlayer = UpdatePlayer(p.x, p.y, p.xSpeed, p.ySpeed, p.direction, p.id, p.accelerating)

fun registerPackets(kryo: Kryo) {
    kryo.register(NewConnection::class.java)
    kryo.register(UpdatePlayer::class.java)
    kryo.register(RemoveObject::class.java)
    kryo.register(AddPlayer::class.java)
    kryo.register(AddAsteroid::class.java)
    kryo.register(RequestCreateProjectile::class.java)
    kryo.register(ProjectileIDReply::class.java)
}