package dungeonmania.entities.enemies.movement;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class AlliedMovement implements MovementStrategy {
    private boolean isAdjacentToPlayer;

    public AlliedMovement(boolean isAdjacentToPlayer) {
        this.isAdjacentToPlayer = isAdjacentToPlayer;
    }

    @Override
    public Position move(Game game, Entity entity) {
        Position nextPos;
        GameMap map = game.getMap();
        Player player = game.getPlayer();
        nextPos = isAdjacentToPlayer ? player.getPreviousDistinctPosition()
                : map.dijkstraPathFind(entity.getPosition(), player.getPosition(), entity);
        if (entity instanceof Mercenary) {
            ((Mercenary) entity).updateIsAdjacentToPlayer(nextPos, player);
        }
        return nextPos;
    }
}
