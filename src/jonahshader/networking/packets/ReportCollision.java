package jonahshader.networking.packets;

public class ReportCollision {
    public int playerId, projectileId, asteroidId;

    public ReportCollision(int playerId, int projectileId, int asteroidId) {
        this.playerId = playerId;
        this.projectileId = projectileId;
        this.asteroidId = asteroidId;
    }

    public ReportCollision() {}
}
