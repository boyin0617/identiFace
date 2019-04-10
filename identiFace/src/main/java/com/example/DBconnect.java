package com.example;

import java.sql.*;

import org.springframework.web.servlet.ModelAndView;

import com.example.*;
import com.example.entity.*;
public class DBconnect {

	private Connection con;
	private Statement st;
	private ResultSet rs;
	
	
	public DBconnect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			System.out.println("加載資料庫驅動");
			String url = "jdbc:mysql://localhost:3306/project?serverTimezone=UTC";// 聲明資料庫project的url
			String user = "root";// 資料庫帳號
			String pass = "";// 資料庫密碼
			// 建立資料庫連結，獲得連結對象conn
			con = DriverManager.getConnection(url, user, pass);
			st = con.createStatement();
			
			
		}catch(Exception ex) {
			System.out.println("Error: " + ex);
		}
		
	}
	public void getData(String  id) {
		try {
			String query = "select * from face where faceId Like '"+ id + "' ";
			rs = st.executeQuery(query);
			System.out.println("Records from DB");
			System.out.println("faceId " +" imageId " + " name " + "age " + "gender");
			while(rs.next()) {
				String faceid = rs.getString("faceId");
				String imageid = rs.getString("imageId");
				String name = rs.getString("name");
				String age = rs.getString("age");
				String gender = rs.getString("gender");
	
				
				System.out.println(faceid+"         "  + imageid+"       " +   name+" "  + age +"  "+ gender);
			}
			
		}
		catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
//    public static void main(String args[]) throws Exception {
//		DBconnect connect = new DBconnect();
//		connect.getData(null);
//    }
//	
}
