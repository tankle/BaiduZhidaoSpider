package com.hitsz;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.experimental.theories.Theories;

import com.hitsz.dao.Term;
import com.hitsz.util.Constants;
import com.hitsz.util.DBUtil;
import com.hitsz.util.FileUtil;
import com.hitsz.util.NetUtil;

/**
 * 
 * @author JasonTan
 * E-mail: tankle120@gmail.com
 * Create on：2013-5-6 下午10:16:52 
 *
 */
public class Baidu {

//	private static String keywords = "股市行情";
	private static String URL_MID_STR1 = "/search?word=";
	private static String URL_MID_STR2 = "&nocluster&lm=0&rn=10&sort=0&ie=gbk&pn=";
	
	private static BaiduUtil bdu = new BaiduUtil();
	
	static String baiduFile = "resource" + File.separator + "data.txt";
	static String answerFile = "resource" + File.separator + "answer.txt";

	/**
	 * 问句list
	 */
	public List<String> keywords = new ArrayList<String>();
	
	/**
	 * 对每个问答对网页进行解析
	 */
	public void parseQAPage(){
		
		bdu.qaList.clear();
		
		for(int i=0; i<bdu.termList.size(); i++){
			int id = bdu.termList.get(i).getId();
			
			String fileName = getFileName(id+"", "qapair");
			
			String content = FileUtil.readFileByLines(fileName);
			
			bdu.parseTerm(content);
			
		}
	}
	
	/**
	 * 下载每个我问答对网页
	 */
	public void downLoadTerm(){

		 for(int i=0; i < bdu.termList.size(); i++){
			 Term term = bdu.termList.get(i);
			 String url = term.getUrl();
			 if(null == url)
				 continue;
			 
			 String content = null;
			 try {
				NetUtil netutil = NetUtil.getInstance();
				
				content = netutil.getHtml(url);
				
				String fileName = getFileName(term.getId()+"", "qapair");
				System.out.println("FileName -->" + fileName);
				FileUtil.writeFile(fileName, content, true);
				
				try {
					Thread.sleep(2 * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
		 }		 

	}

	/**
	 * 现在问句keyword的10个网页
	 * 
	 * @param keyword
	 */
	public void downLoadPages(final String keyword) {
		/*
		 * 设置代理
		 */
		if(Constants.ISPROXY)
			NetUtil.setProxy();
	
//		new Thread(){
//	
//			@Override
//			public void run() {			
				int num  = 0;
				
				while(num < Constants.LIMITS){
					
					String content = getHtml(keyword,num * 10);
					
//					if(null == content){
//						try {
//							sleep(3 * 60 * 1000);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						continue;
//					}
					
					saveContent(keyword, num * 10,content);
					
					num++;
					
					/**
					 * 
					 */
					try {
						Thread.sleep(3 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
//			}
			
//		}.start();
		
		
//
//		outList();
//
//		saveList(baiduFile,bdu.termList);
//
//		bdu.parseTerm();
//		
//		outQAList();
//
//		saveList(answerFile, bdu.qaList);
	}
	
	
	/**
	 * 解析keyword对应的第num个网页
	 * 
	 * @param keyword
	 * @param num
	 */
	public void parsePage(String keyword){
		
		BaiduUtil.count = 0;
		String fileName = null;
		bdu.termList.clear();
		
		
		for(int i=0; i<10; i++){
			fileName = getFileName(keyword, i * 10);
			
			String content = FileUtil.readFileByLines(fileName);
			
			bdu.getItemList(content);
		}
		
		fileName = getFileName(keyword,"term");
		System.out.println("fileName--->" + fileName);
		saveTermList(fileName, bdu.termList);
	}
	
	/**
	 * 
	 * @param keyword
	 * @return
	 */
	private String getFileName(String keyword, String folder) {
		String fileName = "resource" + File.separator + folder + File.separator+ 
				keyword +".txt";
		return fileName;
	}

	/**
	 *  保存文件的格式： keywords---i page(s).html
	 * @param keywords2
	 * @param i
	 * @param content
	 */
	protected  void saveContent(String keyword, int i, String content) {
		
		String fileName = getFileName(keyword,i);
		
		System.out.println("Save the html's content. The filename is -->" + fileName);
		
		content = Calendar.getInstance().getTime().toLocaleString() +"\r\n" + content;
		
		FileUtil.writeFile(fileName, content, false);
	}

	/**
	 * 根据keyword和i生成一个文件名
	 * 
	 * @param keyword
	 * @param i
	 * @return
	 */
	private String getFileName(String keyword, int i) {
		String fileName = "resource" + File.separator +"baidu" + File.separator+ 
				keyword + "--->" + i +" page(s).html";
		return fileName;
	}

	/**
	 * 
	 */
	private  void outQAList() {
		for (int i = 0; i < bdu.qaList.size(); i++) {
			String content = "The " + i + "th(s) qa is : \n"
					+ bdu.qaList.toString() + "\n";
			System.out.println(content);
		}		
	}

	/**
	 * 
	 * @param fileName
	 * @param list
	 */
	private  void saveTermList(String fileName , List<Term> list) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName, true);

			for (int i = 0; i < list.size(); i++) {
				String content = "\nThe " + i + "th(s) item is : \n"
						+ list.get(i).toString() + "\n";
			//	System.out.println("Writing ..." + content);
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

//	/**
//	 * 
//	 */
//	private  void outList() {
//
//		for (int i = 0; i < bdu.termList.size(); i++) {
//			String content = "The " + i + "th(s) item is : \n"
//					+ bdu.termList.get(i).toString() + "\n";
//			System.out.println(content);
//		}
//
//	}

	/**
	 * 根据关键字和网页第几页来下载对应的网页内容
	 * @param keyword 
	 * @param num
	 */
	private String getHtml(String keyword, int num) {

		/**
		 * 对keyword进行编码
		 */
		try {
			keyword  = URLEncoder.encode(keyword,Constants.ENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final String url = Constants.URLHEAD_BAIDU + URL_MID_STR1 + keyword + URL_MID_STR2 + num;

		System.out.println("The url is "+url);

		String content = null;
		
		try {
			NetUtil netutil = NetUtil.getInstance();
			
			content = netutil.getHtml(url);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		// System.out.println(content);
		if(null == content){
			System.out.println("Get the Html content Error!!!");
			return null;
		}
		
		return content;
	}
	
	
	/**
	 * 
	 * @param keywords
	 */
	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}
	/**
	 * 
	 * @return
	 */
	public List<String> getKeywords(){
		
		getKeywordsList();
		
		return this.keywords;
	}
	
	/**
	 * 从数据库中读取所有问句
	 */
	private void getKeywordsList() {
		
		Connection conn = DBUtil.getDBConnection();	
		Statement stmt = DBUtil.getStatment(conn);

		String sql = "select * from query";
		
		ResultSet rs = null;
		
		try {
			rs = stmt.executeQuery(sql);
						
			while(rs.next()){
				String query = rs.getString("query");

				keywords.add(query);
				
				System.out.println("id is " + rs.getString("id") + " queryid is "+
						rs.getString(2) + " query is " + rs.getString("query"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(stmt != null){			
					stmt.close();
					stmt = null;
				}				
				if(null != conn){
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
