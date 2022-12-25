package com.Connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCconnection {
 public  Connection getConnectionDetails() {
	 Connection con=null;
	 try {
		 //load the driver
		 Class.forName("com.mysql.jdbc.Driver");
		 
		 //establish connection
		 con = DriverManager.getConnection("jdbc:mysql://localhost:3306/miniproject", "root", "root");
	 }catch(Exception e) {
		 System.out.println(e);
	 }
	 return con;
 }
}
