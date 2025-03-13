package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.enemies.movement.ApproachPlayerMove;
import dungeonmania.entities.enemies.movement.RandomMove;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        calculateMovement(map);
        nextPos = getMovementStrategy().move(game, this);
        map.moveTo(this, nextPos);
    }

    public void calculateMovement(GameMap map) {
        if (map.getPlayer().getEffectivePotion() instanceof InvincibilityPotion) {
            setMovementStrategy(new ApproachPlayerMove());
        } else {
            setMovementStrategy(new RandomMove());
        }
    }

}
