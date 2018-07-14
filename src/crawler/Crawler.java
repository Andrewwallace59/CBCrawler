package crawler;

import java.util.Scanner;

public class Crawler {
	
	public static void main(String args[]) {
		System.out.println("Please enter target domain: ");
		Scanner scanner = new Scanner(System.in);
		Spider spider = new Spider();
		spider.search(scanner.nextLine());
		scanner.close();
	}

}
