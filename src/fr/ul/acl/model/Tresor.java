package fr.ul.acl.model;
import fr.ul.acl.view.VisualEffect;

public class Tresor extends Trigger {

    public Tresor() {
        super(
            VisualEffect.getTexture("tresor")
        );
    }

    @Override
    public void activate() {
        if (!activated) {
            // TODO: Give treasure / reward to Hero
        }
        super.activate();
    }
}