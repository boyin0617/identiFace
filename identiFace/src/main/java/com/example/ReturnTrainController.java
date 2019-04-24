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

import java.io.File;
import java.io.IOException;
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

import com.egroupai.engine.entity.ModelSwitch;
import com.egroupai.engine.entity.TrainFace;
import com.egroupai.engine.util.AttributeCheck;
import com.egroupai.engine.util.CmdUtil;
import com.egroupai.engine.util.TxtUtil;
import com.example.GetByFaceId;
import com.example.entity.trainResult;
import com.example.function.CreateTxtPath;
import com.example.function.GetFileSize;
import com.example.function.delFolderTxtFUNC;
import com.example.storage.StorageFileNotFoundException;
import com.example.storage.StorageService;
import com.example.FileUploadController;

@Controller
public class ReturnTrainController {

	private Connection con;
	private Statement st;
	private ResultSet rs;

	static String folderPath = "D:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN";
	public static final String uploadingDir = folderPath + "/outputface/";


	// 手動訓練
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public String uploadingPost(@RequestBody List<String> uploadingFiles) throws Exception {
		String txtpath = "D:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\list.txt";
		int count = 0; // 計算NO的個數
		String uploaddir = "D:\\Git\\repository\\identiFace\\upload-dir\\";
		ArrayList<String> filespath = new ArrayList<>();
		String faceId = uploadingFiles.get(0);
		
		//移陣列第二張當大頭貼，放到headshot，改名faceId.jpg
		String headshot = uploadingFiles.get(1);
		Path sourcePath = Paths
				.get(uploaddir + headshot);
		Path destinationPath = Paths
				.get("D:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\headshot\\" + headshot);
		System.out.println("sourcePath : "+sourcePath);
		System.out.println("destination : "+destinationPath);
		try {
			Files.copy(sourcePath, destinationPath);
		} catch (FileAlreadyExistsException e) {
			System.out.println("檔案已經存在");
		} catch (IOException e) {
			// something else went wrong
			e.printStackTrace();
		}
		
		//重新命名大頭貼
		new File("D:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\headshot\\" + headshot)
				.renameTo(new File("D:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\headshot\\" + faceId + ".jpg"));
		
		//讀剩下的照片	
		for (int i = 1; i < uploadingFiles.size(); i++) {
			File file = new File(uploaddir + uploadingFiles.get(i));
			System.out.println("讀檔");
			count++;
			
			
			// 寫入TXT檔值
			String content1 = uploaddir + uploadingFiles.get(i) + "	"
					+ faceId + "[No]" + count*200 + "\r\n";
			boolean flagcreate = CreateTxtPath.createTxtFile(txtpath);
			boolean flagwrite = CreateTxtPath.writeTxtFileAppend(txtpath, content1);
			String result = CreateTxtPath.readTxtFile(txtpath);

		}

		File judgeFolderNotNull = new File("D:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\eGroup"); // 当前目录下的 testdir目录
		TrainFace trainFace = new TrainFace();
		if (judgeFolderNotNull.list().length > 0) {
			trainFace.setModelExist(true);
		} else {
			trainFace.setModelExist(false);
		}
		trainFace.setTrainListPath("list.txt");
		trainFace.setModelPath("eGroup\\eGroup.Model");
		trainFace(trainFace);
		
		ModelSwitch modelSwitch = new ModelSwitch();
		modelSwitch.setNewModelBinaryPath(folderPath+"\\eGroup\\eGroup.Model.binary");
		modelSwitch.setNewModelFaceInfoPath(folderPath+"\\eGroup\\eGroup.Model.faceInfor");
		modelSwitch.setSwitchFilePath(folderPath+"\\Singal_For_Model_Switch.txt");
		modelSwitch(modelSwitch);
		
// delFolderTxtFUNC.delAllFile("D:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\face");
		delFolderTxtFUNC.deltxt("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\list.txt");
		System.out.println("刪除");

		// 導到 /file Controller
		return "成功訓練"+faceId;
	}

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
	
	private static void modelSwitch(ModelSwitch modelSwitch){
		// init func
		final AttributeCheck attributeCheck = new AttributeCheck();
		
		// init variable
		final File newModelBinary = new File(modelSwitch.getNewModelBinaryPath());
		final File newModelFaceInfo = new File(modelSwitch.getNewModelFaceInfoPath());
		
		// Check Model Files 
		if(newModelBinary.exists()&&newModelFaceInfo.exists()&&attributeCheck.stringsNotNull(modelSwitch.getSwitchFilePath())) {			
			// Model 
			final List<String> dataList = new ArrayList<>();
			dataList.add(modelSwitch.getNewModelBinaryPath());
			dataList.add(modelSwitch.getNewModelFaceInfoPath());
			
			// init func
			final TxtUtil txtUtil = new TxtUtil();
			txtUtil.create(modelSwitch.getSwitchFilePath(), dataList);
		}
	}
}