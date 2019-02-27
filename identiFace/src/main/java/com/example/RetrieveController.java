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
import com.example.dao.memberDAO;

@Controller
public class RetrieveController {
	@Autowired
	memberDAO memberDAO;
	private static final String Date  = null;
//問題，沒辦法同時執行retrieveface跟getResult
	@RequestMapping("/retrieveface")
	public ModelAndView home() throws SQLException {
		
		ModelAndView model = new ModelAndView("result");
		List<Member> memberlist = new ArrayList();
//		String name = "";
//		String ENGINEPATH = "C:\\eGroupAI_FaceEngine_v3.1.0";
//		// RetrieveFace
//		RetrieveFace retrieveFace = new RetrieveFace();
//		retrieveFace.setThreshold(0.7);
//		retrieveFace.setHideMainWindow(false);
//		retrieveFace.setShowThreadWindow(true);
//		retrieveFace.setResolution("720p");
//		retrieveFace.setOutputFacePath("outputFace1");
//		retrieveFace.setOutputFramePath("outputFrame1");
//		retrieveFace.setCam("0");
//		retrieveFace.setMinimumFaceSize(100);
//		retrieveFace.setThreshold(0.7);
//		retrieveFace.setTrainedBinaryPath("eGroup\\eGroup.Model.binary");
//		retrieveFace.setTrainedFaceInfoPath("eGroup\\eGroup.Model.faceInfor");
//		retrieveFace.setJsonPath("output");
//		retrieveFace(retrieveFace);
		memberlist = GetResult.main();

//		memberlist = GetResult.main();
		

//		if(memberlist.get(i).getMemberId() == -1) {
//			model.addObject("error", "辨識到多人，請再試一次");
//			return model;
//		}
//		else if(member == null) {
//			model.addObject("error", "辨識失敗，請再試一次");
//			return model;
//		}
		model.addObject("members", memberlist);
		// 找資料庫
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			System.out.println("加载資料庫驅動");
//			String url = "jdbc:mysql://localhost:3306/project?useSSL=false&serverTimezone=UTC";// 聲明資料庫project的url
//			String user = "root";// 資料庫帳號
//			String pass = "a8s5d1f9";// 資料庫密碼
//			// 建立資料庫連接，獲得連接對象conn
//			Connection connect = DriverManager.getConnection(url, user, pass);
//			System.out.println("資料庫連接成功");

//			for(int i = 0;i < memberlist.size();i++) {
//				Statement stmt = connect.createStatement();
//				ResultSet rs = stmt.executeQuery("select * from face WHERE faceId = "+memberlist.get(i).getFaceId());
//				while (rs.next()) {
//					name = rs.getString("name");
//				}
//				model.addObject("name",name);
//			}
//			} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			System.out.print("get data error!");
//			e.printStackTrace();
//		}
		return model;
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
	
//	 @RequestMapping(value = {"/member"}, method = RequestMethod.GET)
//
//	    public Iterable<Member> RetrieveAllMember() throws SQLException{
//		 	Iterable<Member> memberlist = memberDAO.findAll();
//			return memberlist;
//		 
//	 }
}
