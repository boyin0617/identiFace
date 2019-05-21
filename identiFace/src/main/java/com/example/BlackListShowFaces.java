package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.lang.reflect.Type;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.PUBLIC_MEMBER;import org.springframework.context.support.StaticApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.egroupai.engine.entity.Face;
import com.egroupai.engine.util.CopyUtil;
import com.example.function.delFolderTxtFUNC;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Controller
public class BlackListShowFaces {
	
	Map<String, List<String>> resultmap = null;
	boolean stopsignal = false;
	
	@GetMapping("/blacklist/return")
	@ResponseBody
	public Map<String, List<String>> blacklistreturn() {
		
		return resultmap;

	}
	
	@GetMapping("/blacklist/getresult/stop")
	@ResponseBody
	public void stopBlackListResult() {
		
		stopsignal = true;
	}

	static protected String ENGINEPATH = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN";

	// 抓結果
	@GetMapping("/blacklist/getresult")
	@ResponseBody
	public void getBlackListResult() {
		stopsignal = false;
		List<Face> faceList = new ArrayList<>();
		String cacheJsonName = "output.cache.egroup";
		String headshotpath = "D:\\Git\\repository\\identiFace\\upload-dir\\";
		JsonArray jo = null;
		String faceListstring = "";
		List<String> headshottime = new ArrayList<>();
		resultmap = new HashMap<>();
		delFolderTxtFUNC.delAllFile("D:\\Git\\repository\\identiFace\\upload-dir\\");
		while (!stopsignal) {
			faceList = getCacheResult(ENGINEPATH, cacheJsonName);
			faceListstring = new Gson().toJson(faceList);
			Gson gson = new Gson();
			String faceId = "";
			String time = "";
			jo = gson.fromJson(faceListstring, JsonArray.class);
			System.out.println("執行中");
			System.out.println(jo.size());
			int count = 0;
			for (int i = jo.size() - 1; i >= 0; i--,count++) {
				if(count >= 10) break;
				int hasfound = 0;
				JsonObject jsonobject = jo.get(i).getAsJsonObject();
				hasfound = jsonobject.get("hasFound").getAsInt();
				System.out.println("Hasfound = " + hasfound);
				if (hasfound == 1) {
					headshottime = new ArrayList<>();
					faceId = jsonobject.get("personId").getAsString();
					time = jsonobject.get("systemTime").getAsString();
					System.out.println("辨識到faceId :" + faceId + " ，time : " + time);
					boolean exist = resultmap.containsKey(faceId);
					// 檢查有沒有在map
					if (!exist) {
						Path sourcePath = Paths
								.get("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\headshot\\" + faceId + ".jpg");
						Path destinationPath = Paths.get("D:\\Git\\repository\\identiFace\\upload-dir\\" + faceId + ".jpg");
						try {
							Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							// something else went wrong
							e.printStackTrace();
						}
						headshottime.add(0, faceId+".jpg");
						headshottime.add(1, time);
						// 存到resultmap裡
						resultmap.put(faceId, headshottime);
					} else {
						// 修改時間
						resultmap.get(faceId).set(1, time);
					}
					
				}
			}
			try {
				// 調速度的
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (Object key : resultmap.keySet()) {
	            System.out.println(key + " : " + resultmap.get(key));
	        }
		}
	}

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