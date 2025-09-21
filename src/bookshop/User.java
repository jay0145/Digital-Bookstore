package bookshop;

import java.util.List;
import java.util.Scanner;

abstract class User {
	private int userID;
	private String username;
	private String surname;
	private int houseNum;
	private String postcode;
	private String city;
	private String role;
	
	public User(int userid, String username, String surname, int houseNum, String postcode,
			String city, String role) {
		
		this.userID = userid;
		this.username = username;
		this.surname = surname;
		this.houseNum = houseNum;
		this.postcode = postcode;
		this.city = city;
		this.role = role;
	}
	
	public int getuserID() {
		return this.userID;
	}
	public String getUsername() {
		return this.username;
	}
	public String getSurname() {
		return this.surname;
	}
	public int getHouseNum() {
		return this.houseNum;
	}
	public String getPostcode() {
		return this.postcode;
	}
	public String getCity() {
		return this.city;
	}
	public String getRole() {
		return this.role;
	}
	
	public static User getUser(String inputUsername, List<Customer> validCustomers,
			List<Admin> validAdmins) {
		for (Customer c: validCustomers) {
			if (c.getUsername().equals(inputUsername)){
				return c;
			}
		}
		for (Admin a: validAdmins) {
			if (a.getUsername().equals(inputUsername)) {
				return a;
			}
		}
		return null;
		
	}
	
	public static boolean userExists(String inputUsername, List<User> validUsers) {
		for (User u: validUsers) {
			if (u.getUsername().equals(inputUsername)){
				return true;
			}
		}
		return false; //input did not match to a User
		
	}
	
	protected int checkOption(String option, int numOfOptions, Scanner sc) {
		boolean valid = false;
		int intOption = 0;
		while (!valid) { 
			try {
				intOption = Integer.parseInt(option);
				for(int i=1; i<=numOfOptions; i++) {
					if (intOption==i) {
						valid = true;
						return intOption;
					}
				}
				System.out.print("Please enter a number from the given options.\nEnter: ");
				option = sc.nextLine();
			} catch (NumberFormatException e) {
			System.out.print("Please enter a number from the given options.\nEnter: ");
			option = sc.nextLine();
			}
		}
		return 0;
	}
	
	protected String checkBookId(String bookId, Scanner scanner) {
		boolean valid = false;
		String newBookId = bookId ;
		while (!valid) {
			try {
				int intBookId = Integer.parseInt(newBookId);
				if (newBookId.length()==8) {
					valid = true;
					return newBookId;
				} else {
					System.out.print("Book ID must be an 8 digit number.\nEnter: ");
					newBookId = scanner.nextLine();
				}
				
			} catch (NumberFormatException e) {
				System.out.print("Book ID must be an 8 digit number.\nEnter: ");
				newBookId = scanner.nextLine();
			}
		}
		return newBookId;
	}
}
