package com.hitsz.util;

import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
	/**
	 * 写文件的函数
	 * @param fileName 文件名
	 * @param content 写入内容
	 * @param append 是否追加
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
}
