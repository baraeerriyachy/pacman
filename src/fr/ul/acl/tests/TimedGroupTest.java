
package fr.ul.acl.tests;

import fr.ul.acl.model.TimedGroup;
import fr.ul.acl.model.Trigger;
import fr.ul.acl.view.VisualEffect;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimedGroupTest {

    private Trigger createTrigger() {
        return new Trigger(VisualEffect.getTexture("trap"));
    }

    @Test
    void constructorActivatesTargetsWhenInitiallyActive() {
        Trigger trigger = createTrigger();

        new TimedGroup(List.of(trigger), true, 1000, 0);

        assertTrue(trigger.isActivated());
    }

    @Test
    void updateDoesNothingBeforeDuration() {
        Trigger trigger = createTrigger();
        TimedGroup group = new TimedGroup(
                List.of(trigger), false, 1000, 0
        );

        group.update(999);

        assertFalse(trigger.isActivated());
    }

    @Test
    void updateActivatesTargetsWhenDurationIsReached() {
        Trigger trigger = createTrigger();
        TimedGroup group = new TimedGroup(
                List.of(trigger), false, 1000, 0
        );

        group.update(1000);

        assertTrue(trigger.isActivated());
    }

    @Test
    void updateDeactivatesTargetsAfterNextDuration() {
        Trigger trigger = createTrigger();
        TimedGroup group = new TimedGroup(
                List.of(trigger), true, 1000, 0
        );

        group.update(1000);

        assertFalse(trigger.isActivated());
    }
}
