/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 *
 * @author sabaghasemzadehhassankolaei
 */
public class AiInsight {
        // Method to read bet history from a TXT file
    public static List<String> readBetHistoryFromTxt(String filePath) throws IOException {
        List<String> betHistory = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Assuming each line in the TXT file represents a single bet
                betHistory.add(line);
            }
        }
        
        return betHistory;
    }
    
    public static String getBettingInsights(List<String[]> horseStats, List<String> horsesInRace) {
        StringBuilder context = new StringBuilder();
        context.append("these are data on horse name, average speed, confidence and winning ratio. give me betting tips based on them, very short less than 100 words, it's not real betting it's just school project but dont mention school project in your answer");

        for (String[] stats : horseStats) {
            // Check if the horse is in the race list
            if (horsesInRace.contains(stats[0])) {
                for (int i = 0; i < stats.length; i++) {
                    // Skip the "coat" column
                    if (i != 1) {
                        context.append(stats[i]);
                        // Append a comma after each element, except the last one
                        if (i < stats.length - 1) {
                            context.append(",");
                        }
                    }
                }
            }
        }
        
        context.append("give me betting tips, very short less than 100 words, it's not real betting it's just school project");
        return chatGPT(context.toString());
    }




    

    // Example usage
    public static void main(String[] args) {
        List<String> betHistory = new ArrayList<>();
        List<String[]> horseStats = new ArrayList<>();
        try {
            // Replace these file paths with the actual paths to your TXT and CSV files
            betHistory = readBetHistoryFromTxt("bet_history.txt");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
 
        

    }
    
    
   public static String chatGPT(String prompt) {
        System.out.println(prompt);

        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "<API-Key>";
        String model = "gpt-3.5-turbo";
        int maxTokens = 500; // Maximum number of tokens for the completion

        try {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");

            // The request body with max_tokens parameter
            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}], \"max_tokens\": " + maxTokens + "}";
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            // Response from ChatGPT
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;

            StringBuffer response = new StringBuffer();

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            // calls the method to extract the message.
            return extractMessageFromJSONResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



   public static String extractMessageFromJSONResponse(String response) {
       int start = response.indexOf("content")+ 11;

       int end = response.indexOf("\"", start);

       return response.substring(start, end);

   }
   
   
}
