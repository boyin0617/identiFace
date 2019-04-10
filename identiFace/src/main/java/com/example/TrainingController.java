package com.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.example.entity.Customer;
import com.example.CreateTxtPath;
 
@Controller
public class TrainingController {
	
	//上傳檔案
	static String folderPath = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN";
    public static final String uploadingDir =  folderPath + "/uploadingDir/";

//    //資料庫參數
	private Connection con;
	private Statement st;
	private ResultSet rs;
	
    Logger log = LoggerFactory.getLogger(this.getClass());
     
    @RequestMapping(value="/train", method=RequestMethod.GET)
    public String CustomerForm(Model model) {
        model.addAttribute("Customer", new Customer());   
        return "train";
    }
 
    @RequestMapping(value="/train", method=RequestMethod.POST)
    public String CustomerSubmit(@ModelAttribute Customer Customer, Model model) throws IOException, SQLException, ClassNotFoundException {
         
        model.addAttribute("Customer", Customer);
        String face =Customer.getName(); // name = face id
       
        //連接資料庫
        Class.forName("com.mysql.cj.jdbc.Driver");
		System.out.println("加載資料庫驅動");
		String url = "jdbc:mysql://localhost:3306/project?serverTimezone=UTC";// 聲明資料庫project的url
		String user = "root";// 資料庫帳號
		String pass = "";// 資料庫密碼
		// 建立資料庫連結，獲得連結對象conn
		con = DriverManager.getConnection(url, user, pass);
		st = con.createStatement();
		
		//資料庫抓值SELECT * FROM `Users` ORDER BY UserID DESC LIMIT 1
		String query = "select * from face order by  faceId desc limit 1 ";
		rs = st.executeQuery(query);
		System.out.println("Records from DB");
		System.out.println("faceId " +" imageId " + " name " + "age "   + "gender");
		while(rs.next()) {
			String faceid = rs.getString("faceId");
			String imageid = rs.getString("imageId");
			String name = rs.getString("name");
			String age = rs.getString("age");
			String gender = rs.getString("gender");

			
			System.out.println(faceid+"         "  + imageid+"       " +   name+" "  + age +"  "+ gender);
			
			
			int faceId = Integer.parseInt(faceid) +1;
			
			
			System.out.println("last value" +faceId);
            model.addAttribute("value", name);
//		DBconnect connect = new DBconnect();
//		connect.getData(face);
        
        System.out.println("id: " + face );
       
        
        return "result_train";
        }
		return query;
    }
    
    
    
    
    
    
    
}