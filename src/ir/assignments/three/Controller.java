// crawler4j

package ir.assignments.three;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {

	public static void main(String[] args) throws Exception {
		String crawlStorageFolder = "/data/crawl/root";
        int numberOfCrawlers = 7;

        CrawlConfig config = new CrawlConfig();
        config.setUserAgentString("UCI Inf141-CS121 crawler 76513468 10616769 20426226 64137163");
        config.setCrawlStorageFolder(crawlStorageFolder);
        
        // Add a politeness delay of 1200 ms 
        config.setPolitenessDelay(1200);

        config.setMaxDepthOfCrawling(-1);

        
        // Set the maximum number of pages to crawl, -1 means unlimited
        config.setMaxPagesToFetch(10);
        
        // Allow the crawler to resume crawling after it has stopped
        config.setResumableCrawling(false);
        
        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed("http://www.ics.uci.edu/");

        // Start the timer for crawlTime 
        Crawler.timeOfLastUpdate = System.currentTimeMillis( );
        
        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(Crawler.class, numberOfCrawlers);
        
        // to test crawl in Crawler class
        System.out.println("The result collection of URLs:");
        System.out.println(Crawler.crawl("http://www.ics.uci.edu/"));
        
        // Find out how much time it took to crawl the entire domain
        crawlTime();
        
        // Find out how many unique pages were found
        Crawler.findUnique();
    }
	
	// Method to find out how much time it took to crawl the entire domain and print it
	public static void crawlTime() {
		long timeElapsed = 0;
		try {
			Scanner sc = new Scanner(new File("timeFile.txt"));
			if (sc.hasNextInt()) {
				timeElapsed= sc.nextInt();
			}
			sc.close();
		}
		catch (FileNotFoundException e) {
		e.printStackTrace();
		}		


		System.out.println("Time elapsed: "+timeElapsed+" ms");
	}
	


	
	// Method to find the number of subdomains found.
	// Submit the list of subdomains ordered alphabetically and the number 
	// of unique pages detected in each subdomain. 
	// The file should be called Subdomains.txt, and its content should be 
	// lines containing the URL, a comma, a space, and the number.
	
	// Method to find the longest page in terms of number of words.
	// (Don't count HTML markup as words.)
	
	// Method to find the 500 most common words in this domain.
	// (Ignore English stop words, which can be found, for example, 
	// at http://www.ranks.nl/stopwords.) 
	// Submit the list of common words ordered by frequency 
	// (and alphabetically for words with the same frequency) 
	// in a file called CommonWords.txt.
	


	
	

}


