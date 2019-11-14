package jonahshader.networking.packets;

/**
 * server to client, telling the client to create a projectile
 */
public class CreateProjectile {
    public int id, ownerId;
    public float x, y, direction;

    public CreateProjectile(float x, float y, float direction, int ownerId, int id) {
        this.id = id;
        this.ownerId = ownerId;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public CreateProjectile() {}
}
