package jonahshader.networking.packets;

public class AddAsteroid {
    public float x, y, speed, direction, diameter;
    public int id;

    public AddAsteroid(float x, float y, float speed, float direction, float diameter, int id) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.direction = direction;
        this.diameter = diameter;
        this.id = id;
    }

    public AddAsteroid(){}
}
