package fr.ul.acl.model;

import java.util.List;

import fr.ul.acl.view.VisualEffect;

public class Mechanism extends Trigger{
    private static final VisualEffect SECRET_WALL = new VisualEffect("texture/secret_wall.gif");
    private List<Trigger> requirements;

    public Mechanism(VisualEffect visual) {
        super(visual);
    }

    public static Mechanism secret_wall(){
        return new Mechanism(SECRET_WALL);
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