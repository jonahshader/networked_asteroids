package jonahshader.networking.packets;

public class UpdatePlayer {
    public float x, y, xSpeed, ySpeed, direction;
    public int id;
    public boolean accelerating;

    public UpdatePlayer(float x, float y, float xSpeed, float ySpeed, float direction, int id, boolean accelerating) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.accelerating = accelerating;
        this.direction = direction;
        this.id = id;
    }

    public UpdatePlayer(){}
}
