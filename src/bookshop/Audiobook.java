package bookshop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Audiobook extends Book{
	
	private static List<Audiobook> AudioStock = new ArrayList<Audiobook>();
	private double duration;
	private String format; 
	
	public Audiobook(int barcode, String type, String title, String language, String genre, Date releaseDate, int stock,
			double price, double duration, String format ) {
		super(barcode, type, title, language, genre, releaseDate, stock, price);
		
		this.duration = duration;
		this.format = format;
	}
	
	public static List<Audiobook> getAudioStock(){
		return AudioStock;
	}
	
	public static void loadAudiobooks(File stock){
		List<Audiobook> availBooks = new ArrayList<Audiobook>();
		try {
			Scanner sc = new Scanner(stock);
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] recordList = line.split(",");
				recordList = Utils.stripArray(recordList);
				if (recordList[1].equals("audiobook")) {
					Audiobook ab = new Audiobook(Integer.parseInt(recordList[0]), recordList[1], recordList[2],
							recordList[3], recordList[4], Utils.castToDate(recordList[5]), Integer.parseInt(recordList[6]),
							Double.parseDouble(recordList[7]),Double.parseDouble(recordList[8]), recordList[9]);
					
					availBooks.add(ab);
				}
			}
			sc.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error has occurred.");
		}
		Audiobook.AudioStock = availBooks;
	}
	
	public double getDuration() {
		return this.duration;
	}
	
	public String getFormat() {
		return this.format;
	}
}
