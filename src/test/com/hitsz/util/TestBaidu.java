package test.com.hitsz.util;


import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.hitsz.dao.QA;
import com.hitsz.spider.Baidu;
import com.hitsz.util.QADBUtil;

public class TestBaidu {
	static Baidu baidu = null;
	
	
	@BeforeClass
	public static void create(){
		baidu = new Baidu();
		
		baidu.getQuerysFromDB();
	}
	
	/**
	 * 测试下载每个问句的搜索网页
	 */
	@Test
	public void testDownloadPages() {
		List<String> keywords = baidu.getKeywords();
		
	
		for(String keyword : keywords){
			baidu.downLoadPages(keyword);
			
		}
	
	}
	
	/**
	 * 
	 * 分析每个问题的搜索网页，并将每个结果存入到数据库中
	 * 对每个问题解析完之后，需要对TermList清空
	 * 
	 */
	@Test
	public void testParsePage(){
		
		List<String> keywords = baidu.getKeywords();
		
		for(String keyword : keywords){
			baidu.parsePage(keyword);	
			
			baidu.saveItemListIntoDB(keyword);
			
//			baidu.saveTermList(keyword);
			
			baidu.getBdu().getTermList().clear();
		}
	}
	
	/**
	 *  从数据库qapair_list中获取所有的Term的URL，然后进行下载
	 */
	@Test
	public void testDownLoadItem(){
		
		List<String> urls = QADBUtil.getUrlsFromQAPair();
		int count = 0;
		for(String url : urls){
			
			/**
			 *  如果某个网页由于频繁的访问baidu而被禁止，
			 *  就是线程休息5分钟在再访问
			 */
			int i = 1;
			 while( !baidu.downLoadItem(url)){
				 try {
					Thread.sleep(i * 5 * 60 * 1000);
					i++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				 		
			 }
			
			/**
			 *   每总共下载 10个网页就暂停 30秒，
			 *   每下载 1 个网页暂停 3 秒
			 *   主要是防止 baidu 封锁 IP
			 */
			try {
				if(10 == count){
					Thread.sleep( 60 * 1000);
					count = 0;
				}
				else{
					Thread.sleep( 5 * 1000);
					count++;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 分析每个问答对的网页
	 * 并保存到数据库中
	 */
	@Test
	public void testParseQAPage(){
		
		List<QA> qas = baidu.parserQAPages();
		
		QADBUtil.saveQAList(qas);
		
		
	}
	
	/**
	 * 从数据库中读取问题，到将所有结果保存到数据库中
	 */
	@Test
	public void testAll(){
		
		List<String> keywords = baidu.getKeywords();

		for(String keyword : keywords){
			baidu.downLoadPages(keyword);
			
			baidu.parsePage(keyword);	
			
			baidu.saveItemListIntoDB(keyword);
			
			baidu.getBdu().getTermList().clear();
			
		}
	
		List<String> urls = QADBUtil.getUrlsFromQAPair();
		int count = 0;
		for(String url : urls){
			
			/**
			 *  如果某个网页由于频繁的访问baidu而被禁止，
			 *  就是线程休息5分钟在再访问
			 */
			int i = 1;
			 while( !baidu.downLoadItem(url)){
				 try {
					Thread.sleep(i * 5 * 60 * 1000);
					i++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				 
			 }
			
			/**
			 *   每总共下载 10个网页就暂停 30秒，
			 *   每下载 1 个网页暂停 3 秒
			 *   主要是防止 baidu 封锁 IP
			 */
			try {
				if(10 == count){
					Thread.sleep( 60 * 1000);
					count = 0;
				}
				else{
					Thread.sleep( 5 * 1000);
					count++;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		List<QA> qas = baidu.parserQAPages();
		
		QADBUtil.saveQAList(qas);
		
	}

}
