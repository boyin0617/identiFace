package com.egroupai.engine.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class test {

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		List<String> test = new ArrayList<>();
		List<String> test2 = new ArrayList<>();
		Map<String, List<String>> map = new HashMap<>();
		map.put("boyin", test);
		map.put("ivy", test2);
		map.get("boyin").add("hiiiii");
		map.get("boyin").add("hiiiiiboyin");
		map.get("ivy").add("nooooooo");
		
		for(String key : map.keySet()){
			List<String> value = map.get(key);
			System.out.println(key);
			for(int i = 0;i < value.size();i++) {
				System.out.print(value.get(i)+" ");
			}
			System.out.println("");
	}
}
}
