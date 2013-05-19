package test.com.hitsz.util;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.hitsz.spider.BaiduUtil;
import com.hitsz.util.FileUtil;

public class TestBaiduUtil {
	 String title = null;
	 String question = null;
	 String category = null;
	 String qId = null;
	 String qDate = null;
	 String answer = null;
	 String aDate = null;
	 String aId = null;
	 String aLevel = null;
	 String aExpert = null;
	
	@Override
	public String toString() {
		return "TestBaiduUtil [title=" + title + ", question=" + question
				+ ", category=" + category + ", qId=" + qId + ", qDate="
				+ qDate + ", answer=" + answer + ", aDate=" + aDate + ", aId="
				+ aId + ", aLevel=" + aLevel + ", aExpert=" + aExpert + "]";
	}

//	@Test
//	public void test() {
//		String content = FileUtil.readFileByLines("resource" + File.separator 
//				+ "test.txt");
//		
//		BaiduUtil bdu = new BaiduUtil();
//		 
//		 title = bdu.getTitle(content);
//		 
//		 question = bdu.getQuestion(content);
//		 
//		 category = bdu.getCategory(content);
//		 
//		 qId = bdu.getQId(content);
//		 
//		 qDate = bdu.getQDate(content);
//		 
//		 answer = bdu.getAnswer(content);
//		 
//		 aDate = bdu.getADate(content);
//		 
//		 aId = bdu.getAID(content);
//		 
//		 aLevel = bdu.getLevel(content);
//		 
//		 aExpert = bdu.getExpert(content);
//		
//		// System.out.println(toString());
//	}

}
