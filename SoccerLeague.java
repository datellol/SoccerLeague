package soccerLeague;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
//import org.junit.*;

public class SoccerLeague {
	
	private static boolean isValidFormat = false;
	private static Map<String, Integer> GeneralTable = new HashMap<>();
	private static Matcher matchResult = null;
	private final static String RESULT_PATTERN = "^([\\w\\s]+)\\s(\\d{1,3}),\\s([\\w\\s]+)\\s(\\d{1,3})$";
	
	public static void main(String[] args) {
		
		if(args.length == 0) { //if no arguments --> ask what to do
			int option = 0;
			Scanner input = new Scanner(System.in);
			option = menu(input);
			
			//No args option 1
			if(option == 1) {
				System.out.println("\nType the number of match results to enter:");
				int counter = 0;
				String inputString = input.nextLine();
				
				if (inputString == null) {
					System.out.println("You did not enter a valid and positive number\nBye!");
					input.close();
					return;
				}
			    try {
			    	counter  = Integer.parseInt(inputString);
			    }
			    catch (NumberFormatException nfe) { 
			    	System.out.println("You did not enter a valid and positive number\nBye!");
			    	input.close();
			    	return;
			    }
			    
			    for(int i = 1; i <= counter; i++) {
			    	System.out.println("Type the result number: " + i + " with pattern: \"TEAM_NAME X, TEAM_NAME\" where X is a number, or \"EXIT\" to quit");
			    	inputString = input.nextLine();
			    	if(inputString.toUpperCase().equals("EXIT")) {
			    		System.out.println("Stopping reading inputs\n");
			    		break;
			    	}
			    	else if(isValidFormat(inputString)) {
			    		registerPoints(inputString);
			    	}
			    	else --i;
			    }
			    input.close();
			    printGeneralTable();			    
			}
			//No args option 1
			
			//No args option 2
			else if(option == 2) {
				System.out.println("\nType an absolute file path with results:");				
				String path = input.nextLine();
				//path = adjustOSXPath(path);
				try {
					readFile(path);
				}
				catch(IOException e) {
					System.out.println("There was a problem with file: " +
										path + "\n" +
										e.getMessage());
				}
				printGeneralTable();
			}
			//No args option 2
			
			//No args option 3 
			else {
				System.out.println("Bye!");
			}
			//No args option 3
		}
		
		//With arg == 1 
		else if(args.length == 1) { //if argument length == 1 --> it is a file
			System.out.println("Trying single argument as a text file with results");
			String path = args[0].toString();
			//path = adjustOSXPath(path);
			try {
				readFile(path);
			}
			catch(IOException e) {
				System.out.println("There was a problem with file: " +
									path + "\n" +
									e.getMessage());
			}
			printGeneralTable();
		}
		//With arg == 1
		
		//With args > 1 it is a result
		else {
			StringBuilder result = new StringBuilder();
			Arrays.stream(args).forEach(string -> result.append(string).append(" "));
			isValidFormat = isValidFormat(result.toString().trim());
			if(isValidFormat) {
				registerPoints(result.toString().trim());
			}
		}
		//With args > 1 it is a result
	}
	

	/*METHODS*/
	static int menu(Scanner input) {
		int option = 0;
		System.out.println(
							"Please choose an option:\n" +
							"1 - Type n results manually\n" +
							"2 - Read n results from a text file\n" +
							"3 - Exit"
						   );
		String inputString = input.nextLine();
		
		if (inputString == null) {
	    	System.out.println("Not a valid option\n");
	    	option = 3;
		}
		try {
			option  = Integer.parseInt(inputString);
	    }
	    catch (NumberFormatException nfe) { 
	    	System.out.println("Not a valid option\n");
	    	option = 3;
	    }
		return option;
	}
	
	static boolean isValidFormat(String stringResult) {
		boolean result = false;
		Pattern pattern = Pattern.compile(/*RegexPatterns.*/RESULT_PATTERN);
		matchResult = pattern.matcher(stringResult.trim());
		if(matchResult.find()) { result = true; }
		return result;
	}
	
	static void registerPoints(String line) {
		int r1 = Integer.parseInt(matchResult.group(2)); //r1 -> team1's result
		int r2 = Integer.parseInt(matchResult.group(4)); //r2 -> team2's result
		String t1 = matchResult.group(1); // t1 -> team1
		String t2 = matchResult.group(3); // t2 -> team2
		
		if(r1 == r2) {
			GeneralTable.merge(t1, 1, Integer::sum);
			GeneralTable.merge(t2, 1, Integer::sum);
		}
		else if(r1 > r2) {
			GeneralTable.merge(t1, 3, Integer::sum);
			if(! GeneralTable.containsKey(t2)) { GeneralTable.put(t2, 0); }
		}
		else {
			GeneralTable.merge(t2, 3, Integer::sum);
			if(! GeneralTable.containsKey(t1)) { GeneralTable.put(t1, 0); }
		}		
	}
	
	static Map<String, Integer> orderGeneralTable() {
		//Integer[] sortedPoints = GeneralTable.values().toArray(new Integer[GeneralTable.values().size()]);
		//Arrays.sort(sortedPoints);
		LinkedHashMap<String, Integer> sortedGeneralTable = 
				GeneralTable.entrySet()
				.stream()
			    .sorted(Entry.comparingByValue(Comparator.reverseOrder()))
			    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
			                              (t, r) -> t, LinkedHashMap::new));
		
		return sortedGeneralTable;
	}

	static String adjustOSXPath(String path) {
		return path.replaceAll("\\", "/");
	}
	
	static void readFile(String path) throws IOException {
		String line = "";
		StringBuilder omissions = new StringBuilder();
		try (
				FileReader file = new FileReader(path);
				BufferedReader buffer = new BufferedReader(file)
			) {			
	    		while((line = buffer.readLine()) != null)
	    			if(isValidFormat(line)) {
	    				registerPoints(line);
	    			}
	    			else {
	    				//System.out.println("Omitting line: " + line + "\n");
	    				omissions.append(line + "\n");
	    			}
				}
		if(!omissions.isEmpty()) System.out.println("\nOmitting lines:\n" + omissions);
	}

	static void printGeneralTable() {
		if(GeneralTable.isEmpty()) {
			System.out.println("\nThere is no contents for this league general table\nbye!");
		}
		else {
			System.out.println("\nPrinting contents of General Table:\n");
			Map<String, Integer> sortedGeneralTable = orderGeneralTable();
			//GeneralTable.forEach((team,points)->System.out.println(team + ", " + points + (points == 1 ? " pt" : " pts")));
			int i = 0;
			for(Entry<String, Integer> team : sortedGeneralTable.entrySet()) {
				System.out.println(++i + ". " + team.getKey()+ ", " + team.getValue() + (team.getValue() == 1 ? " pt" : " pts"));
			}
			
		}
	}
}