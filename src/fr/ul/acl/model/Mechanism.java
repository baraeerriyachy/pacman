package fr.ul.acl.model;

import java.util.List;

import fr.ul.acl.view.VisualEffect;

public class Mechanism extends Trigger{
    private List<Trigger> requirements;

    public Mechanism(VisualEffect visual) {
        super(visual);
    }

    public void setRequirements(List<Trigger> requirements) {
        this.requirements = requirements;
        for (Trigger trigger : requirements) {
            trigger.addDependent(this);
        }
    }

    public boolean canActivate() {
        return requirements.stream()
                .allMatch(Trigger::isActivated);
    }
}