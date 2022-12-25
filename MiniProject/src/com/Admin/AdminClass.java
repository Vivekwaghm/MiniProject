package com.Admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import com.Connection.JDBCconnection;
import com.main.MainClass;
import com.products.Productadd;

public class AdminClass extends Productadd {
	Scanner sc  = new Scanner(System.in);
	
	
	
	public void adminLogin() {
		try {
			System.out.println("Enter Admin's Username");
			String username = sc.next();
			System.out.println("Enter Admin's Password");
			String password = sc.next();
			int choice;
			JDBCconnection jdbccon = new JDBCconnection();
			Connection con = jdbccon.getConnectionDetails();
			
			PreparedStatement stmt = con.prepareStatement("select * from admindata");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				
				if(username.equals(rs.getString(2)) && password.equals(rs.getString(3))) {
					System.out.println("~~~~~~~~~~~~~~ You are logged in as a Admin ~~~~~~~~~~~~~");
					System.out.println();
					do {
						System.out.println();
						System.out.println("****************************************************************************");
					System.out.println("Press 1. to see the list of details of all registered users");
					System.out.println("press 2. to check the quantity available for a product from its product id");
					System.out.println("Press 3. to check the history of perticular user");
					System.out.println("Press 4. to Add products in list");
					System.out.println("Press 5. to Exit");
					System.out.println();
					
					System.out.println("Enter Choice : ");
					choice = sc.nextInt();
					
					
					if(choice==1) {
						this.seeListOfUsers();}
						
					else if(choice ==2 ) {
						this.checkQuantity();}
					
					else if(choice==3) {
						this.getCustomerHistory();
					}
					else if(choice ==4) {
						this.addProduct();
					}
					else if(choice ==5) {
						System.out.println("Thank you for Visiting!!");
						System.out.println();
						MainClass m=new MainClass();
						m.main(null);
					}
					
				} while(choice!=5);
				}
				else {
					System.out.println("!!!!! you are not admin !!!!!");
				}
			}
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void checkQuantity() {
		try {
			System.out.println("Enter product id to check quantity");
			int Productid = sc.nextInt();
			
			JDBCconnection jdbccon = new JDBCconnection();
			Connection con = jdbccon.getConnectionDetails();
			
			PreparedStatement stmt= con.prepareStatement("select * from products where Product_id=?");
			stmt.setInt(1, Productid);
			
			ResultSet rs=stmt.executeQuery();
			while(rs.next()) {
			System.out.println("Quantity Available for "+rs.getString(4)+ " is : "+rs.getInt(5));
			}
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void seeListOfUsers() {
		try {
			JDBCconnection jdbccon = new JDBCconnection();
			Connection con = jdbccon.getConnectionDetails();
			
			PreparedStatement stmt = con.prepareStatement("select * from userdata");
			ResultSet rs=stmt.executeQuery();
			
			while(rs.next()) {
				System.out.println("User ID : "+rs.getInt(1)+" |  FirstName : "+rs.getString(2)+"  |  LastName : "+rs.getString(3)+"  |  EmailID : "+rs.getString(4)+"  |  Mobile Number : "+rs.getLong(5)+"  |  UserName : "+rs.getString(6)+"  |  Password : "+rs.getString(7));
			}
			
			con.close();
			stmt.close();
		}catch(Exception e ) {
			System.out.println(e);
		}
	}

	@Override
	public void addProduct() {
		
		super.addProduct();
	}

	public void getCustomerHistory() {
		try {
			System.out.println("Enter the customer id to see his/her purchased history");
			int id = sc.nextInt();
			
			JDBCconnection jdbccon = new JDBCconnection();
			Connection con = jdbccon.getConnectionDetails();
			
			PreparedStatement ps = con.prepareStatement("select * from bill where UserData_id=?");
			ps.setInt(1, id);
			//ps.setInt(2, id);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				
				
				System.out.println("*************************************************************************");
				System.out.println();
				System.out.println("Customer Name     :  "+rs.getString(2));
				System.out.println("Customer mobNo    :  "+rs.getLong(3));
				System.out.println("Customer address  :  "+rs.getString(4));
				System.out.println("Product purchased :  "+rs.getString(6));
				System.out.println("Quantity Purchased:  "+rs.getInt(7));
				System.out.println("Total Amount Paid :  "+rs.getInt(5));
				System.out.println();
				System.out.println("***************************************************************************");
				
			}
		}catch(Exception e) {
			
		}
	}
}
