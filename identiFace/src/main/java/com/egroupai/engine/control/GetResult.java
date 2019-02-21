package com.egroupai.engine.control;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.egroupai.engine.entity.Face;
import com.egroupai.engine.util.CopyUtil;
import com.example.entity.Member;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import com.google.gson.JsonParser;

import java.sql.*;
/** 
* @author 雿�� Daniel
* @date 2018撟�8���16� 銝��8:35:11 
* @version 
* @description:
*/
public class GetResult {
	static protected String ENGINEPATH = "C:\\eGroupAI_FaceEngine_v3.1.0";
	
	public static List<Member> main() throws SQLException{
		
		Member member = new Member();
		List<Member> memberlist = new ArrayList<>();
		
		List<Face> faceList = new ArrayList<>();
		long faceId = 0;
		long memberId = 0;
		String phonenumber = "";
		String email = "";
		String name = "";
		Date birth = new Date();
		Date date = new Date();
	    SimpleDateFormat todaydate = new SimpleDateFormat("yyyy-MM-dd");
	    String today = todaydate.format(date);
		// Get All Retrieve Data
		Integer startIndex = 0;
		String jsonName = "output."+today+".egroup";	// Get All Retrieve Data
		ArrayList<String> foundlist = new ArrayList<>();
		
		while(true) {
			long startTime = System.currentTimeMillis();
			faceList = getAllResult(ENGINEPATH,jsonName ,startIndex);
			if(faceList.size()>0){
				startIndex = faceList.get(faceList.size()-1).getEndIndex();
			}
			String jsoncontext = new Gson().toJson(faceList);
			System.out.println("Get Json Using Time:" + (System.currentTimeMillis() - startTime) + " ms,startIndex="+startIndex+",faceList="+jsoncontext);
			// If your fps is 10, means recognize 10 frame per seconds, 1000 ms /10 frame = 100 ms
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(int i = 0; i <= faceList.size()-1;i++) {
			String testjson = new Gson().toJson(faceList.get(i));
			JsonParser jp = new JsonParser();
			JsonObject jo = jp.parse(testjson).getAsJsonObject();
			int found = jo.get("hasFound").getAsInt();
			if(found == 1) {
				name = jo.get("personId").getAsString();
				if(!foundlist.contains(name)) {
					foundlist.add(name);
//					if(foundlist.size() > 1) {
//						 member.setMemberId((long) (-1));
//						 System.out.println("辨識到"+foundlist.size()+"人，請再試一次");
//						 System.out.println("memberId為"+member.getMemberId());
//						 return member;
//					}
				}
				System.out.println("辨識成功 ! 辨識到: "+name);
			}else if(found == 0) {
				System.out.println("辨識失敗，請再試一次");
			}
			}
			break;
		}
		if(foundlist.size() == 0) {
			member.setFaceId((long) 0);
			System.out.println("沒有辨識到，請再試一次");
			return memberlist;
		}
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("加載資料庫驅動");
			String url="jdbc:mysql://localhost:3306/project?autoReconnect=true&useSSL=false&serverTimezone=UTC";//聲明資料庫project的url  
			String user="root";//資料庫帳號
			String pass="a8s5d1f9";//資料庫密碼
			//建立資料庫連結，獲得連結對象conn  
			Connection connect=DriverManager.getConnection(url,user,pass);
			System.out.println("資料庫連接成功");
			Statement stmt = connect.createStatement();
			for(int i = 0;i < foundlist.size();i++) {
			ResultSet rs = stmt.executeQuery("SELECT * FROM `face` WHERE `name` LIKE '"+foundlist.get(i)+"'");
				while (rs.next()) {
					faceId = rs.getInt("faceId");
				}
			ResultSet rs2 = stmt.executeQuery("SELECT * FROM `member` WHERE `faceId` LIKE '"+faceId+"'");
			while (rs2.next()) {
					
					memberId = rs2.getLong("memberId");
					phonenumber = rs2.getString("phone");
					name = rs2.getString("name");
					email = rs2.getString("email");
					birth = rs2.getDate("birth");
				}
			System.out.println("會員名字 : "+foundlist.get(i)+", 資料庫名字為"+name+", faceid為 :  "+faceId+", 電話為 : " + phonenumber + ", email為 : " + email + ", birth為:" + birth);
			member = new Member();
			//存到member
			member.setMemberId((long) memberId);
			member.setFaceId((long) faceId);
			member.setName(name);
			member.setEmail(email);
			member.setPhone(phonenumber);
			member.setBirth(birth);
			System.out.println("資料庫名字為"+member.getName()+", faceid為 :  "+member.getFaceId()+", 電話為 : " + member.getPhone() + ", email為 : " + member.getEmail() + ", birth為:" + member.getBirth());
			memberlist.add(member);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.print("get data error!");
			e.printStackTrace();
		}
		for(int i = 0;i < memberlist.size();i++) {
		System.out.println("第"+i+"個,名字 :"+memberlist.get(i).getName()+", faceid :"+memberlist.get(i).getFaceId()+", email :"+memberlist.get(i).getEmail());
		}
		// Stop by yourself
		return memberlist;
	}
		
	
	/**
	 * Get Retrieve result json
	 * @author Daniel
	 *
	 * @param jsonPath
	 * @param startIndex
	 * @return
	 */
	public static List<Face> getAllResult(String jsonPath,String jsonName,int startIndex) {
		// init func
		final Gson gson = new Gson();
		final CopyUtil copyUtil = new CopyUtil();

		// init variable
		final Type faceListType = new TypeToken<ArrayList<Face>>() {}.getType();
		List<Face> faceList = new ArrayList<Face>();

		// Get retrieve result
		final File sourceJson = new File(jsonPath.toString() + "/"+jsonName+".json");
		final StringBuilder jsonFileName = new StringBuilder(jsonPath + "/"+jsonName+"_copy.json");
		final File destJson = new File(jsonFileName.toString());
		if(sourceJson.exists()&&sourceJson.length()!=destJson.length()) {
			try {
				copyUtil.copyFile(sourceJson, destJson);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			final File jsonFile = new File(jsonFileName.toString());
			FileReader fileReader = null;
			BufferedReader bufferedReader = null;

			// If json exists
			if (jsonFile.exists()) {
				try {
					fileReader = new FileReader(jsonFileName.toString());
				} catch (FileNotFoundException e) {
				}
				bufferedReader = new BufferedReader(fileReader);
				// Read Json
				final StringBuilder jsonContent = new StringBuilder();
				String line;
				try {
					// Read line
					line = bufferedReader.readLine();
					while (line != null) {
						jsonContent.append(line + "\n");
						line = bufferedReader.readLine();
					}
					// If has data
					if (jsonContent.toString() != null) {
						// Get last one object
						int endIndex = jsonContent.lastIndexOf("}\n\t,");
						String json;
						// Reorganization json
						if (endIndex != -1 && startIndex != endIndex && startIndex < endIndex) {
							if (startIndex > 0) {
								json = "[" + jsonContent.toString().substring(startIndex + 2, endIndex) + "}]";
							} else {
								json = jsonContent.toString().substring(startIndex, endIndex) + "}]";
							}
//							System.out.println("json="+json);
							faceList = gson.fromJson(json, faceListType);
							faceList.get(faceList.size() - 1).setEndIndex(endIndex + 2);
						}
					}
				} catch (IOException e) {
				} finally {
					try {
						bufferedReader.close();
					} catch (IOException e) {
					}
				}
			}
		}		
		return faceList;
	}
	
	/**
	 * Get Retrieve result json
	 * @author Daniel
	 *
	 * @param jsonPath
	 * @param startIndex
	 * @return
	 */
}
