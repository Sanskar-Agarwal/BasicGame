/**
 * Bullet Class which is a subclass of GameObject
 * Has a variable to define it's step-size and constructors
 */

public class Bullet extends GameObject {

    //storing step-size of a bullet
    private final int BULLET_STEP=25;

    /**
     * Default Constructor to update strep-size and filepath
     */

    public Bullet(){
        set_step_size(BULLET_STEP);
        set_filename("res/images/shot.png");

    }

}
