package dungeonmania.entities.enemies.movement;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

public class ApproachPlayerMove implements MovementStrategy {
    public ApproachPlayerMove() {
    }

    public Position move(Game game, Entity entity) {
        GameMap map = game.getMap();
        Position nextPos;
        Position currPos = entity.getPosition();
        Position plrDiff = Position.calculatePositionBetween(map.getPlayer().getPosition(), currPos);
        Position moveX = (plrDiff.getX() >= 0) ? Position.translateBy(currPos, Direction.RIGHT)
                : Position.translateBy(currPos, Direction.LEFT);
        Position moveY = (plrDiff.getY() >= 0) ? Position.translateBy(currPos, Direction.UP)
                : Position.translateBy(currPos, Direction.DOWN);
        Position offset = currPos;
        if (plrDiff.getY() == 0 && map.canMoveTo(entity, moveX))
            offset = moveX;
        else if (plrDiff.getX() == 0 && map.canMoveTo(entity, moveY))
            offset = moveY;
        else if (Math.abs(plrDiff.getX()) >= Math.abs(plrDiff.getY())) {
            if (map.canMoveTo(entity, moveX))
                offset = moveX;
            else if (map.canMoveTo(entity, moveY))
                offset = moveY;
            else
                offset = currPos;
        } else {
            if (map.canMoveTo(entity, moveY))
                offset = moveY;
            else if (map.canMoveTo(entity, moveX))
                offset = moveX;
            else
                offset = currPos;
        }
        nextPos = offset;
        return nextPos;
    }
}
