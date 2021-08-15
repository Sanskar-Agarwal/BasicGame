import bagel.*;
import bagel.Image;
import bagel.Window;
import java.util.ArrayList;
import java.io.*;
import java.text.DecimalFormat;


/**
 * ShadowTreasure is the class which contains our main method and update method running simulation of game
 * This implements a game where a player is playing with the objective of killing all zombies and reaching a treasure
 * Killing a zombie consumes energy, sandiwches are present to help the player increase energy
 * outputs csv file with location of bullet, console outputs players location
 * and energy-level along with success if player was successful
 */

public class ShadowTreasure extends AbstractGame {

    //final value attributes for gme
    private final Image BACKGROUND = new Image("res/images/background.png");
    private static final int DEATH_RANGE = 25;
    private static final int SHOOTING_RANGE=150;

    //MAX_DISTANCE is  more than diagonal length of screen 1024 pixel x 768pixel
    private static final int MAX_DISTANCE=12801;
    private static final int KILL_RANGE=50;
    private static final int ZOMBIE_ENERGY=3;

    // for rounding double number
    private static DecimalFormat df = new DecimalFormat("0.00");

    //counting variables used
    private int zombies_dead=0;
    private int sandwiches_eaten=0;
    private int closest_zindex;
    private int closest_sindex;

    //check if shot has been fired
    private boolean hasfired=false;


    // tick cycle and var
    private final int TICK_CYCLE = 10;
    private int tick;

    // list of characters
    private Player player ;
    private ArrayList<Sandwich> sandwiches= new ArrayList<>();
    private ArrayList<Zombie> zombies = new ArrayList<>();
    private Treasure treasure;
    private  Bullet bullet = new Bullet();

    // end of game indicator
    private boolean endOfGame;


    /**
     * method to load environment, set start of game and initialise ticks
     */
    public ShadowTreasure() throws IOException {
        this.loadEnvironment("res/IO/environment.csv");
        this.tick = 1;
        this.endOfGame = false;

    }

    /**
     * Used to set end of game
     * @param endOfGame boolean value passed to indicate game has ended
     */

    public void setEndOfGame(boolean endOfGame) {
        this.endOfGame = endOfGame;
    }

