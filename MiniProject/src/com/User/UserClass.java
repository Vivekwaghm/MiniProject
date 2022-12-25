package com.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import com.Connection.*;
import com.Payment.Payment;
import com.main.MainClass;
import com.products.Cart;

public class UserClass {
	int StudentId ;
	String firstname;
	String lastname;
	
	public int customerID;
	private String customerPass;
	//private Cart customerCart=new Cart();
	private int cartFlag=0;
	private int billPaidFlag=0;
	private int checkFlag=-1;
	
	private ArrayList<Integer> pid=new ArrayList<Integer>();
	private ArrayList<String> name=new ArrayList<String>();
	private ArrayList<String> descr=new ArrayList<String>();
	private ArrayList<Integer> qty=new ArrayList<Integer>();
	private ArrayList<Integer> price=new ArrayList<Integer>();
	
	private int products_Check;
	
	public UserClass(){
		
	}

//	public UserClass(int id, String pass){
//		customerID=id;
//		customerPass=pass;
//	}
	
	Cart cart = new Cart();
	Scanner sc = new Scanner(System.in);
	

	public void registerUser() {
		try {
			
			System.out.println("Enter your first name");
			String fname = sc.next();
		
			System.out.println("Enter your last name");
			String lname=sc.next();
			
			System.out.println("Enter your Email id");
			String email = sc.next();
			
			System.out.println("Enter your Mobile number");
			long mobno = sc.nextLong();
			sc.nextLine();
			
			System.out.println("Enter your Adress");
			String address = sc.nextLine();
			sc.nextLine();
			
			System.out.println("Enter Username");
			String username = sc.next();
			
			System.out.println("Enter your password");
			String password=sc.next();
			
			JDBCconnection jdbccon = new JDBCconnection();
			Connection con = jdbccon.getConnectionDetails();
			
			//prepare statement
			PreparedStatement stmt = con.prepareStatement("insert into UserData( FirstName, LastName, UserEmail, UserMobileNo, Adress, Username, Password) values (?,?,?,?,?,?,?)");
		
			stmt.setString(1, fname);
			stmt.setString(2, lname);
			stmt.setString(3, email);
			stmt.setLong(4, mobno);
			stmt.setString(5, address);
			stmt.setString(6, username);
			stmt.setString(7, password);
			
			stmt.executeUpdate();
			System.out.println("Registration Completed!!!");
			
			
			con.close();
			stmt.close();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void loginUser() throws ClassNotFoundException, SQLException, IOException {
		System.out.println("Enter Username to login");
		String username = sc.next(); //vivek
		System.out.println("Enter Password to login");
		String password = sc.next(); //vivek123
		
		boolean flag = false;
		String name = null;
		int id=0;
		String pass=null;
		
		//JDBC drivers
		JDBCconnection jdbccon = new JDBCconnection();
		Connection con = jdbccon.getConnectionDetails();
		
		PreparedStatement stmt = con.prepareStatement("select * from userdata");
		ResultSet rs= stmt.executeQuery();
		
		while(rs.next()) {
			if(username.equals(rs.getString(7)) && password.equals(rs.getString(8))){
				name = rs.getString(2);
				id = rs.getInt(1);
				pass = rs. getString(8);
				
				flag = true;	
			}	
		}
		if(flag == true) {
			System.out.println("~~~~~~~~~~~~~~~~ Welcome "+name+ "~~~~~~~~~~~~~~~");
			//UserClass uc = new UserClass(id,pass);
			this.customerID=id;
			this.CustomerPage();
				}
			else {
				System.out.println(" Invalid Username and Password !!!!!! ");
			}
		}
		
	public void CustomerPage()throws IOException, ClassNotFoundException, SQLException
	{
		Scanner sc = new Scanner(System.in);
		//BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		products_Check=this.initializeProducts(); //this statement always runs
		//System.out.println("Products check = "+products_Check);
		System.out.println("WELCOME TO CUSTOMER SECTION\n");
		int ch;
		do
		{
			System.out.println("*****************************************************\n");
			System.out.println("1 - VIEW PRODUCTS LIST");//completed
			System.out.println("2 - SEARCH A PRODUCT NAMEWISE");//completed
			System.out.println("3 - ADD PRODUCT TO CART");//completed
			System.out.println("4 - REMOVE PRODUCT FROM CART");//completed
			System.out.println("5 - VIEW CART");//completed
			System.out.println("6 - PROCEED TO PAYMENT");
			System.out.println("7 - LOGOUT FROM SYSTEM");//completed
			System.out.println("*****************************************************\n");
			
			System.out.print("Enter choice : ");
			ch=sc.nextInt();
			if(ch==1)
				this.displayProductList();
			else if(ch==2)
				this.searchNameWise();
			else if(ch==3)
			{
				this.addProductsToCart();
			}
			else if(ch==4)
			{
				int rem;
				System.out.print("ENTER PRODUCT ID TO REMOVE FROM CART = ");
				rem=sc.nextInt();
				cart.removeFromCart(rem);
				this.updateArrayList();
			}
			else if(ch==5)
				cart.viewCart();
			else if(ch==6)
			{
				this.proceedPayment(cart);
			}
			else if(ch==7)
			{
				ch=this.checkExit();
				//System.out.println("After checking exit ch = "+ch);
				if(ch==0)
					ch=7;
				else {
				MainClass m=new MainClass();
				m.main(null);
				}
			}
			else
				System.out.println("Wrong choice");
		}while(ch!=7);
	}

	private void addProductsToCart()throws IOException
	{
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		String chc;
		do
		{
			int p_id;
			String p_name,p_descr;
			int p_price;
			int q_pur;
			
			int q_avail;
			System.out.print("ENTER PRODUCT ID TO ADD TO CART = ");
			p_id=Integer.parseInt(br.readLine());
			q_avail=searchProd(p_id);
			if(q_avail==-1)
				System.out.println("PRODUCT NOT FOUND !");
			else
			{
				System.out.println("QUNATITY AVAILABLE = "+q_avail);
				System.out.println("ENTER QUANTITY TO PURCHASE = ");
				q_pur=Integer.parseInt(br.readLine());
				if(q_pur>q_avail)
					System.out.println("STOCK NOT AVAILABLE");
				else
				{
						updateQty(q_pur,p_id);
					
					//adding product to cart code begins below
					p_name=name.get(pid.indexOf(p_id));
					p_descr=descr.get(pid.indexOf(p_id));
					p_price=q_pur*(price.get(pid.indexOf(p_id)));
					
					cart.addToCart(p_id, p_name, p_descr, q_pur, p_price);
					cartFlag=1;
				}
					
			}
			System.out.print("DO YOU WANT TO CONTINUE PRESS ( Y for yes, N for no)");
			chc=br.readLine();
		}while(chc.equalsIgnoreCase("Y"));
	}

	private int searchProd(int x)throws IOException //for searching product and retrieving the available quantity
	{
		int res=pid.indexOf(x);
		if(res!=-1)
		{
			return qty.get(res);
		}
		else
		{
			return -1;
		}
	}
	
	private void searchNameWise()throws IOException
	{
		if(products_Check==0)
			System.out.println("PRODUCTS NOT AVAILABLE !");
		else
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			String sr;
			int res;
			String chc;
			do
			{
				System.out.print("ENTER PRODUCT NAME TO SEARCH : ");
				sr=br.readLine();
				res=name.indexOf(sr);
				if(res==-1)
					System.out.println("PRODUCT NOT FOUND !");
				else
				{
					System.out.println("PRODUCT DETAILS ARE :\n");
					System.out.printf("PRODUCT ID         = %-5d\n",pid.get(res));
					System.out.printf("PRODUCT NAME       = %-20s\n",name.get(res));
					System.out.printf("PRODUCT TYPE       = %-20s\n", descr.get(res));
					if(qty.get(res)!=0)
						System.out.printf("QUANTITY AVAILABLE = %-5d\n", qty.get(res));
					else
						System.out.printf("QUANTITY AVAILABLE = %-5s\n", "NOT IN STOCK");
					System.out.printf("PRODUCT PRICE      = %-10d\n",price.get(res));
				}
				System.out.print("PRESS Y to continue , N for exit : ");
				chc=br.readLine();
			
			}while(chc.equalsIgnoreCase("Y"));
			
		}
	}

	private void proceedPayment(Cart cart1)throws IOException, ClassNotFoundException, SQLException
	{
		if(cartFlag==1)
		{
			String c_name="";
			String b_add="";
			long c_phn=0;
			try
			{
				JDBCconnection jdbccon = new JDBCconnection();
				Connection con = jdbccon.getConnectionDetails();
				PreparedStatement ps=con.prepareStatement("select * from userdata where UserData_id=?");
				ps.setInt(1, customerID);
				ResultSet rs=ps.executeQuery();
				while(rs.next())
				{
					c_name=rs.getString(2)+" "+rs.getString(3);
					b_add=rs.getString(6);
					c_phn=rs.getLong(5);
				}
				
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			
			Payment p=new Payment(cart1,c_name,b_add,c_phn,customerID);
			p.paymentPage();
			if(p.billPaid_f==1)
				billPaidFlag=1;
			if(billPaidFlag==1)
			{
				cart=new Cart();
				cartFlag=0;
				billPaidFlag=0;
				checkFlag=-2;
			}
		}
		else
			System.out.println("CART IS EMPTY! ");
	}
	
	public void displayProductList() {
		if(products_Check==0)
			System.out.println("PRODUCTS NOT AVAILABLE !");
		else
		{
			int x;
			x=pid.size();
			int i;
			System.out.println("***********************************************************************************************************************\n");
			System.out.printf("%-20s \t %-20s \t %-20s \t %-20s \t %-20s\n", "Product_ID","Product_Name","Product_Quantity","Product_Price","Product_Description");
			System.out.println("***********************************************************************************************************************\n");
			for(i=0;i<x;i++)
			{
				if(qty.get(i)!=0)
					System.out.printf("%-20d \t %-20s \t %-20s \t %-20d \t %-20s\n",pid.get(i),name.get(i),qty.get(i),price.get(i),descr.get(i) );
				else
					System.out.printf("%-20d \t %-20s \t %-20s \t %-20s \t %-20s\n",pid.get(i),name.get(i),"NOT IN STOCK",price.get(i),descr.get(i) );
			}
			System.out.println("***********************************************************************************************************************\n");
		}
		}
	
	private int initializeProducts()throws IOException //return 1 if products are available 0 if not avaialable
	{
		int x=0;
		try
		{
			JDBCconnection jdbccon = new JDBCconnection();
			Connection con = jdbccon.getConnectionDetails();
			PreparedStatement ps=con.prepareStatement("select * from products");
			ResultSet rs=ps.executeQuery();
			//*******
			while(rs.next())
			{
				x++;
				pid.add(rs.getInt(1));
				name.add(rs.getString(4));
				descr.add(rs.getString(2));
				qty.add(rs.getInt(5));
				price.add(rs.getInt(3));
			}
			
			
			
			//for counting the number of rows in result set
		/*	if(rs.last()) {
				x=rs.getRow();
				System.out.println("Row get="+x);
				rs.beforeFirst();
			}
			//*******
			if(x==0)
				return 0;
			else
			{
				while(rs.next())
				{
					pid.add(rs.getInt(1));
					name.add(rs.getString(4));
					descr.add(rs.getString(2));
					qty.add(rs.getInt(5));
					price.add(rs.getInt(3));
				}
			}*/
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return x;
	}

	private void updateQty(int sub,int x)throws IOException //x =productID sub = quantity to be subtracted
	{
		try
		{
			int res=pid.indexOf(x);
			int min=qty.get(res);
			if(min-sub>0)
			{
				qty.set(res, min-sub);
			}
			else
				qty.set(res,0);
			JDBCconnection jdbccon = new JDBCconnection();
			Connection con = jdbccon.getConnectionDetails();
			PreparedStatement ps=con.prepareStatement("update products set Quantity=? where product_id=?");
			ps.setInt(1,qty.get(res));
			ps.setInt(2, x);
			int m=ps.executeUpdate();
			if(m==0)
				System.out.println("PRODUCT UPDATION FAILED !");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	private void updateArrayList()throws IOException
	{
		pid.clear();
		name.clear();
		descr.clear();
		qty.clear();
		price.clear();
		initializeProducts();
	}
	
	private int checkExit()throws IOException, ClassNotFoundException, SQLException
	{
		if(cartFlag==1)
		{
			//BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			Scanner sc = new Scanner(System.in);
			String chc;
			System.out.println("YOU HAVE A PENDING CART !");
			System.out.print("DO YOU WANT TO MAKE PAYMENT ( PRESS Y ) ELSE CANCEL THE CART ( PRESS N) : ");
			chc=sc.next();
			if(chc.equalsIgnoreCase("Y"))
			{
				proceedPayment(cart);
				if(billPaidFlag!=1 && checkFlag==-1)
					return -1;
				else
					return 0;
			}
			else
			{
				cart.cancelCart();
				cart=new Cart();
				cartFlag=0;
				billPaidFlag=0;
			}
		}
		System.out.println("THANK YOU !");
		System.out.println();
		return 7;//to exit the customerPage
			
	}
}
	

