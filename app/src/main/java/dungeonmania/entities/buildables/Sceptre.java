package dungeonmania.entities.buildables;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;

public class Sceptre extends Buildable {
    private static int mindControlDuration;

    public Sceptre(int mindControlDuration) {
        super(null);
        Sceptre.mindControlDuration = mindControlDuration;
    }

    public static int getMindControlDuration() {
        return Sceptre.mindControlDuration;
    }

    @Override
    public void setDurability(int durability) {
    }

    @Override
    public void use(Game game) {
    }

    @Override
    public BattleStatistics applyBuff(BattleStatistics origin) {
        return BattleStatistics.applyBuff(origin, new BattleStatistics(0, 0, 0, 1, 1));
    }
}
