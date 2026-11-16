package fr.ul.acl.model;

import java.util.List;

public class TimedGroup {

    private List<Trigger> targets;

    private long duration;

    private long lastTransition;
    private boolean active;

    public TimedGroup(List<Trigger> triggers,
                  boolean active,
                  long duration,
                  long initialDelay) {

        this.targets = triggers;
        this.active = active;
        this.duration = duration;

        this.lastTransition = initialDelay;

        if(active){
            for (Trigger trigger : targets) {
                trigger.setActived();
            }
        }
    }

    public void update(long now) {

        if (now - lastTransition >= duration) {
            active = !active;
            lastTransition = now;

            for (Trigger target : targets) {
                if(active)
                    target.activate();
                else
                    target.deactivate();
            }
        }
    }
}