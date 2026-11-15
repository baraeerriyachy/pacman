package fr.ul.acl.model;

import java.awt.Image;
import java.util.List;
import java.util.ArrayList;
import fr.ul.acl.view.VisualEffect;

public class Trigger {
    private static final VisualEffect BEAR_TRAP = new VisualEffect("texture/bear_trap.gif");
    private static final VisualEffect LEVER = new VisualEffect("texture/lever.gif");

    protected boolean playingVisual;
    protected boolean activated;
    protected final VisualEffect visual;
    private final List<Mechanism> dependents = new ArrayList<>();

    public void addDependent(Mechanism mechanism) {
        dependents.add(mechanism);
    }

    public Trigger(VisualEffect visual) {
        this.visual = visual;
        this.activated = false;
        this.playingVisual = false;
    }

    public static Trigger bear_trap() {
        return new Trigger(BEAR_TRAP);
    }

    public static Trigger lever() {
        return new Trigger(LEVER);
    }


    public void activate() {
        if (!activated) {
            visual.reset();
            playingVisual = true;
        }
    }

    public boolean isActivated() {
        return activated;
    }

    public Image getImage() {
        if(playingVisual)
        {
            if(visual.isFinished()){
                activated = true;
                playingVisual = false;
                for(Mechanism m : dependents){
                    if(m.canActivate()){
                        m.activate();
                    }
                }
            }
            Image i = visual.getCurrentFrame();
            visual.nextFrame();
            return i;
        }

        if(activated)
            return visual.getLastFrame();
        else
            return visual.getFirstFrame();
    }

}