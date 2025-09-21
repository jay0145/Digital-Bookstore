package bookshop;

import java.util.Date;

abstract class Book {
	
	private int barcode;
	private String type;
	private String genre;
	private String title;
	private Date releaseDate; //format: dd-mm-yyyy
	private int stock;
	private String language;
	private double price;
	
	public Book(int barcode, String type, String title, String language, String genre, Date releaseDate,
			int stock, double price) {
		
		this.barcode = barcode;
		this.type = type;
		this.genre = genre;
		this.title = title;
		this.releaseDate = releaseDate;
		this.stock = stock;
		this.language = language;
		this.price = price;
		
	}
	
	public int getBarcode() {
		return this.barcode;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getGenre() {
		return this.genre;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public Date getReleaseDate() {
		return this.releaseDate;
	}
	
	public int getStock() {
		return this.stock;
	}
	
	public String getLanguage() {
		return this.language;
	}
	
	public double getPrice() {
		return this.price;
	}
	
	public void incrementStock() {
		this.stock += 1;
	}
	
	public void setStock(int num) {
		this.stock = num;
	}
	
}
