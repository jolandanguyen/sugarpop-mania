package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssassinTest {

    @Test
    @Tag("12-5")
    @DisplayName("Testing a assassin can be bribed with a certain amount")
    public void bribeAmount() {
        //                                                          Wall     Wall     Wall    Wall    Wall
        // P1       P2/Treasure      P3/Treasure    P4/Treasure      M4       M3       M2     M1      Wall
        //                                                          Wall     Wall     Wall    Wall    Wall
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_bribeAmount", "c_assassinTest_bribeAmount");

        String assId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        // pick up first treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(7, 1), getAssassinPos(res));

        // attempt bribe
        assertThrows(InvalidActionException.class, () -> dmc.interact(assId));
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // pick up second treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(6, 1), getAssassinPos(res));

        // attempt bribe
        assertThrows(InvalidActionException.class, () -> dmc.interact(assId));
        assertEquals(2, TestUtils.getInventory(res, "treasure").size());

        // pick up third treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(5, 1), getAssassinPos(res));

        // achieve bribe
        res = assertDoesNotThrow(() -> dmc.interact(assId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
    }

    // @Test
    // @Tag("12-6")
    // @DisplayName("Testing a assassin can be bribed within a radius")
    // public void bribeRadius() {
    //     //                                         Wall     Wall    Wall    Wall  Wall
    //     // P1       P2/Treasure      P3    P4      M4       M3       M2     M1    Wall
    //     //                                         Wall     Wall    Wall    Wall  Wall
    //     DungeonManiaController dmc = new DungeonManiaController();
    //     DungeonResponse res = dmc.newGame("d_assassinTest_bribeRadius", "c_assassinTest_bribeRadius");

    //     String assId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

    //     // pick up treasure
    //     res = dmc.tick(Direction.RIGHT);
    //     assertEquals(1, TestUtils.getInventory(res, "treasure").size());
    //     assertEquals(new Position(7, 1), getAssassinPos(res));

    //     // attempt bribe
    //     assertDoesNotThrow(() -> dmc.interact(assId));
    //     assertEquals(1, TestUtils.getInventory(res, "treasure").size());
    // }

    @Test
    @Tag("12-8")
    @DisplayName("Testing a assassin is bribed next to the player, then follow the player")
    public void allyMovementStick() {
        /**
         * W W W W W W E
         * W T P - - M -
         * W W W W W W -
         *
         * bribe_radius = 100
         * bribe_amount = 1
         */
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_assassinTest_allyMovementStick", "c_assassinTest_allyMovementStick");

        String assId = TestUtils.getEntitiesStream(res, "assassin").findFirst().get().getId();

        // pick up treasure
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(4, 1), getAssassinPos(res));

        // Wait until the assassin is next to the player
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(3, 1), getAssassinPos(res));
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(1, 1), getPlayerPos(res));
        assertEquals(new Position(2, 1), getAssassinPos(res));

        // achieve bribe - fail
        res = assertDoesNotThrow(() -> dmc.interact(assId));
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertEquals(new Position(1, 1), getPlayerPos(res));
    }

    private Position getPlayerPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "player").get(0).getPosition();
    }

    private Position getAssassinPos(DungeonResponse res) {
        return TestUtils.getEntities(res, "assassin").get(0).getPosition();
    }
}
