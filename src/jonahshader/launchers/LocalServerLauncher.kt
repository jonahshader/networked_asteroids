package jonahshader.launchers

import jonahshader.networking.GameServer

fun main() {
    val gameServer = GameServer()
    gameServer.start(12345)
}