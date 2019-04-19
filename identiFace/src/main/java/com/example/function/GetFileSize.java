package com.example.function;

import java.io.File;
import java.text.DecimalFormat;
import java.io.FileInputStream;

public class GetFileSize {
	public long getFileSizes(File f) throws Exception {// 取得檔案大小
		long s = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			s = fis.available();
		} else {
			f.createNewFile();
			System.out.println("檔不存在");
		}
		return s;
	}

// 遞迴
	public long getFileSize(File f) throws Exception// 取得資料夾大小
	{
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	public String FormetFileSize(long fileS) {// 轉換檔案大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public long getlist(File f) {// 遞迴求取目錄檔個數
		long size = 0;
		File flist[] = f.listFiles();
		size = flist.length;
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getlist(flist[i]);
				size--;
			}
		}
		return size;

	}

	public static void main(String args[]) {
		GetFileSize g = new GetFileSize();
		long startTime = System.currentTimeMillis();
		try {
			long l = 0;
			String path = "C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\uploadingDir";
			File ff = new File(path);
			if (ff.isDirectory()) { // 如果路徑是資料夾的時候
				System.out.println("檔個數 " + g.getlist(ff));
				System.out.println("目錄");
				l = g.getFileSize(ff);
				System.out.println(path + "目錄的大小為：" + g.FormetFileSize(l));
			} else {
				System.out.println(" 檔個數 1");
				System.out.println("檔");
				l = g.getFileSizes(ff);
				System.out.println(path + "檔的大小為：" + g.FormetFileSize(l));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("總共花費時間為：" + (endTime - startTime) + "毫秒...");
	}
}