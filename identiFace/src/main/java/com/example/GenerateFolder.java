package com.example;

import java.io.File;
 
public class GenerateFolder {
	public static boolean mkDirectory(String path) {
		File file = null;
		try {
			file = new File(path);
			if (!file.exists()) {
				return file.mkdirs();
			}
			else{
				return false;
			}
		} catch (Exception e) {
		} finally {
			file = null;
		}
		return false;
	}
 
	public static void main(String[] args) {
		String mkDirectoryPath = "D:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\123123123";
		if (mkDirectory(mkDirectoryPath)) {
			System.out.println(mkDirectoryPath + "建立完毕");
		}
		else{
			System.out.println(mkDirectoryPath + "建立失败！此目录或许已经存在！");
		}
	}
 
}
