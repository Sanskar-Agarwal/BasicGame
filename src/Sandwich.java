/**
 * Sandwich class is a subclass of GameObject
 * Class has a constructor for Sandwich objects in game
 */
public class Sandwich extends GameObject{
    public Sandwich(){
        set_filename("res/images/sandwich.png");
    }

    /**
     * constructor which gives filepath for sandwich and sets its coordinates
     * @param x x-coordinate of sandwich
     * @param y y-coordinate of sandwich
     */

    public Sandwich(double x, double y){
        set_position(x,y);
        set_filename("res/images/sandwich.png");
    }

}
