package crawler;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SpiderLeg {
	 //fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	
    private String target;
    private List<String> links = new LinkedList<String>();
	
	public SpiderLeg(String target) {
		this.target = target;
	}
	
	public UrlRecord crawl(String url) {
		System.out.println("Crawling: " + url);
		try {
			Connection con = Jsoup.connect(url).userAgent(USER_AGENT);
			Document htmlDoc = con.get();
			UrlRecord record = new UrlRecord(url, htmlDoc.title(), con.response().statusCode());
			
			Elements linksFound = htmlDoc.select("a[href]");
			
			for(Element link : linksFound) {
				if(link.absUrl("href").contains(target))
					links.add(link.absUrl("href"));
			}
			return record;
		} catch(IOException ioe) {
			System.out.println("ERROR - Problem with in out HTTP request " + ioe);
			return null;
		}
	}
	
	public List<String> getLinks() {
		return this.links;
	}

}
