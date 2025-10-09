package com.yoad.arkanoid.geometry;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RectangleTest {

    @Test
    void contains_insideVsClearlyOutside() {
        // Rectangle: x in [100, 180], y in [50, 90]
        Rectangle r = new Rectangle(new Point(100, 50), 80, 40);

        // clearly inside
        assertTrue(r.contains(new Point(120, 60)));
        assertTrue(r.contains(new Point(179.0, 89.0)));

        // clearly outside (farther than any tiny tolerance your impl may allow)
        assertFalse(r.contains(new Point(95.0,  60.0)));  // left
        assertFalse(r.contains(new Point(185.0, 60.0)));  // right
        assertFalse(r.contains(new Point(120.0, 45.0)));  // above
        assertFalse(r.contains(new Point(120.0, 95.0)));  // below
    }
}
