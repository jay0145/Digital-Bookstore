package bookshop;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Utils {
	
	public static Date castToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		try {
			Date releaseDate = formatter.parse(strDate);
			return releaseDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null; //if no date is found in string, 
		
	}
	public static String[] stripArray(String[] array) {
		String[] books = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			String stripped = array[i].strip();
			books[i] = stripped;
		}
		return books;
	}
	
	public static void loadBooks(File stock) {
		Paperback.loadPbBooks(stock);
		Ebook.loadEbooks(stock);
		Audiobook.loadAudiobooks(stock);
		
	}
}
