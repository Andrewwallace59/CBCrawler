package crawler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Spider {
	public Set<String> pagesSeen = new HashSet<String>();//unique list
	private Set<UrlRecord> records = new HashSet<UrlRecord>();//Output data
	private List<String> pagesToSee = new LinkedList<String>();//dynamic list
	
	public void search(String target) {
		pagesToSee.add(target);
		String currentUrl = target;
		while(!pagesToSee.isEmpty()) {
			SpiderLeg leg = new SpiderLeg(currentUrl);	
			currentUrl = getNextTarget();
			
			UrlRecord record = leg.crawl(currentUrl);
			
			if(record!=null) {
				pagesToSee.addAll(leg.getLinks());
				records.add(record);
			}
		}
		System.out.println(String.format("Crawl Complete: Visited %s web pages.", pagesSeen.size()));
		exportCSV();
	}
		
	private String getNextTarget() {
		String next = "";
		while(!pagesToSee.isEmpty()) { //More pages to see
			next = pagesToSee.remove(0); //ready next link
			if(!pagesSeen.contains(next)) //if link has not been seen before
				break; //escape loop	
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
}
