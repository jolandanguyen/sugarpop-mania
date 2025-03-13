package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.entities.enemies.movement.AlliedMovement;
import dungeonmania.entities.enemies.movement.ApproachPlayerMove;
import dungeonmania.entities.enemies.movement.FollowHostile;
import dungeonmania.entities.enemies.movement.RandomMove;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;
import dungeonmania.battles.BattleStatistics;

public abstract class Bribable extends Enemy implements Interactable {

    private double allyAttack;
    private double allyDefence;
    private boolean allied = false;
    private boolean isAdjacentToPlayer = false;
    private int bribeDuration = -1;
    private int bribeRadius = 1;
    private int bribeAmount = 1;
    // public BribeBehaviour bribebehaviour = null;

    public Bribable(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double allyAttack, double allyDefence) {
        super(position, health, attack);
        this.bribeAmount = bribeAmount;
        this.bribeRadius = bribeRadius;
        this.allyAttack = allyAttack;
        this.allyDefence = allyDefence;
    }

    public double getAllyAttack() {
        return allyAttack;
    }

    public void setAllyAttack(double allyAttack) {
        this.allyAttack = allyAttack;
    }

    public double getAllyDefence() {
        return allyDefence;
    }

    public void setAllyDefence(double allyDefence) {
        this.allyDefence = allyDefence;
    }

    public boolean isAllied() {
        return allied;
    }

    public void setAllied(boolean allied) {
        this.allied = allied;
    }

    public boolean isAdjacentToPlayer() {
        return isAdjacentToPlayer;
    }

    public void setAdjacentToPlayer(boolean isAdjacentToPlayer) {
        this.isAdjacentToPlayer = isAdjacentToPlayer;
    }

    public int getBribeDuration() {
        return bribeDuration;
    }

    public void setBribeDuration(int bribeDuration) {
        this.bribeDuration = bribeDuration;
    }

    public int getBribeRadius() {
        return bribeRadius;
    }

    public void setBribeRadius(int bribeRadius) {
        this.bribeRadius = bribeRadius;
    }

    public int getBribeAmount() {
        return bribeAmount;
    }

    public void setBribeAmount(int bribeAmount) {
        this.bribeAmount = bribeAmount;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (allied)
            return;
        super.onOverlap(map, entity);
    }

    /**
     * check whether the current merc can be bribed
     * @param player
     * @return
     */
    private boolean canBeBribed(Player player) {
        if (player.countEntityOfType(Sceptre.class) >= 1) {
            return true;
        }
        int treasures = player.countEntityOfType(Treasure.class);
        int sunStones = player.countEntityOfType(SunStone.class);
        return ((bribeRadius >= 0) && (treasures >= bribeAmount) && (treasures > sunStones));
    }

    @Override
    public boolean isInteractable(Player player) {
        return !allied && canBeBribed(player);
    }

        @Override
    public void move(Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        calculateMovement(map);
        nextPos = getMovementStrategy().move(game, this);
        map.moveTo(this, nextPos);
        bribeDuration -= 1;
        if (bribeDuration == 0) {
            allied = false;
        }
    }

    public void calculateMovement(GameMap map) {
        if (allied) {
            setMovementStrategy(new AlliedMovement(isAdjacentToPlayer));

        } else if (map.getPlayer().getEffectivePotion() instanceof InvisibilityPotion) {
            setMovementStrategy(new RandomMove());

        } else if (map.getPlayer().getEffectivePotion() instanceof InvincibilityPotion) {
            setMovementStrategy(new ApproachPlayerMove());

        } else {
            setMovementStrategy(new FollowHostile());
        }
    }
    @Override
    public BattleStatistics getBattleStatistics() {
        if (!isAllied())
            return super.getBattleStatistics();
        return new BattleStatistics(0, getAllyAttack(), getAllyDefence(), 1, 1);
    }

    public void updateIsAdjacentToPlayer(Position pos, Player player) {
        if (!isAdjacentToPlayer() && Position.isAdjacent(player.getPosition(), pos)) {
            setAdjacentToPlayer(true);
        }
    }
}
