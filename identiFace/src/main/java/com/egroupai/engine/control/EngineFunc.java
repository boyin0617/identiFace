package com.egroupai.engine.control;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import com.egroupai.engine.entity.ModelMerge;
import com.egroupai.engine.entity.ModelSwitch;
import com.egroupai.engine.entity.RetrieveFace;
import com.egroupai.engine.entity.TrainFace;
import com.egroupai.engine.util.AttributeCheck;
import com.egroupai.engine.util.CmdUtil;
import com.egroupai.engine.util.TxtUtil;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.sql.*;
/**
 * EngineControl Func
* @author 雿�� Daniel
* @date 2018撟�8���10� 銝��10:54:20 
* @version 
* @description:
 */
public class EngineFunc{
	static protected String ENGINEPATH = "C:\\eGroupAI_FaceEngine_v3.1.0";
	public static void main(String args[]) throws IOException, SQLException {
		String  binarypath = "";
		String  faceInforpath = "";
		
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
			ResultSet rs = stmt.executeQuery("select * from face WHERE imageId = 0");
			while (rs.next()) {
				binarypath = rs.getString("binary_path");
				faceInforpath = rs.getString("faceInfor_path");
				}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.print("get data error!");
			e.printStackTrace();
		}
		
		
		// RetrieveFace
		RetrieveFace retrieveFace = new RetrieveFace();
		retrieveFace.setThreshold(0.7);
		retrieveFace.setHideMainWindow(false);
		retrieveFace.setShowThreadWindow(true);
		retrieveFace.setResolution("720p");
		retrieveFace.setOutputFacePath("outputFace1");
		retrieveFace.setOutputFramePath("outputFrame1");
		retrieveFace.setCam("0");
		retrieveFace.setMinimumFaceSize(100);
		retrieveFace.setThreshold(0.7);
		retrieveFace.setTrainedBinaryPath(binarypath);
		retrieveFace.setTrainedFaceInfoPath(faceInforpath);
		retrieveFace.setJsonPath("output");
		retrieveFace(retrieveFace);
		
//讀取json檔(日期為當天日期)
//		Date date = new Date();
//	    SimpleDateFormat todaydate = new SimpleDateFormat("yyyy-MM-dd");
//	    String today = todaydate.format(date);       
//	    System.out.println(today);
//		FileReader file = new FileReader("C:\\eGroupAI_FaceEngine_v3.1.0\\output."+today+".egroup.json");
//		BufferedReader br = new BufferedReader(file);
//		String line;
//		int count = 0;
//		ArrayList<String> mylist = new ArrayList();
//		while((line = br.readLine()) != null) {
//			if(line.contains("hasFound")||line.contains("personId")) {
//				mylist.add(line);
//			}
//			if(line.contains("hasFound")) {
//				count++;
//			}
//		}
//		for(int i = 0;i < mylist.size();i++) {
//			System.out.println(mylist.get(i));
//		}
//		
//		System.out.println("辨識結果");
//		System.out.println("辨識成功"+count+"次");
		// TrainFace
/*		TrainFace trainFace = new TrainFace();
		trainFace.setModelExist(false);
		trainFace.setTrainListPath("list.txt");
		trainFace.setModelPath("eGroup5\\eGroup.Model");
		trainFace(trainFace);
*/

//		// ModelMerge
//		ModelMerge modelMerge = new ModelMerge();
//		modelMerge.setListPath("ModelList.egroup.List");
//		modelMerge.setTrainedBinaryPath("eGroup\\eGroup_merged.binary");
//		modelMerge.setTrainedFaceInfoPath("eGroup\\eGroup_merged.faceInfor");
//		modelMerge(modelMerge);
		
//		// ModelSwitch
//		ModelSwitch modelSwitch = new ModelSwitch();
//		modelSwitch.setNewModelBinaryPath(ENGINEPATH+"/eGroup5/eGroup.Model.binary");
//		modelSwitch.setNewModelFaceInfoPath(ENGINEPATH+"/eGroup5/eGroup.Model.faceInfor");
//		modelSwitch.setSwitchFilePath(ENGINEPATH+"/Singal_For_Model_Switch.txt");
//		modelSwitch(modelSwitch);
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
	
	private static boolean retrieveFace(RetrieveFace retrieveFace){		
		boolean flag = false;
		// init func 
		retrieveFace.generateCli();
		if(retrieveFace.getCommandList()!=null){
			final CmdUtil cmdUtil = new CmdUtil();
			flag = cmdUtil.cmdProcessBuilder(retrieveFace.getCommandList());				
		}
		return flag;
	}
	
	private static boolean modelMerge(ModelMerge modelMerge){
		boolean flag = false;
		// init func 
		modelMerge.generateCli();
		if(modelMerge.getCommandList()!=null){
			final CmdUtil cmdUtil = new CmdUtil();
			flag = cmdUtil.cmdProcessBuilder(modelMerge.getCommandList());				
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
