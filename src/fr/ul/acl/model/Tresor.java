package fr.ul.acl.model;
import fr.ul.acl.view.VisualEffect;

public class Tresor extends Trigger {

    private static final VisualEffect TRESOR = new VisualEffect("texture/tresor.gif");

    public Tresor() {
        super(
            TRESOR
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