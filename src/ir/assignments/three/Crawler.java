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
	private static int totalURLs = 0; //total amount of URLs we found
    private static List<String> diffSubdomains = new ArrayList<String>(); //list of different subdomains we found
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

   private void initialFileWriter(){
      
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
     
      //count different subdomains
      if(!diffSubdomains.contains(subDomain)){
         diffSubdomains.add(subDomain);
      }
      System.out.println("Different subdomain counting : " + diffSubdomains.size());
	
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
         
         //store all URLs we found into a text called URLs
         try{
            FileWriter fileOfURLs = new FileWriter("URLs.txt");//,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileOfURLs);
            bufferedWriter.write(url);
            bufferedWriter.newLine();
            bufferedWriter.close();
         }catch(IOException e){
            e.printStackTrace();
         }
		}
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
