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

import com.egroupai.engine.entity.ModelMerge;
import com.egroupai.engine.entity.ModelSwitch;
import com.egroupai.engine.entity.RetrieveFace;
import com.egroupai.engine.entity.TrainFace;
import com.egroupai.engine.util.AttributeCheck;
import com.egroupai.engine.util.CmdUtil;
import com.egroupai.engine.util.TxtUtil;

@Controller
public class TrainController {
	@PostMapping("/trainface")
    public ModelAndView TrainForm(@ModelAttribute("name") String name) {
	       ModelAndView model = new ModelAndView("main");

	       model.addObject("name", name);

	       return model;

	    }
    @ResponseBody
    	void home() throws SQLException {
		// TODO Auto-generated method stub
		String ENGINEPATH = "C:\\eGroupAI_FaceEngine_v3.1.0";
		
		TrainFace trainFace = new TrainFace();
		trainFace.setModelExist(false);
		trainFace.setTrainListPath("list.txt");
		trainFace.setModelPath("eGroupTest\\eGroupTest.Model");
		trainFace(trainFace);
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
