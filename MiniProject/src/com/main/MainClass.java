package com.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import com.Admin.AdminClass;
import com.User.UserClass;
import com.products.Productadd;

public class MainClass {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		try {

			AdminClass ac = new AdminClass();
			Productadd productadd = new Productadd();
			UserClass userclass = new UserClass();
			// productadd.addProduct();

			Scanner sc = new Scanner(System.in);
			System.out.println("Press 1. for Admin login");
			System.out.println("Press 2. to register as a user");
			System.out.println("Press 3. to login as a user");
			System.out.println("press 0: to exit");
			int choice = sc.nextInt();
			switch (choice) {
			case 1:
				ac.adminLogin();
				break;
			case 2:

				userclass.registerUser();
				break;

			case 3:
				userclass.loginUser();
				break;
			case 0:
				System.out.println("Thank you for Visiting !!!");
				break;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
