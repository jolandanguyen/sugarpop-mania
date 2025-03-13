package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.exceptions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BuildablesTest {
    @Test
    @Tag("5-1")
    @DisplayName("Test IllegalArgumentException is raised when attempting to build an unknown entity - sword")
    public void buildSwordIllegalArgumentException() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        dmc.newGame("d_BuildablesTest_BuildSwordIllegalArgumentException",
                "c_BuildablesTest_BuildSwordIllegalArgumentException");
        assertThrows(IllegalArgumentException.class, () -> dmc.build("sword"));
    }

    @Test
    @Tag("5-2")
    @DisplayName("Test InvalidActionException is raised when the player "
            + "does not have sufficient items to build a bow or shield")
    public void buildInvalidActionException() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        dmc.newGame("d_BuildablesTest_BuildInvalidArgumentException", "c_BuildablesTest_BuildInvalidArgumentException");
        assertThrows(InvalidActionException.class, () -> dmc.build("bow"));

        assertThrows(InvalidActionException.class, () -> dmc.build("shield"));
    }

    @Test
    @Tag("5-3")
    @DisplayName("Test building a bow")
    public void buildBow() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildBow", "c_BuildablesTest_BuildBow");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());

        // Pick up Wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        // Pick up Arrow x3
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(3, TestUtils.getInventory(res, "arrow").size());

        // Build Bow
        assertEquals(0, TestUtils.getInventory(res, "bow").size());
        res = assertDoesNotThrow(() -> dmc.build("bow"));
        assertEquals(1, TestUtils.getInventory(res, "bow").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "arrow").size());
    }

    @Test
    @Tag("5-4")
    @DisplayName("Test building a shield with a key")
    public void buildShieldWithKey() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildShieldWithKey", "c_BuildablesTest_BuildShieldWithKey");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());

        // Pick up Wood x2
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "wood").size());

        // Pick up Key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "key").size());

        // Build Shield
        assertEquals(0, TestUtils.getInventory(res, "shield").size());
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
    }

    @Test
    @Tag("5-5")
    @DisplayName("Test building a shield with treasure")
    public void buildShieldWithTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildShieldWithTreasure",
                "c_BuildablesTest_BuildShieldWithTreasure");
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // Pick up Wood x2
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "wood").size());

        // Pick up Treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // Build Shield
        assertEquals(0, TestUtils.getInventory(res, "shield").size());
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
    }

    @Test
    @Tag("5-6")
    @DisplayName("Test responsse buildables parameter is a list of buildables that the player can currently build")
    public void dungeonResponseBuildables() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_DungeonResponseBuildables",
                "c_BuildablesTest_DungeonResponseBuildables");

        List<String> buildables = new ArrayList<>();
        assertEquals(buildables, res.getBuildables());

        // Gather entities to build bow
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Bow added to buildables list
        buildables.add("bow");
        assertEquals(buildables, res.getBuildables());

        // Gather entities to build shield
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        // Shield added to buildables list
        buildables.add("shield");
        assertEquals(buildables.size(), res.getBuildables().size());
        assertTrue(buildables.containsAll(res.getBuildables()));
        assertTrue(res.getBuildables().containsAll(buildables));

        // Build bow
        res = assertDoesNotThrow(() -> dmc.build("bow"));
        assertEquals(1, TestUtils.getInventory(res, "bow").size());

        // Bow disappears from buildables list
        buildables.remove("bow");
        assertEquals(buildables, res.getBuildables());

        // Build shield
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // Shield disappears from buildables list
        buildables.remove("shield");
        assertEquals(buildables, res.getBuildables());
    }

    @Test
    @Tag("5-7")
    @DisplayName("Test building shield with sunstone")
    public void buildShieldWithSunStone() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildShieldWithSunStone",
                "c_BuildablesTest_BuildShieldWithSunStone");
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Wood x2
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "wood").size());

        // Pick up Treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Shield
        assertEquals(0, TestUtils.getInventory(res, "shield").size());
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
    }

    @Test
    @Tag("5-8")
    @DisplayName("Test build shield with treasure when exists sunstone")
    public void buildShieldWithSunStoneWithExistsTreasure() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildShieldWithTreasureAndSunStone",
                "c_BuildablesTest_BuildShieldWithTreasureAndSunStone");
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());

        // Pick up Wood x2
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "wood").size());

        // Pick up SunStone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Treasure
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "treasure").size());

        // Build Shield
        assertEquals(0, TestUtils.getInventory(res, "shield").size());
        res = assertDoesNotThrow(() -> dmc.build("shield"));
        assertEquals(1, TestUtils.getInventory(res, "shield").size());

        // Materials used in construction disappear from inventory
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
    }

    @Test
    @Tag("5-9")
    @DisplayName("Test build sceptre")
    public void buildingSceptre() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildSceptre", "c_BuildablesTest_BuildSceptre");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        // Pick up key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "key").size());

        // Pick up sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Sceptre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));

        // Check items used
        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Player now has sceptre
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());
    }

    @Test
    @Tag("5-10")
    @DisplayName("Test build sceptre unsuccessful")
    public void buildingSceptreUnsuccessful() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_BuildSceptre", "c_BuildablesTest_BuildSceptre");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "key").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        assertThrows(InvalidActionException.class, () -> dmc.build("sceptre"));
        // Pick up wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        assertThrows(InvalidActionException.class, () -> dmc.build("sceptre"));

        // Pick up key
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "key").size());

        assertThrows(InvalidActionException.class, () -> dmc.build("sceptre"));
    }

    @Test
    @Tag("5-11")
    @DisplayName("Test bribe using sceptre successful")
    public void usingSceptre() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_UsingSceptre", "c_BuildablesTest_UsingSceptre");

        assertEquals(0, TestUtils.getInventory(res, "wood").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());
        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());

        String mercId = TestUtils.getEntitiesStream(res, "mercenary").findFirst().get().getId();
        // Pick up Wood
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "wood").size());

        // Pick up Sun Stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up Sun Stone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getInventory(res, "sun_stone").size());

        // Create sceptre
        assertEquals(0, TestUtils.getInventory(res, "sceptre").size());
        res = assertDoesNotThrow(() -> dmc.build("sceptre"));
        assertEquals(1, TestUtils.getInventory(res, "sceptre").size());

        assertEquals(1, TestUtils.getEntities(res, "mercenary").size());

        assertEquals(new Position(2, 1), TestUtils.getEntities(res, "player").get(0).getPosition());
        assertEquals(new Position(4, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());

        // Try mind control
        assertEquals(0, TestUtils.getInventory(res, "treasure").size());
        assertDoesNotThrow(() -> dmc.interact(mercId));

        // Is allied and is following player
        res = dmc.tick(Direction.RIGHT);
        assertEquals(0, res.getBattles().size());
        assertEquals(new Position(3, 1), TestUtils.getEntities(res, "player").get(0).getPosition());
        assertEquals(new Position(2, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());

        // 1 tick
        res = dmc.tick(Direction.RIGHT);
        assertEquals(new Position(4, 1), TestUtils.getEntities(res, "player").get(0).getPosition());
        assertEquals(new Position(3, 1), TestUtils.getEntities(res, "mercenary").get(0).getPosition());

        // No mindcontrol, fight;
        res = dmc.tick(Direction.LEFT);
        assertEquals(1, res.getBattles().size());
        assertEquals(0, TestUtils.getEntities(res, "mercenary").size());
    }

    @Test
    @Tag("5-12")
    @DisplayName("Test build midnight armour")
    public void buildMidnightArmour() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_buildMidnightArmour",
                "c_BuildablesTest_buildMidnightArmour");

        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // Pick up sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Midnight Armour
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        res = assertDoesNotThrow(() -> dmc.build("midnight_armour"));

        // Check items used
        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Player now has midnight armout
        assertEquals(1, TestUtils.getInventory(res, "midnight_armour").size());
    }

    @Test
    @Tag("5-13")
    @DisplayName("Test build midnight armour unsuccessful")
    public void buildMidnightArmourUnsuccessful() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_buildMidnightArmour",
                "c_BuildablesTest_buildMidnightArmour");

        assertEquals(0, TestUtils.getInventory(res, "sword").size());

        // Pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Build Midnight Armour with no sunStone
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));

        // Check items
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Player has no midnight armour
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
    }

    @Test
    @Tag("5-14")
    @DisplayName("Test build midnight armour unsuccessful with zombie")
    public void buildMidnightArmourUnsuccessfulWithZombie() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_BuildablesTest_buildMidnightArmourZombies",
                "c_BuildablesTest_buildMidnightArmourZombies");

        assertEquals(0, TestUtils.getInventory(res, "sword").size());
        assertEquals(0, TestUtils.getInventory(res, "sun_stone").size());

        // Pick up sword
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, getZombies(res).size());
        // Pick up sunstone
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Build Midnight Armour
        assertThrows(InvalidActionException.class, () -> dmc.build("midnight_armour"));

        // Check items used
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        assertEquals(1, TestUtils.getInventory(res, "sun_stone").size());

        // Player has no midnight armour
        assertEquals(0, TestUtils.getInventory(res, "midnight_armour").size());
    }

    private List<EntityResponse> getZombies(DungeonResponse res) {
        return TestUtils.getEntities(res, "zombie_toast");
    }
}
