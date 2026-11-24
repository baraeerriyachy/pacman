
package fr.ul.acl.tests;

import fr.ul.acl.model.Trigger;
import fr.ul.acl.view.VisualEffect;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TriggerTest {

    private Trigger createTrigger() {
        return new Trigger(VisualEffect.getTexture("trap"));
    }

    @Test
    void constructorInitializesTriggerAsInactive() {
        Trigger trigger = createTrigger();

        assertFalse(trigger.isActivated());
    }

    @Test
    void activateChangesStateImmediately() {
        Trigger trigger = createTrigger();

        trigger.activate();

        assertTrue(trigger.isActivated());
    }

    @Test
    void deactivateChangesStateImmediately() {
        Trigger trigger = createTrigger();

        trigger.activate();
        trigger.deactivate();

        assertFalse(trigger.isActivated());
    }

    @Test
    void activateDoesNotChangeStateWhenAlreadyActivated() {
        Trigger trigger = createTrigger();

        trigger.activate();
        trigger.activate();

        assertTrue(trigger.isActivated());
    }

    @Test
    void deactivateDoesNotChangeStateWhenAlreadyInactive() {
        Trigger trigger = createTrigger();

        trigger.deactivate();

        assertFalse(trigger.isActivated());
    }

    @Test
    void setActivedChangesStateImmediately() {
        Trigger trigger = createTrigger();

        trigger.setActived();

        assertTrue(trigger.isActivated());
    }

    @Test
    void canBeActivatedAndDeactivatedMultipleTimes() {
        Trigger trigger = createTrigger();

        trigger.activate();
        assertTrue(trigger.isActivated());

        trigger.deactivate();
        assertFalse(trigger.isActivated());

        trigger.activate();
        assertTrue(trigger.isActivated());
    }
}
