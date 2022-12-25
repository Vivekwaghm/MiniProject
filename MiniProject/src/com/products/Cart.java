package com.products;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.Connection.JDBCconnection;

public class Cart {
	private ArrayList<Integer> pId = new ArrayList();
	private ArrayList<String> pName=new ArrayList();
	private ArrayList<Integer> pPrc=new ArrayList();
	private ArrayList<Integer> qpur=new ArrayList<Integer>();
	
	public ArrayList<Integer> getpId() {
		return pId;
	}
	public ArrayList<String> getpName() {
		return pName;
	}
	public ArrayList<Integer> getpPrc() {
		return pPrc;
	}
	public ArrayList<Integer> getqpur() {
		return qpur;
	}
	
	public void addToCart(int p_id, String p_name,String p_descr, int p_price, int q_pur) {
		pId.add(p_id);
		pName.add(p_name);
		pPrc.add(p_price);
		qpur.add(q_pur);
	}
	
	public void viewCart() {
		int cartSize = pId.size();
		if(cartSize!=0) {
			System.out.println("Your Cart is");
			for(int i=0;i<cartSize;i++) {
				System.out.println("Product ID : "+pId.get(i)+"    Product Name : "+pName.get(i)+"    Purchase Quantity : "+pPrc.get(i)+"    Product Price : "+qpur.get(i));
			}
		}else {
			System.out.println("Cart is emplty");
		}
	}
	
	public void removeFromCart(int p_id)throws IOException {
		int res;
		int x = -1;
		int prevqty=0;
		int newqty=0;
		res=pId.indexOf(p_id);
		
		if(res == -1) {
			System.out.println("YOU HAVE NOT PURCHASED THIS PRODUCT");
		}else{
			pId.remove(res);
			pName.remove(res);
			qpur.remove(res);
			
			try {
				JDBCconnection jdbccon = new JDBCconnection();
				Connection con = jdbccon.getConnectionDetails();
				
				PreparedStatement stmt = con.prepareStatement("select Quantity from products where Product_id=?");
				stmt.setInt(1, p_id);
				
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					prevqty = rs.getInt(1);
				}
				newqty = prevqty + pPrc.get(res);
				pPrc.remove(res);
				PreparedStatement ps = con.prepareStatement("update products set Quantity=? where Product_id=? ");
				ps.setInt(1, newqty);
				ps.setInt(2, p_id);
				
				x=ps.executeUpdate();
			}catch(Exception e) {
				System.out.println(e);
			}
			if(x!=0) {
				System.out.println("CART UPDATED");
			}
		}
	}
	
	public void cancelCart()throws IOException
	{
		try
		{
			int prevq=0;
			int newq=0;
			JDBCconnection jdbccon = new JDBCconnection();
			Connection con = jdbccon.getConnectionDetails();
			PreparedStatement ps=con.prepareStatement("update products set Quantity=? where product_id=?");
			int x;
			int y;
			for(x=0;x<pId.size();x++)
			{
				PreparedStatement ps1=con.prepareStatement("select Quantity from products where product_id=?");
				ps1.setInt(1,pId.get(x));
				ResultSet rs=ps1.executeQuery();
				while(rs.next())
				{
					prevq=rs.getInt(1);
				}
				newq=prevq+pPrc.get(x);
				ps.setInt(1, newq);
				ps.setInt(2,pId.get(x));
				y=ps.executeUpdate();
				if(y==0)
					System.out.println("PRODUCT NOT UPDATED BACK TO PRODUCTS TABLE !");
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
