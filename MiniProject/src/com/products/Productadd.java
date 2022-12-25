package com.products;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

import com.Connection.JDBCconnection;

public class Productadd {
	public void addProduct() {
		try {
			Scanner sc = new Scanner(System.in);
			System.out.println("How many products you want to add in list");
			int numOfProducts = sc.nextInt();

			for (int i = 1; i <= numOfProducts; i++) {
				
				System.out.println("Enter the Name of product");
				String name = sc.nextLine();
				sc.nextLine();
				System.out.println("Enter the Price of the product");
				int price = sc.nextInt();
				
				System.out.println("enter the Quantity of product available");
				int quantity = sc.nextInt();
				sc.nextLine();
				
				System.out.println("Write product description in 100 charecters");
				String Description = sc.nextLine();
				
				//System.out.println(Description);
				
				JDBCconnection jdbccon = new JDBCconnection();
				Connection con = jdbccon.getConnectionDetails();
				
					
				// prepare Statement
				PreparedStatement stmt = con
						.prepareStatement("insert into Products(Description,Price,Name,Quantity) values (?,?,?,?)");
				stmt.setString(1, Description);
				stmt.setInt(2, price);
				stmt.setString(3, name);
				stmt.setInt(4, quantity);

				int result = stmt.executeUpdate();

				System.out.println("Product Added to the list : " + result);
				con.close();
				stmt.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
