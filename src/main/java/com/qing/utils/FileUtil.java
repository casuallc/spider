package com.qing.utils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtil {

	public static void appendToFile(List<String> lines, String root, String ...path) throws Exception {
		Path p = Paths.get(root, path);
		Files.write(p, lines, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
	}
	
	public static List<String> inputStreamToStringList(InputStream is) throws Exception {
		List<String> list = new ArrayList<>();
		try(Scanner sc = new Scanner(is)) {
			list.add(sc.nextLine());
		}
		return list;
	}
}
