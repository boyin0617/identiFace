package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.egroupai.engine.entity.Face;
import com.egroupai.engine.entity.ModelMerge;
import com.egroupai.engine.entity.ModelSwitch;
import com.egroupai.engine.entity.RetrieveFace;
import com.egroupai.engine.entity.TrainFace;
import com.egroupai.engine.util.AttributeCheck;
import com.egroupai.engine.util.CmdUtil;
import com.egroupai.engine.util.CopyUtil;
import com.egroupai.engine.util.TxtUtil;
import com.example.function.delFolderTxtFUNC;
import com.example.storage.StorageService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Controller
public class TrainController {
	
	//先檢查有沒有訓練過此人，將辨識擷取的照片傳10張到專案裡的upload-dir，回傳檔名陣列
	@CrossOrigin
	@GetMapping("/showfaces")
	@ResponseBody
	public List<List<String>> selecttenpic(){
		//delFolderTxtFUNC.delAllFile("D:\\Git\\repository\\identiFace\\upload-dir\\");
		List<List<String>> resultlist = new ArrayList<>();
		resultlist = getLastTen();
		String path = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\outputface\\" ;
		for(int i = 0;i < resultlist.size();i++) {
			System.out.println(resultlist.get(i).get(0)+" --- "+resultlist.get(i).get(1));
			Path sourcePath = Paths.get(path+resultlist.get(i).get(1));
			Path destinationPath = Paths.get("D:\\Git\\repository\\identiFace\\upload-dir\\"+resultlist.get(i).get(1));
			try {
				Files.copy(sourcePath, destinationPath);
			} catch (FileAlreadyExistsException e) {
				System.out.println("檔案已經存在");
			} catch (IOException e) {
				// something else went wrong
				e.printStackTrace();
			}
		}
		
		
		
//		File file = new File(path);
//		String[] allfiles = file.list();
//		List<String> urllist = new ArrayList<>();
//		int count = 0;
//		
//		for(int i = allfiles.length-1; i >= 0;i--) {
//			System.out.println(allfiles[i]);
//			System.out.println(path+allfiles[i]);
//			
//			Path sourcePath = Paths.get(path+allfiles[i]);
//			Path destinationPath = Paths.get("D:\\Git\\repository\\identiFace\\upload-dir\\"+allfiles[i]);
//
//			try {
//				Files.copy(sourcePath, destinationPath);
//				urllist.add(allfiles[i]);
//			} catch (FileAlreadyExistsException e) {
//				System.out.println("檔案已經存在");
//			} catch (IOException e) {
//				// something else went wrong
//				e.printStackTrace();
//			}
//		    
//		    count++;
//		    
//				if(count == 10) {
//					break;
//				}
//		}
		return resultlist;
	}	
    
	static protected String ENGINEPATH = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN";

	// 抓結果
	public static List<List<String>> getLastTen(){
		
		List<Face> faceList = new ArrayList<>();
		String cacheJsonName = "output.cache.egroup";
		final Type faceListType = new TypeToken<ArrayList<Face>>() {
		}.getType();
		JsonArray jo = null;
		String faceListstring = "";		
		List<String> urlresult = new ArrayList<>();
		List<List<String>> resultlist = new ArrayList<>();
		int count = 0;

		while (count < 1) {
			long startTime = System.currentTimeMillis();
			faceList = getCacheResult(ENGINEPATH, cacheJsonName);
//			System.out.println("Get Json Using Time:" + (System.currentTimeMillis() - startTime) + " ms,faceList="
//					+ new Gson().toJson(faceList));
			// If your fps is 10, means recognize 10 frame per seconds, 1000 ms /10 frame =
			// 100 ms
			faceListstring = new Gson().toJson(faceList);
			Gson gson = new Gson();
			String faceId = "";
			jo = gson.fromJson(faceListstring, JsonArray.class);
			for (int i = jo.size() - 1; i >= 0; i--) {
				if(resultlist.size() == 10) {
					break;
				}
				urlresult = new ArrayList<>();
				int hasfound = 0;
				String facepath = "";
				JsonObject jsonobject = jo.get(i).getAsJsonObject();
				hasfound = jsonobject.get("hasFound").getAsInt();
				facepath = jsonobject.get("frameFace").getAsJsonObject().get("frameFacePath").getAsString();
				String facename = StringUtils.substringAfter(facepath, "/");
				if (hasfound == 1) {
					faceId = jsonobject.get("personId").getAsString();
					System.out.println("辨識到faceId :" + faceId+" ，facename : "+facename);
					urlresult.add(faceId);
					urlresult.add(facename);
					resultlist.add(urlresult);			
				}else {
					faceId = null;
					System.out.println("沒有辨識到人  ， facename : "+facename);
					urlresult.add(faceId);
					urlresult.add(facename);
					resultlist.add(urlresult);
				}
			}
			try {
				// 調速度的
				Thread.sleep(0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
		}
		return resultlist;
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