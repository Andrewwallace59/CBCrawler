package crawler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Spider {
	public Set<String> pagesSeen = new HashSet<String>();//unique list
	private Set<UrlRecord> records = new HashSet<UrlRecord>();//Output data
	private List<String> pagesToSee = new LinkedList<String>();//dynamic list
	
	private String domainTarget;
	
	public void search(String target) {
		pagesToSee.add(target);
		String currentUrl = target;
		try {
			domainTarget = getDomainName(target);
		} catch (URISyntaxException e) {
			System.out.println("ERROR - Problem finding domain. " + e);
		}
		System.out.println("Crawling " + domainTarget);
		
		while(!pagesToSee.isEmpty()) {
			SpiderLeg leg = new SpiderLeg(domainTarget);	
			currentUrl = getNextTarget();
			if(currentUrl.equals("DONE"))
				break;
			
			UrlRecord record = leg.crawl(currentUrl);
			if(record!=null) {
				if(!records.contains(record)) {
					pagesToSee.addAll(leg.getLinks());
					records.add(record);
					System.out.println(String.format("Record %s: %s", records.size(), record.getUrl()));
				} 
			}
			//displayLists();
		}
		System.out.println(String.format("Crawl Complete: Visited %s web pages.", pagesSeen.size()));
		exportCSV();
	}
	
	//Debugging method.
//	private void displayLists() {
//		System.out.println("ToSee: " + pagesToSee.size());
//		for(String url: pagesToSee) {
//			System.out.println("ToSee: " + url);
//		}	
//		System.out.println("Seen: " + pagesSeen.size());
//		for(String url: pagesSeen) {
//			System.out.println("Seen: " + url);
//		}
//		System.out.println("Records: " + records.size());
//	}
		
	private String getNextTarget() {
		String next = "DONE";
		while(!pagesToSee.isEmpty()) { //More pages to see
			next = pagesToSee.remove(0); //ready next link
			if(!pagesSeen.contains(next)) //if link has not been seen before
				break; //escape loop
			else
				next = "DONE";//When list becomes empty, ensures last url is not repeatedly crawled.
		}
		this.pagesSeen.add(next); //Add next to seen list
		return next; //return next url to visit.
	}
	
	private void exportCSV() {
		try {
			PrintWriter writer = new PrintWriter(new File("SpiderWebs.csv"));
			StringBuilder sb = new StringBuilder();
			sb.append("Url");
			sb.append(",");
			sb.append("Title");
			sb.append(",");
			sb.append("Status Code");
			sb.append("\n");
			for(UrlRecord record:records) {
				sb.append(record.getUrl());
				sb.append(",");
				sb.append(record.getTitle());
				sb.append(",");
				sb.append(record.getStatus());
				sb.append("\n");
			}
			writer.write(sb.toString());
			writer.close();
		} catch (IOException ioe) {
			System.out.println("ERROR - Problem exporting CSV file." + ioe);
		}
	}
	
	//Removes extra characters from domain name 
	public static String getDomainName(String url) throws URISyntaxException {
		   URI uri = new URI(url);
		   String domain = uri.getHost();
		   return domain.startsWith("www.") ? domain.substring(4) : domain;
	}
}
