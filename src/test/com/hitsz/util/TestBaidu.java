
package test.com.hitsz.util;


import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.hitsz.model.QA;
import com.hitsz.spider.Baidu;
import com.hitsz.util.Log;
import com.hitsz.util.QADBUtil;
import com.hitsz.util.config.ConfigUtil;
import com.hitsz.util.config.Configuration;
import com.hitsz.util.config.PropNotConfigedException;

public class TestBaidu {
	static Baidu baidu = null;
	
	
	@BeforeClass
	public static void create(){
		baidu = new Baidu();
	}
	
	/**
	 * 测试下载每个问句的baidu搜索网页
	 */
	@Test
	public void testDownloadPages() {
		
		baidu.getQuerysFromDB(0);
		
		List<String> keywords = baidu.getKeywords();
		
	
		for(String keyword : keywords){
			Log.info("Downloading the pages about \"" + keywords + "\" now !\n");
			baidu.downLoadPages(keyword);
			
			QADBUtil.setQueryFinished(keyword);
			
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
		
		baidu.getQuerysFromDB(1);
		
		List<String> keywords = baidu.getKeywords();
		
		for(String keyword : keywords){
			baidu.parsePage(keyword);	
			
			baidu.saveItemListIntoDB(keyword);
			
//			baidu.saveTermList(keyword);
			
			baidu.getBdu().getTermList().clear();
		}
	}
	
	/**
	 *  从数据库qapair_list中获取所有 还没有下载过
	 *  的item的URL，然后进行下载
	 */
	@Test
	public void testDownLoadItem(){
		
		List<String> urls = QADBUtil.getUrlsFromQAPair(0);
		int count = 0;
		for(String url : urls){
			
			/**
			 *  如果某个网页由于频繁的访问baidu而被禁止，
			 *  就是线程休息5分钟在再访问
			 */
			int i = 1;
			int oneDelay = 1000;
			int tenDelay = 1000;
			int maxDelay = 1000;
			try {
				oneDelay = ConfigUtil.strToInt(ConfigUtil.getPropValue("oneDelay"))  * 1000;
				tenDelay = ConfigUtil.strToInt(ConfigUtil.getPropValue("tenDelay"))  * 1000;
				maxDelay = ConfigUtil.strToInt(ConfigUtil.getPropValue("maxDelay")) * 1000;
			} catch (PropNotConfigedException e1) {
				e1.printStackTrace();
			}
			/**
			 * 循环下载某个下载不成功的网页
			 * 知道大于maxdelay
			 */
			 while( !baidu.downLoadItem(url)){
				 Log.info("Will sleep "+ i * oneDelay +" seconds and try download again !!!");
				 try {
					 if(i * oneDelay > maxDelay)
						 break;
					Thread.sleep(i * oneDelay);
					i++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				 
			 }
			
			 /**
			  * 设置finished标记位
			  * 
			  */
			 QADBUtil.setQAPairResultFinished(url);
			 
			/**
			 *   每总共下载 10个网页就暂停 tenDelay秒，
			 *   每下载 1 个网页暂停 onedelay 秒
			 *   主要是防止 baidu 封锁 IP 或者禁止访问
			 */
			try {
				if(10 == count){
					Thread.sleep( tenDelay);
					count = 0;
				}
				else{
					Thread.sleep( oneDelay);
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
	
		List<String> urls = QADBUtil.getUrlsFromQAPair(0);
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
