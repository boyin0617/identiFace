package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.egroupai.engine.control.GetResult;
import com.egroupai.engine.entity.Face;
import com.egroupai.engine.entity.ModelSwitch;
import com.egroupai.engine.entity.RetrieveFace;
import com.egroupai.engine.entity.TrainFace;
import com.egroupai.engine.util.AttributeCheck;
import com.egroupai.engine.util.CmdUtil;
import com.egroupai.engine.util.TxtUtil;
import com.example.GetByFaceId;
import com.example.entity.trainResult;
import com.example.function.CreateTxtPath;
import com.example.function.GenerateFolder;
import com.example.function.GetFileSize;
import com.example.function.delFolderTxtFUNC;
import com.example.storage.StorageFileNotFoundException;
import com.example.storage.StorageService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.example.FileUploadController;

@Controller
public class ReturnTrainController {

	static String folderPath = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN";
	public static final String uploadingDir = folderPath + "/outputface/";

	// 先用圖片辨識檢查有沒有訓練過此人，沒有的話訓練此人
	@RequestMapping(value = "/showfaces/train", method = RequestMethod.POST)
	@ResponseBody
	public String uploadingPost(@RequestBody List<String> uploadingFiles) throws Exception {
		String ENGINEPATH = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN";
		String txtpath = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\list.txt";
		String photolist = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\photolist.egroupList";
		int count = 0; // 計算NO的個數
		String uploaddir = "D:\\Git\\repository\\identiFace\\upload-dir\\";
		ArrayList<String> filespath = new ArrayList<>();
		String faceId = uploadingFiles.get(0);

		// 寫入photolist檔值
		boolean flagcreate = CreateTxtPath.createTxtFile(photolist);
		for (int z = 1; z < uploadingFiles.size(); z++) {
			String content1 = uploaddir + uploadingFiles.get(z) + "\r\n";
			boolean flagwrite = CreateTxtPath.writeTxtFileAppend(photolist, content1);
			String result = CreateTxtPath.readTxtFile(photolist);
		}

		// TODO Auto-generated method stub

		// RetrieveFace
		RetrieveFace retrieveFace = new RetrieveFace();
		retrieveFace.setThreshold(0.7);
		retrieveFace.setHideMainWindow(false);
		retrieveFace.setShowThreadWindow(true);
		retrieveFace.setResolution("720p");
		retrieveFace.setOutputFacePath("outputFace");
		retrieveFace.setOutputFramePath("outputFrame");
		retrieveFace.setPhotoListPath("photolist.egroupList");
		retrieveFace.setMinimumFaceSize(100);
		retrieveFace.setThreshold(0.7);
		retrieveFace.setTrainedBinaryPath("eGroup\\eGroup.Model.binary");
		retrieveFace.setTrainedFaceInfoPath("eGroup\\eGroup.Model.faceInfor");
		retrieveFace.setJsonPath("output");
		retrieveFace(retrieveFace);

		// 取得Real-time結果
		List<Face> faceList = new ArrayList<>();
		String cacheJsonName = "output.cache.egroup";
		ArrayList<String> getfacelist = new ArrayList<>();
		int hasfound = 0;
		JsonArray jo = null;
		String faceListstring = "";

		// while (true) {
		long startTime = System.currentTimeMillis();
		faceList = GetResult.getCacheResult(ENGINEPATH, cacheJsonName);
		faceListstring = new Gson().toJson(faceList);
		Gson gson = new Gson();
		String name = "";

		jo = gson.fromJson(faceListstring, JsonArray.class);
		for (int i = 1; i < jo.size(); i++) {
			JsonObject jsonobject = jo.get(i).getAsJsonObject();
			hasfound = jsonobject.get("hasFound").getAsInt();
			System.out.println(hasfound);
			if (hasfound == 1) {

				// 已訓練過，return faceid
				name = jsonobject.get("personId").getAsString();
				System.out.println("已辨識到" + name);
				return name;

			}
		}
		System.out.println("沒辨識到人");
		// 移陣列第二張當大頭貼，放到headshot，改名faceId.jpg
		String headshot = uploadingFiles.get(1);
		Path sourcePath = Paths.get(uploaddir + headshot);
		Path destinationPath = Paths.get("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\headshot\\" + headshot);
		try {
			Files.copy(sourcePath, destinationPath);
		} catch (FileAlreadyExistsException e) {
			System.out.println("檔案已經存在");
		} catch (IOException e) {
			// something else went wrong
			e.printStackTrace();
		}

		// 重新命名大頭貼
		new File("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\headshot\\" + headshot)
				.renameTo(new File("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\headshot\\" + faceId + ".jpg"));

		for(int i = 1;i < uploadingFiles.size();i++) {
		System.out.println("讀檔");
		count++;

		// 寫入TXT檔值
		String content1 = uploaddir + uploadingFiles.get(i) + "\t" + faceId + "[No]" + i + "\r\n";
		boolean flagcreate1 = CreateTxtPath.createTxtFile(txtpath);
		boolean flagwrite = CreateTxtPath.writeTxtFileAppend(txtpath, content1);
		String result = CreateTxtPath.readTxtFile(txtpath);

		// }

		}
		
		File judgeFolderNotNull = new File("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\eGroup"); // 当前目录下的
		// testdir目录
		TrainFace trainFace = new TrainFace();
		if (judgeFolderNotNull.list().length > 0) {
			trainFace.setModelExist(true);
		} else {
			trainFace.setModelExist(false);
		}
		trainFace.setTrainListPath("list.txt");
		trainFace.setModelPath("eGroup\\eGroup.Model");
		trainFace(trainFace);

//		ModelSwitch modelSwitch = new ModelSwitch();
//		modelSwitch.setNewModelBinaryPath(ENGINEPATH+"/eGroup/eGroup.Model.binary");
//		modelSwitch.setNewModelFaceInfoPath(ENGINEPATH+"/eGroup/eGroup.Model.faceInfor");
//		modelSwitch.setSwitchFilePath(ENGINEPATH+"/Singal_For_Model_Switch.txt");
//		modelSwitch(modelSwitch);
		System.out.println("switchfinished");
		
		
		delFolderTxtFUNC.delAllFile("D:\\Git\\repository\\identiFace\\upload-dir\\");
		// delFolderTxtFUNC.delAllFile("D:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\face");
		delFolderTxtFUNC.deltxt("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\list.txt");
//		try {
//			// 調速度的
//			Thread.sleep(500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	//2019-05-14 02:12:22	Fail	FileNotFound	C:\eGroupAI_FaceEngine_CPU_V3.1.3_SN\headshot\533.jpg	boyin[No]1
	//2019-05-14 02:13:29	Pass	C:\eGroupAI_FaceEngine_CPU_V3.1.3_SN\headshot\5.jpg	boyin[No]1
		//讀取trainResult的log檔看有沒有訓練成功
		int success = 0;
		try {
		File file = new File("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\Log.TrainResultCPU.eGroup");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		int traincount = 0;
		String trainresult = null;
		while ((trainresult = br.readLine())!=null) {
			if(traincount == 5) {
				break;
			}
			System.out.println(trainresult);
			if(trainresult.contains("Pass")) {
				System.out.println("Pass");
				success += 1;
			}
			traincount++;
		}
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		delFolderTxtFUNC.deltxt("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\output.cache.egroup.json");
		delFolderTxtFUNC.deltxt("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\output.cache.egroup_copy.json");
		delFolderTxtFUNC.deltxt("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\photolist.egroupList");
		delFolderTxtFUNC.deltxt("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\Log.TrainResultCPU.eGroup");
		if(success > 0) {
			return "訓練成功";
		} else {
			return "訓練失敗";
		}
	}

	// 導到 /file Controller

	private static boolean trainFace(TrainFace trainFace) {
		boolean flag = false;
		// init func
		trainFace.generateCli();
		if (trainFace.getCommandList() != null) {
			final CmdUtil cmdUtil = new CmdUtil();
			flag = cmdUtil.cmdProcessBuilder(trainFace.getCommandList());
		}
		return flag;
	}

	private static void modelSwitch(ModelSwitch modelSwitch) {
		// init func
		final AttributeCheck attributeCheck = new AttributeCheck();

		// init variable
		final File newModelBinary = new File(modelSwitch.getNewModelBinaryPath());
		final File newModelFaceInfo = new File(modelSwitch.getNewModelFaceInfoPath());

		// Check Model Files
		if (newModelBinary.exists() && newModelFaceInfo.exists()
				&& attributeCheck.stringsNotNull(modelSwitch.getSwitchFilePath())) {
			// Model
			final List<String> dataList = new ArrayList<>();
			dataList.add(modelSwitch.getNewModelBinaryPath());
			dataList.add(modelSwitch.getNewModelFaceInfoPath());

			// init func
			final TxtUtil txtUtil = new TxtUtil();
			txtUtil.create(modelSwitch.getSwitchFilePath(), dataList);
		}
	}

	private static boolean retrieveFace(RetrieveFace retrieveFace) {
		boolean flag = false;
		// init func
		retrieveFace.generateCli();
		if (retrieveFace.getCommandList() != null) {
			final CmdUtil cmdUtil = new CmdUtil();
			flag = cmdUtil.cmdProcessBuilder(retrieveFace.getCommandList());
		}
		return flag;
	}
}