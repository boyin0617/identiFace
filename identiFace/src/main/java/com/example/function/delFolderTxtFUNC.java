package com.example.function;

import java.io.File;

public class delFolderTxtFUNC {
	/**
	 * 刪除資料夾
	 * 
	 * @param filePathAndNameString 資料夾路徑及名稱 如c:/fqf
	 * @param fileContentString
	 * @return boolean
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 刪除完裡面所有內容
			String filePath = folderPath;
//			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 刪除空資料夾
			System.out.println("刪除空資料夾");

		} catch (Exception e) {
			System.out.println("刪除資料夾操作出錯");
			e.printStackTrace();

		}

	}
	public static void deltxt(String txtpath) {
		System.out.println("deltxt路徑"+txtpath);
		File file = new File(txtpath);
		if (file.delete()) {
			System.out.println(file.getName() + " 刪除成功!");
		} else {
			System.out.println("刪除失敗");
		}

	}
	public static void delAllFile(String path) {
		try {
		System.out.println("delallfile路徑"+path);
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
//				System.out.println("111"+path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
//				System.out.println("222"+path + File.separator + tempList[i]);
			}
			
			if (temp.isFile()) {
				System.out.println(temp);
				if(temp.delete()) {
					System.out.println("temp.delete()~~~~~~~~~~~~~");
				}
				else {
					System.out.println("not deleted");
				}
			}
//			if (temp.isDirectory()) {
//				delAllFile(path + "/" + tempList[i]);// 先刪除資料夾裡面的檔
//				System.out.println("刪除成功");
//				delFolder(path + "/" + tempList[i]);// 再刪除空資料夾
//			}
		}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		}
	
	public static void deleteAllFilesOfDir(File path) {
		if (null != path) {
			if (!path.exists())
				return;
			if (path.isFile()) {
				boolean result = path.delete();
				int tryCount = 0;
				while (!result && tryCount++ < 10) {
					System.gc(); // 回收资源
					result = path.delete();
				}
			}
			File[] files = path.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					deleteAllFilesOfDir(files[i]);
				}
			}
		}
	}
	
	public static boolean makeFolder(String path) {
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
	public static void main(String args[]) {
		File path = new File("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\face");
		String txtpath ="C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\list.txt";
		deleteAllFilesOfDir(path);
	}
}
