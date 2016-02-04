package ir.assignments.three;

import java.io.File;
import java.util.ArrayList;
import java.util.*;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;

/**
 * A collection of utility methods for text processing.
 */
public class Modified_Utilities {
   
   private static List<String> stopWords = new ArrayList<String>();
   
   private static void createStopWordsList(){
      String delimiter = new String("[^a-zA-Z']+");
      File stopW = new File("stopWords.txt");
      try(Scanner scan = new Scanner(stopW);){ // scan the input file
         while(scan.hasNextLine()){
            String line = scan.nextLine();
            String[] parts = line.split(delimiter);
            for(String part: parts)
            {
               if(!part.equals(""))
               {
                  stopWords.add(part);
               }
            }
         }
      }
      catch(FileNotFoundException e)
      {
         System.out.println("Can NOT find file...");
      }
      
   }
   
	/**
	 * Reads the input text file and splits it into alphanumeric tokens.
	 * Returns an ArrayList of these tokens, ordered according to their
	 * occurrence in the original text file.
	 * 
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case. 
	 * 
	 * @param input The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by occurrence.
	 */
	public static ArrayList<String> tokenizeFile(String text, boolean useStopWords) {
      createStopWordsList();
      ArrayList<String> words = new ArrayList<String>();// create an arraylist for seperated word
      String delimiter = new String("[^a-zA-Z']+"); // create a delimiter, including all non-alphabet characters
      String[] parts = text.split(delimiter);// split lines according to the delimiter
      for(String part: parts){
         if(!part.equals("")){
            if(useStopWords && !stopWords.contains(part.toLowerCase())){//check if to use stop words
               words.add(part.toLowerCase());} // put the splited words into an arraylist(words),
                                                 // delete all the empty strings,
                                                 // and convert all the upper cases to lower cases
            else if(!useStopWords){
               words.add(part.toLowerCase());
            }
         }
      }
		return words;
	}
	
	/**
	 * Takes a list of {@link Frequency}s and prints it to standard out. It also
	 * prints out the total number of items, and the total number of unique items.
    *
	 * @param frequencies A list of frequencies.
	 */
    
	public static void printFrequencies(List<Frequency> frequencies) 
   {
   
      /*count for total items*/
      int totalItems = 0; 
      for(Frequency f:frequencies)
      {
         totalItems += f.getFrequency();
      }
      
      /*print out results*/
      System.out.format("\nTotal item count: %d\n",totalItems);
      System.out.format("Unique item count: %d\n\n",frequencies.size());
      
		for(Frequency f:frequencies)
      {
         System.out.format("%-15s   %d\n",f.getText(),f.getFrequency());
      }
      
	}
}
