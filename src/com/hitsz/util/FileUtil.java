package com.hitsz.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
	/**
	 * 
	 * @param fileName
	 * @param content
	 * @param append
	 */
	public static void writeFile(String fileName, String content, boolean append){
		try {
			FileWriter fw = new FileWriter(fileName, append);
			
			fw.write(content);
			fw.write("\r\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public  static String readFileByLines(String fileName) {
		File file = new File(fileName);
		String str = null;
		BufferedReader reader = null;
		try {
			System.out.println("Read file " + fileName);
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			//
			while ((tempString = reader.readLine()) != null) {
				str += tempString;
		//		System.out.println("line " + line + ": " + tempString);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return str;
	}
}
