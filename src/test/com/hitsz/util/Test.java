package test.com.hitsz.util;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test {

	/**
	 * 测试正则表达式
	 */

	@org.junit.Test
	public void test() {

		String content = " <dt class=\"result-title\" alog-alias=\"result-title-0\">"
				+ "<a href=\"http://zhidao.baidu.com/question/541916153.html \""
				+ "target=\"_blank"
				+ "log=\"si:1\"><em>你好</em></a>  阿三的过</dt>"
				+ "<dd class=\"result-info\">"
				+ "<p><span class=\"answer-flag\"><b>:</b></span>"
				+ "<em></em><dd class=\"result-cate\">  "
				+ " <span alog-group=\"result-userinfo\""
				+ ">    2011-08-20    <span class="
				+ "\"reply-uname\">名字<b>:&nbsp;</b> ";

		String regex = "[a-zA-Z]+://[^\\s]*";
		
		Pattern pa = Pattern.compile(regex);
		Matcher ma = pa.matcher(content);

		String url = null;

		if (ma.find()) {
			url = ma.group();
		}
		System.out.println(url);

	}

	@org.junit.Test
	public void testDate(){
		System.out.println(Calendar.getInstance().getTime().toLocaleString());
	}
}
