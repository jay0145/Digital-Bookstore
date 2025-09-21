package bookshop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Ebook extends Book{
	private static List<Ebook> EStock = new ArrayList<Ebook>();
	private int numOfPages;
	private String format;

	public Ebook(int barcode, String type, String title, String language, String genre, Date releaseDate, int stock,
			double price, int numOfPages, String format ) {
		super(barcode, type, title, language, genre, releaseDate, stock, price);
		
		this.numOfPages = numOfPages;
		this.format = format;
		
	}
	
	public static List<Ebook> getEStock(){
		return EStock;
	}
	
	public static void loadEbooks(File stock){
		List<Ebook> availBooks = new ArrayList<Ebook>();
		try {
			Scanner sc = new Scanner(stock);
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] recordList = line.split(",");
				recordList = Utils.stripArray(recordList);
				//System.out.println(castToDate("21-08-2020"));
				if (recordList[1].equals("ebook")) {
					Ebook eb = new Ebook(Integer.parseInt(recordList[0]), recordList[1], recordList[2],
							recordList[3], recordList[4], Utils.castToDate(recordList[5]), Integer.parseInt(recordList[6]),
							Double.parseDouble(recordList[7]),Integer.parseInt(recordList[8]), recordList[9]);
					
					availBooks.add(eb);
				}
			}
			sc.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error has occurred.");
		}
		Ebook.EStock = availBooks;
	}
	
	public int getNumOfPages() {
		return this.numOfPages;
	}
	
	public String getFormat() {
		return this.format;
	}

}
