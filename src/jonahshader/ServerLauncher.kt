package jonahshader

import jonahshader.networking.GameServer

fun main() {
    val gameServer = GameServer()
    gameServer.start()
    val logic = ServerGameLogic(gameServer)
    logic.start()
}

