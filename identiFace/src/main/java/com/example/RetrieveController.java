package com.example;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.egroupai.engine.control.GetResult;
import com.egroupai.engine.entity.Face;
import com.egroupai.engine.entity.ModelMerge;
import com.egroupai.engine.entity.ModelSwitch;
import com.egroupai.engine.entity.RetrieveFace;
import com.egroupai.engine.entity.TrainFace;
import com.egroupai.engine.util.AttributeCheck;
import com.egroupai.engine.util.CmdUtil;
import com.egroupai.engine.util.TxtUtil;
import com.example.entity.Customer;
import com.example.entity.Member;
import com.example.function.GenerateFolder;
import com.example.util.TerminateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.example.dao.memberDAO;

@Controller
public class RetrieveController {
	@Autowired
	memberDAO memberDAO;
	private static final String Date  = null;
	
	//取得結果(回傳辨識到的faceId 字串陣列)
	@RequestMapping("/getresult")
	@ResponseBody
	public List<String> home() throws SQLException {
		
		List<String> resultlist = GetResult.main();

		return resultlist;
	}
	
	//重新訓練(辨識到人，創資料夾，移照片到資料夾，) (未完成)
	@PostMapping("/retrain")
	@ResponseBody
	public Map<String, List<String>> retrain(@ModelAttribute String faceId) throws SQLException {
		
		Map<String, List<String>> resultmap = GetResult.retrain();
		
		for (String key : resultmap.keySet()) {
            
        }
		return resultmap;
	}
	
	//開鏡頭
	@RequestMapping("/retrieveface")
	public String RetrieveFace() {
		
		RetrieveFace retrieveFace = new RetrieveFace();
		retrieveFace.setThreshold(0.7);
		retrieveFace.setHideMainWindow(false);
		retrieveFace.setShowThreadWindow(true);
		retrieveFace.setResolution("720p");
		retrieveFace.setOutputFacePath("outputFace");
		retrieveFace.setOutputFramePath("outputFrame");
		retrieveFace.setCam("1");
		retrieveFace.setMinimumFaceSize(100);
		retrieveFace.setThreshold(0.7);
		retrieveFace.setTrainedBinaryPath("eGroup\\eGroup.Model.binary");
		retrieveFace.setTrainedFaceInfoPath("eGroup\\eGroup.Model.faceInfor");
		retrieveFace.setJsonPath("output");
		retrieveFace(retrieveFace);
		return "redirect:/";		
	}
	
	 //關鏡頭
	 @RequestMapping(value = "/retrieveface/terminate", method = RequestMethod.GET)
	 @ResponseBody
	  public void terminateEngine(){
	   
	   terminateRetrieveProcess("RetrieveFace.exe");
	
	  }
	  // terminate
	  protected static void terminateRetrieveProcess(String processName) {
	   final TerminateUtil  terminate = new TerminateUtil();
	   terminate.cmdProcessTerminate(processName);  
	   System.out.println("Terminate process="+processName);
	  }
	
	//前端輸入faceId，創faceId資料夾，開鏡頭，辨識的照片存在faceId資料夾
	@PostMapping("/retrieveface/withfaceId")
	public String RetrieveFace(@ModelAttribute("faceid") String faceId) throws ClassNotFoundException, SQLException {
		 String ENGINEPATH = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN";	 
		 
			// 取得Real-time結果
			List<Face> faceList = new ArrayList<>();
			String cacheJsonName = "output.cache.egroup";
			final Type faceListType = new TypeToken<ArrayList<Face>>() {}.getType();
			ArrayList<String> getfacelist = new ArrayList<>();
			int hasfound = 0;
			JsonArray jo = null;
			String faceListstring = "";
			
			
			
				while (true) {
					long startTime = System.currentTimeMillis();
					faceList = GetResult.getCacheResult(ENGINEPATH, cacheJsonName);
					faceListstring = new Gson().toJson(faceList);
					Gson gson = new Gson();
					String name = "";

					jo = gson.fromJson(faceListstring, JsonArray.class);
					for (int i = 0; i < jo.size(); i++) {
						JsonObject jsonobject = jo.get(i).getAsJsonObject();
						hasfound = jsonobject.get("hasFound").getAsInt();
						System.out.println(hasfound);
						if (hasfound == 1) {
							name = jsonobject.get("personId").getAsString();
								System.out.println("當下辨識到"+name);
							}
						else {
							System.out.println("新用戶 即將創folder");
							boolean successful = GenerateFolder.mkDirectory("D:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\" + faceId);
							System.out.println("新用戶 即將創folder"+ successful);
							
						}
						}
					try {
						// 調速度的
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				 
	  RetrieveFace retrieveFace = new RetrieveFace();
	  retrieveFace.setThreshold(0.7);
	  retrieveFace.setHideMainWindow(false);
	  retrieveFace.setShowThreadWindow(true);
	  retrieveFace.setResolution("720p");
	  retrieveFace.setOutputFacePath(faceId);
	  retrieveFace.setOutputFramePath("outputFrame");
	  retrieveFace.setCam("0");
	  retrieveFace.setMinimumFaceSize(100);
	  retrieveFace.setThreshold(0.7);
	  retrieveFace.setTrainedBinaryPath("eGroup\\eGroup.Model.binary");
	  retrieveFace.setTrainedFaceInfoPath("eGroup\\eGroup.Model.faceInfor");
	  retrieveFace.setJsonPath("output");
	  retrieveFace(retrieveFace);
	 
			
		return "autoupload";
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
	
