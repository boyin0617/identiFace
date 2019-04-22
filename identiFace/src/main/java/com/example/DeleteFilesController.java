package com.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.function.delFolderTxtFUNC;

@Controller
public class DeleteFilesController {
	@GetMapping("/deletefiles")
	@ResponseBody
	public void deletefiles(){
		String folderpath = "D:\\Git\\repository\\identiFace\\upload-dir\\";
		delFolderTxtFUNC.delAllFile(folderpath);
		System.out.println("刪除成功");
	}
}
