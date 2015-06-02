package com.hitsz.spider;

import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hitsz.model.BaiduUser;
import com.hitsz.model.QA;
import com.hitsz.util.Log;

/**
 * 
 * @author JasonTan
 * E-mail: tankle120@gmail.com
 * Create on: 2013-5-14下午9:17:06
 *
 */
public class BaiduPairParser {

	/**
	 * 
	 * @param input
	 * @return
	 * @throws IOException
	 */
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
		//下载时间
		Element downloadtime = doc.getElementsByAttributeValue("class", "downloadtime").first();
		
		qa.setDownDate(downloadtime.text());
		
		Element askDiv = article.getElementsByAttributeValue("id", "wgt-ask").first();
		//问题标题
		qa.setQuestion(getAskTitle(askDiv));
		//问题提问时间
		qa.setQuestionDate(getQuestionDate(askDiv));
		//提问者id
		qa.setQuestionId(getQuestionID(askDiv));
		//问题的分类
		qa.setCategory(getCategory(askDiv));
		//问题的详细描述
		qa.setDescription(getQuestionContent(askDiv));
		
		//////////////////////获得回答的信息
		Element answerDiv = article.getElementsByAttributeValueContaining("class", "bd answer")
				.first();
		//还没有回答
		if(null == answerDiv){
			return qa;
		}
		//回答的时间
		qa.setAnswerDate(getAnswerDate(answerDiv));
		//回答的内容
		qa.setAnswer(getAnswerContent(answerDiv));
		
		////////////// 获取回答者信息的javascript标签
		/**
		 * 标签内容大致如下：
		 * 将等号后面的提取出来，然后一Json的形式解析
		 * F.context('answers')['1387195381'] = {uid:"620215951",
		 * imId:"8fbed0a1b6c7d7d3bdabbdabf724",isBest:"1",id:"1387195381",userName:"小肚子将将",
		 * userNameEnc:"%D0%A1%B6%C7%D7%D3%BD%AB%BD%AB",user:{sex:"1",iconType:"6",
		 * gradeIndex:"4",grAnswerNum:"45",isAuth:"0",authTitle:"",
		 * isUserAdmin:"0",userAdminLevel:"",userAdminTitle:"",
		 * isFamous:"0",isMaster:"0",goodRate:"88"},isAnonymous:"0",
		 * isCurrentUser:"0",mapUrl:"",refer:"",replyAskNum:"",
		 * threadId:"5548781525",hasComment:"1",qid:"550375818",raid:"",recommendCanceled:"0"};

		 */
		Elements javascripts = article.getElementsByTag("script");
		String info = null;
		for(Element e : javascripts){
			if(e.data().contains("F.context('answers')")){
				info = e.data().substring(e.data().indexOf("=")+1).trim();
				//因为有些标签中含有这样的内容，而JSONObject视为不合法的
				//<a href=\x22http:\/\/baike.baidu.com\/view\/2085.htm\x22 target=\x22_blank\x22>
				//	http:\/\/baike.baidu.com\/view\/2085.htm<\/a>
				// \x22是 无法解析的
				info = info.replaceAll("\\\\x22", "'");
				break;
			}
		}
		if(null == info){
			Log.info("Can't find the script bookmark");
		}
		JSONObject jsonObj = null;
		try{
			Log.info(info.toString());
			jsonObj = new JSONObject(info);
		}catch(JSONException e){
			Log.info(e.getMessage());
		}
		
		System.out.println(jsonObj.toString());
		//是否是被提问者采纳的标记
		qa.setIsBest(getIsBest(jsonObj));
		
		//回答者的基本信息
		BaiduUser user = new BaiduUser();
		//用户名字
		String name = getAnswerUsernName(jsonObj);
		user.setUsername(name);
		if(name.equals("热心网友")){
			//用户等级
			user.setGradeIndex(0);
			 //用户的采纳数
			user.setGrAnswerNum(0);
			//用户的采纳率
			user.setGoodRate(0);
			//用户的擅长领域
			user.setCarefield("暂未定制");
		}else{
			JSONObject userInfo =  jsonObj.getJSONObject("user");
			//用户等级
			user.setGradeIndex(getGradeIndex(userInfo));
			 //用户的采纳数
			user.setGrAnswerNum(getGrAnswerNum(userInfo));
			//用户的采纳率
			user.setGoodRate(getUserGoodRate(userInfo));
			//用户的擅长领域
			user.setCarefield(getUserCarefield(userInfo));
		}
		
//		//用户名字
//		user.setUsername(getAnswerUserName(answerDiv));
//		//用户等级
//		user.setGradeIndex(getUserLevel(answerDiv));
//		//用户采纳率
//		user.setGoodRate(getUserGoodRate(answerDiv));
//		//用户的擅长领域
//		user.setCarefield(getUserCarefield(answerDiv));
		
		qa.setBaiduUser(user);
		
