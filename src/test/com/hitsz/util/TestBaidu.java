package test.com.hitsz.util;


import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.hitsz.Baidu;

public class TestBaidu {
	static Baidu baidu = null;
	
	
	@BeforeClass
	public static void create(){
		baidu = new Baidu();
	}
	
	@Test
	public void testDownloadPages() {
		
		
		List<String> keywords = baidu.getKeywords();
		
	
		for(String keyword : keywords){
			baidu.downLoadPages(keyword);
			
		}
	
	}
	
	@Test
	public void testParsePage(){
		List<String> keywords = baidu.getKeywords();
		
		for(String keyword : keywords){
			baidu.parsePage(keyword);	
		}
	}
	
	@Test
	public void testDownLoadTerm(){
		List<String> keywords = baidu.getKeywords();
		
		for(String keyword : keywords){
			baidu.parsePage(keyword);	
			
			baidu.downLoadTerm();
		}
	}
	
	
	@Test
	public void testParseQAPage(){
		List<String> keywords = baidu.getKeywords();
		
		for(String keyword : keywords){
			baidu.parsePage(keyword);	
			
			baidu.parseQAPage();
		}
	}
	
	@Test
	public void testAll(){
		List<String> keywords = baidu.getKeywords();
		
		
		for(String keyword : keywords){
			baidu.downLoadPages(keyword);
			
		}
		
		for(String keyword : keywords){
			baidu.parsePage(keyword);	
		}
		
	}

}
