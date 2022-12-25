package com.products;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.Connection.JDBCconnection;
import com.User.UserClass;

public class Bills {

	private int bill_id;
	private String cust_name;
	private String bill_addr;
	private long cust_phone;
	private int customerId;
	//private float total_amount;
	public int total_amount;
	
	private ArrayList<Integer> pid=new ArrayList<Integer>();
	private ArrayList<String> pname=new ArrayList<String>();
	private ArrayList<Integer> qty=new ArrayList<Integer>();
	private ArrayList<Integer> price=new ArrayList<Integer>();
	
	
	public Bills(String cust_name, String bill_addr, long cust_phone,int cust_id,
			ArrayList<Integer> pid, ArrayList<String> pname, ArrayList<Integer> qty, ArrayList<Integer> price) throws IOException {
		this.customerId=cust_id;
		this.cust_name = cust_name;
		this.bill_addr = bill_addr;
		this.cust_phone = cust_phone;
		this.total_amount = 0;
		this.pid = pid;
		this.pname = pname;
		this.qty = price;
		this.price = qty;
	}
	
	public int setBillId() {
		int x=0;
		try {
			JDBCconnection jdbccon = new JDBCconnection();
			Connection con = jdbccon.getConnectionDetails();
			
			PreparedStatement ps = con.prepareStatement("select Bill_Id from bill");
			ResultSet rs =ps.executeQuery();
			
			while(rs.next()) {
				x= rs.getInt(1);
			}
			
			ps.close();
			con.close();
		}catch(Exception e) {
			System.out.println(e);
		}
		return x+1;
	}
	
	private void generateBill()throws IOException {
		bill_id=setBillId();
		int sum=0;
		for(int i=0;i<pid.size();i++) {
			sum=sum+price.get(i);
		}
		total_amount=sum;
		
	/*	try {
			JDBCconnection jdbccon = new JDBCconnection();
			Connection con =jdbccon.getConnectionDetails();
			PreparedStatement stmt = con.prepareStatement("insert into bill(CustomerName,CustMobile,BillAdress,TotalAmount)values(?,?,?,?)");
			stmt.setString(1, cust_name);
			stmt.setLong(2, cust_phone);
			stmt.setString(3, bill_addr);
			stmt.setInt(4, total_amount);
			
			x=stmt.executeUpdate();
			System.out.println("BILL ADDED TO DATABASE");
		}catch(Exception e) {
			System.out.println(e);
		}*/
	}
	
	public void displayBill()throws IOException, ClassNotFoundException, SQLException
	{
		generateBill();
		if(total_amount==0) {
			System.out.println("Your cart is empty");
			UserClass uc  = new UserClass();
			uc.CustomerPage();
		}
		else {
		int x;
		System.out.println("YOUR BILL IS :\n");
		System.out.println("************************************************************************************************\n");
		System.out.println("BILL ID                  =  "+bill_id);
		System.out.println("CUSTOMER NAME            =  "+cust_name);
		System.out.println("CUSTOMER CONTACT NUMBER  =  "+ cust_phone);
		System.out.println("CUSTOMER ADDRESS         =  "+ bill_addr);
		System.out.println("************************************************************************************************\n");
		System.out.printf("%-20s \t %-20s \t %-20s \t %-20s\n", "PRODUCT_ID","PRODUCT_NAME","QUANTITY PURCHASED","TOTAL_PRICE");
		for(x=0;x<pid.size();x++)
		{
			System.out.printf("%-20d \t %-20s  \t %-20d \t %-20d\n", pid.get(x),pname.get(x) ,qty.get(x),price.get(x));
		}
		System.out.println("************************************************************************************************\n");
		System.out.printf("TOTAL AMOUNT PAYABLE = Rs. "+total_amount+"\n");
		System.out.println("************************************************************************************************\n");
		}
	}
	public void addToDatabase()throws IOException
	{
		int x=0;
		try
		{
			JDBCconnection jdbccon = new JDBCconnection();
			Connection con =jdbccon.getConnectionDetails();
			
			for(int i=0;i<pid.size();i++) {
			PreparedStatement ps=con.prepareStatement("insert into bill(Bill_Id,CustomerName,CustMobile,BillAddress,TotalAmountPaid,ProductName,PurchasedQty,UserData_id)values(?,?,?,?,?,?,?,?)");
			ps.setInt(1, bill_id++);
			ps.setString(2, cust_name);
			ps.setLong(3, cust_phone);
			ps.setString(4, bill_addr);
			ps.setInt(5, total_amount);
			ps.setString(6, pname.get(i));
			ps.setInt(7, qty.get(i));
			ps.setInt(8, customerId);
			
			x=ps.executeUpdate();
			}
		   
			
			if(x==0)
				System.out.println("BILL NOT ADDED TO DATABASE !");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
