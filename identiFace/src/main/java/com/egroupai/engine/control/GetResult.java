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
	
	public static void main(String args[]) throws SQLException{
		List<Face> faceList = new ArrayList<>();
		String name = "";
		String faceId = "";
		String phone = "";
		String email = "";
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
				Thread.sleep(1000);
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
				}
				System.out.println("辨識成功 ! 辨識到: "+name);
			}else if(found == 0) {
				System.out.println("辨識失敗，請再試一次");
			}
			}
			for (int j = 0; j <= foundlist.size()-1; j++) {
				System.out.println("名單"+(j+1)+"號: "+foundlist.get(j));
			}
			break;
		}
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("加载数据库驱动成功");
			String url="jdbc:mysql://localhost:3306/project?autoReconnect=true&useSSL=false&serverTimezone=UTC";//声明数据库project的url  
			String user="root";//数据库账号  
			String pass="a8s5d1f9";//数据库密码
			//建立数据库连接，获得连接对象conn  
			Connection connect=DriverManager.getConnection(url,user,pass);
			System.out.println("数据库连接成功");
			Statement stmt = connect.createStatement();
			for (int i = 0; i <= foundlist.size()-1; i++) {
			ResultSet rs = stmt.executeQuery("SELECT * FROM `face` WHERE `name` LIKE '"+foundlist.get(i)+"'");
				while (rs.next()) {
					faceId = rs.getString("faceId");
				}
			ResultSet rs2 = stmt.executeQuery("SELECT * FROM `member` WHERE `faceId` LIKE '"+faceId+"'");
			while (rs2.next()) {
					phone = rs2.getString("phone");
					email = rs2.getString("email");
				}
			System.out.println("會員名字 : "+foundlist.get(i)+", faceid為 :  "+faceId+", 電話號碼為 : "+phone+", email為 : "+email);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.print("get data error!");
			e.printStackTrace();
		}
		// Stop by yourself
		
		//Get Real-time data
//		String cacheJsonName = "output.cache.egroup";	// Get Real-time data
//		while(true) {
//			long startTime = System.currentTimeMillis();
//			faceList = getCacheResult(ENGINEPATH,cacheJsonName);
//			System.out.println("Get Json Using Time:" + (System.currentTimeMillis() - startTime) + " ms,faceList="+new Gson().toJson(faceList));
//			// If your fps is 10, means recognize 10 frame per seconds, 1000 ms /10 frame = 100 ms
//			try {
//				Thread.sleep(300);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		// Stop by yourself
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
	public static List<Face> getCacheResult(String jsonPath,String jsonName) {
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
						final int endIndex = jsonContent.lastIndexOf("}\n\t,");
						final String json = jsonContent.toString().substring(0, endIndex) + "}]";
						faceList = gson.fromJson(json, faceListType);						
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
}
