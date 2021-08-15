import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;
/**
 * Player class is a subclass of GameObject
 * It has variables storing its step size and energy
 * It has a constructor for its initialisation, and associated functions to update, draw and get its energy
 */
public class Player extends GameObject{

    //storing energy and step-size of player
    private int energy;
    private final int STEP_SIZE=10;

    //draw option and font for energy
    private final Font font=new Font("res/font/DejaVuSans-Bold.ttf",20);
    private final DrawOptions OPT= new DrawOptions();

    /**
     * Constructor method for Player updates location,energy and step-size
     * @param x x-coordinate of player
     * @param y y-coordinate of player
     * @param energy energy level of player
     */

    public Player(double x, double y, int energy) {
        set_filename("res/images/player.png");
        set_position(x,y);
        set_step_size(STEP_SIZE);
        this.energy = energy;
    }

    /**
     * method to display energy of player at desired location
     */

    public void draw_energy(){
        font.drawString("energy: "+energy,20,760,OPT.setBlendColour(Colour.BLACK));

    }

    /**
     * get method to get player's energy
     * @return energy player's energy level
     */

    public int getEnergy(){
        return energy;
    }

    /**
     * updates player's energy when it interacts with a sandwich
     */

    public void eatSandwich(){
        energy += 5;
    }

    /**
     * updates player's energy when it interacts with a zombie
     */

    public void reachZombie(){
        energy -= 3;
    }
}

