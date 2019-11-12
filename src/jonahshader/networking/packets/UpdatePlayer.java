package jonahshader.networking.packets;

public class UpdatePlayer {
    public float x, y, speed, direction;
    public int id;

    public UpdatePlayer(float x, float y, float speed, float direction, int id) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.direction = direction;
        this.id = id;
    }

    public UpdatePlayer(){}
}
