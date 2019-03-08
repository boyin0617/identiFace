//package com.egroupai.engine.control;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//
//public class test {
//
//	public static void main(String[] args) throws SQLException, ClassNotFoundException {
//		// TODO Auto-generated method stub
//		Class.forName("com.mysql.cj.jdbc.Driver");
//		String name = "";
//		System.out.println("加載資料庫驅動");
//		String url="jdbc:mysql://localhost:3306/project?autoReconnect=true&useSSL=false&serverTimezone=UTC";//聲明資料庫project的url  
//		String user="root";//資料庫帳號
//		String pass="a8s5d1f9";//資料庫密碼
//		//建立資料庫連結，獲得連結對象conn  
//		Connection connect = DriverManager.getConnection(url,user,pass);
//		System.out.println("資料庫連接成功");
//		Statement stmt = connect.createStatement();
//		ResultSet rs = stmt.executeQuery("SELECT * FROM `recognise`");
//		int count;
//		if(rs.last()) {
//			count = rs.getRow();
//		} else {
//			count = 0;
//		}
//		
//		System.out.println(count);
//	}
//}
