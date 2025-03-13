package dungeonmania.entities.enemies.movement;

import dungeonmania.util.Position;
import dungeonmania.Game;
import dungeonmania.entities.Entity;

public interface MovementStrategy {
    Position move(Game game, Entity entity);
}
