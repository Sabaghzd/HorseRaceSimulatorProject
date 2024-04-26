
/**
 * Write a description of class Part1.Horse here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Horse
{
    //Fields of class Part1.Horse
    char horseSymbol;
    String horseName;
    double horseConfidence;
    int distanceTravelled;
    boolean hasFallen;


    //Constructor of class Part1.Horse
    /**
     * Constructor for objects of class Part1.Horse
     */
    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
        this.horseSymbol = horseSymbol;
        this.horseName = horseName;
        this.horseConfidence = horseConfidence;

    }

    //Other methods of class Part1.Horse
    public void fall()
    {
        this.hasFallen = true;
    }

    public double getConfidence()
    {
        return this.horseConfidence;

    }

    // Method to adjust confidence after a race
    public void adjustConfidence(boolean won) {
        if (won) {
            horseConfidence += 0.05; // Increase confidence slightly if won
            if (horseConfidence > 1.0) {
                horseConfidence = 1.0; // Ensure confidence doesn't exceed 1.0
            }
        } else {
            horseConfidence -= 0.05; // Decrease confidence slightly if fell
            if (horseConfidence < 0.0) {
                horseConfidence = 0.0; // Ensure confidence doesn't go below 0.0
            }
        }
    }

    public int getDistanceTravelled()
    {
        return this.distanceTravelled;
    }

    public String getName()
    {
        return this.horseName;
    }

    public char getSymbol()
    {
        return this.horseSymbol;
    }

    public void goBackToStart()
    {
        this.distanceTravelled = 0;
    }

    public boolean hasFallen()
    {
        return hasFallen;
    }

    public void moveForward()
    {
        this.distanceTravelled++;
    }

    public void setConfidence(double newConfidence)
    {
        this.horseConfidence = newConfidence;
    }

    public void setSymbol(char newSymbol)
    {
        this.horseSymbol = newSymbol;
    }

}
