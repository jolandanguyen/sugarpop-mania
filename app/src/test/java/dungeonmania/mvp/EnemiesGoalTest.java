package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
// import dungeonmania.util.Position;
// import dungeonmania.response.models.BattleResponse;
// import dungeonmania.response.models.RoundResponse;
import dungeonmania.response.models.EntityResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EnemiesGoalTest {
    @Test
    @Tag("4-1")
    @DisplayName("Test victory when player defeats one spider")
    public void defeatOneSpider() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse response = controller.newGame(
            "d_EnemiesGoalTest_oneSpiderDies", "c_EnemiesGoalTest_oneSpiderDies");
        List<EntityResponse> entities = response.getEntities();
        assertTrue(TestUtils.countEntityOfType(entities, "spider") == 1);
        response = controller.tick(Direction.RIGHT);
        entities = response.getEntities();
        System.out.println(TestUtils.countEntityOfType(entities, "spider"));
        assertTrue(TestUtils.countEntityOfType(entities, "spider") == 0);

        assertEquals("", TestUtils.getGoals(response));

    }

    @Test
    @Tag("4-2")
    @DisplayName("Test victory when player defeats all zombie toast spawners")
    public void defeatTwoZombieToastSpawners() {
         //  PLA  ZTS   ZTS
        //  SWO
        DungeonManiaController dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame(
            "d_EnemiesGoalTest_ZombieToastSpawnerDies", "c_EnemiesGoalTest_ZombieToastSpawnerDies");
        assertEquals(2, TestUtils.getEntities(res, "zombie_toast_spawner").size());
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();
        String spawnerId2 = TestUtils.getEntities(res, "zombie_toast_spawner").get(1).getId();
        // System.out.println(spawnerId);
        // pick up sword
        res = dmc.tick(Direction.DOWN);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());

        // move right
        res = dmc.tick(Direction.RIGHT);
        // cardinally adjacent: true, has sword: true
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(1, TestUtils.countType(res, "zombie_toast_spawner"));
        List<EntityResponse> entities = res.getEntities();
        System.out.println(TestUtils.countEntityOfType(entities, "zombie_toast_spawner"));
        res = dmc.tick(Direction.RIGHT);
        res = assertDoesNotThrow(() -> dmc.interact(spawnerId2));
        assertEquals(0, TestUtils.countType(res, "zombie_toast_spawner"));

        assertEquals("", TestUtils.getGoals(res));
    }

}
