// crawler4j
/*
Kristina Wong, 76513468
Haoming Li, 20426226
Shengjie Xu, 10616769
Yirui Jiang, 64137163
*/

package ir.assignments.three;

import java.util.Collection;
import java.util.Set;
import java.util.*;
import ir.assignments.three.Frequency;
import ir.assignments.three.Modified_Utilities;
import ir.assignments.three.Modified_WordFrequencyCounter;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.regex.Pattern;
import java.io.*;

public class Crawler extends WebCrawler {
      /** contains subdomain names and pages*/
    class subDom
    {
        public String subd;
        public int pages;
        public subDom(String subd,int pages){
            this.subd = subd;
            this.pages = pages;
        }
    }

    /** contains urls and the number of words in that page*/
    private static class urlWordsCounters
    {
        public String url;
        public int count;
        public urlWordsCounters(String url,int count){
            this.url = url;
            this.count = count;
        }    
    }
   
	 private static int totalURLs = 0;
    private static List<subDom> diffSubdomains = new ArrayList<subDom>();//list of different subdomains and pages we found
    private static List<String> subStr = new ArrayList<String>();//list of different subdomains we found
    private static List<Frequency> lst = new ArrayList<Frequency>();//list of word frequencies
    private static Modified_WordFrequencyCounter wfc = new Modified_WordFrequencyCounter(); //use for counting frequencies
    private static urlWordsCounters largestPage = new urlWordsCounters("None",0); // the largest page and the number of words in the page
        
