package com.qing.analysis.spider.parse;

import java.util.Scanner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qing.analysis.b.B;
import com.qing.analysis.b.Up;

public class BUpParser {

	public static void main(String args[]) throws Exception {
		BUpParser main = new BUpParser();
		main.run();
	}
	
	
	void run() throws Exception {
		try (Scanner in = new Scanner(getClass().getClassLoader().getResourceAsStream("temp.txt"));) {
			in.useDelimiter("\\A");
			String line = in.nextLine();
			
//			Object obj = JSON.parse(line);
//			print(obj);
//			
		}
		
	}
	
	void print(Object obj) {
		if(obj instanceof JSONObject) {
			JSONObject temp = (JSONObject)obj;
			temp.forEach((key, value) -> {
				System.out.println(key + ": " + value);
//				System.out.println("private String " + key + ";");
				print(value);
			});
		}
	}
	
	
}
