package jonahshader.networking

class UpdatePlayer(val x: Float, val y: Float, val speed: Float, val direction: Float, id: Int) // Update player values
class CreateClientPlayer(val x: Float, val y: Float, id: Int) // Notifies client that this is the client's player that it will control