    /**
     * This method has been taken from Project 1 Sample Solution and slightly modified
     * Load an from input from filename and store relevant data in objects
     * @param filename name of file we are reading
     */
    private void loadEnvironment(String filename){
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String type = parts[0];
                type = type.replaceAll("[^a-zA-Z0-9]", ""); // remove special characters
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                switch (type) {
                    case "Player" -> this.player = new Player(x, y, Integer.parseInt(parts[3]));
                    case "Zombie"  -> zombies.add(new Zombie(x,y));
                    case "Sandwich"  -> sandwiches.add(new Sandwich(x,y));
                    case "Treasure" -> this.treasure= new Treasure(x,y);
                    default    -> throw new BagelError("Unknown type: " + type);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Used to draw all objects that still exists in the game
     */

    public void draw_existing(){
        for(Sandwich sandwich: sandwiches){
            if(sandwich.does_exist()){
                sandwich.draw();
            }
        }
        for(Zombie zombie: zombies){
            if(zombie.does_exist()){
                zombie.draw();
            }
        }
        if(treasure.does_exist()){
            treasure.draw();
        }
        if(player.does_exist()){
            player.draw();
            player.draw_energy();
        }
    }

    /**
     * Takes x and y coordinates of two objects and returns their distance
     * @param x1 x-coordinate of object 1
     * @param y1 y-coordinate of object 1
     * @param x2 x-coordinate of object 2
     * @param y2 y-coordinate of object 2
     * @return distance the distance between the two objects
     */

    public double distance(double x1,double y1, double x2, double y2){
        double distance;
        distance = Math.sqrt(Math.pow((x1-x2),2)+Math.pow((y1-y2),2));
        return distance;

    }

    /**
     * Checks which zombies are still alive and returns the index of closest zombie
     * @return closest_index index of closest zombie
     */

    public int closest_zindex(){
        int index=0;
        int closest_index=0;
        double dist;
        double min_dist=MAX_DISTANCE;
        for(Zombie zombie: zombies){
            index++;
            if(zombie.does_exist()){
                dist=distance(player.get_position().x,player.get_position().y,zombie.get_position().x,zombie.get_position().y);
                if(dist<min_dist){
                    min_dist=dist;
                    closest_index= (index-1);
                }
            }

        }
        return closest_index;
    }

    /**
     * Checks which sandwiches are still un-eaten and returns the index of closest sandwich
     * @return closest_index index of closest sandwich
     */

    public int closest_sindex(){
        int index=0;
        int closest_index=0;
        double dist;
        double min_dist=MAX_DISTANCE;
        for(Sandwich sandwich: sandwiches){
            index++;
            if(sandwich.does_exist()){
                dist=distance(player.get_position().x,player.get_position().y,sandwich.get_position().x,sandwich.get_position().y);
                if(dist<=min_dist){
                    min_dist=dist;
                    closest_index= (index-1);
                }
            }

        }
        return closest_index;
    }

    /**
     * Used to set death of all alive objects once the game is about to end
     */
    public void set_death(){
        for(Sandwich sandwich: sandwiches){
            sandwich.set_death();
        }
        for(Zombie zombie: zombies){
            zombie.set_death();
        }
        treasure.set_death();
        player.set_death();
    }

    /**
     * This function is called if either the game has no zombies or sandwiches
     */

    public void empty_input(){
        if(sandwiches.isEmpty()){
            sandwiches.add(new Sandwich(0.0,0.0));
            sandwiches.get(0).set_death();
            sandwiches_eaten++;
        }
        if(zombies.isEmpty()){
            zombies.add(new Zombie(0.0,0.0));
            zombies.get(0).set_death();
            zombies_dead++;
        }
    }

    /**
     * method to save our record into file in chosen filepath
     * https://www.youtube.com/watch?v=lp0xQXUEw-k : code taken from this tutorial and modfied
     * @param x x-value to be written in file
     * @param y y-value to be written in file
     * @param filepath path of file we want to create or write into
     */

    public void saveRecord(double x,double y,String filepath){
        String x_s=String.valueOf(df.format(x));
        String y_s=String.valueOf(df.format(y));
        try{
            FileWriter fw = new FileWriter(filepath,true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(x_s+","+y_s);
            pw.flush();
            pw.close();

        }
        catch(Exception e){
            System.exit(-1);

        }

    }


    /***
     * Performs State Update
     * @param input takes input for the update
     */
    @Override
    public void update(Input input) {
        if (this.endOfGame || input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        else{
            empty_input();
            BACKGROUND.drawFromTopLeft(0, 0);
            //desired tick cycle
            if(tick>TICK_CYCLE){
                tick=1;
                //To end game if we reach treasure or can no longer successfully finish the game
                if(KILL_RANGE>=distance(player.get_position().x,player.get_position().y,treasure.get_position().x,treasure.get_position().y)&&zombies_dead==zombies.size()||(sandwiches_eaten==sandwiches.size()&&zombies_dead!=zombies.size()&&player.getEnergy()<ZOMBIE_ENERGY&&!hasfired)){
                    System.out.print(player.getEnergy());
                    if(KILL_RANGE>=distance(player.get_position().x,player.get_position().y,treasure.get_position().x,treasure.get_position().y)){
                        System.out.print(",success!");
                    }
                    set_death();
                    setEndOfGame(true);
                }

                else{
                    closest_zindex=closest_zindex();
                    closest_sindex=closest_sindex();
                    //If we are in eating range of a sandwich that does exist update energy and set it as eaten
                    if(KILL_RANGE>=distance(player.get_position().x,player.get_position().y,sandwiches.get(closest_sindex).get_position().x,sandwiches.get(closest_sindex).get_position().y)&&sandwiches.get(closest_sindex).does_exist()){
                        player.eatSandwich();
                        sandwiches.get(closest_sindex).set_death();
                        sandwiches_eaten++;
                    }
                    //If we reach the shooting range of a zombie that is alive this if else loop fires and updates the bullet to kill the zombie
                    if(SHOOTING_RANGE>=distance(player.get_position().x,player.get_position().y,zombies.get(closest_zindex).get_position().x,zombies.get(closest_zindex).get_position().y)&&zombies.get(closest_zindex).does_exist()){
                        if(!hasfired){
                            hasfired=true;
                            player.reachZombie();
                            bullet.set_position(player.get_position().x,player.get_position().y);
                            saveRecord(bullet.get_position().x,bullet.get_position().y,"res/IO/output.csv");
                        }

                        else{
                            bullet.pointTo(zombies.get(closest_zindex).get_position());
                            bullet.move();
                            saveRecord(bullet.get_position().x,bullet.get_position().y,"res/IO/output.csv");
                            bullet.draw();
                            if(DEATH_RANGE>=distance(bullet.get_position().x,bullet.get_position().y,zombies.get(closest_zindex).get_position().x,zombies.get(closest_zindex).get_position().y)){
                                zombies.get(closest_zindex).set_death();
                                zombies_dead++;
                                hasfired=false;
                            }

                        }


                    }
                    //All zombies are dead, player now moves towards treasure
                    if(zombies_dead==zombies.size()){
                        player.pointTo(treasure.get_position());
                    }
                    //If player has the energy to kill a zombie or has already shot at an alive zombie player moves to it
                    if((player.getEnergy()>=ZOMBIE_ENERGY&&(zombies_dead!=zombies.size()))||hasfired){
                        player.pointTo(zombies.get(closest_zindex).get_position());

                    }
                    //If player's energy is less than energy required to kill a zombie and it hasn't fired a shot it moves to closest sandwich
                    if((zombies_dead!=zombies.size())&&player.getEnergy()<ZOMBIE_ENERGY&&sandwiches_eaten!=sandwiches.size()&&!hasfired&&sandwiches.get(closest_sindex).does_exist()){
                        player.pointTo(sandwiches.get(closest_sindex).get_position());

                    }
                    //printing out player location and moving him by one step
                    //System.out.println(df.format(player.get_position().x) + "," + df.format(player.get_position().y));
                    player.move();
                }



            }
            //updating tick
            tick++;

        }
        //drawing all existing objects
        draw_existing();
    }


    /**
     * Main Method which serves as entry point for our game
     * @param args a string which stores arguments
     * @throws IOException manages IO Exception
     */
    public static void main(String[] args) throws IOException {
        ShadowTreasure game = new ShadowTreasure();
        game.run();
    }
}
