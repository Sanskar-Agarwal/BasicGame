/**
 * Zombie class is a subclass of GameObject
 * Class has a constructor for Zombie objects in game
 */

public class Zombie extends GameObject{

    public Zombie(){
        set_filename("res/images/zombie.png");
    }

    /**
     * constructor which gives filepath for sandwich and sets its coordinates
     * @param x x-coordinate of Zombie
     * @param y y-coordinate of Zombie
     */

    public Zombie(double x, double y){
        set_filename("res/images/zombie.png");
        set_position(x,y);
    }

}