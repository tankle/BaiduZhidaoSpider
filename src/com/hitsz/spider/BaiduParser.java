package com.hitsz.spider;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hitsz.dao.BaiduUser;
import com.hitsz.dao.QA;

public class BaiduParser {

	public QA getQA(File input) throws IOException{
		Document doc = null;
		
		doc = Jsoup.parse(input, "UTF-8");
		
		Element article = doc.getElementById("qb-content");
		
		if(null == article){
			article = doc.getElementsByAttributeValueContaining("class", "qb-content").first();
			if(null == article )
				return null;
		}
		
		QA qa = new QA();
		
		Element askDiv = article.getElementsByAttributeValue("id", "wgt-ask").first();
		
		qa.setTitle(getAskTitle(askDiv));
		
		qa.setQuestionDate(getQuestionDate(askDiv));
		
		qa.setQuestionId(getQuestionID(askDiv));
		
		qa.setCategory(getCategory(askDiv));
		
		qa.setQuestion(getQuestionContent(askDiv));
		
		//////////////////////
		Element answerDiv = article.getElementsByAttributeValueContaining("class", "bd answer")
				.first();
		
		if(null == answerDiv){
			return qa;
		}
		
		qa.setAnswerDate(getAnswerDate(answerDiv));
		
		qa.setAnswer(getAnswerContent(answerDiv));
		
		BaiduUser user = new BaiduUser();
		
		user.setUsername(getAnswerUserName(answerDiv));
		
		user.setLevel(getUserLevel(answerDiv));
		
		user.setGoodRate(getUserGoodRate(answerDiv));
		
		user.setCarefield(getUserCarefield(answerDiv));
		
		qa.setBaiduUser(user);
		
		return qa;
	}
	/**
	 * 
	 * @param answerDiv
	 * @return
	 */
	private String getUserCarefield(Element answerDiv) {
		/**
		 * <p class="carefield"><span>擅长：</span><span>暂未定制</span></p>
		 */
		Element carefield = answerDiv.getElementsByAttributeValue("class", "carefield").first();
		String carefieldStr = "";
		if(null != carefield){
			Elements carefieldSpan = carefield.getElementsByTag("a");
			for(Element e: carefieldSpan){
				carefieldStr += e.ownText() + " ";
			}
		}
		if("" == carefieldStr)
			return "暂未定制";
		return carefieldStr;
	}

	/**
	 * 
	 * @param answerDiv
	 * @return
	 */
	private String getUserGoodRate(Element answerDiv) {
		/**
		 * <span class="ml-10">采纳率91%</span>
		 */
		Element goodRate = null;
		Elements spans  = answerDiv.getElementsByTag("span");
		for(Element e : spans){
			if(e.text().contains("采纳率")){
				goodRate = e;
				break;
			}
		}
		if(null == goodRate)
			return "null";
		return goodRate.text();
	}

	/**
	 * 
	 * @param answerDiv
	 * @return
	 */
	private String getUserLevel(Element answerDiv) {
		Element level = null;
		
		/**
		 * <span>十八级</span>
		 */
		Elements es  = answerDiv.getElementsByTag("span");
		for(Element e : es){
			if(e.text().contains("级")){
				level = e;
				break;
			}
		}
		
		/**
		 * <a class="f-aid" href="http://www.baidu.com/search/zhidao_help.html#如何选择头衔"
		 *  target="_blank">五级</a>
		 * 
		 */
		if(null == level){
			es.clear();
			es = answerDiv.getElementsByTag("a");
			for(Element e : es){
				if(e.text().contains("级")){
					level = e;
					break;
				}
			}
		}
		if(null == level)
			return "null";
		return level.text();	
	}

	/**
	 * 
	 * @param answerDiv
	 * @return
	 */
	private String getAnswerUserName(Element answerDiv) {
		Element username = answerDiv.getElementsByAttributeValue("class", "user-name").first();
		if(null == username)
			return "热心网友";
		
		return username.text();		
	}

	/**
	 * 
	 * @param answerDiv
	 * @return
	 */
	private String getAnswerContent(Element answerDiv) {
		Element ansConDiv = answerDiv.getElementsByAttributeValue("class", "line content").first();
		
		Elements answerPre = ansConDiv.getElementsByAttributeValue("accuse", "aContent");
		
		String ansContent = "";
		for(Element e : answerPre){
			ansContent += e.text() + " ";
		}
		return ansContent;
	}

	/**
	 * 
	 * @param answerDiv
	 * @return
	 */
	private String getAnswerDate(Element answerDiv) {
		/**
		 * 时间是在第一个span里
		 */
		Element date = answerDiv.getElementsByTag("span").first();
		if(null != date)
			return date.text();
		return "null";		
	}

	/**
	 * 
	 * @param askDiv
	 * @return
	 */
	private String getQuestionContent(Element askDiv) {
		Elements content = askDiv.getElementsByTag("pre");
		
		String contentStr = "";
		
		if(null != content){
			for(Element e : content){
					contentStr += e.text() + "\n";
			}
		}
		
		return contentStr;
	}

	/**
	 * 
	 * @param askDiv
	 * @return
	 */
	private String getCategory(Element askDiv) {
		Elements cates = askDiv.getElementsByAttributeValueContaining("class", "classinfo");
		String cateStr = "";
		for(Element e : cates){
			for(Element child : e.children())
				cateStr += child.ownText()+ " ";
		}
		return cateStr;
	}

	/**
	 * 
	 * @param askDiv
	 * @return
	 */
	private String getQuestionID(Element askDiv) {

		Element username = askDiv.getElementsByClass("user-name").first();
		
		if(null == username){
			return "热心网友";
		}

		return username.text();
	}

	/**
	 * 
	 * @param askDiv
	 * @return
	 */
	private String getQuestionDate(Element askDiv) {
		Element date = askDiv.getElementsByAttributeValueContaining("class", "ask-time").first();
		if(null == date)
			return "null";
		return date.text();
	}

	/**
	 * 
	 * @param askDiv
	 * @return
	 */
	private String getAskTitle(Element askDiv) {
		Element title = askDiv.getElementsByAttributeValue("class", "ask-title").first();
		
		return title.text();
	}
}
