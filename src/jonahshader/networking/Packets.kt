package jonahshader.networking

import com.esotericsoftware.kryo.Kryo
import jonahshader.gameparts.Asteroid
import jonahshader.gameparts.Player
import jonahshader.networking.packets.*

fun makeAddPlayerFromPlayer(p: Player) : AddPlayer = AddPlayer(p.x, p.y, p.xSpeed, p.ySpeed, p.direction, p.id, p.accelerating, false, p.alive)
fun makeAddAsteroidFromAsteroid(a: Asteroid) : AddAsteroid = AddAsteroid(a.x, a.y, a.xSpeed, a.ySpeed, a.diameter, a.id)
fun makeUpdatePlayerFromPlayer(p: Player) : UpdatePlayer = UpdatePlayer(p.x, p.y, p.xSpeed, p.ySpeed, p.direction, p.id, p.accelerating, p.alive)

fun registerPackets(kryo: Kryo) {
    kryo.register(NewConnection::class.java)
    kryo.register(UpdatePlayer::class.java)
    kryo.register(RemoveObject::class.java)
    kryo.register(AddPlayer::class.java)
    kryo.register(AddAsteroid::class.java)
    kryo.register(AddProjectile::class.java)
    kryo.register(RequestCreateProjectile::class.java)
    kryo.register(ProjectileIDReply::class.java)
    kryo.register(ReportCollision::class.java)
    kryo.register(RequestRemoveProjectile::class.java)
    kryo.register(UpdateScore::class.java)
    kryo.register(PlayerLife::class.java)
}