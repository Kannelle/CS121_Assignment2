// crawler4j

package ir.assignments.three;

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
        
        // Add a politeness delay of 600 ms 
        config.setPolitenessDelay(600);

        // Set maximum crawl depth
        config.setMaxDepthOfCrawling(1);

        
        // Set the maximum number of pages to crawl
        config.setMaxPagesToFetch(8000);
        
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
        long start = System.currentTimeMillis( );
        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(Crawler.class, numberOfCrawlers);
        
        // Find out how much time it took to crawl the entire domain
        crawlTime(start);
    }
	
	// Method to find out how much time it took to crawl the entire domain and print it
	public static void crawlTime (long startTime) {
		long endTime = System.currentTimeMillis( );
		long elapsedTime = endTime - startTime;
		System.out.println("Time elapsed: "+elapsedTime+" ms");
	}
	
	// Method to determine how many unique pages found in the entire domain
	// (Uniqueness is established by the URL, not the page's content.)
	
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