		return qa;
	}
	
	/**
	 * 获取用户擅长领域的字符串
	 * 
	 * @param userInfo
	 * @return
	 */
	private String getUserCarefield(JSONObject userInfo) {
		JSONArray carefields = null;
		try{
			carefields = userInfo.getJSONArray("carefield");
		}catch(JSONException e){
			Log.info("the field 'carefield' is not exist !");
		}
		String careStr = "";
		if(null != carefields){
			for(int i=0; i < carefields.length(); i++){
				JSONObject o = carefields.getJSONObject(i);
				careStr += o.getString("cname")+" ";
			}
		}else{
			careStr = "暂未定制";
		}
		return careStr;
	}
	/**
	 * 用户的采纳率
	 * @param userInfo
	 * @return
	 */
	private int getUserGoodRate(JSONObject userInfo) {
		int goodRate = 0;
        try{
        	goodRate = userInfo.getInt("goodRate");
        }catch(JSONException e){
        	Log.info("the field 'goodRate' is not exist !");
        }       	        
		return goodRate;	
	}
	/**
	 * 用户的回答数
	 * 
	 * @param userInfo
	 * @return
	 */
	private int getGrAnswerNum(JSONObject userInfo) {
		int grAnswerNum = 0;
        try{
        	grAnswerNum = userInfo.getInt("grAnswerNum");
        }catch(JSONException e){
        	Log.info("the field 'grAnswerNum' is not exist !");
        }       	        
		return grAnswerNum;
	}
	/**
	 * 用户的等级
	 * 
	 * @param userInfo
	 * @return
	 */
	private int getGradeIndex(JSONObject userInfo) {
		int gradeIndex = 0;
        try{
        	gradeIndex = userInfo.getInt("gradeIndex");
        }catch(JSONException e){
        	Log.info("the field 'gradeIndex' is not exist !");
        }       	        
		return gradeIndex;
	}

	/**
	 * 获取用户的名字
	 * @param jsonObj
	 * @return
	 */
	private String getAnswerUsernName(JSONObject jsonObj) {
		String userName = null;
        try{
        	userName = jsonObj.getString("userName");
        }catch(JSONException e){
        	Log.info("the field 'userName' is not exist !");
        	userName = "热心网友";
        }
		return userName;
	}

	/**
	 * 获取是否采纳标识位
	 * @param jsonObj
	 * @return
	 */
	private int getIsBest(JSONObject jsonObj) {
		int isBest = 0;
        try{
        	isBest = jsonObj.getInt("isBest");
        }catch(JSONException e){
        	Log.info("the field 'isBest' is not exist !");
        }       	        
		return isBest;
	}
//	/**
//	 * 
//	 * @param answerDiv
//	 * @return
//	 */
//	private String getUserCarefield(Element answerDiv) {
//		/**
//		 * <p class="carefield"><span>擅长：</span><span>暂未定制</span></p>
//		 */
//		Element carefield = answerDiv.getElementsByAttributeValue("class", "carefield").first();
//		String carefieldStr = "";
//		if(null != carefield){
//			Elements carefieldSpan = carefield.getElementsByTag("a");
//			for(Element e: carefieldSpan){
//				carefieldStr += e.ownText() + " ";
//			}
//		}
//		if("" == carefieldStr)
//			return "暂未定制";
//		return carefieldStr;
//	}
//
//	/**
//	 * 
//	 * @param answerDiv
//	 * @return
//	 */
//	private String getUserGoodRate(Element answerDiv) {
//		/**
//		 * <span class="ml-10">采纳率91%</span>
//		 */
//		Element goodRate = null;
//		Elements spans  = answerDiv.getElementsByTag("span");
//		for(Element e : spans){
//			if(e.text().contains("采纳率")){
//				goodRate = e;
//				break;
//			}
//		}
//		if(null == goodRate)
//			return "null";
//		return goodRate.text();
//	}
//
//	/**
//	 * 
//	 * @param answerDiv
//	 * @return
//	 */
//	private String getUserLevel(Element answerDiv) {
//		Element level = null;
//		
//		/**
//		 * <span>十八级</span>
//		 */
//		Elements es  = answerDiv.getElementsByTag("span");
//		for(Element e : es){
//			if(e.text().contains("级")){
//				level = e;
//				break;
//			}
//		}
//		
//		/**
//		 * <a class="f-aid" href="http://www.baidu.com/search/zhidao_help.html#如何选择头衔"
//		 *  target="_blank">五级</a>
//		 * 
//		 */
//		if(null == level){
//			es.clear();
//			es = answerDiv.getElementsByTag("a");
//			for(Element e : es){
//				if(e.text().contains("级")){
//					level = e;
//					break;
//				}
//			}
//		}
//		if(null == level)
//			return "null";
//		return level.text();	
//	}
//
//	/**
//	 * 
//	 * @param answerDiv
//	 * @return
//	 */
//	private String getAnswerUserName(Element answerDiv) {
//		Element username = answerDiv.getElementsByAttributeValue("class", "user-name").first();
//		if(null == username)
//			return "热心网友";
//		
//		return username.text();		
//	}

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
	 * 如果找不到user-name标签，则返回 “热心网友”
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
