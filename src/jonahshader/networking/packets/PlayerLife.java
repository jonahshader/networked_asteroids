package jonahshader.networking.packets;

public class PlayerLife {
    public boolean alive;
    public int id;

    public PlayerLife(boolean alive, int id) {
        this.alive = alive;
        this.id = id;
    }

    public PlayerLife() {}
}
