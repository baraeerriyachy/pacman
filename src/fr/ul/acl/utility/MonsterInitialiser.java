package fr.ul.acl.utility;

import java.awt.Point;
import fr.ul.acl.model.Monstre;
import fr.ul.acl.model.Plateau;
import fr.ul.acl.model.Position;

public final class MonsterInitialiser {

    private static final int DEFAULT_MOVES = 3;

    private static final Point[] directions = {
            new Point(1, 0),
            new Point(-1, 0),
            new Point(0, 1),
            new Point(0, -1)
        };

    private MonsterInitialiser() {
        // Utility class
    }

    public static void initialize(Monstre m, Plateau plateau) {
        Point direction = directions[
            (int) (Math.random() * directions.length)
        ];

        int max = getMaxMove(m, plateau, direction);

        if (max == 0) {
            direction = new Point(-direction.x, -direction.y);
            max = getMaxMove(m, plateau, direction);
        }

        if (max == 0) {
            // Change axis
            direction = direction.x != 0
                ? new Point(0, 1)
                : new Point(1, 0);

            max = getMaxMove(m, plateau, direction);

            if (max == 0) {
                direction = new Point(-direction.x, -direction.y);
                max = getMaxMove(m, plateau, direction);
            }
        }

        m.setDirection(direction.x, direction.y);
        m.setMaxMove(max);
    }

    private static int getMaxMove(
        Monstre m, Plateau plateau, Point direction
    ) {
        int max = 0;

        for (int i = 1; i <= DEFAULT_MOVES; i++) {

            Position next = new Position(
                m.getPos().x + direction.x * i,
                m.getPos().y + direction.y * i
            );

            if (!plateau.isInsideMap(next)) {
                break;
            }

            if (!m.isIntangible() && !plateau.isFree(next)) {
                break;
            }

            max++;
        }

        return max;
    }
}
