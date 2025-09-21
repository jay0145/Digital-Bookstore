package bookshop;
import java.util.*;
import java.io.*;
import java.text.*;

public class Customer extends User{
	private static List<Customer> validCustomers = new ArrayList<Customer>();
	
	private double credit;
	private List<Book> basket = new ArrayList<Book>();
	String role;
	
	public Customer(int userid, String username, String surname, int houseNum, String postcode,
			String city, double credit, String role) {
		super(userid, username, surname, houseNum, postcode, city, role);
		
		this.credit = credit;
		this.basket = basket;
		this.role = role;
		
	}
	
	public static List<Customer> getValidCustomers(){
		return validCustomers;
	}
	
	public static void loadCustomers(File users) {
		List<Customer> userList = new ArrayList<Customer>();
		try {
			Scanner sc = new Scanner(users);
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] recordList = line.split(",");
				recordList = Utils.stripArray(recordList);
				if (recordList[recordList.length - 1].equals("customer")) {
					
					Customer cust = new Customer(Integer.parseInt(recordList[0]), recordList[1], recordList[2],
							Integer.parseInt(recordList[3]), recordList[4], recordList[5], Double.parseDouble(recordList[6]), recordList[7]);
					userList.add(cust);
				}
			}
			sc.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error - File not found.");
			
		}
		Customer.validCustomers =  userList;
	}

	
	public double getCredit() {
		return this.credit;
	}
	
	public List<Book> getBasket(){
		return this.basket;
	}
	
	public void displayBalance() {
		String fCredit = String.format("%.2f", this.credit);
		System.out.println("Your Current Balance is: £" + fCredit);
	}
	
	public void welcomeMessage() {
		System.out.println("Welcome "+this.getSurname()+"! \n"
				 + "You have logged in as: "+this.getRole());
	}
	
	public void displayMenu() {
		System.out.print("\nMenu: (Select option by number) \n"
		+ "[0]Logout\n[1]View Books\n[2]View Basket\n[3]View Balance\n"
		+ "[4]Add Item to Basket\n[5]Search\n[6]Pay\nEnter choice: ");
	}
	
	public void displayReceipt(double total) {
		System.out.println("Thank you for the purchase! £" + String.format("%.2f", total) + " paid and your remaining credit"
				+ " balance is £" + String.format("%.2f",this.getCredit()) + ".\nYour delivery address is HouseNo: " + this.getHouseNum() + " Postcode: " 
				+ this.getPostcode() + " City: " + this.getCity() + ".");
	}
	
	public void viewBooks(File stock) {
		// Order by Price (7th element)
		List<String> bookEntries = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(stock))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bookEntries.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Custom comparator based on the price (7th element)
        Comparator<String> quantityComparator = Comparator.comparingDouble(line -> {
            String[] elements = line.split(",");
            if (elements.length >= 7) {
                return Double.parseDouble(elements[7].trim());
            } else {
                // Handle cases where the stock is not present or not in the expected position
                return 0;
            }
        });

        Collections.sort(bookEntries, quantityComparator);
        
        // Column headings
        System.out.println("Books");
        System.out.println("-----");
        System.out.printf("%-10s %-10s %-25s %-10s %-18s %-15s %-8s %-8s %-8s %-8s%n",
                "Book ID", "Type", "Title", "Language", "Genre", "Date Published",
                "Quantity", "Price", "Pages", "Format");

        // Print book entries in a formatted table
        for (String entry : bookEntries) {
            String[] elements = entry.split(", ");
            elements[7] = String.format("%.2f", Double.parseDouble(elements[7]));
            System.out.printf("%-10s %-10s %-25s %-10s %-18s %-15s %-8s %-8s %-8s %-8s%n",
                    elements[0], elements[1], elements[2], elements[3], elements[4], elements[5],
                    elements[6], elements[7], elements[8], elements[9]);
        }
	}
	
	public void searchForBook(String searchstr) {
		System.out.println("not implemented.");
	}
	
	public void addItemToBasket(String barcode, Scanner sc) {
		//add Book object to shopping basket
		String validBarcode = checkBookId(barcode, sc);
		boolean bookExists = checkIfBookExists(validBarcode);
		boolean bookAlreadyInBasket = checkIfBookInBasket(validBarcode);
		if (bookExists==true) {
			if (!bookAlreadyInBasket) {
				addBookObject(validBarcode, sc);
			}
			else {
				System.out.println("Book is already added to basket.");
			}
		}
		else {
			System.out.println("Book with barcode: " + validBarcode + " was not found.");
		}
	}
	
	private void addBookObject(String barcode, Scanner sc) {
		for (Paperback p: Paperback.getPbStock()) {
			if (p.getBarcode()==Integer.parseInt(barcode)) {
				Paperback temp = new Paperback(p.getBarcode(), p.getType(), p.getTitle(), p.getLanguage(), p.getGenre(), p.getReleaseDate(),
						p.getStock(), p.getPrice(), p.getNumOfPages(), p.getCondition());
				System.out.println(temp.getBarcode()+" | "+temp.getType()+" | "+temp.getTitle());
				System.out.print("Add to Basket? Y/N: ");
				String choice = sc.nextLine();
				if (choice.toUpperCase().equals("Y")) {
					System.out.print("Enter Quantity: ");
					String quantity = sc.nextLine();
					
					if (Integer.parseInt(quantity)<=p.getStock()) {
						temp.setStock(Integer.parseInt(quantity));
						this.basket.add(temp);
						System.out.println(temp.getTitle() + " added to basket!");
					}else {
						System.out.println("Not enough books in stock to satisfy request.");
					}
					
				}
				else if (choice.toUpperCase().equals("N")) {
					System.out.println("Back to Menu.");
				}
				else {
					System.out.println("Please enter a Y or N.");
				}
			}
		}
		for (Ebook e: Ebook.getEStock()) {
			if (e.getBarcode()==Integer.parseInt(barcode)) {
				Ebook temp = new Ebook(e.getBarcode(), e.getType(), e.getTitle(), e.getLanguage(), e.getGenre(), e.getReleaseDate(),
						e.getStock(), e.getPrice(), e.getNumOfPages(), e.getFormat());
				System.out.println(temp.getBarcode()+" | "+temp.getType()+" | "+temp.getTitle());
				System.out.print("Add to Basket? Y/N: ");
				String choice = sc.nextLine();
				if (choice.toUpperCase().equals("Y")) {
					System.out.print("Enter Quantity: ");
					String quantity = sc.nextLine();
					
					if (Integer.parseInt(quantity)<=e.getStock()) {
						temp.setStock(Integer.parseInt(quantity));
						this.basket.add(temp);
						System.out.println(temp.getTitle() + " added to basket!");
					}else {
						System.out.println("Not enough books in stock to satisfy request.");
					}
					
				}
				else if (choice.toUpperCase().equals("N")) {
					System.out.println("Back to Menu.");
				}
				else {
					System.out.println("Please enter a Y or N.");
				}
			}
		}
		for (Audiobook a: Audiobook.getAudioStock()) {
			if (a.getBarcode()==Integer.parseInt(barcode)) {
				Audiobook temp = new Audiobook(a.getBarcode(), a.getType(), a.getTitle(), a.getLanguage(), a.getGenre(), a.getReleaseDate(),
						a.getStock(), a.getPrice(), a.getDuration(), a.getFormat());
				System.out.println(temp.getBarcode()+" | "+temp.getType()+" | "+temp.getTitle());
				System.out.print("Add to Basket? Y/N: ");
				String choice = sc.nextLine();
				if (choice.toUpperCase().equals("Y")) {
					System.out.print("Enter Quantity: ");
					String quantity = sc.nextLine();
					
					if (Integer.parseInt(quantity)<=a.getStock()) {
						temp.setStock(Integer.parseInt(quantity));
						this.basket.add(temp);
						System.out.println(temp.getTitle() + " added to basket!");
					}else {
						System.out.println("Not enough books in stock to satisfy request.");
					}
				}
				else if (choice.toUpperCase().equals("N")) {
					System.out.println("Back to Menu.");
				}
				else {
					System.out.println("Please enter a Y or N.");
				}
			}
		}
	}
	
	private boolean checkIfBookInBasket(String barcode) {
		for (Book b: this.getBasket()) {
			if (b.getBarcode()==Integer.parseInt(barcode)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkIfBookExists(String barcode) {
		for (Paperback p: Paperback.getPbStock()) {
			if (p.getBarcode()==Integer.parseInt(barcode)) {
				return true;
			}
		}
		for (Ebook e: Ebook.getEStock()) {
			if (e.getBarcode()==Integer.parseInt(barcode)) {
				return true;
			}
		}
		for (Audiobook a: Audiobook.getAudioStock()) {
			if (a.getBarcode()==Integer.parseInt(barcode)) {
				return true;
			}
		}
		return false;
	}
	
	public void pay(File Stock, File UserAccounts) {
		//check sufficient funds
		double total = calculateTotal();
		if (total<=this.credit) {
			int booksInStock = checkAllBooksInStock();
			if (booksInStock==1) {
				
				for (Book b: this.basket) {
					String bookIdToUpdate = String.valueOf(b.getBarcode());
					int quantityOfBook = b.getStock();
					
					List<String> bookRecords = readRecords(Stock);

			        // Update the stock value for the book record
			        updateStockValue(bookRecords, bookIdToUpdate, quantityOfBook, total);
	
			        // Write the updated records back to the file
			        writeRecords(Stock, bookRecords);
				}
				
				updateCustomerBalance(total, UserAccounts);
        		cancelAllItems(); //clears basket
				displayReceipt(total);
				Utils.loadBooks(Stock);
				
			}else {
				System.out.println("Book ID: " + booksInStock + " is out of stock.");
			}
		}else {
			System.out.println("You have Insufficient Funds to pay.");
		}
	}
	
	private List<String> readRecords(File txtfile) {
        List<String> bookRecords = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(txtfile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bookRecords.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bookRecords;
    }
	
	//updates specific stock value in text file
    private void updateStockValue(List<String> bookRecords, String bookId, int quantityOfBook, double total) {
        for (int i = 0; i < bookRecords.size(); i++) {
            String record = bookRecords.get(i);
            String[] elements = record.split(", ");
            if (elements[0].equals(bookId)) {
            	int newStockValue = Integer.parseInt(elements[6]) - quantityOfBook;
        		elements[6] = String.valueOf(newStockValue);
        		bookRecords.set(i, String.join(", ", elements));
        		break;
            }
        }
        
    }
    
    private void writeRecords(File file, List<String> bookRecords) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String record : bookRecords) {
                writer.write(record);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	private void updateCustomerBalance(double total, File UserAccounts){
    	this.credit -= total;
    	String userIdToUpdate = String.valueOf(this.getuserID());
		double credit = this.getCredit();

		List<String> userRecords = readRecords(UserAccounts);

        // Update the stock value for the book record
        updateUserCredit(userRecords, userIdToUpdate, credit);

        // Write the updated records back to the file
        writeRecords(UserAccounts, userRecords);
		
    }
	
	private void updateUserCredit(List<String> userRecords, String userID, double credit) {
        for (int i = 0; i < userRecords.size(); i++) {
            String record = userRecords.get(i);
            String[] elements = record.split(", ");
            if (elements[0].equals(userID)) {
            	double newCreditValue = credit;
        		elements[6] = String.format("%.2f", newCreditValue);
        		userRecords.set(i, String.join(", ", elements));
        		break;
            }
        }
        
    }
	
	public int checkAllBooksInStock() {
		for (Book b: this.basket) {
			for (Paperback p: Paperback.getPbStock()) {
				if (b.getBarcode()==p.getBarcode()) {
					if(p.getStock()==0) {
						return b.getBarcode();
					}
				}
			}
			for (Ebook e: Ebook.getEStock()) {
				if (b.getBarcode()==e.getBarcode()) {
					if (e.getStock()==0) {
						return b.getBarcode();
					}
				}
			}
			for (Audiobook a: Audiobook.getAudioStock()) {
				if (b.getBarcode()==a.getBarcode()) {
					if (a.getStock()==0) {
						return b.getBarcode();
					}
					
				}
			}
		}
		return 1;
	}
	
	public double calculateTotal() {
		double total= 0;
		for (Book b: this.basket) {
			total+= b.getPrice() * b.getStock();
		}
		return total;
	}
	
	public void cancelAllItems() {
		this.basket.clear();
		System.out.println("Basket Cleared!");
	}
	
	public void viewBasket(Scanner sc) {
		double total = 0;
		System.out.println("Basket");
		System.out.println("------");
		System.out.printf("%-10s %-10s %-25s %-10s %-18s %-15s %-8s %-8s%n",
                "Book ID", "Type", "Title", "Language", "Genre", "Date Published",
                "Quantity", "Price");
		for (Book b: this.basket) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	        String formattedDate = dateFormat.format(b.getReleaseDate());
			total += b.getPrice() * b.getStock();
			System.out.printf("%-10s %-10s %-25s %-10s %-18s %-15s %-8s %-8s%n",
	                b.getBarcode(), b.getType(), b.getTitle(), b.getLanguage(),b.getGenre(), formattedDate,
	                b.getStock(), String.format("%.2f",b.getPrice()));
		}
		System.out.println("Total: £" + String.format("%.2f",total));
		
		System.out.print("[1]Return to Main Menu\n[2]Clear Basket\nEnter: ");
		String option = sc.nextLine();
		int choice = checkOption(option, 2, sc);
		if (choice==2) {
			cancelAllItems();
		}
	}
	
	

}
