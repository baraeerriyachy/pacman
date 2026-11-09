package fr.ul.acl.model;

import javax.swing.ImageIcon;
import java.awt.Image;

public abstract class Trigger {

    protected boolean activated;

    protected ImageIcon activationImg;
    protected ImageIcon staticImg;

    public Trigger(ImageIcon staticImg, ImageIcon activationImg) {
        this.staticImg = staticImg;
        this.activationImg = activationImg;
        this.activated = false;
    }

    public abstract void activate();

    public boolean isActivated() {
        return activated;
    }

    public Image getImage() {
        return activated ? activationImg.getImage() : staticImg.getImage();
    }
}