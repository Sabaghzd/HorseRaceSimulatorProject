
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author sabaghasemzadehhassankolaei
 */
public class RaceGUI extends javax.swing.JFrame {
        private String winnerGUI;
        private Race race;
        private Timer[] timers;
        private final int delay = 10; // Delay in milliseconds
        private long startTime;
        private int number;
        private int length;
       
    /**
     * Creates new form mainGUI
     */
        
    public RaceGUI() {
       main(null);
        
    }
    public RaceGUI(Map<String, ImageIcon> horseData, Map<String, Double> horseConfidence, int length) {
        initComponents();
        number = horseData.size();
        createRace(horseData, horseConfidence, length); // Create the race with provided data
        timers = new Timer[race.getNumberOfHorses()]; // Initialize the timers array
        updateGUI(); // Update the GUI after creating the race
        resetRace();
        jButton1.setOpaque(true);
        jButton1.setBorderPainted(false);
        
        jButton2.setOpaque(true);
        jButton2.setBorderPainted(false);
        
        jButton3.setOpaque(true);
        jButton3.setBorderPainted(false);
    }
    
        public String getWinner() {
        
        return winnerGUI;
    }

    private void startTimers() {
        // Map to store the average speeds of each horse
        Map<String, Double> averageSpeeds = new HashMap<>();

        // Map to store the final confidences of each horse
        Map<String, Double> finalConfidences = new HashMap<>();

        // Array to store finish times of each horse
        long[] takenTimes = new long[timers.length];

        // Record the start time of the race
        long startTime = System.currentTimeMillis();

        // Start timers for each horse
        for (int i = 0; i < timers.length; i++) {
            final int horseIndex = i;
            timers[i] = new Timer(delay, e -> {
                // Check if the timer should continue running for this horse
                if (!raceFinished()) {
                    if (!race.getHorseByLane(horseIndex + 1).hasFallen()) {
                        // Timer continues running
                    } else {
                        // Horse has fallen, stop the timer
                        timers[horseIndex].stop();
                        long endTime = System.currentTimeMillis();
                        long timeTaken = endTime - startTime;
                        takenTimes[horseIndex] = timeTaken; // Store the finish time for this horse

                        System.out.println("Horse " + (horseIndex + 1) + " has fallen after " + timeTaken + " milliseconds.");

                        // Add the fallen horse to the map with its final confidence
                        Horse fallenHorse = race.getHorseByLane(horseIndex + 1);
                        finalConfidences.put(fallenHorse.getName(), fallenHorse.getConfidence());
                        double averageSpeed = (double) fallenHorse.getDistanceTravelled() / (timeTaken / 100); // Multiply by 1000 to convert milliseconds to seconds
                        averageSpeeds.put(fallenHorse.getName(), averageSpeed);
                    }
                } else {
                    // Race has finished, stop the timer
                    timers[horseIndex].stop();
                    long endTime = System.currentTimeMillis();
                    long timeTaken = endTime - startTime;
                    takenTimes[horseIndex] = timeTaken; // Store the finish time for this horse

                    System.out.println("Horse " + (horseIndex + 1) + " finished the race in " + timeTaken + " milliseconds.");

                    // Calculate and display average speed for each horse when it finishes
                    if (takenTimes[horseIndex] != 0) {
                        Horse finishedHorse = race.getHorseByLane(horseIndex + 1);
                        long raceTime = takenTimes[horseIndex];
                        double averageSpeed = (double) finishedHorse.getDistanceTravelled() / (raceTime / 100); // Multiply by 1000 to convert milliseconds to seconds
                        System.out.println("Average speed for Horse " + (horseIndex + 1) + ": " + averageSpeed + " units per second.");

                        // When each horse finishes, add its average speed to the map
                        averageSpeeds.put(finishedHorse.getName(), averageSpeed);

                        // Add the final confidence of the horse to the map
                        finalConfidences.put(finishedHorse.getName(), finishedHorse.getConfidence());
                    }
                }
            });
            timers[i].start(); // Start the timer for this horse

            // Print starting message
            System.out.println("Starting timer for Horse " + (horseIndex + 1));

            // Print initial confidence values
            System.out.println("Initial confidence for Horse " + (horseIndex + 1) + ": " + race.getHorseByLane(horseIndex + 1).getConfidence());
        }

        // Add a listener to check when all timers have finished
        Timer checkAllFinishedTimer = new Timer(delay, e -> {
            boolean allTimersStopped = true;
            boolean allHorsesFallen = true;
            for (int i = 0; i < timers.length; i++) {
                if (timers[i].isRunning()) {
                    allTimersStopped = false;
                }
                if (!race.getHorseByLane(i + 1).hasFallen()) {
                    allHorsesFallen = false;
                }
            }
            if (allTimersStopped || allHorsesFallen) {
                // All timers have stopped, so the race is finished
                // Calculate the race duration
                long raceDuration = System.currentTimeMillis() - startTime;

                // Determine the winner
                String winner = getWinner(race);
                winnerGUI = winner;
                

                // Pass the averageSpeeds, finalConfidences, raceDuration, and winner to RaceStatsDisplay
                RaceStatsDisplay statsDisplay = new RaceStatsDisplay(averageSpeeds, finalConfidences, raceDuration, winner, race);
                
                // Create a javax.swing.Timer to schedule a task to display the RaceStatsDisplay frame after 2 seconds
                javax.swing.Timer timer = new javax.swing.Timer(2000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Display the RaceStatsDisplay frame
                        statsDisplay.setVisible(true);
                        dispose();
                    }
                });

                // Start the timer
                timer.setRepeats(false); // Ensure the timer only runs once
                timer.start();

                // Stop the checkAllFinishedTimer
                ((Timer) e.getSource()).stop();
            }
        });
        checkAllFinishedTimer.start();
    }
    
    private String getWinner(Race race) {
        double maxDistance = Double.MIN_VALUE;
        Horse winner = null;

        java.util.List<Horse> horses = race.getHorses();
        for (Horse horse : horses) {
            if (race.raceWonBy(horse)) {
                winner = horse;
            }
        }

        return (winner != null) ? winner.getName() : "No winner";
    }

    

    private void updateGUI() {
        new Thread(() -> {
            while (!raceFinished()) {
                // Update the positions of the horses in the GUI first
                SwingUtilities.invokeLater(() -> {
                    for (int i=1; i<=number; i++) {
                        moveHorse(getHorseLabel(i), race.getHorseByLane(i).getDistanceTravelled());
                    }
                });

                // Pause for a short time to update the GUI
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Check if any horse has fallen and rotate its label if necessary
                
                
                for (int i=1;i<=number;i++) {
                    if (race.getHorseByLane(i).hasFallen()) {
                        rotateHorse(getHorseLabel(i));
                    }
                }
            }
            // If the race has finished, update the GUI one more time
            SwingUtilities.invokeLater(() -> {

                for (int i=1; i<=number; i++) {
                    moveHorse(getHorseLabel(i), race.getHorseByLane(i).getDistanceTravelled());
                }
            });
            
        }).start();
    }
    private void resetRace() {
        
            horse1lbl.setLocation(20, 20); // Initial position of horse 1
            horse2lbl.setLocation(20, 120); // Initial position of horse 2
            horse3lbl.setLocation(20, 220); // Initial position of horse 3
            
            for (int i=1; i<=number; i++) {
                getHorseLabel(i).setIcon(race.getHorseByLane(i).getSymbol());
                getHorseLabel(i).putClientProperty("rotated", false);
            }
            
            
            race.resetRace();
    }
    private void moveHorse(JLabel horseLabel, int distance) {
        int x = 60 + distance * 20; // Adjust the multiplier based on the desired speed
        horseLabel.setLocation(x, horseLabel.getY());
    }
    
    // Method to create the race with provided data.
    private void createRace(Map<String, ImageIcon> horseData, Map<String, Double> horseConfidence, int length) {
        // Create horses with user input or generated data
        race = new Race(length); // Set the race length
        int horseNumber = 1;
        for (Map.Entry<String, ImageIcon> entry : horseData.entrySet()) {
            String horseName = entry.getKey();
            ImageIcon horseIcon = entry.getValue();

            // Get the horse's confidence from the confidence map
            Double confidence = horseConfidence.get(horseName);

            // Create the horse with its icon and confidence
            Horse horse = new Horse(horseIcon, horseName, confidence != null ? confidence : 0.6); // Assuming a default confidence of 0.6 if not provided
            race.addHorse(horse);
            horseNumber++;
            System.out.println(horse.horseName);

            // Update the corresponding label with the horse's icon
            switch (horseNumber) {
                case 1:
                    horse1lbl.setIcon(horseIcon);
                    break;
                case 2:
                    horse2lbl.setIcon(horseIcon);
                    break;
                case 3:
                    horse3lbl.setIcon(horseIcon);
                    break;
                case 4:
                    horse3lbl.setIcon(horseIcon);
                    break;
                case 5:
                    horse3lbl.setIcon(horseIcon);
                    break;
                // Add more cases as needed
                default:
                    break;
            }
        }
    }
    
    
    private void rotateHorse(JLabel horseLabel) {
        // Get the "rotated" property from the JLabel
        Boolean rotated = (Boolean) horseLabel.getClientProperty("rotated");

        // If "rotated" property is not set, default it to false
        if (rotated == null) {
            rotated = false;
        }

        // Check if the horse label has been rotated before
        if (!rotated) {
            // Get the icon from the JLabel
            Icon icon = horseLabel.getIcon();

            // Check if the icon is not null
            if (icon != null) {
                // Get the image from the ImageIcon
                Image image = ((ImageIcon) icon).getImage();

                // Create a new BufferedImage for the rotated image
                BufferedImage rotatedImage = new BufferedImage(image.getHeight(null), image.getWidth(null), BufferedImage.TYPE_INT_ARGB);

                // Create a graphics context for the rotated image
                Graphics2D g2d = rotatedImage.createGraphics();

                // Apply rotation transformation
                g2d.rotate(Math.toRadians(90), image.getHeight(null) / 2, image.getHeight(null) / 2);

                // Draw the original image onto the rotated image
                g2d.drawImage(image, 0, 0, null);
                g2d.dispose();

                // Set the rotated image as the icon of the JLabel
                horseLabel.setIcon(new ImageIcon(rotatedImage));

                // Set the "rotated" property to true to indicate that the horse label has been rotated
                horseLabel.putClientProperty("rotated", true);
            }
        }
    }




    private boolean raceFinished() {
        // Check if any horse has reached the end of the race
        for (int i = 1; i <= race.getNumberOfHorses(); i++) {
            if (race.getHorseByLane(i).getDistanceTravelled() >= race.getRaceLength()) {
                return true;
            }
        }
        return false;
    }

    private String getLaneRepresentation(Horse horse) {
        StringBuilder lane = new StringBuilder("|");
        for (int i = 0; i < race.getRaceLength(); i++) {
            if (i == horse.getDistanceTravelled()) {
                if (horse.hasFallen()) {
                    lane.append('⌢'); // Dead symbol
                } else {
                    lane.append(horse.getSymbol());
                }
            } else {
                lane.append(' ');
            }
        }
        lane.append('|');
        return lane.toString();
    }
    
    private JLabel getHorseLabel(int lane) {
        switch (lane) {
            case 1:
                return horse1lbl;
            case 2:
                return horse2lbl;
            case 3:
                return horse3lbl;
            case 4:
                return horse4lbl;
            case 5:
                return horse5lbl;
            default:
                throw new IllegalArgumentException("Invalid lane number: " + lane);
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        horse1lbl = new javax.swing.JLabel();
        horse5lbl = new javax.swing.JLabel();
        horse3lbl = new javax.swing.JLabel();
        horse4lbl = new javax.swing.JLabel();
        horse2lbl = new javax.swing.JLabel();
        backGround = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(null);

        horse1lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/redRoan copy.png"))); // NOI18N
        jPanel1.add(horse1lbl);
        horse1lbl.setBounds(20, 10, 170, 90);
        jPanel1.add(horse5lbl);
        horse5lbl.setBounds(30, 400, 190, 70);

        horse3lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/buckskin copy.png"))); // NOI18N
        jPanel1.add(horse3lbl);
        horse3lbl.setBounds(20, 200, 150, 100);
        jPanel1.add(horse4lbl);
        horse4lbl.setBounds(30, 320, 170, 70);

        horse2lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pieBald copy.png"))); // NOI18N
        jPanel1.add(horse2lbl);
        horse2lbl.setBounds(20, 110, 170, 90);

        backGround.setIcon(new javax.swing.ImageIcon(getClass().getResource("/scenic.jpg"))); // NOI18N
        backGround.setText("jLabel4");
        jPanel1.add(backGround);
        backGround.setBounds(0, 0, 650, 480);

        jComboBox1.setBackground(new java.awt.Color(0, 0, 0));
        jComboBox1.setFont(new java.awt.Font("Chakra Petch", 0, 13)); // NOI18N
        jComboBox1.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "pic 1", "pic 2", "pic 3", "pic 4" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox1);
        jComboBox1.setBounds(550, 490, 60, 20);

        jLabel2.setFont(new java.awt.Font("Chakra Petch", 0, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Backgound image:");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(430, 490, 108, 20);

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setFont(new java.awt.Font("Chakra Petch", 0, 13)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Add Bet");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3);
        jButton3.setBounds(250, 490, 77, 23);

        jButton2.setBackground(new java.awt.Color(0, 0, 0));
        jButton2.setFont(new java.awt.Font("Chakra Petch", 0, 13)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Customise Race");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2);
        jButton2.setBounds(110, 490, 124, 23);

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("Chakra Petch", 0, 13)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Start Race");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(10, 490, 92, 23);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        // Get the selected index from the JComboBox
        int selectedIndex = jComboBox1.getSelectedIndex();

        // Update the icon of jLabel4 based on the selected index
        switch (selectedIndex) {
            case 0:
            backGround.setIcon(new ImageIcon(getClass().getResource("scenic.jpg")));
            break;
            case 1:
            backGround.setIcon(new ImageIcon(getClass().getResource("desert.jpg")));
            break;
            case 2:
            ImageIcon icon = new ImageIcon(getClass().getResource("underwater.jpg"));
            java.awt.Image img = icon.getImage();
            java.awt.Image newImg = img.getScaledInstance(1000, 600, java.awt.Image.SCALE_SMOOTH);
            icon = new ImageIcon(newImg);
            backGround.setIcon(icon);
            break;
            case 3:
            backGround.setIcon(new ImageIcon(getClass().getResource("stadium.jpeg")));
            break;

            // Add more cases as needed
            default:
            break;
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        horseStatsDisplay stats = new horseStatsDisplay();
        stats.show();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        setHorses set = new setHorses();
        System.out.println("p");
        set.show();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        new Thread(() -> {
            resetRace();
            startTimers();
            race.startRace();

            //updateGUI();
        }).start();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Map<String, ImageIcon> horseData = new HashMap<>();
        // Add horse data to the map
        horseData.put("Horse 1", new ImageIcon(RaceGUI1.class.getResource("/redRoan copy.png")));
        horseData.put("Horse 2", new ImageIcon(RaceGUI1.class.getResource("/buckSkin copy.png")));
        horseData.put("Horse 3", new ImageIcon(RaceGUI1.class.getResource("/pieBald copy.png")));

        // Create a map to store horse confidence values (name -> confidence)
        Map<String, Double> horseConfidence = new HashMap<>();
        // Add horse confidence values to the map
        horseConfidence.put("Horse 1", 0.8);
        horseConfidence.put("Horse 2", 0.6);
        horseConfidence.put("Horse 3", 0.7);

        // Create an instance of RaceGUI and pass the maps to its constructor
        RaceGUI gui = new RaceGUI(horseData, horseConfidence, 20);
        gui.show();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel backGround;
    private javax.swing.JLabel horse1lbl;
    private javax.swing.JLabel horse2lbl;
    private javax.swing.JLabel horse3lbl;
    private javax.swing.JLabel horse4lbl;
    private javax.swing.JLabel horse5lbl;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
