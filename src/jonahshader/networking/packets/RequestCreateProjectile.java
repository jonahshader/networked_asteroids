package jonahshader.networking.packets;

/**
 * client to server, requesting server to create a projectile
 */
public class RequestCreateProjectile {
    public float x, y, direction;
    public int ownerId;

    public RequestCreateProjectile(float x, float y, float direction, int ownerId) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.ownerId = ownerId;
    }

    public RequestCreateProjectile() {}
}
