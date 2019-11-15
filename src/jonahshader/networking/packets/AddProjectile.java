package jonahshader.networking.packets;

/**
 * server to client, telling the client to create a projectile
 */
public class AddProjectile {
    public int id;
    public float x, y, baseXSpeed, baseYSpeed, direction;

    public AddProjectile(float x, float y, float baseXSpeed, float baseYSpeed, float direction, int id) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.baseXSpeed = baseXSpeed;
        this.baseYSpeed = baseYSpeed;
        this.direction = direction;
    }

    public AddProjectile() {}
}
