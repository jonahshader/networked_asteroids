package jonahshader.networking.packets;

public class AddPlayer {
    public float x, y, xSpeed, ySpeed, direction;
    public int id;
    public boolean accelerating, owner;

    public AddPlayer(float x, float y, float xSpeed, float ySpeed, float direction, int id, boolean accelerating, boolean owner) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.direction = direction;
        this.id = id;
        this.accelerating = accelerating;
        this.owner = owner;
    }

    public AddPlayer(){}
}
