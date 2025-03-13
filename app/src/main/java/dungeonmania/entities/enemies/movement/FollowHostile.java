package dungeonmania.entities.enemies.movement;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class FollowHostile implements MovementStrategy {
    public FollowHostile() {
    }

    @Override
    public Position move(Game game, Entity entity) {
        GameMap map = game.getMap();
        Player player = game.getPlayer();
        return map.dijkstraPathFind(entity.getPosition(), player.getPosition(), entity);
    }
}
