package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Sceptre;
import dungeonmania.util.Position;

public class Mercenary extends Bribable {
    public static final int DEFAULT_BRIBE_AMOUNT = 1;
    public static final int DEFAULT_BRIBE_RADIUS = 1;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;

    public Mercenary(Position position, double health, double attack, int bribeAmount, int bribeRadius,
            double allyAttack, double allyDefence) {
        super(position, health, attack, bribeAmount,
            bribeRadius, allyAttack, allyDefence);
    }

    /**
     * bribe the merc
     */
    private void bribe(Player player) {
        if (player.countEntityOfType(Sceptre.class) >= 1) {
            setBribeDuration(Sceptre.getMindControlDuration());
            return;
        }
        int bribeAmount = getBribeAmount();
        for (int i = 0; i < bribeAmount; i++) {
            player.bribe();
        }
        setBribeDuration(-1);

    }

    @Override
    public void interact(Player player, Game game) {
        setAllied(true);
        bribe(player);
        if (!isAdjacentToPlayer() && Position.isAdjacent(player.getPosition(), getPosition()))
            setAdjacentToPlayer(true);
    }
}
