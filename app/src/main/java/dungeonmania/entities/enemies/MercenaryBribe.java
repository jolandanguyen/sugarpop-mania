// package dungeonmania.entities.enemies;

// import dungeonmania.Game;
// import dungeonmania.battles.BattleStatistics;
// import dungeonmania.entities.Entity;
// import dungeonmania.entities.Interactable;
// import dungeonmania.entities.Player;
// import dungeonmania.entities.buildables.Sceptre;
// import dungeonmania.entities.collectables.SunStone;
// import dungeonmania.entities.collectables.Treasure;
// import dungeonmania.entities.collectables.potions.InvincibilityPotion;
// import dungeonmania.entities.collectables.potions.InvisibilityPotion;
// import dungeonmania.entities.enemies.movement.AlliedMovement;
// import dungeonmania.entities.enemies.movement.ApproachPlayerMove;
// import dungeonmania.entities.enemies.movement.FollowHostile;
// import dungeonmania.entities.enemies.movement.RandomMove;
// import dungeonmania.map.GameMap;
// import dungeonmania.util.Position;

// public class MercenaryBribe {
//     private void bribe(Player player) {
//         if (player.countEntityOfType(Sceptre.class) >= 1) {
//             bribeDuration = Sceptre.getMindControlDuration();
//             return;
//         }
//         for (int i = 0; i < bribeAmount; i++) {
//             player.bribe();
//         }
//         bribeDuration = -1;

//     }
// }
