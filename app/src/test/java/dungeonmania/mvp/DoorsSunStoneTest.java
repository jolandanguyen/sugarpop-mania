package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DoorsSunStoneTest {
    @Test
    @Tag("4-1")
    @DisplayName("Test player can pick up a sunstone and add to inventory")
    public void pickUpSunStone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_DoorsKeysTest_pickUpSunStone", "c_DoorsKeysTest_pickUpSunStone");

        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        // pick up sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

    }

    @Test
    @Tag("4-2")
    @DisplayName("Test player can use a sunstone to open and walk through a door")
    public void useSunStoneWalkThroughOpenDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_DoorsKeysTest_useSunStoneWalkThroughOpenDoor",
                "c_DoorsKeysTest_useSunStoneWalkThroughOpenDoor");

        // pick up sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();

        // walk through door & check sunstone retained
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
    }

    @Test
    @Tag("4-3")
    @DisplayName("Test player uses key over sunstone to open and walk through a door")
    public void useSunStoneOrKeyWalkThroughOpenDoor() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_DoorsKeysTest_useSunStoneOrKeyWalkThroughOpenDoor",
                "c_DoorsKeysTest_useSunStoneWalkThroughOpenDoor");

        // pick up sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // pick up key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "key").size());
        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();

        // walk through door
        res = dmc.tick(Direction.RIGHT);

        // key is used to open door, sunstone retained
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
    }

}
