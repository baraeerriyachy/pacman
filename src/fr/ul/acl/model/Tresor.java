package fr.ul.acl.model;
import fr.ul.acl.view.VisualEffect;

public class Tresor extends Trigger {
	
	private boolean collected = false;
	
    public Tresor() {
        super(
            VisualEffect.getTexture("tresor")
        );
    }

    public boolean collect() {
        if (collected) {
            return false;
        }

        collected = true;
        super.activate();
        return true;
    }
    
    
    
    @Override
    public void activate() {
        collect();
    }
}