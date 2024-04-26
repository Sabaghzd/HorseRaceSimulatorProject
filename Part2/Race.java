/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.*;
/**
 *
 * @author sabaghasemzadehhassankolaei
 */
public class Race {

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 *
 * @author McFarewell
 * @version 1.0
 */

    private int raceLength;
    private List<Horse> horses;
    private int numberOfRaces;


    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     *
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance)
    {
        // initialise instance variables
        raceLength = distance;
        this.horses = new ArrayList<>();
        this.numberOfRaces = 0;

    }

    /**
     * Adds a horse to the race in a given lane
     *
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse)
    {
        horses.add(theHorse);

    }

    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */public void startRace()
    {
        resetRace();
        // declare a local variable to tell us when the race is finished
        boolean finished = false;

        // reset all the lanes (all horses not fallen and back to 0).
        for (Horse horse:horses) {
            horse.goBackToStart();
        }
        

        while (!finished)
        {
            // move each horse
            for (Horse horse:horses) {
                moveHorse(horse);
            }
            

            // print the race positions
            printRace();

            boolean allHorsesFallen = true;
            for (Horse horse : horses) {
                if (!horse.hasFallen()) {
                    // If any horse hasn't fallen, set the flag to false and break the loop
                    allHorsesFallen = false;
                    break;
                }
            }

            // Check if all horses have fallen
            if (allHorsesFallen) {
                finished = true;
                System.out.println("No Winner");
            }

            

            // if any of the three horses has won the race is finished
            for (Horse horse:horses) {
                if (raceWonBy(horse))
                    {
                        finished = true;
                        System.out.println("The winner is: ");
                        System.out.println(horse.getName());
                        
                    }          
            }
            


            // wait for 100 milliseconds
            try{
                TimeUnit.MILLISECONDS.sleep(100);
            } catch(Exception e){}
        }
    }
     
    public Horse getHorseByLane(int i) {
        return horses.get(i-1);
    }
    
    public List<Horse> getHorses() {
        return this.horses;
    }
    
    
    public int getRaceLength() {
        return this.raceLength;
    }


    



    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     *
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse)
    {
        //if the horse has fallen it cannot move, 
        //so only run if it has not fallen

        if  (!theHorse.hasFallen())
        {
            //the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence())
            {
                theHorse.moveForward();
            }

            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence
            //so if you double the confidence, the probability that it will fall is *2
            if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
            {
                theHorse.fall();
                theHorse.adjustConfidence(false);
            }
        }

        if (raceWonBy(theHorse)) {
            theHorse.adjustConfidence(true);

        }
    }

    /**
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    public boolean raceWonBy(Horse theHorse)
    {
        if (theHorse.getDistanceTravelled() == raceLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /***
     * Print the race on the terminal
     */
    private void printRace()
    {
        System.out.print('\u000C');  //clear the terminal window

        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();

        
        for (int i=1; i<= horses.size(); i++) {
            printLane(getHorseByLane(i));
            System.out.println();
        }

        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();
    }

    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();

        //print a | for the beginning of the lane
        System.out.print('|');

        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);

        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            System.out.print('\u2322');
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }

        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);

        //print the | for the end of the track
        System.out.print('|' + " " + theHorse.getName() + " (Current confidence " + theHorse.getConfidence() + ')');
    }
    
    
    public void resetRace() {
        // Reset the positions of all horses to 0
        
            for (Horse horse : horses) {
                horse.goBackToStart(); // Reset the horse's position
                horse.setFallen(false); // Set fallen status to false
            }

        }
        // You may also need to reset any other race-related parameters here
    

    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     *
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }
    
    public int getNumberOfHorses() {
        return horses.size();
    }
    
    public void setDistance (int len) {
        this.raceLength = len;

    }
}


