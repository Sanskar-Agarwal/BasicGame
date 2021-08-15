/**
 * Treasure class is a subclass of GameObject
 * Class has a constructor for Treasure objects in game
 */


public class Treasure extends GameObject{

    public Treasure(){
        set_filename("res/images/treasure.png");
    }

    /**
     * constructor which gives filepath for Treasure and sets its coordinates
     * @param x x-coordinate of Treasure
     * @param y y-coordinate of Treasure
     */

    public Treasure(double x,double y){
        set_filename("res/images/treasure.png");
        set_position(x,y);
    }
}

