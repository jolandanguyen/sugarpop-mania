package dungeonmania.goals;

import dungeonmania.Game;
import dungeonmania.entities.enemies.ZombieToastSpawner;

public class EnemiesGoal implements GoalStrategy {
    private int target;
    public EnemiesGoal(int target) {
        this.target = target;
    }

    @Override
    public boolean achieved(Game game) {
        if (game.getMap().getEntities(ZombieToastSpawner.class).isEmpty()
        && game.getDefeatedEnemiesCount() >= target) {
            return true;
        }
        return false;
    }

    @Override
    public String toString(Game game) {
        return ":enemies";
    }
}
