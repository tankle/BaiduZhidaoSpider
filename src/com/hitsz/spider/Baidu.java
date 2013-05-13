package com.hitsz.spider;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.hitsz.dao.QA;
import com.hitsz.dao.Item;
import com.hitsz.util.Constants;
import com.hitsz.util.DBUtil;
import com.hitsz.util.FileUtil;
import com.hitsz.util.NetUtil;
import com.hitsz.util.QADBUtil;

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
	
	private  BaiduUtil bdu = new BaiduUtil();
	
	public  BaiduUtil getBdu() {
		return bdu;
	}

	public  void setBdu(BaiduUtil bdu) {
		this.bdu = bdu;
	}

	static String baiduFile = "resource" + File.separator + "data.txt";
	static String answerFile = "resource" + File.separator + "answer.txt";

	/**
	 * 问句list
	 */
	public List<String> keywords = null;
	
//	/**
//	 * 对每个问答对网页进行解析
//	 */
//	public void parseQAPage(){
//		
//		bdu.qaList.clear();
//		
//
//		for(int i=0; i<bdu.termList.size(); i++){
//			int id = bdu.termList.get(i).getId();
//			
//			String fileName = getFileName(id+"", "qapair");
//			
//			String content = FileUtil.readFileByLines(fileName);
//			
//			bdu.parseTerm(content);	
//		}
//	}
	
	/**
	 * 从数据库中查询出所有的问题的id
	 * 然后小哦难过qapair文件夹下读取相应的文件进行分析，返回一个QA
	 * 必须保证qapair下的id.html必须存在
	 * 
	 */
	public List<QA> parserQAPages(){
		List<QA> qalist = new LinkedList<QA>();
		
		BaiduParser parser = new BaiduParser();
		
		List<String> ids = QADBUtil.getTermIDsFromDB();
		
		for(String id : ids){
			String fileName = getFileName(id, "html","qapair");
			
			File input = new File(fileName);
			
			System.out.println("parsering "+fileName +"\n");
			
			QA qa = null;
			try {
				qa = parser.getQA(input);
			} catch (IOException e) {
				System.err.println("The file: " + fileName +" is not found!!!");
			}
			
			if(null != qa){
				qa.setId(id);
				qalist.add(qa);
			}
		}
//		saveQAList(qalist);
		
		return qalist;
	}
	
	@Deprecated
	private void saveQAList(List<QA> qalist) {
		String filename ;
		FileWriter fw = null ;
		int i=0;
		for(QA qa: qalist){
			filename = getFileName(qa.getId(),"txt","qa");
			
			try {
				fw = new FileWriter(filename, false);
				fw.write(qa.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(null != fw)
						fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 下载每个问答对网页
	 */
	@Deprecated
	public void downLoadItem(){
		 for(int i=0; i < bdu.itemList.size(); i++){
			 Item item = bdu.itemList.get(i);
			 String url = item.getUrl();
			 if(null == url)
				 continue;
			 
			 String content = null;
			 try {
				NetUtil netutil = NetUtil.getInstance();
				
				content = netutil.getHtml(url);
				
				String fileName = getFileName(item.getId()+"", "html","qapair");
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
	 *   根据url来下载对应的问答对网页，
	 *   然后保存到qapair文件夹下
	 * @param url
	 */
	public void downLoadItem(String url) {
		 String content = null;
		 
		NetUtil netutil = NetUtil.getInstance();
		
		try {
			content = netutil.getHtml(url);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//保存网页
		content = getFilehead() + content;
		String fileName = getFileName(bdu.getId(url)+"", "html","qapair");
		System.out.println("save the " + fileName);
		FileUtil.writeFile(fileName, content, true);

	}

	/**
	 * 现在问句keyword的10个网页
	 * 
	 * @param keyword
	 */
	public void downLoadPages(final String keyword) {

		int num = 0;

		while (num < Constants.LIMITS) {

			String content = getHtml(keyword, num * 10);

			saveContent(keyword, num * 10, content);

			num++;

			try {
				Thread.sleep(3 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

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
		
		for(int i=0; i<10; i++){
			fileName = getFileName(keyword, i * 10);
			
//			String content = FileUtil.readFileByLines(fileName);
			
//			bdu.getItemList(content);
			
			File input = new File(fileName);
			
			bdu.getItemList(input);
		}
		
	}
	
	/**
	 *  根据前缀和后缀，以及文件夹名字来获得一个相对文件的名字
	 *  
	 * @param prefix
	 * @param suffix
	 * @param folder
	 * @return
	 */
	private String getFileName(String prefix, String suffix, String folder) {
		
		prefix = prefix.replaceAll("/", "|");
		
		String fileName = "resource" + File.separator + folder + File.separator+ 
				prefix + "." + suffix;
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
		
		content = getFilehead() + content;
		
		FileUtil.writeFile(fileName, content, false);
	}

	/**
	 * 每个下载的文件头部是这样的
	 * 
	 * <div class="version">1.0</div>
	 * <div class="downloadtime">Mon Apr 05 14:22:53 CST 2010</div>
	 */
	private String getFilehead() {
		String head = "<div class=\"version\">1.0</div>";
		head = head +"<div class=\"downloadtime\">" + Calendar.getInstance().getTime().toLocaleString() +"</div>\r\n";
		return head;
		
	}


	
	/**
	 * 根据keyword和i生成一个文件名
	 * 
	 * @param keyword
	 * @param i
	 * @return
	 */
	private String getFileName(String keyword, int i) {
		/**
		 * 将问句中的问号替换掉
		 * 因为windows下的文件名不能含有<,>,(,),?,*等
		 */
		if(Constants.OS_NAME.startsWith("win") || Constants.OS_NAME.startsWith("Win")){
			keyword = keyword.replaceAll("\\?", "");
		}
		
		String fileName = "resource" + File.separator +"baidu" + File.separator+ 
				keyword + "---" + i +" page(s).html";
		return fileName;
	}
//
//	/**
//	 * 
//	 */
//	private  void outQAList() {
//		for (int i = 0; i < bdu.qaList.size(); i++) {
//			String content = "The " + i + "th(s) qa is : \n"
//					+ bdu.qaList.toString() + "\n";
//			System.out.println(content);
//		}		
//	}

	/**
	 * 将每个Term的list存入数据库中
	 * @param query 
	 */
	public void saveItemListIntoDB(String query){
		if(null != bdu.itemList)
			QADBUtil.saveTermList(bdu.itemList,query);
	}
	
	/**
	 * 
	 * @param fileName
	 * @param list
	 */
	public  void saveItemList(String fileName ) {
		
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName, true);

			for (int i = 0; i < bdu.itemList.size(); i++) {
				String content = "\nThe " + i + "th(s) item is : \n"
						+ bdu.itemList.get(i).toString() + "\n";
				System.out.println("Writing ..." + content);
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

		return this.keywords;
	}
	
	/**
	 * 从数据库中读取问题List
	 */
	public void getQuerysFromDB(){
		
		this.keywords = QADBUtil.getQueryListFromDB();
	}




}
