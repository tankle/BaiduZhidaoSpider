package com.hitsz.spider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.crypto.Data;

import com.hitsz.dao.QA;
import com.hitsz.dao.Term;
import com.hitsz.util.Constants;
import com.hitsz.util.NetUtil;

/**
 * 
 * @author JasonTan
 * E-mail: tankle120@gmail.com
 * Create on：2013-5-10 下午9:53:55 
 *
 */
public class BaiduUtil {
	
	
	List<Term> termList = new ArrayList<Term>();
	public List<Term> getTermList() {
		return termList;
	}

	public void setTermList(List<Term> termList) {
		this.termList = termList;
	}


	List<QA>	qaList = new ArrayList<QA>();
	
	public static int count = 0;
	
	
	public BaiduUtil() {
		
	}
	
	
	/**
	 * 锟劫讹拷知锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷每锟斤拷item锟斤拷始锟斤拷锟�	 */
	final String itemBegin = "<dl class=\"result-item\">";
	final String itemEnd = "</dd></dl>";
	
	/**
	 * 锟斤拷取锟斤拷锟叫碉拷item锟斤拷锟斤拷锟斤拷锟斤拷一锟斤拷list
	 * @param content
	 * @return
	 */
	public List<Term> getItemList(String content){
		
		String date = "";
		
		date = content.substring(4, Calendar.getInstance().getTime().
				toLocaleString().length());
		
		int start = 0;
		int end = 0;
		start = content.indexOf(itemBegin);
		end = content.indexOf(itemEnd, start);
		
		while(validate(start, end)){
			String tmp = content.substring(start,end);

			Term term = getOneTerm(tmp);
			
			term.setRankid(count++);
			term.setDowndate(date);
			
//			System.out.println(term);
			
			termList.add(term);
			
			start = content.indexOf(itemBegin, end) ;
			end = content.indexOf(itemEnd, start);
		}
		
        return termList;
	}

	/**
	 * 锟斤拷每锟斤拷item锟斤拷锟叫斤拷锟斤拷锟斤拷锟斤拷锟斤拷一锟斤拷Item
	 * @param item
	 * @return
	 */
	private Term getOneTerm(String item) {
		String titleHead = "log=";
		String titleMid = "\"si:1\">";
		String titleEnd = "</a>";
		
		String ansHead = "答";
		String ansMid = "<b>:</b></span>";
		String ansEnd = "</p>";
				
		String url = null;
		int id = 0;
		String title = null;
		String answer = null;
		String date = null;
		
		int start, end;
		start = end = 0;
		
		/**
		 * 提取url
		 */
		url = getUrl(item);
		url = url.substring(0,url.length()-1);
		
		/**
		 * 提取id
		 */
		id = getId(url);
		
		/**
		 * 提取title
		 */
		start = item.indexOf(titleHead);
		end = item.indexOf(titleEnd, start);
		
		/**
		 * 锟斤拷为title锟侥革拷式锟斤拷锟铰ｏ拷
		 * 		target="_blank" log="si:1">锟斤拷锟节癸拷票<em>锟斤拷锟斤拷</em>锟斤拷锟斤拷?</a>
		 * 锟斤拷si锟斤拷*锟叫碉拷*锟角变化锟侥ｏ拷锟斤拷锟斤拷只匹锟斤拷log锟斤拷然锟斤拷锟劫硷拷锟斤拷"si锟斤拷*">锟叫硷拷某锟斤拷龋锟斤拷源锟斤拷锟狡ワ拷锟�		 * title 
		 */
		title = item.substring(start + titleHead.length() + titleMid.length(),end);
		
		//锟芥换一些锟斤拷锟斤拷址锟斤拷锟�em></em>
		title = replaceMark(title);
		
		/**
		 * 锟斤拷取answer
		 */
		start = item.indexOf(ansHead);
		end = item.indexOf(ansEnd,start);
		/**
		 * answer锟侥革拷式锟斤拷锟铰ｏ拷
		 * <span class="answer-flag">锟斤拷<b>:</b></span>1 锟斤拷锟斤拷锟斤拷瞎锟较碉拷锟�		 * 			锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟较ｏ拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷母锟斤拷式锟叫�</p>  </dd>  
		 * 				<dd class="result-cate">  
		 * 匹锟斤拷头为锟斤拷锟斤拷
		 */
		start = start + ansHead.length() + ansMid.length();
		if(validate(start, end) && end < item.length()){
			answer = item.substring(start,end);
			answer = replaceMark(answer);
		}
	
		/**
		 * 锟斤拷取时锟斤拷
		 */
		date = getDate(item);
		
		
		Term term = new Term(id, url, title, answer, date);
		
//		System.out.println(term.toString());
//		System.out.println("\n");
		
		return term;
	}
	

