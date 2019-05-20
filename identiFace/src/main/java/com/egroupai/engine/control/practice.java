package com.egroupai.engine.control;

import java.awt.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.dom4j.Branch;
import org.springframework.context.annotation.Bean;

import io.opencensus.stats.Aggregation.Count;

public class practice {

	public static void main(String[] args) {
		//123123123
//		Scanner input = new Scanner(System.in);
//		int time = input.nextInt();
//		int shift = input.nextInt();
//		input.nextLine();
//		String[] encryption = null;
//		String answer = "";
//		ArrayList<String> letter = new ArrayList(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
//				"s", "t", "u", "v", "w", "x", "y", "z"));
//		for(int i = 0;i < time;i++) {
//			answer = input.nextLine();
//			encryption = answer.split("");
//			int trueindex;
//			for(int j = 0;j < encryption.length;j++) {
//			int index = letter.indexOf(encryption[j]);
//			if(index-shift < 0) {
//				trueindex = letter.size() - Math.abs(index - shift);
//			} else {
//				trueindex = index-shift;
//			}
//			encryption[j] = letter.get(trueindex);
//			}
//			for(String e : encryption) {
//				System.out.print(e);
//			}
//			System.out.println("");
//		}
		
		//2019-05-14 02:12:22	Fail	FileNotFound	C:\eGroupAI_FaceEngine_CPU_V3.1.3_SN\headshot\533.jpg	boyin[No]1
		//2019-05-14 02:13:29	Pass	C:\eGroupAI_FaceEngine_CPU_V3.1.3_SN\headshot\5.jpg	boyin[No]1
		int success = 0;
		try {
		File file = new File("C:\\eGroupAI_FaceEngine_CPU_V3.1.3_SN\\Log.TrainResultCPU.eGroup");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		int count = 0;
		String trainresult = null;
		while ((trainresult = br.readLine())!=null) {
			if(count == 5) {
				break;
			}
			System.out.println(trainresult);
			if(trainresult.contains("Pass")) {
				System.out.println("Pass");
				success += 1;
			}
			count++;
		}
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
