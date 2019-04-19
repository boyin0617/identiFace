package com.example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.egroupai.engine.entity.ModelMerge;
import com.egroupai.engine.entity.ModelSwitch;
import com.egroupai.engine.entity.RetrieveFace;
import com.egroupai.engine.entity.TrainFace;
import com.egroupai.engine.util.AttributeCheck;
import com.egroupai.engine.util.CmdUtil;
import com.egroupai.engine.util.TxtUtil;
import com.example.storage.StorageService;
import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

@Controller
public class TrainController {

	@GetMapping("/showfaces")
	@ResponseBody
	public List<String> selecttenpic(){
		System.out.println("trainface");
		String path = "D:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\outputface\\" ;
		File file = new File(path);
		String[] allfiles = file.list();
		List<String> urllist = new ArrayList<>();
		int count = 0;
		
		for(int i = allfiles.length-1; i >= 0;i--) {
			System.out.println(allfiles[i]);
			System.out.println(path+allfiles[i]);
			
			Path sourcePath = Paths.get(path+allfiles[i]);
			Path destinationPath = Paths.get("D:\\Git\\repository\\identiFace\\upload-dir\\"+allfiles[i]);

			try {
				Files.copy(sourcePath, destinationPath);
			} catch (FileAlreadyExistsException e) {
				// destination file already exists
			} catch (IOException e) {
				// something else went wrong
				e.printStackTrace();
			}
		    
		    count++;
		    
				if(count == 10) {
//					is.close();
					break;
				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
		}
		return urllist;
	}
                
}


