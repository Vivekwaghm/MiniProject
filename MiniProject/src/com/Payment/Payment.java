package com.Payment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import com.User.UserClass;
import com.products.Bills;
import com.products.Cart;

public class Payment {
	Bills bill;
	public Cart cart;
	public int billPaid_f=0;
public Payment(Cart cart1, String name, String address, long number, int cust_id) throws IOException{
	cart = cart1;
	bill = new Bills(name , address ,number ,cust_id,cart.getpId(),cart.getpName(),cart.getqpur(),cart.getpPrc());
	billPaid_f=0;
}

public void paymentPage() throws IOException, ClassNotFoundException, SQLException {
	Scanner sc = new Scanner(System.in);
	System.out.println("WELCOME TO PAYMENT PAGE");
	int choice;
	do {
		System.out.println("*****************************************\n");
		System.out.println("1 - PAY BILL");
		System.out.println("2 - DISPLAY BILL");
		System.out.println("3 - EXIT");
		System.out.println("*****************************************\n");
		System.out.print("Enter choice : ");
		choice=sc.nextInt();
		
		if(choice==1) {
			bill.displayBill();
			System.out.println("Enter the amount to be paid");
			int amt=sc.nextInt();
			if(amt<bill.total_amount || amt>bill.total_amount) {
				System.out.println("Invalid amount entered");
				System.out.println("Enter the amount again");
				amt=sc.nextInt();
			}
			if(amt==bill.total_amount) {
			bill.addToDatabase();
			System.out.println("BILL PAID SUCCESSFULLY!!");
			billPaid_f=1;
			choice = 3;
			}else {
				System.out.println("Bill NOT PAID!!");
				paymentPage();
			}
		}
		if(choice ==2) {
			bill.displayBill();
		}
		if(choice ==3) {
			System.out.println("Thank you");
			System.out.println();
			UserClass uc = new UserClass();
			uc.CustomerPage();
		}
		else {
			System.out.println("Wrong Choice");
		}
	}while(choice!=3);
}

}
