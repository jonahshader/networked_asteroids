package jonahshader.networking.packets;

/**
 * client to server, requesting server to create a projectile
 */
public class RequestCreateProjectile {
    public float x, y, baseXSpeed, baseYSpeed, direction;
    public int localId;

    public RequestCreateProjectile(float x, float y, float baseXSpeed, float baseYSpeed, float direction, int localId) {
        this.x = x;
        this.y = y;
        this.baseXSpeed = baseXSpeed;
        this.baseYSpeed = baseYSpeed;
        this.direction = direction;
        this.localId = localId;
    }

    public RequestCreateProjectile() {}
}
