package soccerLeagueTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.jupiter.api.Test;

public class SoccerLeagueTest {

	public static void main(String[] args) {
		String result, command = "java -cp bin soccerLeague/SoccerLeague ";
		String[] validTests = {  //expected results are in expected_ suffix files
							"tests/valid/leagueResults_01"
							,"tests/valid/leagueResults_02"
							,"tests/valid/leagueResults_03"
							,"tests/valid/leagueResults_04"
							/*,"tests/valid/leagueResults_05"
							,"tests/valid/leagueResults_06"
							,"tests/valid/leagueResults_07"*/
						},
				invalidTests = {  
							"tests/invalid/leagueResults_08"
							,"tests/invalid/leagueResults_09"
							/*,"tests/invalid/leagueResults_10"
							,"tests/invalid/leagueResults_11"
							,"tests/invalid/leagueResults_12"
							,"tests/invalid/leagueResults_13"
							,"tests/invalid/leagueResults_14"*/
		};

		try {
			for(String test : validTests) {
				result = executeTest(test.concat("_expected"), command.concat(test), true);
				System.out.println(test.concat(result).concat("\n"));
			}

			for(String test : invalidTests) {
				result = executeTest(test.concat("_expected"), command.concat(test), false);
				System.out.println(test.concat(result).concat("\n"));
			}
		}
		catch (Exception e) {
			System.out.print(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static String executeTest(String expectedResultsFile, String test, boolean isValid) throws Exception {
		String result;
		StringBuilder expected;
		expected = readExpectedResults(expectedResultsFile);
		Process pro = Runtime.getRuntime().exec(test);		
		pro.waitFor();
		result = checkTest(expected, pro.getInputStream(), isValid);
		return result;
	}
	
	static StringBuilder readExpectedResults(String file) throws IOException {
		String line = "";
		StringBuilder expected = new StringBuilder();
		try (
				FileReader buffer = new FileReader(file);
				BufferedReader contents = new BufferedReader(buffer)
			) {			
	    		while((line = contents.readLine()) != null)
	    			expected.append(line);
				}
		return expected;
	}
	
	
	
	@Test
	private static String checkTest(StringBuilder expected, InputStream output, boolean valid) throws Exception {
        String line, result = " ";
        StringBuilder executionResult = new StringBuilder();
        BufferedReader in = new BufferedReader(
            new InputStreamReader(output));
        while ((line = in.readLine()) != null) {
        	executionResult.append(line);
        }
        try {
        	if(valid)
        		assertEquals(expected.compareTo(executionResult), 0);
        	else
        		assertNotEquals(expected.compareTo(executionResult), 0);
        	result = ": OK";
        }
        catch (AssertionError error) {
        	result = result.concat("Error: ")
        			.concat(error.getMessage())
        			.concat("\n")
        			.concat(expected.toString())
        			.concat("\nvs\n")
        			.concat(executionResult.toString());
        }
        return result;
      }
}