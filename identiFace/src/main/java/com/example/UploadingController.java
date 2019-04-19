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
import org.springframework.web.bind.annotation.PathVariable;
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

import com.egroupai.engine.entity.TrainFace;
import com.egroupai.engine.util.CmdUtil;
import com.example.GetByFaceId;
import com.example.entity.trainResult;
import com.example.function.CreateTxtPath;
import com.example.function.GetFileSize;
import com.example.function.delFolderTxtFUNC;
import com.example.storage.StorageFileNotFoundException;
import com.example.storage.StorageService;
import com.example.FileUploadController;
@Controller
public class UploadingController {
	
	

	private Connection con;
	private Statement st;
	private ResultSet rs;
	
	static String folderPath = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN";
    public static final String uploadingDir =  folderPath + "/face/";

    @RequestMapping("/upload")
    public String uploading(Model model) {
        File file = new File(uploadingDir);
        
        model.addAttribute("files", file.listFiles());
        return "upload";
    }

    //手動訓練
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadingPost(@RequestParam("uploadingFiles") MultipartFile[] uploadingFiles,@RequestParam("faceId") String faceId) throws Exception {
    	String txtpath ="C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\list.txt";
    	int count = 0; //計算NO的個數
    	
		ArrayList<String> filespath = new ArrayList<>();
        for(MultipartFile uploadedFile : uploadingFiles) {
            File file = new File(uploadingDir + uploadedFile.getOriginalFilename());
            //System.out.println(uploadedFile.getOriginalFilename());
            filespath.add(uploadedFile.getOriginalFilename());
            System.out.println("讀檔");
            count++;        
        
    	//抓檔案個數
        GetFileSize g = new GetFileSize();
		long startTime = System.currentTimeMillis();
		long l = 0;
		String path = uploadingDir;
		File ff = new File(path);
		if (ff.isDirectory()) { // 如果路徑是資料夾的時候
			System.out.println("檔個數 " + g.getlist(ff));
//			System.out.println("目錄");
			l = g.getFileSize(ff);
//			System.out.println(path + "目錄的大小為：" + g.FormetFileSize(l));
		} else {
			System.out.println(" 檔個數 1");
			System.out.println("檔");
			l = g.getFileSizes(ff);
//			System.out.println(path + "檔的大小為：" + g.FormetFileSize(l));
		}

		
      //寫入TXT檔值    
      String content1 = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\face\\"+ uploadedFile.getOriginalFilename() +"	"+"yan"+"[No]"+count+ "\r\n";
      boolean flagcreate = CreateTxtPath.createTxtFile(txtpath);
      boolean flagwrite = CreateTxtPath.writeTxtFileAppend(txtpath,content1);
      String result = CreateTxtPath.readTxtFile(txtpath);
     
      }
//        trainresult.setId("testid");
//        trainresult.setPaths(filespath);
     
//        File judgeFolderNotNull = new File("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\eGroup");  // 当前目录下的 testdir目录
//        TrainFace trainFace = new TrainFace();
//        if(judgeFolderNotNull.list().length>0){
//        	trainFace.setModelExist(true);
//        }
//        else{
//        	trainFace.setModelExist(false);
//	    }
//        trainFace.setTrainListPath("list.txt");
//		trainFace.setModelPath("eGroup\\eGroup.Model");
//		trainFace(trainFace);
        
//		// TrainFace
//	    TrainFace trainFace = new TrainFace();
//		trainFace.setModelExist(true);
//		trainFace.setTrainListPath("list.txt");
//		trainFace.setModelPath("eGroup\\eGroup.Model");
//		trainFace(trainFace);
		
		
		delFolderTxtFUNC.delAllFile("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\face");
		delFolderTxtFUNC.deltxt("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\list.txt");		       
    
		rs.close();		
		
		//導到 /file Controller
	    return "redirect:/file";
    }
    
    
    
    private static boolean trainFace(TrainFace trainFace){		
		boolean flag = false;
		// init func 
		trainFace.generateCli();
		if(trainFace.getCommandList()!=null){
			final CmdUtil cmdUtil = new CmdUtil();
			flag = cmdUtil.cmdProcessBuilder(trainFace.getCommandList());				
		}
		return flag;
	}
}