	/**
	 * 匹锟斤拷URl
	 * @param item
	 * @return
	 */
	private String getUrl(String item) {
		String regex ="[a-zA-Z]+://[^\\s]*";;
		Pattern pa = Pattern.compile(regex);
		Matcher ma = pa.matcher(item);
		
		String url = null;
		
		if(ma.find()){
			url = ma.group();
		}	
		return url;
	}

	/**
	 * 匹锟斤拷锟斤拷锟斤拷
	 * @param item
	 * @return
	 */
	private String getDate(String item) {
		String date = null;
		String regex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
		Pattern pa = Pattern.compile(regex);
		Matcher ma = pa.matcher(item);
		
		if(ma.find()){
			date = ma.group();
		}	
		return date;
	}

	/**
	 * 锟芥换锟斤拷锟�	 * @param str
	 * @return
	 */
	private String replaceMark(String str) {
		String em = "</em>";
		String em1 = "<em>"; 
		
		str = str.replace(em, "");
		str = str.replace(em1, "");
		return str;
	}

	/**
	 * 锟斤拷取url锟斤拷锟叫碉拷id
	 * @param url
	 * @return
	 */
	private int getId(String url) {
		String regex = "\\d+";
		Pattern pa = Pattern.compile(regex);
		Matcher ma = pa.matcher(url);
		
		String id = null;
		
		if(ma.find()){
			id = ma.group();
		}
		
		return Integer.parseInt(id);
	}

	/**
	 * 锟叫断得碉拷锟斤拷锟铰憋拷锟角诧拷锟角合凤拷锟斤拷
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	private boolean validate(int start, int end) {
		if(start < 0 || end < 0)
			return false;
		if(start > end)
			return false;
		return true;
	}


	//<--------------------------------------------------------------------->//
	
	/**
	 * 锟斤拷每锟斤拷锟绞达拷越锟斤拷薪锟斤拷锟�	 * 锟斤拷锟斤拷锟斤拷
	 * 	title  question category  qId  qDate 
	 * 	answer aDate aId aLevel  aExpert 
	 * @param content
	 */
	public void parseTerm(String content) {
		
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
		 
		 title = getTitle(content);
		 
		 question = getQuestion(content);
		 
		 category = getCategory(content);
		 
		 qId = getQId(content);
		 
		 qDate = getQDate(content);
		 
		 answer = getAnswer(content);
		 
		 aDate = getADate(content);
		 
		 aId = getAID(content);
		 
		 aLevel = getLevel(content);
		 
		 aExpert = getExpert(content);
		 
//		 QA qa = new QA(title, question, category, qId,
//				 qDate, answer, aDate, aId, aLevel, aExpert);
		 
//		 qaList.add(qa);		
	}



	/**
	 * 锟矫筹拷
	 * @param content
	 * @return
	 */
	public String getExpert(String content) {
		//锟矫筹拷
		//</p>
		//>锟斤径</a>
		
		String exHead = "锟矫筹拷";
		String exEnd = "</p>";
		
		String expert = "";
		String str = null;
		int start , end;	
		
		start = content.indexOf(exHead);
		end = content.indexOf(exEnd,start);
		
		if(validate(start, end)){
			str = content.substring(start, end);
			
			String headStr = "\">";
			String endStr = "</a>";
			
			start = str.indexOf(headStr);
			end  = str.indexOf(endStr,start);
			
			while(validate(start, end)){
				expert += " "+str.substring(start + headStr.length(),end);
				start = str.indexOf(headStr,end);
				end = str.indexOf(endStr, start);
			}
		}
		
		System.out.println("expert -->" + expert);
		
		return expert;
	}

