package fr.ul.acl.model;

import javax.swing.ImageIcon;

public class Lever extends Trigger {

    private static final ImageIcon STATIC_IMG =
    new ImageIcon(Lever.class.getResource("/texture/lever.png"));

    public Lever() {
        super(
            STATIC_IMG,
            new ImageIcon(Lever.class.getResource("/texture/lever_on.gif"))
        );
    }

    @Override
    public void activate() {
        if (!activated) {
            activated = true;
            // TODO: Open door / activate mechanism
        }
    }
}