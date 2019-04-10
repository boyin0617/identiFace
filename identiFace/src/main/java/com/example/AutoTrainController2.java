package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.egroupai.engine.entity.TrainFace;
import com.egroupai.engine.util.CmdUtil;
import com.example.GetFileSize;
import com.example.TrainingController;
import com.example.delFolderTxtFUNC;
import com.example.GenerateFolder;

@Controller
public class AutoTrainController2 {

	private Connection con;
	private Statement st;
	private ResultSet rs;

	static String folderPath = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN";
	public static final String uploadingDir = folderPath + "/face/";

	@RequestMapping("/autoupload")
	public String uploading(Model model) {
		File file = new File(uploadingDir);

		return "autoupload";
	}

	@RequestMapping(value = "/autoupload", method = RequestMethod.POST)
	public void uploadingPost(@RequestParam("uploadingFiles") MultipartFile[] uploadingFiles) throws Exception {

		String txtpath = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\list.txt";
		// 連接資料庫
		Class.forName("com.mysql.cj.jdbc.Driver");
		System.out.println("加載資料庫驅動");
		String url = "jdbc:mysql://localhost:3306/project?serverTimezone=UTC";// 聲明資料庫project的url
		String user = "root";// 資料庫帳號
		String pass = "a8s5d1f9";// 資料庫密碼
		// 建立資料庫連結，獲得連結對象conn
		con = DriverManager.getConnection(url, user, pass);
		st = con.createStatement();

		// 資料庫抓值SELECT * FROM `Users` ORDER BY UserID DESC LIMIT 1
		String query = "select * from face order by  faceId desc limit 1 ";
		rs = st.executeQuery(query);
		System.out.println("Records from DB");
		System.out.println("faceId " + " imageId " + " name " + "age " + "gender");
		while (rs.next()) {
			String faceid = rs.getString("faceId");
			String imageid = rs.getString("imageId");
			String name = rs.getString("name");
			String age = rs.getString("age");
			String gender = rs.getString("gender");

			System.out.println(faceid + "         " + imageid + "       " + name + " " + age + "  " + gender);
			int faceId = Integer.parseInt(faceid) + 1;
			System.out.println("last value" + faceId);

			GenerateFolder.mkDirectory("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\" + Integer.toString(faceId));
			// uploadedFile.transferTo(file);

			// 抓檔案個數
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

			File file = new File("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\face\\");
			String[] list = file.list();
			int count = 1;
			for (int i = 0; i < list.length; i++) {
				System.out.println(list[i]);

				// 寫入TXT檔值
				String content1 = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\face\\" + list[i] + "	" + "yan" + "[No]"
						+ count + "\r\n";
				boolean flagcreate = CreateTxtPath.createTxtFile(txtpath);
				boolean flagwrite = CreateTxtPath.writeTxtFileAppend(txtpath, content1);
				String result = CreateTxtPath.readTxtFile(txtpath);
				System.out.println(i);
				count += 1;
			}
		}

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

}
