package watson.watson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetDateAndTimes {
	public static List getDateAndTimes(String entry) throws Exception {
		Path path = Paths.get(System.getProperty("user.dir")).resolve("/Users/rohanasosa/git/WatsonLogAnalyzer/newestLogData.csv");
		
		BufferedReader reader = new BufferedReader(new FileReader(path.toFile()));
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
		List dateList = new ArrayList();
        String datePattern = "\\[\\d{1,2}/\\d{1,2}/\\d{2}\\s+\\d{1,2}:\\d{2}:\\d{2}:\\d{3}\\sUTC\\]" + ",\" [\\S]{8}";
		Pattern p = Pattern.compile(datePattern + "\\s+" + entry);
		Matcher m = p.matcher(responseString);
		while (m.find()) {
			dateList.add(m.group().replaceAll(entry, ""));
		}
		return dateList;
		
	}
}