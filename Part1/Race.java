import java.util.*;

import java.util.concurrent.TimeUnit;
import java.lang.Math;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 *
 * @author McFarewell
 * @version 1.0
 */
public class Race
{
    private int raceLength;
    private List<Horse> lanes;


    /**
     * Constructor for objects of class Part1.Race
     * Initially there are no horses in the lanes
     *
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance, int numberOfLanes)
    {
        // initialise instance variables
        raceLength = distance;
        lanes = new ArrayList<Horse>();
        for (int i = 0; i < numberOfLanes; i++) {
            lanes.add(null); // Initially, all lanes are vacant
        }
    }

    /**
     * Adds a horse to the race in a given lane
     *
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber)
    {
        if (laneNumber < 1 || laneNumber > lanes.size()) {
            System.out.println("Invalid lane number. Please choose a lane within the range.");
            return;
        }
        lanes.set(laneNumber - 1, theHorse);
    }

    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the
     * race is finished
     */public void startRace()
    {
        // declare a local variable to tell us when the race is finished
        boolean finished = false;

        // reset all the lanes (all horses not fallen and back to 0).
        for (Horse h:lanes) {
            if (h!=null) {
                h.goBackToStart();
            }

        }

        while (!finished)
        {
            // move each horse
            for (Horse h:lanes) {
                if (h!=null) {
                    moveHorse(h);
                }
            }


            // print the race positions
            printRace();

            // if any of the three horses has won the race is finished
            if (allHorsesHaveFallen()) {
                finished = true;
                System.out.println("No Winner");
            }

            // if any of the three horses has won the race is finished
            boolean winnerFound = false;
            for (Horse horse : lanes) {
                if (horse != null) {
                    if (raceWonBy(horse)) {
                        if (!winnerFound) {
                            finished = true;
                            System.out.println("The winner is: ");
                            winnerFound = true;
                        }
                        System.out.println(horse.getName());
                    }
                }

            }



            // wait for 100 milliseconds
            try{
                TimeUnit.MILLISECONDS.sleep(100);
            } catch(Exception e){}
        }
    }

    private boolean allHorsesHaveFallen() {
        for (Horse horse : lanes) {
            if (horse!=null) {
                if (!horse.hasFallen()) {
                    return false;
                }
            }

        }
        return true;
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
    private boolean raceWonBy(Horse theHorse)
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
        System.out.print("\033[H\033[2J"); // clear the terminal window

        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();

        for (Horse horse : lanes) {
            if (horse !=null) {
                printLane(horse);
                System.out.println();
            }

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
        System.out.printf("| %s (Current confidence %.2f)\n", theHorse.getName(), theHorse.getConfidence());

    }


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
}
