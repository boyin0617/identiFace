package com.egroupai.engine.control;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class practice {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int time = input.nextInt();
		int shift = input.nextInt();
		input.nextLine();
		String[] encryption = null;
		String answer = "";
		ArrayList<String> letter = new ArrayList(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
				"s", "t", "u", "v", "w", "x", "y", "z"));
		for(int i = 0;i < time;i++) {
			answer = input.nextLine();
			encryption = answer.split("");
			int trueindex;
			for(int j = 0;j < encryption.length;j++) {
			int index = letter.indexOf(encryption[j]);
			if(index-shift < 0) {
				trueindex = letter.size() - Math.abs(index - shift);
			} else {
				trueindex = index-shift;
			}
			encryption[j] = letter.get(trueindex);
			}
			for(String e : encryption) {
				System.out.print(e);
			}
			System.out.println("");
		}
	}
}