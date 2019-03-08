package com.example;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.egroupai.engine.control.GetResult;
import com.egroupai.engine.entity.ModelMerge;
import com.egroupai.engine.entity.ModelSwitch;
import com.egroupai.engine.entity.RetrieveFace;
import com.egroupai.engine.entity.TrainFace;
import com.egroupai.engine.util.AttributeCheck;
import com.egroupai.engine.util.CmdUtil;
import com.egroupai.engine.util.TxtUtil;
import com.example.entity.Customer;
import com.example.entity.Member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.example.dao.memberDAO;

@Controller
public class RetrieveController {
	@Autowired
	memberDAO memberDAO;
	private static final String Date  = null;
	
	@RequestMapping("/getresult")
	public String home() throws SQLException {
		
		ModelAndView model = new ModelAndView("result");
		List<Member> memberlist = new ArrayList();
		Gson gson = new Gson();
		ArrayList<String> resultstring = new ArrayList<>();
		String name = "";
		String ENGINEPATH = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN";

		GetResult.main();

//		model.addObject("members", memberlist);

		return "redirect:/";
	}
	
	@RequestMapping("/retrieveface")
	public String RetrieveFace() {
		
		RetrieveFace retrieveFace = new RetrieveFace();
		retrieveFace.setThreshold(0.7);
		retrieveFace.setHideMainWindow(false);
		retrieveFace.setShowThreadWindow(true);
		retrieveFace.setResolution("720p");
		retrieveFace.setOutputFacePath("outputFace");
		retrieveFace.setOutputFramePath("outputFrame");
		retrieveFace.setCam("0");
		retrieveFace.setMinimumFaceSize(100);
		retrieveFace.setThreshold(0.7);
		retrieveFace.setTrainedBinaryPath("eGroup\\eGroup.Model.binary");
		retrieveFace.setTrainedFaceInfoPath("eGroup\\eGroup.Model.faceInfor");
		retrieveFace.setJsonPath("output");
		retrieveFace(retrieveFace);
		return "redirect:/";
		
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

	@GetMapping("/selectmember")
	public ModelAndView SelectMember(@RequestParam(value = "memberId", required = false) Long memberId ) throws SQLException {
		ModelAndView model = new ModelAndView("selectmember");
		Date birth = new Date();
		String name ="";
		String phone = "";
		String email = "";
		model.addObject("memberId", memberId);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("加载資料庫驅動");
			String url = "jdbc:mysql://localhost:3306/project?useSSL=false&serverTimezone=UTC";// 聲明資料庫project的url
			String user = "root";// 資料庫帳號
			String pass = "a8s5d1f9";// 資料庫密碼
			// 建立資料庫連接，獲得連接對象conn
			Connection connect = DriverManager.getConnection(url, user, pass);
			System.out.println("資料庫連接成功");
			Statement stmt = connect.createStatement();
			ResultSet rs = stmt.executeQuery("select * from member WHERE member_Id = " + memberId);
			while (rs.next()) {
				name = rs.getString("name");
				phone = rs.getString("phone");
				email = rs.getString("email");
				birth = rs.getDate("birth");
			}
				model.addObject("name",name);
				model.addObject("phone",phone);
				model.addObject("email",email);
				model.addObject("birth",birth);
			
		} catch (ClassNotFoundException e) {
			System.out.print("get data error!");
			e.printStackTrace();
		}
		return model;

	}
}
	
