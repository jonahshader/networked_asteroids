package jonahshader.networking.packets;

public class UpdatePlayer {
    public float x, y, xSpeed, ySpeed, direction;
    public int id;
    public boolean accelerating, alive;

    public UpdatePlayer(float x, float y, float xSpeed, float ySpeed, float direction, int id, boolean accelerating, boolean alive) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.accelerating = accelerating;
        this.alive = alive;
        this.direction = direction;
        this.id = id;
    }

    public UpdatePlayer(){}
}
