package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.enemies.movement.ApproachPlayerMove;
import dungeonmania.entities.enemies.movement.RandomMove;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.HydraBattleStatistics;

public class Hydra extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;
    public static final double DEFAULT_HEALTH_INCREASE_AMOUNT = 5.0;
    public static final double DEFAULT_HEALTH_INCREASE_RATE = 1;

    private HydraBattleStatistics hydraBattleStatistics;

    public Hydra(Position position, double hydraHealth,
    double hydraAttack, double hydraHealthIncreaseAmount, double hydraHealthIncreaseRate) {
        super(position, hydraHealth, hydraAttack);
        hydraBattleStatistics = new HydraBattleStatistics(hydraHealth, hydraAttack, 0,
        HydraBattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                HydraBattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER, hydraHealthIncreaseAmount, hydraHealthIncreaseRate);
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

    @Override
    public BattleStatistics getBattleStatistics() {
        return this.hydraBattleStatistics;
    }
}
