package dungeonmania.entities.enemies.movement;

import dungeonmania.util.Position;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

public class RandomMove implements MovementStrategy {
    public RandomMove() {
    }

    public Position move(Game game, Entity entity) {
        Random randGen = new Random();
        Position nextPos;
        GameMap map = game.getMap();
        List<Position> pos = entity.getPosition().getCardinallyAdjacentPositions();
        pos = pos.stream().filter(p -> map.canMoveTo(entity, p)).collect(Collectors.toList());
        if (pos.size() == 0) {
            nextPos = entity.getPosition();
        } else {
            nextPos = pos.get(randGen.nextInt(pos.size()));
        }
        return nextPos;
    }
}
