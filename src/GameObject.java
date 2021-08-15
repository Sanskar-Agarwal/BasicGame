import bagel.*;
import bagel.util.Point;

/**
 * GameObject is a superclass that is inherited by other classes.
 * It provides a prototype for all objects in our game Shadow Treasure.
 * It contains methods and variables that are common to  objects used
 */

public class GameObject {

    //Image, position,filepath and existence variable of game objects
    public Image image;
    private Point pos;
    private String FILENAME;
    private boolean exists= true;

    //Step-size and direction variables for player and bullet movement
    private int STEP_SIZE;
    double directionX;
    double directionY;


    /**
     * setter method to set filename
     * @param s string which contains filepath
     */

    public void set_filename(String s) {
        this.FILENAME = s;
    }

    /**
     * setter method to set position of object
     * @param x x coordinate of object
     * @param y y coordinate of object
     */

    public void set_position(double x,double y){
        this.pos= new Point(x,y);
    }

    /**
     * setter method to set step size of object
     * @param x defines the step size of object
     */

    public void set_step_size(int x){
        this.STEP_SIZE=x;

    }

    /**
     * sets existence to false ie death
     */

    public void set_death(){
        this.exists=false;
    }

    /**
     * checks if object still exists
     * @return exists variable defining existence
     */

    public boolean does_exist(){
        return exists;
    }

    /**
     * method to get position
     * @return pos the current position of object
     */

    public Point get_position(){
        return this.pos;
    }

    /**
     * This method is taken from Project 1 Solution
     * Points to a destination
     * @param dest destination we want to point to
     */

    public void pointTo(Point dest){
        this.directionX = dest.x-this.pos.x;
        this.directionY = dest.y-this.pos.y;
        normalizeD();
    }

    /**
     * This method is taken from Project 1 Solution
     * normalizes direction
     */

    public void normalizeD(){
        double len = Math.sqrt(Math.pow(this.directionX,2)+Math.pow(this.directionY,2));
        this.directionX /= len;
        this.directionY /= len;
    }

    /**
     * Draws image of object
     */

    public void draw(){
        this.image = new Image(FILENAME);
        image.drawFromTopLeft(pos.x,pos.y);
    }

    /**
     * moves object to new position in defined step size
     */

    public void move(){
        this.pos=new Point(this.pos.x+STEP_SIZE*directionX,this.pos.y+STEP_SIZE*directionY);
    }
}
