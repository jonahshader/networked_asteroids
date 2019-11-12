package jonahshader.networking.packets;

public class AddPlayer {
    public float x, y, speed, direction;
    public int id;
    public boolean owner;

    public AddPlayer(float x, float y, float speed, float direction, int id, boolean owner) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.direction = direction;
        this.id = id;
        this.owner = owner;
    }

    public AddPlayer(){}
}
