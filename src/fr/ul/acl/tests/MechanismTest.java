
package fr.ul.acl.tests;

import fr.ul.acl.model.Mechanism;
import fr.ul.acl.model.Trigger;
import fr.ul.acl.view.VisualEffect;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MechanismTest {

    private Trigger createTrigger() {
        return new Trigger(VisualEffect.getTexture("trap"));
    }

    @Test
    void canActivateWhenAllRequirementsAreActivated() {
        Trigger trigger1 = createTrigger();
        Trigger trigger2 = createTrigger();

        trigger1.activate();
        trigger2.activate();

        Mechanism mechanism = new Mechanism(
                VisualEffect.getTexture("trap")
        );
        mechanism.setRequirements(List.of(trigger1, trigger2));

        assertTrue(mechanism.canActivate());
    }

    @Test
    void cannotActivateWhenOneRequirementIsInactive() {
        Trigger trigger1 = createTrigger();
        Trigger trigger2 = createTrigger();

        trigger1.activate();

        Mechanism mechanism = new Mechanism(
                VisualEffect.getTexture("trap")
        );
        mechanism.setRequirements(List.of(trigger1, trigger2));

        assertFalse(mechanism.canActivate());
    }
}
