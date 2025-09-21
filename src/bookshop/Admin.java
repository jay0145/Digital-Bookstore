package bookshop;
import java.util.*;
import java.io.*;

public class Admin extends User{
	
	static private List<Admin> validAdmins = new ArrayList<Admin>();
	String role;
	
	public Admin(int userid, String username, String surname, int houseNum,
			String postcode, String city, String role) {
		super(userid, username, surname, houseNum, postcode, city, role);
		
		this.role = role;
	}
	
	public static List<Admin> getValidAdmins() {
		return validAdmins;
	}
	
	public void welcomeMessage() {
		System.out.println("Welcome "+this.getSurname()+"! \n"
				 + "You have logged in as: "+this.getRole());
	}
	
	public void displayMenu() {
		System.out.print("\nMenu: (Select option by number) \n"+
				 "[0]Logout\n[1]Add Book\n[2]View Books\nEnter choice: ");
	}
	
	public static void loadAdmins(File users) {
		List<Admin> userList = new ArrayList<Admin>();
		try {
			Scanner sc = new Scanner(users);
			while(sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] recordList = line.split(",");
				recordList = Utils.stripArray(recordList);
				if (recordList[recordList.length - 1].equals("admin")) {
					
					Admin ad = new Admin(Integer.parseInt(recordList[0]), recordList[1], recordList[2],
							Integer.parseInt(recordList[3]), recordList[4], recordList[5], recordList[7]);
					userList.add(ad);
				}
			}
			sc.close();
			
		} catch(FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error - File not found.");
			
		}
		Admin.validAdmins = userList;
	}
	
	
	public void viewBooks(File stock) {
		// Order by quantity (6th element)
		List<String> bookEntries = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(stock))) {
            String line;
            while ((line = reader.readLine()) != null) {
                bookEntries.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Custom comparator based on the quantity (6th element)
        Comparator<String> quantityComparator = Comparator.comparingInt(line -> {
            String[] elements = line.split(",");
            if (elements.length >= 7) {
                return Integer.parseInt(elements[6].trim());
            } else {
                // Handle cases where the quantity is not present or not in the expected position
                return 0;
            }
        });

        Collections.sort(bookEntries, quantityComparator);
        
        // Column headings
        System.out.printf("%-10s %-10s %-25s %-10s %-18s %-15s %-8s %-8s %-8s %-8s%n",
                "Book ID", "Type", "Title", "Language", "Genre", "Date Published",
                "Quantity", "Price", "Pages", "Format");

        // Print book entries in a formatted table
        for (String entry : bookEntries) {
            String[] elements = entry.split(", ");

            System.out.printf("%-10s %-10s %-25s %-10s %-18s %-15s %-8s %-8s %-8s %-8s%n",
                    elements[0], elements[1], elements[2], elements[3], elements[4], elements[5],
                    elements[6], elements[7], elements[8], elements[9]);
        }
	}
	
	public void addBook(Scanner sc, File stock) {
		String[] bookDetails = new String[10];
		bookDetails = getBookDetails(bookDetails, sc);
		appendBookToFile(bookDetails, stock);
		Utils.loadBooks(stock);
	}
	
	public void appendBookToFile(String[] bookDetails, File stock) { //writes the book details entries at the end of file
		try {
			FileWriter fw = new FileWriter(stock, true);
		    BufferedWriter bw = new BufferedWriter(fw);
		    bw.write(bookDetails[0]+", "+bookDetails[1]+", "+bookDetails[2]+", "+bookDetails[3]+", "
		    +bookDetails[4]+", "+bookDetails[5]+", "+bookDetails[6]+", "+bookDetails[7]+", "+bookDetails[8]+", "+bookDetails[9]);
		    bw.newLine();
		    bw.close();
		} catch (IOException e) {
		    System.out.println("Error - File not found.");
		    e.printStackTrace();
		}
		
	}
	
	private String[] getBookDetails(String[] bookDetails, Scanner sc) {
		System.out.println("Enter Book Details");
		 //add book to list and reload books
		String[] typeOptions = {"paperback", "ebook", "audiobook"};
		String[] genreOptions = {"Politics", "Business", "Computer Science", "Biography"};
		String[] languageOptions = {"English", "French"};
		String[] ebookFormatOptions = {"EPUB", "MOBI", "PDF"};
		String[] audiobookFormatOptions = {"MP3", "WMA", "AAC"};
		String[] conditionOptions = {"new", "used"};
		
		System.out.print("Enter 8-digit Book ID: ");
		String uniqueBookId = sc.nextLine();
		bookDetails[0]= checkBookId(uniqueBookId, sc); // checks if book is 8 digit
		
		System.out.print("Type of book: Select an Option\n[1]Paperback\n[2]Ebook\n"
			 		+ "[3]Audiobook\nEnter: ");
		String option = sc.nextLine();
		int booktype = checkOption(option, 3, sc);
		bookDetails[1]= typeOptions[booktype-1];
		
		System.out.print("Title: ");
		bookDetails[2]=sc.nextLine();
		
		System.out.print("Language: Select an Option \n[1]English\n[2]French\nEnter: ");
		option = sc.nextLine();
		int lang = checkOption(option, 2, sc);
		bookDetails[3]=languageOptions[lang-1];
		
		System.out.print("Genre: Select an Option \n[1]Politice\n[2]Business\n"
				+ "[3]Computer Science\n[4]Biography\nEnter:  ");
		option = sc.nextLine();
		int genre = checkOption(option, 4, sc);
		bookDetails[4]=genreOptions[genre-1];
		
		System.out.print("Release Date(dd-mm-yyyy): ");
		bookDetails[5]=sc.nextLine();
		
		System.out.print("Quantity: ");
		bookDetails[6] = sc.nextLine();
		
		System.out.print("Retail Price(£): ");
		bookDetails[7] = sc.nextLine();
		
		if (bookDetails[1].equals("paperback")) {
			 //ask paperback details
			System.out.print("Number of Pages:");
			bookDetails[8] = sc.nextLine();
			System.out.print("Condition: Select an Option\n[1]New\n[2]Used\nEnter: ");
			option = sc.nextLine();
			int bookCondition = checkOption(option, 2, sc);
			bookDetails[9] = conditionOptions[bookCondition-1];
			
		 } else if (bookDetails[1].equals("ebook")) {
			 //ask ebook details
			 System.out.print("Number of Pages: ");
			 bookDetails[8] = sc.nextLine();
			 System.out.print("Format: Select an Option\n[1]EPUB\n[2]MOBI\n[3]PDF\nEnter: ");
			 int eFormat = checkOption(sc.nextLine(), 3, sc);
			 bookDetails[9] = ebookFormatOptions[eFormat-1];
			 
		 } else if (bookDetails[1].equals("audiobook")) {
			 //ask audiobook details
			 System.out.print("Duration(hrs): ");
			 bookDetails[8] = sc.nextLine();
			 System.out.print("Format: Select an Option\n[1]MP3\n[2]WMA\n[3]AAC\nEnter: ");
			 int aFormat = checkOption(sc.nextLine(), 3, sc);
			 bookDetails[9] = audiobookFormatOptions[aFormat-1];
		 }
		return bookDetails;
	}
	

}
