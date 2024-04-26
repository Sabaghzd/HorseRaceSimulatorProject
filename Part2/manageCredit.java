
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sabaghasemzadehhassankolaei
 */
public class manageCredit {
    private static final String CREDIT_FILE_PATH = "credit.txt";

    public static double loadCredit() {
        double credit = 0.0;
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDIT_FILE_PATH))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                credit = Double.parseDouble(line);
            }
        } catch (IOException | NumberFormatException e) {
        }
        return credit;
    }

    public static void saveCredit(double credit) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDIT_FILE_PATH))) {
            writer.write(Double.toString(credit));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateCredit(double amount) {
        double currentCredit = loadCredit();
        double updatedCredit = currentCredit + amount;
        saveCredit(updatedCredit);
    }
    
    public static void updateCredit(double amount, boolean won) {
        double currentCredit = loadCredit();
        double updatedCredit;
        if (won) {
            updatedCredit = currentCredit + amount;
        } else {
            updatedCredit = currentCredit - amount;
        }
        saveCredit(updatedCredit);


        
    }
}