	/**
	 * 锟饺硷拷
	 * @param content
	 * @return
	 */
	public String getLevel(String content) {
		
		return null;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷id
	 * 
	 * @param content
	 * @return
	 */
	public String getAID(String content) {
		//user-name
		String uname = null;
		String answerHead = "qb-username";
		String answerEnd = "?from";
		String answerMid = "/p/";
		
		uname = getSubStr(answerHead, answerEnd, answerMid, content);

		System.out.println("uname -->"+ uname);

		return uname;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷时锟斤拷
	 * 
	 * @param content
	 * @return
	 */
	public String getADate(String content) {
		
		String aDate = null;
		
		String dateHead = "realname-time";
		String dateEnd = "</span>";
		String dateMid = "ins>";
		
		aDate = getSubStr(dateHead, dateEnd, dateMid, content);
		
		System.out.println("aDate -->"+ aDate);

		
//		//2013-04-17 12:10
//		String regex = "[0-9]{4}-[0-9]{2}-[0-9]{2}.*?[0-9]{2}:[0-9]{2}";
//		Pattern pa = Pattern.compile(regex);
//		Matcher ma = pa.matcher(aDate);
//		
//		if(ma.find()){
//			aDate = ma.group();
//		}	
//		System.out.println("aDate -->"+ aDate);
		return aDate;
	}

	public String getSubStr(String headStr, String endStr, String midStr,String content){
		String str = null;
		int start , end;	
		
		start = content.indexOf(headStr);
		end = content.indexOf(endStr,start);
		
		if(validate(start, end)){
			str = content.substring(start, end);
			start = str.indexOf(midStr);
			if(validate(start + midStr.length(), str.length()))
				str = str.substring(start + midStr.length() ,str.length());
		}
		return str;
	}
	
	/**
	 * 锟斤拷取锟斤拷锟斤拷
	 * @param content
	 * @return
	 */
	public String getAnswer(String content) {
		//aContent
		String answer = null;
		String answerHead = "aContent";
		String answerEnd = "</pre>";
		String answerMid = ">";
		
		answer = getSubStr(answerHead, answerEnd, answerMid, content);

		

		answer  = answer.replace("<br />", "\r\n");
		System.out.println("answer -->"+ answer);
		return answer;
	}

	/**
	 * 锟斤拷取锟截达拷锟斤拷时锟斤拷
	 * 
	 * @param content
	 * @return
	 */
	public String getQDate(String content) {
		
		String date = null;
		String dateHead = "ask-time";
		String dateEnd = "</span>";
		String dateMid = "ins>";
		
		date = getSubStr(dateHead, dateEnd, dateMid, content);
		
		System.out.println("Qdate -->"+ date);

//		//2013-04-17 12:10
//		String regex = "[0-9]{4}-[0-9]{2}-[0-9]{2}.*?[0-9]{2}:[0-9]{2}";
//		Pattern pa = Pattern.compile(regex);
//		Matcher ma = pa.matcher(date);
//		
//		if(ma.find()){
//			date = ma.group();
//		}	
//		System.out.println("Qdate -->"+ date);
		return date;
	}

	/**
	 * 锟截达拷锟斤拷id
	 * 
	 * @param content
	 * @return
	 */
	public String getQId(String content) {
		
		String name = null;
		String nameHead = "user-name";
		String nameEnd = "?from";
		String nameMid = "/p/";
		
		name = getSubStr(nameHead, nameEnd, nameMid, content);
		
		System.out.println("name -->"+ name);
		
		return name;
	}

	/**
	 * 锟斤拷锟斤拷
	 * 
	 * @param content
	 * @return
	 */
	public String getCategory(String content) {
		String category = null;
		String caHead = "锟斤拷锟斤拷";
		String caEnd = "</a>";
		String caMid = ">";
		category = getSubStr(caHead, caEnd, caMid, content);
				
		System.out.println("category -->"+ category);
		
		return category;
	}

	/**
	 * 锟斤拷取锟截达拷
	 * 
	 * @param content
	 * @return
	 */
	public String getQuestion(String content) {

		String question = null;
		String questionHead = "accuse=\"qContent\">";
		String questionEnd = "</pre>";
		
		question = getSubStr(questionHead, questionEnd, content);
		
		System.out.println("question -->"+ question);
		
		return question;
	}
	

	/**
	 * 锟斤拷锟斤拷谋锟斤拷锟�	 * 
	 * @param content
	 * @return
	 */
	public String getTitle(String content) {
		String title = null;
		String titleHead = "class=\"ask-title\">";
		String titleEnd = "</span>";
		
		title  = getSubStr(titleHead, titleEnd, content);
			
		System.out.println("title -->"+ title);
		
		return title;
	}
	
	private String getSubStr(String headStr, String endStr, String content){
		String str = null;
		int start , end;
		
		start = content.indexOf(headStr);
		end = content.indexOf(endStr,start);
		
		if(validate(start + headStr.length(), end)){
			str = content.substring(start + headStr.length(), end);
		}
		return str;
	}
}
