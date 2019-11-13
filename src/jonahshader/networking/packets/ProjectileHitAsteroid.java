package jonahshader.networking.packets;

public class ProjectileHitAsteroid {
    public int projectileId, asteroidId, playerId, id;

    public ProjectileHitAsteroid(int projectileId, int asteroidId, int playerId, int id) {
        this.projectileId = projectileId;
        this.asteroidId = asteroidId;
        this.playerId = playerId;
        this.id = id;
    }

    public ProjectileHitAsteroid() {}
}
