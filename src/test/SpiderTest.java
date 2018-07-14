package test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.junit.jupiter.api.Test;

import crawler.Spider;

class SpiderTest {
	
	private static final String TARGET = "https://caseblocks.com";
	private static final String OUTPUT = "SpiderWebs.csv";
				
	@Test
	void testCrawlNoMoreThan3PerSecond() {
		Spider spider = new Spider();
		long start = System.currentTimeMillis();
		spider.search(TARGET);
		long duration = System.currentTimeMillis() - start;
		int count = spider.pagesSeen.size();
		if(duration/count > 3000) //If average duration is greater than 3 seconds, fail.
			fail("Crawler was too quick! Average time: " + duration/count + "ms");
	}
	
	@Test
	void testSingleWebDomainLimit() {
		Spider spider = new Spider();
		spider.search(TARGET);
		for(String url: spider.pagesSeen) {
			if(!url.contains(TARGET)) {
				fail("URL found outside of target domain!");
			}
		}	
	}
	
	@Test
	void testOutputFileTypeSpreadsheet() {
		File file = new File(OUTPUT);
		if(!file.exists()  && !file.isDirectory())
			fail("Output file does not exist!");
	}
	
	@Test
	void testOutputContent() {
		//File file = new File(OUTPUT);
		//Read file..
		//List should contain URLs, titles & Status Codes
		fail("Not yet implemented");
	}
}
