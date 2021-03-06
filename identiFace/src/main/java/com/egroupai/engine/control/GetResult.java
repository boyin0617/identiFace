package com.egroupai.engine.control;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.channels.FileChannel;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.egroupai.engine.entity.Face;
import com.egroupai.engine.util.CopyUtil;
import com.example.function.GenerateFolder;
import com.example.function.delFolderTxtFUNC;
import com.example.entity.Member;
import com.example.entity.MemberRec;
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
	static String faceId = "";
	static boolean run = true;
	static protected String ENGINEPATH = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN";
	
	public static String returnResult() {
		System.out.println(faceId);
		return faceId;	
	}
	
	public static void cancelRun() {
		run = false;
		
	}
	// 一般抓結果
	public static void main(){
		run = true;
		List<Face> faceList = new ArrayList<>();
		String cacheJsonName = "output.cache.egroup";
		final Type faceListType = new TypeToken<ArrayList<Face>>() {
		}.getType();
		List<String> reclist = new ArrayList<>();
		int hasfound = 0;
		JsonArray jo = null;
		String faceListstring = "";
		int count = 0;
		System.out.println("取得結果");
		faceId = "";
		while (run) {
			long startTime = System.currentTimeMillis();
			faceList = getCacheResult(ENGINEPATH, cacheJsonName);
//			System.out.println("Get Json Using Time:" + (System.currentTimeMillis() - startTime) + " ms,faceList="
//					+ new Gson().toJson(faceList));
			// If your fps is 10, means recognize 10 frame per seconds, 1000 ms /10 frame =
			// 100 ms
			faceListstring = new Gson().toJson(faceList);
			Gson gson = new Gson();
			jo = gson.fromJson(faceListstring, JsonArray.class);
			if(jo.size()==0) {
				continue;
			}
			for (int i = jo.size() - 1; i >= 0; i--) {
				count++;
				JsonObject jsonobject = jo.get(i).getAsJsonObject();
				hasfound = jsonobject.get("hasFound").getAsInt();
				if (hasfound == 1) {
					faceId = jsonobject.get("personId").getAsString();
					System.out.println("辨識到faceId :" + faceId);
//					if (!reclist.contains(faceId))
//						reclist.add(faceId);
				} else {
					faceId="";
				}
				if(count >= 3) {
					break;
				}
			}
			try {
				// 調速度的
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		}
	}

	// 重新訓練用的(讀結果，複製照片到...資料夾)
	public static Map<String, List<String>> retrain(){

		// 取得Real-time結果
		Member member = new Member();
		MemberRec memberrec = new MemberRec();
		List<Face> faceList = new ArrayList<>();
		// 改參數
		List<Member> memberlist = new ArrayList<>(3);
		String cacheJsonName = "output.cache.egroup";
		final Type faceListType = new TypeToken<ArrayList<Face>>() {
		}.getType();
		List<String> allfacelist = new ArrayList<>();
		List<String> facepathlist = new ArrayList<>();
		List<List<String>> resultlist = new ArrayList<>();
		Map<String, List<String>> resultmap = new HashMap<>();
		int hasfound = 0;
		JsonArray jo = null;
		String faceListstring = "";
		int count = 0;
		// Get Real-time data
		while (count < 5) {
			long startTime = System.currentTimeMillis();
			faceList = getCacheResult(ENGINEPATH, cacheJsonName);
//			System.out.println("Get Json Using Time:" + (System.currentTimeMillis() - startTime) + " ms,faceList="
//					+ new Gson().toJson(faceList));
			// If your fps is 10, means recognize 10 frame per seconds, 1000 ms /10 frame =
			// 100 ms
			faceListstring = new Gson().toJson(faceList);
			Gson gson = new Gson();
//				Long faceId = (long) -1;
			String memberId = "";
			String phonenumber = "";
			String email = "";
			String facepath = "";
			String faceId = "";
			Date birth = new Date();
			jo = gson.fromJson(faceListstring, JsonArray.class);
			for (int i = jo.size() - 1; i >= 0; i--) {
				JsonObject jsonobject = jo.get(i).getAsJsonObject();
				hasfound = jsonobject.get("hasFound").getAsInt();
				if (hasfound == 1) {
					faceId = jsonobject.get("personId").getAsString();
					System.out.println("辨識到faceId :" + faceId);

					// 創建對應faceId的資料夾
					String path = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\" + faceId + "recfile";
					System.out.println(path);
					File file = new File(path);
					if (!file.exists()) {
						if (GenerateFolder.mkDirectory(path)) {
							System.out.println("成功創建" + faceId + "的資料夾");
						} else {
							System.out.println("創建資料夾失敗");
						}
					} else {
						System.out.println("資料夾已經存在");
					}

					facepath = jsonobject.get("frameFace").getAsJsonObject().get("frameFacePath").getAsString();
					String facepath2 = facepath.replace('/', '\\');
					String filename = StringUtils.substringAfter(facepath, "/");
					File filepath = new File(path + "\\" + filename);
					String tofilepath = path + "\\" + filename;
					String fromfilepath = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\" + facepath2;
					System.out.println("檔名為: " + filename);
					System.out.println("filepath為:" + filepath);
					System.out.println("fromfilepath : " + fromfilepath);
					System.out.println("tofilepath : " + tofilepath);
					if (resultmap.get(faceId) == null) {
						facepathlist = new ArrayList<>();
						facepathlist.add(facepath);
						resultmap.put(faceId, facepathlist);
						// 移動檔案到那個資料夾
						System.out.println("開始複製");
						Path sourcePath = Paths.get(fromfilepath);
						Path destinationPath = Paths.get(tofilepath);

						try {
							Files.copy(sourcePath, destinationPath);
						} catch (FileAlreadyExistsException e) {
							// destination file already exists
						} catch (IOException e) {
							// something else went wrong
							e.printStackTrace();
						}

					} else {
						if (resultmap.get(faceId).size() <= 10) {
							resultmap.get(faceId).add(facepath);
						} else {
							System.out.println("滿10個了 break");
							break;
						}
						// 移動檔案到那個資料夾
//							if(!filepath.exists()) {
						System.out.println("開始複製");
						Path sourcePath = Paths.get(fromfilepath);
						Path destinationPath = Paths.get(tofilepath);

						try {
							Files.copy(sourcePath, destinationPath);
						} catch (FileAlreadyExistsException e) {
							System.out.println("檔案已經存在");
						} catch (IOException e) {
							System.out.println("出錯囉~~~~~~~~~~~~");
							e.printStackTrace();
						}
					}

//						getfacelist.add(faceId);
//						getfacelist.add(facepath);
//						if(resultlist.size()<3) {
//							resultlist.add(getfacelist);
//						} else if(resultlist.size()==3){
//							resultlist.set(0, resultlist.get(1));
//							resultlist.set(1, resultlist.get(2));
//							resultlist.set(2, getfacelist);
//						}
//							Statement stmtcount = connect.createStatement();
//							ResultSet rscount = stmtcount.executeQuery("SELECT * FROM `member_recognize`");
//							int datacount = 0;
//							while(rscount.next()) {
//							if (rscount.last())
//								datacount = rscount.getRow();
//							else
//								datacount = 0;
//							}
//							rscount.close();
//							System.out.println("datacount = "+datacount);
					// 去資料庫找人
//							Statement stmtrs = connect.createStatement();
//							ResultSet rs = stmtrs.executeQuery("SELECT * FROM `face` WHERE `name` LIKE '" + name + "'");
//							while (rs.next()) {
//								faceId = rs.getLong("faceId");
//							}
//							Statement stmtrs2 = connect.createStatement();
//							ResultSet rs2 = stmtrs2.executeQuery("SELECT * FROM `member` WHERE `member_name` LIKE '" + name + "'");
//							while (rs2.next()) {
//								memberId = rs2.getString("memberId");
//								name = rs2.getString("member_name");
//								phonenumber = rs2.getString("phone");
//								email = rs2.getString("email");
//								birth = rs2.getDate("birth");
//								member = new Member();
//								// 存到member
//								member.setMemberId(memberId);
//								member.setFaceId((long) faceId);
//								member.setName(name);
//								member.setEmail(email);
//								member.setPhone(phonenumber);
//								member.setBirth(birth);
//								//改參數
//								if (datacount == 3) {
//									System.out.println("額滿，擠掉");
//									stmt.executeUpdate("DELETE FROM `member_recognize` WHERE `member_recognize`.`recog_id` = 1");
//									stmt.executeUpdate(
//											"UPDATE `member_recognize` SET `recog_id` = '1' WHERE `member_recognize`.`recog_id` = 2");
//									stmt.executeUpdate(
//											"UPDATE `member_recognize` SET `recog_id` = '2' WHERE `member_recognize`.`recog_id` = 3");
//									PreparedStatement pre1 = connect.prepareStatement(
//											"INSERT INTO `member_recognize` (`recog_id`, `recog_member_id` ,`recog_member_name`, `recog_scan_time`) VALUES (3, ?, ?, NOW())");
//									pre1.setString(1, member.getMemberId());
//									pre1.setString(2, member.getName());
//									pre1.executeUpdate();
//									//改參數
//								} else if (datacount < 3) {
//									System.out.println("新增");
//									PreparedStatement pre2 = connect.prepareStatement(
//											"INSERT INTO `member_recognize` (`recog_id`, `recog_member_id` ,`recog_member_name`, `recog_scan_time`) VALUES (?, ?, ?, NOW())");
//									System.out.println("新增了"+member.getName());
//									pre2.setLong(1, (datacount + 1));
//									pre2.setString(2, member.getMemberId());
//									pre2.setString(3, member.getName());
//									pre2.executeUpdate();
//								}
//							}

				}
			}
			try {
				// 調速度的
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
		}
		return resultmap;
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
