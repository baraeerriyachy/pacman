package fr.ul.acl.model;

import javax.swing.ImageIcon;

public class Tresor extends Trigger {

    private static final ImageIcon STATIC_IMG =
    new ImageIcon(Tresor.class.getResource("/texture/tresor.png"));

    public Tresor() {
        super(
            STATIC_IMG,
            new ImageIcon(Tresor.class.getResource("/texture/tresor_open.gif"))
        );
    }

    @Override
    public void activate() {
        if (!activated) {
            activated = true;
            // TODO: Give treasure / reward to Hero
        }
    }
}