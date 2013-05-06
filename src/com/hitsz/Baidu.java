package com.hitsz;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.hitsz.util.NetUtil;

/**
 * 
 * @author Jack_Tan
 * 
 */
public class Baidu {

	private static String keywords = "股市行情";
	private static String str1 = "/search?word=";
	private static String str2 = "&lm=0&rn=10&sort=0&ie=gbk&pn=";
	private static BaiduUtil bdu = new BaiduUtil();
	static String baiduFile = "resource" + File.separator + "data.txt";
	static String answerFile = "resource" + File.separator + "answer.txt";

	public static void main(String[] args) {

		 Test1();
		// Test2();
		//Test3();

	}

	public static String readFileByLines(String fileName) {
		File file = new File(fileName);
		String str = null;
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				str += tempString;
				// 显示行号
				System.out.println("line " + line + ": " + tempString);
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

	private static void Test3() {
		String content = readFileByLines("resource" + File.separator + "test.txt");
		
		bdu.Test(content);
	}

	private static void Test2() {
		String url = " <dt class=\"result-title\" alog-alias=\"result-title-0\">"
				+ "<a href=\"http://zhidao.baidu.com/question/541916153.html \""
				+ "target=\"_blank"
				+ "log=\"si:1\">现在股票<em>行情</em>怎样?</a>  </dt>"
				+ "<dd class=\"result-info\">"
				+ "<p><span class=\"answer-flag\">答<b>:</b></span>"
				+ "<em>行情</em><dd class=\"result-cate\">  "
				+ " <span alog-group=\"result-userinfo\""
				+ ">    2011-08-20    <span class="
				+ "\"reply-uname\">回答者<b>:&nbsp;</b> ";

		String regex = "[a-zA-Z]+://[^\\s]*";
		;
		Pattern pa = Pattern.compile(regex);
		Matcher ma = pa.matcher(url);

		String id = null;

		if (ma.find()) {
			id = ma.group();
		}
		System.out.println(id);
	}

	private static void Test1() {
		/*
		 * 设置代理
		 */
		NetUtil.setProxy();

		int num = 0;
		// http://zhidao.baidu.com/search?lm=0&rn=10&pn=0&fr=search&ie=gbk&word=股市行情&f=sug

		/**
		 * 下载LIMIT条问句检索结果item
		 */
		while (num < BaiduUtil.LIMIT) {
			getHtml(num);
			num += 10;
		}

		outList();

		saveList(baiduFile,bdu.termList);

		bdu.parseTerm();
		
		outQAList();

		saveList(answerFile, bdu.qaList);
	}

	private static void outQAList() {
		for (int i = 0; i < bdu.termList.size(); i++) {
			String content = "The " + i + "th(s) qa is : \n"
					+ bdu.qaList.toString() + "\n";
			System.out.println(content);
		}		
	}

	/**
	 * 保存百度检索的得到的list到文件中
	 */
	private static void saveList(String fileName , List list) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName, true);

			for (int i = 0; i < list.size(); i++) {
				String content = "\nThe " + i + "th(s) item is : \n"
						+ list.get(i).toString() + "\n";
				fw.write(content);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试，用来输出获得的所有的回答list
	 */
	private static void outList() {

		for (int i = 0; i < bdu.termList.size(); i++) {
			String content = "The " + i + "th(s) item is : \n"
					+ bdu.termList.get(i).toString() + "\n";
			System.out.println(content);
		}

	}

	/**
	 * 
	 * @param num
	 */
	private static void getHtml(int num) {

		String url = BaiduUtil.URLHEAD + str1 + keywords + str2 + num;

		System.out.println(url);

		String content = null;
		try {
			content = bdu.getHtml(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println(content);
		if(null == content){
			System.out.println("Get the Html content Error!!!");
			return;
		}
		bdu.getItem(content);
	}

}
