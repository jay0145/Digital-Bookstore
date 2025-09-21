package bookshop;
import java.util.*;
import java.io.*;
import java.text.*;

public class Main {
	
	public static void main(String[] args) {
		 File STOCK = new File("Stock.txt");
		 File USERACCOUNTS = new File("UserAccounts.txt");
		 
		 //loads books from file
		 Paperback.loadPbBooks(STOCK);
		 
		 Audiobook.loadAudiobooks(STOCK);
		 
		 Ebook.loadEbooks(STOCK);
		 
		 //loads users from file
		 
		 Customer.loadCustomers(USERACCOUNTS);
		 
		 Admin.loadAdmins(USERACCOUNTS);

		 //Login system
		 boolean exit = false;
		 System.out.println("Welcome to my bookshop!");
		 while (!exit) {
			 System.out.print("Enter username to login \nOR Q to quit: ");
			 Scanner scanner1 = new Scanner(System.in);
			 String input = scanner1.nextLine();
			 User currentUser = User.getUser(input, Customer.getValidCustomers(), Admin.getValidAdmins());
			 
			 if (Customer.getValidCustomers().contains(currentUser)) {
					 Customer currentCustomer = (Customer)currentUser;
					 currentCustomer.welcomeMessage();
					 String choice = "";
					 while(!choice.equals("0")) {
						 currentCustomer.displayMenu();
						 choice = scanner1.nextLine();
						 if (choice.equals("0")) {
							 System.out.println("\nLogged Out!");
							 break;
						 }
						 else if (choice.equals("1")) {
							 //view book stock by price asc
							 currentCustomer.viewBooks(STOCK);
						 }
						 else if (choice.equals("2")) {
							 //view basket
							 currentCustomer.viewBasket(scanner1);
							 
						 }
						 else if(choice.equals("3")) {
							 //view current balance
							 currentCustomer.displayBalance();
						 }
						 else if (choice.equals("4")) {
							 //add item to basket
							 System.out.print("Enter Item 8-digit Barcode: ");
							 String itemBarcode = scanner1.nextLine();
							 currentCustomer.addItemToBasket(itemBarcode, scanner1);
							 
						 }
						 else if (choice.equals("5")) {
							 //search - incomplete
							 currentCustomer.searchForBook("search");
						 }
						 else if (choice.equals("6")) {
							 //pay
							 currentCustomer.pay(STOCK, USERACCOUNTS);
						 }
					 }
					 
			 } else if (Admin.getValidAdmins().contains(currentUser)) {
				 Admin currentAdmin = (Admin)currentUser;
				 currentAdmin.welcomeMessage();
				 String choice ="";
				 while (!choice.equals("0")) {
					 currentAdmin.displayMenu();
					 choice = scanner1.nextLine();
					 if (choice.equals("1")) {
						 currentAdmin.addBook(scanner1, STOCK);
					 }
					 else if (choice.equals("2")) {
						 currentAdmin.viewBooks(STOCK);
					 }
					 else if (choice.equals("0")){
						 System.out.println("\nLogged out!\n");
						 break;
					 }
					 else {
						 System.out.println("Invalid input. Select one of the options");
					 }
				 }
			 } else if (input.toLowerCase().equals("q")) {
				 exit = true;
				 System.out.println("Thank you for using Bookshop!");
			 } else {
				 System.out.println("Username was not recognised. Try again");
			 }
			 
		 }
		 
	}
	
}
