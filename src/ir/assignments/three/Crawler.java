// crawler4j

package ir.assignments.three;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.*;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.regex.Pattern;
import java.io.*;

public class Crawler extends WebCrawler {
      // contains subdomain names and pages
    class subDom
    {
        public String subd;
        public int pages;
        public subDom(String subd,int pages){
            this.subd = subd;
            this.pages = pages;
        }
    }
    
	 private static int totalURLs = 0;
    private static List<subDom> diffSubdomains = new ArrayList<subDom>();//list of different subdomains and pages we found
    private static List<String> subStr = new ArrayList<String>();//list of different subdomains we found
    
    // Time when the timeCalculator was last called
    public static long timeOfLastUpdate = 0;
    
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))$");
    
    
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
		// If it is, do not visit the page
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
		
		// append URL to the URL collection
		//allURLs.add(url);
		
		System.out.println("URL: " + url);
      
      System.out.println("Subdomain: " + subDomain);
     
      //subdomains
      if(!subStr.contains(subDomain)){
         subStr.add(subDomain);
         subDom sub = new subDom(subDomain,1);
         diffSubdomains.add(sub);
         //sort the subdomains
         Collections.sort(subStr);
         Collections.sort(diffSubdomains, new subDomComparator());
      }
      else{
         int index = subStr.indexOf(subDomain);
         subDom sub1 = diffSubdomains.get(index);
         sub1.pages += 1;
         diffSubdomains.set(index,sub1);
      }
      System.out.println("Different subdomain counting : " + diffSubdomains.size());
      
      try{
         FileWriter fileOfURLs = new FileWriter("Subdomains.txt");
         BufferedWriter bufferedWriter = new BufferedWriter(fileOfURLs);
         
         for(int i=0;i<diffSubdomains.size();i++) //write subdomains' URLs and pages into those subdomains to text file
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
         /*
         * 
         * you may want to use variable 'text' to count the 500 most common words and find the longest page
         *
         */
         
         //store the contents of the urls to a text file named contents.txt
         try{
            FileWriter fileOfContents = new FileWriter("contents.txt",true);
            //FileWriter indices = new FileWriter("Index.txt",true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileOfContents);
            //BufferedWriter indexWriter = new BufferedWriter(indices);
            
            bufferedWriter.write("INDEX " + totalURLs + " " + html.length());
            bufferedWriter.newLine();
            bufferedWriter.write(url);
            bufferedWriter.newLine();
            bufferedWriter.write(html);
            bufferedWriter.newLine();
            
            //indexWriter.write(totalURLs + " " + url);
            //indexWriter.newLine();
            //indexWriter.close();
            bufferedWriter.close();
            
         }catch(IOException e){
            e.printStackTrace();
         }
         timeCalculator();
         
		}
	}
	
	// Method to calculate the time spent crawling with stopping and resuming accounted for
	public static void timeCalculator() {
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
		
		// Add the timeSinceLastUpdate to timeElapsed to get the total time
		timeElapsed += timeSinceLastUpdate;
		
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
	 				|| url.contains("duttgroup.ics.uci.edu");  
 	} 


}
