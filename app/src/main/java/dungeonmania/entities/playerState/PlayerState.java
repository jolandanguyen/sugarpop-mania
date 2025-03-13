package dungeonmania.entities.playerState;

import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.potions.*;;

public abstract class PlayerState {
    private Player player;
    private String state = "Base";

    PlayerState(Player player, String newState) {
        this.player = player;
        this.state = newState;
    }

    public boolean isInvincible() {
        if (state.compareTo("Invincible") == 0) {
            return true;
        }
        return false;
    };

    public boolean isInvisible() {
        if (state.compareTo("Invisible") == 0) {
            return true;
        }
        return false;
    };

    public Player getPlayer() {
        return player;
    }

    public void transition(Potion potion) {
        Player player = getPlayer();
        if (potion instanceof InvisibilityPotion) {
            player.changeState(new InvisibleState(player));
        } else if (potion instanceof InvincibilityPotion) {
            player.changeState(new InvincibleState(player));
        } else {
            player.changeState(new BaseState(player));
        }
    }
}
