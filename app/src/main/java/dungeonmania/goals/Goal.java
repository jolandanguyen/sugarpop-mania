package dungeonmania.goals;

import dungeonmania.Game;

public class Goal {
    private String type;
    private GoalStrategy strategy;
    private int target;
    private Goal goal1;
    private Goal goal2;

    public Goal(String type) {
        this.type = type;
    }

    public Goal(String type, int target) {
        this.type = type;
        this.target = target;
    }

    public Goal(String type, Goal goal1, Goal goal2) {
        this.type = type;
        this.goal1 = goal1;
        this.goal2 = goal2;
    }

    public int getTarget() {
        return target;
    }

    public void findGoalStrategy() {
        switch (type) {
            case "exit":
                strategy = new ExitGoal();
                break;
            case "boulders":
                strategy = new BoudlersGoal();
                break;
            case "treasure":
                strategy = new TreasureGoal(target);
                break;
            case "enemies":
                strategy = new EnemiesGoal(target);
                break;
            case "AND":
                strategy = new ANDGoal(goal1, goal2);
                break;
            case "OR":
                strategy = new ORGoal(goal1, goal2);
                break;
            default:
                strategy = new DefaultGoal();
                break;
        }
    }

    public GoalStrategy getGoalStrategy() {
        return strategy;
    }

    /**
     * @return true if the goal has been achieved, false otherwise
     */
    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;
        findGoalStrategy();
        return strategy.achieved(game);

    }

    public String toString(Game game) {
        findGoalStrategy();
        if (this.achieved(game))
            return "";
        return strategy.toString(game);
    }

}
