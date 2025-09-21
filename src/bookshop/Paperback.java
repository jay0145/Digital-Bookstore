package bookshop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Paperback extends Book{
	
	private static List<Paperback> PbStock = new ArrayList<Paperback>();
	
	private int numOfPages;
	private String condition;
	
	
	public Paperback(int barcode, String type, String title, String language, String genre, Date releaseDate, int stock,
			double price, int numOfPages, String condition ) {
		super(barcode, type, title, language, genre, releaseDate, stock, price);
		
		this.numOfPages = numOfPages;
		this.condition = condition;
	
	}
	
	public static List<Paperback> getPbStock(){
		return PbStock;
	}
	
	public static void loadPbBooks(File stock){
		List<Paperback> availBooks = new ArrayList<Paperback>();
		try {
			Scanner sc = new Scanner(stock);
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] recordList = line.split(",");
				recordList = Utils.stripArray(recordList);
				if (recordList[1].equals("paperback")) {
					Paperback pb = new Paperback(Integer.parseInt(recordList[0]), recordList[1], recordList[2],
							recordList[3], recordList[4], Utils.castToDate(recordList[5]), Integer.parseInt(recordList[6]),
							Double.parseDouble(recordList[7]),Integer.parseInt(recordList[8]), recordList[9]);
					
					availBooks.add(pb);
				}
			}
			sc.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error has occurred.");
		}
		Paperback.PbStock = availBooks;
	}
	
	public int getNumOfPages() {
		return this.numOfPages;
	}
	
	public String getCondition() {
		return this.condition;
	}

}
