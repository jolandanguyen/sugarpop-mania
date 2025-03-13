package dungeonmania.battles;

import java.util.ArrayList;
import java.util.List;

public class HydraBattleStatistics extends BattleStatistics {
    private double hydraHealthIncreaseAmount;
    private double hydraHealthIncreaseRate;
    public HydraBattleStatistics(double health, double attack, double defence, double attackMagnifier,
            double damageReducer, double hydraHealthIncreaseAmount, double hydraHealthIncreaseRate) {
        super(health, attack, defence, attackMagnifier, damageReducer);
        this.hydraHealthIncreaseAmount = hydraHealthIncreaseAmount;
        this.hydraHealthIncreaseRate = hydraHealthIncreaseRate;
    }

    public static List<BattleRound> hydraBattle(BattleStatistics self, HydraBattleStatistics target) {
        List<BattleRound> rounds = new ArrayList<>();
        if (target.isInvincible()) {
            double damageOnSelf = (self.isInvincible()) ? 0 : self.getHealth();
            double damageOnTarget = (target.isInvincible()) ? 0 : target.getHealth();
            self.setHealth((self.isInvincible()) ? self.getHealth() : 0);
            target.setHealth((target.isInvincible()) ? target.getHealth() : 0);
            rounds.add(new BattleRound(-damageOnSelf, -damageOnTarget));
            return rounds;
        }

        while (self.getHealth() > 0 && target.getHealth() > 0) {
            double damageOnSelf = target.getMagnifier() * (target.getAttack() - self.getDefence()) / self.getReducer();
            double damageOnTarget = self.getMagnifier() * (self.getAttack() - target.getDefence())
                    / target.getReducer();
            if (Math.random() < target.hydraHealthIncreaseRate) {
                self.setHealth(self.getHealth() - damageOnSelf);
                target.setHealth(target.getHealth() + target.hydraHealthIncreaseAmount);
            } else {
                self.setHealth(self.getHealth() - damageOnSelf);
                target.setHealth(target.getHealth() - damageOnTarget);
            }
            rounds.add(new BattleRound(-damageOnSelf, -damageOnTarget));
        }
        return rounds;
    }
}
