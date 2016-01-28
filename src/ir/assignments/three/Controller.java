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
        config.setMaxDepthOfCrawling(-1);
        
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
        controller.addSeed("http://www.ics.uci.edu/~lopes/");
        controller.addSeed("http://www.ics.uci.edu/~welling/");
        controller.addSeed("http://www.ics.uci.edu/");

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(Crawler.class, numberOfCrawlers);
    }

}


