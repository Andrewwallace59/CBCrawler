package crawler;

public class UrlRecord {
	
	private String url;
	private String title;
	private int status;
	
	public UrlRecord(String url, String title, int status) {
		this.url = url;
		this.title = title;
		this.status = status;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getStatus() {
		return status;
	}

}
