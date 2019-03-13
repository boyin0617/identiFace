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
import com.google.gson.JsonArray;
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
	static protected String ENGINEPATH = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN";

	public static void main() throws SQLException {

		// 取得Real-time結果
		Member member = new Member();
		List<Face> faceList = new ArrayList<>();
		//改參數
		List<Member> memberlist = new ArrayList<>(3);
		String cacheJsonName = "output.cache.egroup";
		final Type faceListType = new TypeToken<ArrayList<Face>>() {
		}.getType();
		ArrayList<String> getfacelist = new ArrayList<>();
		int hasfound = 0;
		JsonArray jo = null;
		String faceListstring = "";
		// 連接資料庫
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("加載資料庫驅動");
			String url="jdbc:mysql://localhost:3306/identiFace?autoReconnect=true&useSSL=false&serverTimezone=UTC";//声明数据库project的url  
//			String url="jdbc:mysql://140.136.155.124/identiFace?autoReconnect=true&useSSL=false&serverTimezone=UTC";
//			String user="identiFace";//数据库账号  
//			String pass = "Faceidenti";
			String user="root";
			String pass="a8s5d1f9";//数据库密码
			// 建立資料庫連結，獲得連結對象conn
			Connection connect = DriverManager.getConnection(url, user, pass);
			System.out.println("資料庫連接成功");
			Statement stmt = connect.createStatement();
			stmt.executeUpdate("DELETE FROM `member_recognize`");
			// Get Real-time data
			while (true) {
				long startTime = System.currentTimeMillis();
				faceList = getCacheResult(ENGINEPATH, cacheJsonName);
				System.out.println("Get Json Using Time:" + (System.currentTimeMillis() - startTime) + " ms,faceList="
						+ new Gson().toJson(faceList));
				// If your fps is 10, means recognize 10 frame per seconds, 1000 ms /10 frame =
				// 100 ms
				faceListstring = new Gson().toJson(faceList);
				Gson gson = new Gson();
				Long faceId = (long) -1;
				long memberId = 0;
				String phonenumber = "";
				String email = "";
				String name = "";
				Date birth = new Date();
				jo = gson.fromJson(faceListstring, JsonArray.class);
				for (int i = 0; i < jo.size(); i++) {
					JsonObject jsonobject = jo.get(i).getAsJsonObject();
					hasfound = jsonobject.get("hasFound").getAsInt();
					if (hasfound == 1) {
						name = jsonobject.get("personId").getAsString();
						if (!getfacelist.contains(name)) {
							//改參數
							System.out.println("當下辨識到"+name);
							if(getfacelist.size()<3) {
								getfacelist.add(name);
							} else {
								getfacelist.remove(0);
								getfacelist.add(name);
							}
							Statement stmtcount = connect.createStatement();
							ResultSet rscount = stmtcount.executeQuery("SELECT * FROM `recognize`");
							int datacount = 0;
							while(rscount.next()) {
							if (rscount.last())
								datacount = rscount.getRow();
							else
								datacount = 0;
							}
							rscount.close();
							System.out.println("datacount = "+datacount);
							// 去資料庫找人
//							Statement stmtrs = connect.createStatement();
//							ResultSet rs = stmtrs.executeQuery("SELECT * FROM `face` WHERE `name` LIKE '" + name + "'");
//							while (rs.next()) {
//								faceId = rs.getLong("faceId");
//							}
							Statement stmtrs2 = connect.createStatement();
							ResultSet rs2 = stmtrs2.executeQuery("SELECT * FROM `member` WHERE `member_name` LIKE '" + name + "'");
							while (rs2.next()) {
								memberId = rs2.getLong("memberId");
								name = rs2.getString("member_name");
								phonenumber = rs2.getString("phone");
								email = rs2.getString("email");
								birth = rs2.getDate("birth");
								member = new Member();
								// 存到member
								member.setMemberId((long) memberId);
								member.setFaceId((long) faceId);
								member.setName(name);
								member.setEmail(email);
								member.setPhone(phonenumber);
								member.setBirth(birth);
								//改參數
								if (datacount == 3) {
									System.out.println("額滿，擠掉");
									stmt.executeUpdate("DELETE FROM `member_recognize` WHERE `member_recognize`.`recogniseid` = 1");
									stmt.executeUpdate(
											"UPDATE `member_recognize` SET `member_recognize` = '1' WHERE `member_recognize`.`recogniseid` = 2");
									stmt.executeUpdate(
											"UPDATE `member_recognize` SET `member_recognize` = '2' WHERE `member_recognize`.`recogniseid` = 3");
									PreparedStatement pre1 = connect.prepareStatement(
											"INSERT INTO `member_recognize` (`recogniseid`, `recog_member_name`, `phone`, `email`) VALUES (3, ?, ?, ?)");
									pre1.setString(1, member.getName());
									pre1.setString(2, member.getPhone());
									pre1.setString(3, member.getEmail());
									pre1.executeUpdate();
									//改參數
								} else if (datacount < 3) {
									System.out.println("新增");
									PreparedStatement pre2 = connect.prepareStatement(
											"INSERT INTO `member_recognize` (`recogniseid`, `recog_member_name`, `phone`, `email`) VALUES (?, ?, ?, ?)");
									System.out.println("新增了"+member.getName());
									pre2.setLong(1, (datacount + 1));
									pre2.setString(2, member.getName());
									pre2.setString(3, member.getPhone());
									pre2.setString(4, member.getEmail());
									pre2.executeUpdate();
								}
							}
						}
					}
				}
				try {
					// 調速度的
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (int i = 0; i < getfacelist.size(); i++) {
					System.out.println("辨識到" + getfacelist.get(i));
				}
//				for (int i = 0; i < memberlist.size(); i++) {
//					System.out.println("第" + (i + 1) + "個,名字 :" + memberlist.get(i).getName() + ", faceid :"
//							+ memberlist.get(i).getFaceId() + ", email :" + memberlist.get(i).getEmail());
//				}
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.print("get data error!");
			e.printStackTrace();
		}
//		return memberlist;
	}

	/**
	 * Get Retrieve result json
	 * 
	 * @author Daniel
	 *
	 * @param jsonPath
	 * @param startIndex
	 * @return
	 */
	public static List<Face> getAllResult(String jsonPath, String jsonName, int startIndex) {
		// init func
		final Gson gson = new Gson();
		final CopyUtil copyUtil = new CopyUtil();

		// init variable
		final Type faceListType = new TypeToken<ArrayList<Face>>() {
		}.getType();
		List<Face> faceList = new ArrayList<Face>();

		// Get retrieve result
		final File sourceJson = new File(jsonPath.toString() + "/" + jsonName + ".json");
		final StringBuilder jsonFileName = new StringBuilder(jsonPath + "/" + jsonName + "_copy.json");
		final File destJson = new File(jsonFileName.toString());
		if (sourceJson.exists() && sourceJson.length() != destJson.length()) {
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
	 * 
	 * @author Daniel
	 *
	 * @param jsonPath
	 * @param startIndex
	 * @return
	 */

	public static List<Face> getCacheResult(String jsonPath, String jsonName) {
		// init func
		final Gson gson = new Gson();
		final CopyUtil copyUtil = new CopyUtil();

		// init variable
		final Type faceListType = new TypeToken<ArrayList<Face>>() {
		}.getType();
		List<Face> faceList = new ArrayList<Face>();
		String name = "";
		int hasfound = 0;
		ArrayList<String> getfacelist = new ArrayList<>();

		// Get retrieve result
		final File sourceJson = new File(jsonPath.toString() + "/" + jsonName + ".json");
		final StringBuilder jsonFileName = new StringBuilder(jsonPath + "/" + jsonName + "_copy.json");
		final File destJson = new File(jsonFileName.toString());
		if (sourceJson.exists() && sourceJson.length() != destJson.length()) {
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
