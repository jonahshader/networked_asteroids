package jonahshader.networking.packets;

/**
 * client to server, requesting server to create a projectile
 */
public class RequestCreateProjectile {
    public float x, y, direction;
    public int localId;

    public RequestCreateProjectile(float x, float y, float direction, int localId) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.localId = localId;
    }

    public RequestCreateProjectile() {}
}
