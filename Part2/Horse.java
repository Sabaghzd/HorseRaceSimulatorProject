
import javax.swing.ImageIcon;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sabaghasemzadehhassankolaei
 */
/**
 * Write a description of class Horse here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Horse
{
    //Fields of class Horse
    ImageIcon horseSymbol;
    String horseName;
    double horseConfidence;
    int distanceTravelled;
    boolean hasFallen;
    long startTime;  // To store the start time of the race
    long endTime;    // To store the end time of the race
    
      
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
    */
    public Horse(ImageIcon horseSymbol, String horseName, double horseConfidence)
    {
        this.horseSymbol = horseSymbol;
        this.horseName = horseName;
        this.horseConfidence = horseConfidence;

    }
    
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

    //Other methods of class Horse
    public void fall()
    {
        this.hasFallen = true;
    }
    
    public double getConfidence()
    {
        return this.horseConfidence;
        
    }
    
    public int getDistanceTravelled()
    {
        return this.distanceTravelled;
    }
    
    public String getName()
    {
        return this.horseName;
    }
    
    public ImageIcon getSymbol()
    {
        return this.horseSymbol;
    }
    
    public String getCoat() {
        if (this.horseSymbol != null && this.horseSymbol.getDescription() != null) {
            String description = this.horseSymbol.getDescription();
            int lastSlashIndex = description.lastIndexOf("/");
            if (lastSlashIndex != -1) {
                return description.substring(lastSlashIndex + 1); // Extract coat name from file path
            }
        }
        return "Unknown"; // Or any default value you prefer
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
    
    public void setSymbol(ImageIcon newSymbol)
    {
        this.horseSymbol = newSymbol;
    }
   
    
    public void setFallen(boolean newFallen)
    {
        this.hasFallen = newFallen;
    }
    
}

