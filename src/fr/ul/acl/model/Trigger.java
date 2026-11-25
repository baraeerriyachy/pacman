package fr.ul.acl.model;

import java.awt.Image;
import java.util.List;
import java.util.ArrayList;
import fr.ul.acl.view.VisualEffect;

public class Trigger {
    protected boolean playingVisual;
    protected boolean activated;
    protected final VisualEffect visual;
    private final List<Mechanism> dependents = new ArrayList<>();

    public void addDependent(Mechanism mechanism) {
        dependents.add(mechanism);
    }

    public Trigger(VisualEffect visual) {
        if (visual == null) {
            throw new IllegalArgumentException("VisualEffect cannot be null.");
        }
        this.visual = visual;
        this.activated = false;
        this.playingVisual = false;
    }

    public void activate() {
        if (!activated) {
            visual.reset();
            playingVisual = true;
            activated = true;

            for(Mechanism m : dependents){
                if(m.canActivate()){
                    m.activate();
                }
            }
        }
    }

    public void deactivate() {
        if (activated) {
            visual.reverse();
            playingVisual = true;
            activated = false;
        }
    }


    public void setActived(){
        activated = true;
        visual.reverse();
    }

    public boolean isActivated() {
        return activated;
    }

    public Image getImage() {
        if(playingVisual)
        {
            if(visual.isFinished()){
                playingVisual = false;
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