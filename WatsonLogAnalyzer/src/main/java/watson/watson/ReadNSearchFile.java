package watson.watson;
import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ReadNSearchFile {
    
    public static void main(String[] args) throws Exception {
        
        Path path = Paths.get(System.getProperty("user.dir")).resolve("/Users/rohanasosa/git/WatsonLogAnalyzer/newestLogData.csv");
        
        BufferedReader reader = new BufferedReader(new FileReader(path.toFile()));
        
        LinkedHashMap<String, Integer> frequency = new LinkedHashMap<String, Integer>();
        LinkedHashMap<String, Integer> frequencyInOrder = new LinkedHashMap<String, Integer>();
  
        //as long as there is a non-null character, read each line and stop reading lines at null
        String line = reader.readLine();
        StringBuffer response = new StringBuffer();
        
        //creates the stringBuffer by appending each line and adding line breaks
        while(line != null) {
            response.append(line + "\n");
            line = reader.readLine();
        }
        reader.close();
        String responseString = response.toString();
        responseString = responseString.replaceFirst("Date And Time, Log Entry", "");
        //System.out.println(response);
        String datePattern = "\\[\\d{1,2}/\\d{1,2}/\\d{2}\\s+\\d{1,2}:\\d{2}:\\d{2}:\\d{3}\\sUTC\\]" + ",\" [\\S]{8}";
        Pattern r = Pattern.compile(datePattern);
        Matcher m = r.matcher(responseString);
        
        while(m.find()) {
            //String dateAndTime = m.group();
            responseString = responseString.replaceFirst(datePattern, "asdfghjkl");
        }
        
        String [] words2 = responseString.split("asdfghjkl");
        
        for (String word : words2) {
            if(word == null || word.trim().equals("")) {
                continue;
            }
            
            if(frequency.containsKey(word)) {
                frequency.put(word, frequency.get(word) + 1);
            } else {
                frequency.put(word, 1);
            }
        }
        //System.out.println(frequency);
        
        int mostFrequencyUsed = 0;
        String theEntry = null;
     
        //sorting loop for that puts the entries into a new linked hash map in order
		boolean sorted = false;
		while(!sorted) {
			for(String entry : frequency.keySet()){
				Integer theVal = frequency.get(entry);
				if(theVal > mostFrequencyUsed) {
					mostFrequencyUsed = theVal;
					theEntry = entry;
				}
			}
			frequencyInOrder.put(theEntry, frequency.get(theEntry));
			frequency.remove(theEntry);
			mostFrequencyUsed = 0;
			if(frequency.isEmpty()) {
				sorted = true;
			}
		}
       
        
     // file creator
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new File("LogFiles.txt"));
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        StringBuilder builder = new StringBuilder();
        
        //adds labels to the columns
        String ColumnNamesList = "Frequency, Log Entry";
        
        builder.append(ColumnNamesList +"\n");

        //adds both lists to the .csv file and formats it with commas and line breaks
		for(Entry<String, Integer> mpd:frequencyInOrder.entrySet()){  
			   if(mpd.getKey().contains("Exception") || mpd.getValue() > 1) {
				builder.append(mpd.getValue() + "," + mpd.getKey()+" \n");
				System.out.println(mpd.getValue() + ": " + mpd.getKey()+" ");
			   }
			}  

        pw.write(builder.toString());
        pw.close();
        
        String testEntry = "ReportTimer                                                  I B2BXFormLog: ReportTimer: processList:  No files found in sterling for processing";
        System.out.println(GetDateAndTimes.getDateAndTimes(testEntry));
       
        System.out.println("Done!");
        
    }   
}
