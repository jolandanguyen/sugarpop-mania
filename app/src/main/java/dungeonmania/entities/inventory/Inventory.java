package dungeonmania.entities.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.Player;
import dungeonmania.entities.buildables.Bow;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.SunStone;
import dungeonmania.entities.collectables.Sword;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;
import dungeonmania.entities.enemies.ZombieToast;
import dungeonmania.map.GameMap;

public class Inventory {
    private List<InventoryItem> items = new ArrayList<>();
    private GameMap map = new GameMap();

    public void setMap(GameMap map) {
        this.map = map;
    }

    public GameMap getMap() {
        return map;
    }

    public boolean add(InventoryItem item) {
        items.add(item);
        return true;
    }

    public void remove(InventoryItem item) {
        items.remove(item);
    }

    public List<String> getBuildables() {

        int wood = count(Wood.class);
        int arrows = count(Arrow.class);
        int treasure = count(Treasure.class);
        int keys = count(Key.class);
        int sunStones = count(SunStone.class);
        int swords = count(Sword.class);
        int zombies = map.getEntities(ZombieToast.class).size();

        List<String> result = new ArrayList<>();

        if (wood >= 1 && arrows >= 3) {
            result.add("bow");
        }
        if (wood >= 2 && (treasure >= 1 || keys >= 1 || sunStones >= 1)) {
            result.add("shield");
        }
        if ((wood >= 1 || arrows >= 2) && ((treasure >= 1 && sunStones < treasure) || keys >= 1 || sunStones >= 2)
                && sunStones >= 1) {
            result.add("sceptre");
        }
        if (swords >= 1 && sunStones >= 1 && zombies == 0) {
            result.add("midnight_armour");
        }
        return result;
    }

    public InventoryItem checkBuildCriteria(Player p, boolean remove, boolean forceShield, EntityFactory factory) {
        List<Wood> wood = getEntities(Wood.class);
        List<Arrow> arrows = getEntities(Arrow.class);
        List<Treasure> treasure = getEntities(Treasure.class);
        List<Key> keys = getEntities(Key.class);
        List<SunStone> sunStones = getEntities(SunStone.class);
        List<Sword> swords = getEntities(Sword.class);
        int zombies = map.getEntities(ZombieToast.class).size();

        if (wood.size() >= 1 && arrows.size() >= 3 && !forceShield) {
            if (remove) {
                items.remove(wood.get(0));
                items.remove(arrows.get(0));
                items.remove(arrows.get(1));
                items.remove(arrows.get(2));
            }
            return factory.buildBow();

        } else if (wood.size() >= 2 && (treasure.size() >= 1 || keys.size() >= 1 || sunStones.size() >= 1)) {
            if (remove) {
                items.remove(wood.get(0));
                items.remove(wood.get(1));
                // use treasure items first before use sunStone
                if (treasure.size() >= 1 && (treasure.size() > sunStones.size())) {
                    for (Treasure t : treasure) {
                        if (!(t instanceof SunStone)) {
                            items.remove(t);
                            break;
                        }
                    }
                } else if (keys.size() >= 1) {
                    items.remove(keys.get(0));
                }
                //sunStones retained after use, no need to remove
            }
            return factory.buildShield();
        } else if ((wood.size() >= 1 || arrows.size() >= 2)
                && (((treasure.size() >= 1) && (sunStones.size() < treasure.size())) || keys.size() >= 1
                        || sunStones.size() >= 2)
                && sunStones.size() >= 1) {
            if (remove) {
                if (wood.size() >= 1) {
                    items.remove(wood.get(0));
                } else {
                    items.remove(arrows.get(0));
                    items.remove(arrows.get(1));
                }
                if (treasure.size() >= 1 && sunStones.size() < treasure.size()) {
                    for (Treasure t : treasure) {
                        if (!(t instanceof SunStone)) {
                            items.remove(t);
                            break;
                        }
                    }
                } else if (keys.size() >= 1) {
                    items.remove(keys.get(0));
                }
                items.remove(sunStones.get(0));
            }
            return factory.buildSceptre();
        } else if ((swords.size() >= 1 && sunStones.size() >= 1 && zombies == 0)) {
            items.remove(swords.get(0));
            items.remove(sunStones.get(0));
            return factory.buildMidnightArmour();
        }
        return null;
    }

    public <T extends InventoryItem> T getFirst(Class<T> itemType) {
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                return itemType.cast(item);
        return null;
    }

    public <T extends InventoryItem> int count(Class<T> itemType) {
        int count = 0;
        for (InventoryItem item : items)
            if (itemType.isInstance(item))
                count++;
        return count;
    }

    public Entity getEntity(String itemUsedId) {
        for (InventoryItem item : items)
            if (((Entity) item).getId().equals(itemUsedId))
                return (Entity) item;
        return null;
    }

    public List<Entity> getEntities() {
        return items.stream().map(Entity.class::cast).collect(Collectors.toList());
    }

    public <T> List<T> getEntities(Class<T> clz) {
        return items.stream().filter(clz::isInstance).map(clz::cast).collect(Collectors.toList());
    }

    public boolean hasWeapon() {
        return getFirst(Sword.class) != null || getFirst(Bow.class) != null;
    }

    public BattleItem getWeapon() {
        BattleItem weapon = getFirst(Sword.class);
        if (weapon == null)
            return getFirst(Bow.class);
        return weapon;
    }

}
