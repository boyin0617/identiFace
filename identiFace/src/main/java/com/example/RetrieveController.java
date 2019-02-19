package com.example;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

@Controller
public class RetrieveController {

	@RequestMapping("/retrieveface")
	public ModelAndView home() throws SQLException {
		
		ModelAndView model = new ModelAndView("result");
		Member member = new Member();
		String name = "";
		
		String ENGINEPATH = "C:\\eGroupAI_FaceEngine_v3.1.0";
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
		retrieveFace.setTrainedBinaryPath("eGroup\\eGroup.Model.binary");
		retrieveFace.setTrainedFaceInfoPath("eGroup\\eGroup.Model.faceInfor");
		retrieveFace.setJsonPath("output");
		retrieveFace(retrieveFace);
		
		member = GetResult.main();
		model.addObject("member", member);
		//找資料庫
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("加载数据库驱动成功");
			String url = "jdbc:mysql://localhost:3306/project?useSSL=false&serverTimezone=UTC";// 声明数据库project的url
			String user = "root";// 数据库账号
			String pass = "a8s5d1f9";// 数据库密码
			// 建立数据库连接，获得连接对象conn
			Connection connect = DriverManager.getConnection(url, user, pass);
			System.out.println("資料庫連接成功");
			Statement stmt = connect.createStatement();
			ResultSet rs = stmt.executeQuery("select * from face WHERE faceId = "+member.getFaceId());
			while (rs.next()) {
				name = rs.getString("name");
			}
			model.addObject("name",name);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.print("get data error!");
			e.printStackTrace();
		}

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
}