    // Time when the timeCalculator was last called
    public static long timeOfLastUpdate = 0;
    
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
                +   "|ps|eps|tex|ppt|pptx|doc|docx|xls|xlsx|names|data|dat|exe|bz2|tar|msi|bin|7z|psd|dmg|iso|epub|dll|cnf|tgz|sha1" 
	            +   "|wav|avi|mov|mpeg|ram|m4v|mkv|ogg|ogv|pdf" + "|thmx|mso|arff|rtf|jar|csv"
            + "|png|mp3|mp3|zip|gz|xls|xlsx|ppt|pptx|ps|bigwig|bw|mp4))$");

    // Create a collection of all URLs
    private static Collection<String> allURLs = new LinkedHashSet<String>();
	/**
	 * This method is for testing purposes only. It does not need to be used
	 * to answer any of the questions in the assignment. However, it must
	 * function as specified so that your crawler can be verified programatically.
	 * 
	 * This methods performs a crawl starting at the specified seed URL. Returns a
	 * collection containing all URLs visited during the crawl.
	 */
	
	public static Collection<String> crawl(String seedURL) {
		return allURLs;
	}

    //Implement a custom Comparator to sort the list of subDom
   public static class subDomComparator implements Comparator<subDom> {
      public int compare(subDom a, subDom b) 
      {
         return a.subd.compareTo(b.subd);
      }
   }

	/**
	* This method receives two parameters. The first parameter is the page
	* in which we have discovered this new url and the second parameter is
	* the new url. You should implement this function to specify whether
	* the given url should be crawled or not (based on your crawling logic).
	* In this example, we are instructing the crawler to ignore urls that
	* have css, js, git, ... extensions and to only accept urls that start
	* with "http://www.ics.uci.edu/". In this case, we didn't need the
	* referringPage parameter to make the decision.
	*/
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();

		// Check if URL is a sub-domain of ics.uci.edu and if it is a trap
		// If it is a trap, do not visit the page
		return !FILTERS.matcher(href).matches()
		&& href.contains(".ics.uci.edu")
		&& !findTrap(href);
	}
	
	/**
	* This function is called when a page is fetched and ready
	* to be processed by your program.
	*/
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
      String subDomain = page.getWebURL().getSubDomain();
		
		System.out.println("URL: " + url);
      System.out.println("Subdomain: " + subDomain);
     
      //subdomains
      if(!subStr.contains(subDomain)){//check if the subdomain has appeared once, if not, append it to the subdomain list
         subStr.add(subDomain);
         subDom sub = new subDom(subDomain,1);
         diffSubdomains.add(sub);
         //sort the subdomains
         Collections.sort(subStr);
         Collections.sort(diffSubdomains, new subDomComparator());
      }
      else{// if the subdomain has appeared once, increment the page count for that subdomain
         int index = subStr.indexOf(subDomain);
         subDom sub1 = diffSubdomains.get(index);
         sub1.pages += 1;
         diffSubdomains.set(index,sub1);
      }
      System.out.println("Different subdomain counting : " + diffSubdomains.size());
      
      try{
         FileWriter fileOfURLs = new FileWriter("Subdomains.txt");
         BufferedWriter bufferedWriter = new BufferedWriter(fileOfURLs);
         //write subdomains' URLs and pages into Subdomains.txt
         for(int i=0;i<diffSubdomains.size();i++) 
         {
            String str = diffSubdomains.get(i).subd + ", " + diffSubdomains.get(i).pages;
            bufferedWriter.write(str);
            bufferedWriter.newLine();
         }
         bufferedWriter.close();
         
      }catch(IOException e){
         e.printStackTrace();      
	   }
      
      
      
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText(); 
			String html = htmlParseData.getHtml();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();

	      totalURLs += 1;
			System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			System.out.println("Number of outgoing links: " + links.size());
         System.out.println("URL counting: " + totalURLs);
         
         //counting 500 common words
         wfc.computeWordFrequencies(text,true);
         
         //looking for the largest page
         List<String> pageWords = Modified_Utilities.tokenizeFile(text, false);
         int numberWords = pageWords.size();
         
         if(largestPage.count < numberWords){
            largestPage.count = numberWords;
            largestPage.url = url; 
         }
         
         //print out the page with the largest number of words
         System.out.println("\nThe largest page(with number of words) :");
         System.out.println(largestPage.count + " " + largestPage.url);
                  
         try{
            // create directory contents to store the contents of every pages
            File file = new File("contents");
            if (!file.exists()) {
		         if (file.mkdir()) {
			         System.out.println("Directory 'contents' is created!");
		         }
	         }
            FileWriter fileOfContents = new FileWriter("contents/" + totalURLs + ".txt");
            FileWriter indices = new FileWriter("indices.txt",true);
            FileWriter freq = new FileWriter("CommonWords.txt");
            
            BufferedWriter bufferedWriter = new BufferedWriter(fileOfContents);
            BufferedWriter freqWriter = new BufferedWriter(freq);
            BufferedWriter indicesWriter = new BufferedWriter(indices);      
            
            //store 500 common words
            int forPrint;
            if (wfc.getFreqList().size() > 500){
               forPrint = 500;
            }
            else{
               forPrint = wfc.getFreqList().size();
            }
            for(int i=0; i < forPrint;i++){
               freqWriter.write(String.format("%-20s %d",wfc.getFreqList().get(i).getText(), wfc.getFreqList().get(i).getFrequency()));
               freqWriter.newLine();
            }
            
            //create a text file for the indices of contents
            indicesWriter.write("INDEX " + totalURLs + " " + url);
            indicesWriter.newLine();
            
            
            //store the indices and contents to text file           
            bufferedWriter.write("INDEX " + totalURLs + " " + text.length());
            bufferedWriter.newLine();
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            
            bufferedWriter.close();
            freqWriter.close();
            indicesWriter.close();
            
         }catch(IOException e){
            e.printStackTrace();
         }
         timeCalculator(url);
         
		}
	}
	
	// Method to calculate the time spent crawling with stopping and resuming accounted for
	public static void timeCalculator(String url) {
		long timeElapsed = 0;
		
		File file = new File("timeFile.txt");

		if (file.exists()) {
			try {
				// Check if timeFile already has a startTime
				Scanner sc = new Scanner(file);
				if (sc.hasNextInt()) {
					timeElapsed= sc.nextInt();			
				}
				sc.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}			
		}
		
		// Get the current time
		long currentTime = System.currentTimeMillis( );
		
		// Find the time since this method was last called
		long timeSinceLastUpdate = currentTime - timeOfLastUpdate;
		
		if (!url.matches("http://www.ics.uci.edu/")) {
			// Add the timeSinceLastUpdate to timeElapsed to get the total time
			timeElapsed += timeSinceLastUpdate;
		}

		
		// Create or overwrite a file and write the timeElapsed to it
		PrintWriter writer;
		try {
			writer = new PrintWriter("timeFile.txt");
			writer.println(timeElapsed);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
		
		// Update the timeOfLastUpdate variable
		timeOfLastUpdate = currentTime;
	}
	
	// Print the count of the unique pages 
	public static void findUnique() { 
 		System.out.println("Number of unique pages: "+totalURLs); 
 	} 

	
	// Return true if a URL is a crawler trap  
	public static boolean findTrap(String url){ 
	 		return url.contains("calendar.ics.uci.edu")
	 				|| url.contains("duttgroup.ics.uci.edu")
	 				|| url.contains("ics.uci.edu/prospective")
	 				|| url.contains("wics.ics.uci.edu");  
 	} 


}
