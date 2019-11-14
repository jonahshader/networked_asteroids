package jonahshader.networking.packets;

public class ProjectileIDReply {
    public int id, localId;

    public ProjectileIDReply(int id, int localId) {
        this.id = id;
        this.localId = localId;
    }

    public ProjectileIDReply() {}
}
