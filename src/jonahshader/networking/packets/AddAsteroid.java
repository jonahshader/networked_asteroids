package jonahshader.networking.packets;

public class AddAsteroid {
    public float x, y, xSpeed, direction, diameter;
    public int id;

    public AddAsteroid(float x, float y, float xSpeed, float ySpeed, float diameter, int id) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.direction = ySpeed;
        this.diameter = diameter;
        this.id = id;
    }

    public AddAsteroid(){}
}
