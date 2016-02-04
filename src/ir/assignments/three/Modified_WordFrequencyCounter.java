package ir.assignments.three;

import ir.assignments.three.Frequency;
import ir.assignments.three.Modified_Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;
import java.util.Comparator;
import java.lang.ArrayIndexOutOfBoundsException;

/**
 * Counts the total number of words and their frequencies in a text file.
 */
public final class Modified_WordFrequencyCounter {
   private static List<Frequency> lst = new ArrayList<Frequency>();

	public Modified_WordFrequencyCounter(){
   }
   
   public List<Frequency> getFreqList(){
      return this.lst;
   }
   
	/**
   * Implement a custom Comparator to sort the list of two gram frequencies.
   *
   * Firstly, sort the Frequency objects according to their frequency,
   * then, if they have the same frequency, sort them by their text.
   *
   */
   public static class FrequencyComparator implements Comparator<Frequency> {
      public int compare(Frequency a, Frequency b) 
      {
        int freComparison = b.getFrequency() - a.getFrequency();
        // Firstly, sort the Frequency objects according to their frequency,
        // if they have the same frequency, sort them by their text
        return freComparison == 0 ? a.getText().compareTo(b.getText()) : freComparison;      
      }
   }
   
   private static int findIndex(String word){
      for(int i=0; i<lst.size();i++){
         if(word.equals(lst.get(i).getText())){
            return i;
         }
      }
      return -1;
   
   }
   
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 *  
	 * @param words A list of words.
	 * @return A list of word frequencies, ordered by decreasing frequency.
	 */
	public static List<Frequency> computeWordFrequencies(String text, boolean useStopWords){
		
      List<String> words = Modified_Utilities.tokenizeFile(text, useStopWords);
      for(String st: words) // for every word in words:
      {
         // check if the word is in the lstStr(if has appeared once)
         // if not, add it to lst and lstStr
         if(findIndex(st) == -1) 
         {
            lst.add(new Frequency(st,1));  
         }
         
         // if yes,increment the frequency of that word
         else
         {
            // use lstStr to get the index of the Frequency of that word in the lst
            int index = findIndex(st); 
            Frequency temp = lst.get(index);
            temp.incrementFrequency();
            lst.set(index,temp);
         }
      }
      Collections.sort(lst,new FrequencyComparator());// sort the list using a custom Comparator
		return lst;
	}
}
