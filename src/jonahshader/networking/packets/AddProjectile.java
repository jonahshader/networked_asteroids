package jonahshader.networking.packets;

/**
 * server to client, telling the client to create a projectile
 */
public class AddProjectile {
    public int id;
    public float x, y, direction;

    public AddProjectile(float x, float y, float direction, int id) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public AddProjectile() {}
}
