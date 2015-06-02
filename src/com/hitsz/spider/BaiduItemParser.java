package com.hitsz.spider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hitsz.model.Item;
import com.hitsz.util.Log;

/**
 * 
 * @author JasonTan
 * E-mail: tankle120@gmail.com
 * Create on：2013-5-10 下午9:53:55 
 *
 */
public class BaiduItemParser {
	
	
	List<Item> itemList = new ArrayList<Item>();



//	List<QA>	qaList = new ArrayList<QA>();
	
	public static int count = 0;
	
	
	public BaiduItemParser() {
		
	}
	
	public List<Item> getTermList() {
		return itemList;
	}

	public void setTermList(List<Item> termList) {
		this.itemList = termList;
	}
	
	/**
	 * 
	 * 每个item结构大致如下：
	 * 解析每个网页的item-result存入到一个list中
	 * <dl class="result-item"> 
	 * 		<dt class="result-title" alog-alias="result-title-0">  
	 * 			<a href="http://zhidao.baidu.com/question/103374317.html" target="_blank" log="si:1"><em>股票</em>是什么???</a> 
	 * 		</dt>  
	 * 		<dd class="result-info">    
	 * 			<p><span class="ask-flag">问<b>:</b></span><em>股票</em>要怎么买？是什么是基金？我对<em>股票</em>一点都不懂请高手指</p> 
	 * 			<p><span class="answer-flag">答<b>:</b></span>首先鄙视一下复制粘贴的 <em>股票</em>是一...</p>  </dd>  
	 * 		<dd class="result-cate">   
	 * 			<span alog-group="result-userinfo">   
	 * 				<span class="i-evaluate"></span>
	 * 				<span class="agree-num">97</span>   2009-06-26    
	 * 				<span class="reply-uname">回答者<b>:&nbsp;</b>    
	 * 					<a href="http://www.baidu.com/p/%CD%EA%C3%C0%B5%C4%B9%FE%C0%FB?from=zhidao" target="_blank">完美的哈利</a>    
	 *				</span>  
	 * 				<span class="num-answers">
	 * 					<a href="http://zhidao.baidu.com/question/103374317.html" target="_blank">8个回答</a>
	 * 				</span> 
	 * 			</span>   
	 * 		</dd>
	 * </dl>
	 * @param input
	 * @return
	 */
	
	public List<Item> getItemList(File input){
		Document doc = null;
		
		try {
			doc = Jsoup.parse(input, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Element downtimeElem = doc.getElementsByAttributeValue("class", "downloadtime").first();
		
		String downloadTime = downtimeElem.text();
		//<dl class="dl" data-fb="pos:dt>a,type:normal" data-rank="840:2009691234804769108">
//		<dt class="dt mb-4 line" alog-alias="result-title-0">
//		<a href="http://zhidao.baidu.com/question/2009691234804769108.html?fr=iks&amp;word=%CA%B2%C3%B4%CA%C7%B9%C9%C6%B1%3F&amp;ie=gbk" data-log="fm:as,pos:ti,si:1,st:0,title:什么是股票？为什么要买股票？" target="_blank" class="ti"><em>什么是股票</em>？为什么要买股票？</a>
//		</dt>
//		<dd class="dd answer"><i class="i-answer-text">答：</i>第一节 为<em>什么</em>要投资理财？第二节 为<em>什么</em>购买<em>股票</em>？第三节 投资<em>股票</em>的风险**************************************************************************第一节 为<em>什么</em>要投资理财？为<em>什么</em>要买<em>股票</em>？ 回答这个问题之前，先得回答另一个问题：为什...</dd>
//		<dd class="dd explain f-light" alog-group="result-userinfo">
//		<span class="mr-8">2013-12-10</span>
//		<span class="mr-8">
//		回答者:&nbsp;<a href="http://www.baidu.com/p/%B1%A8%B0%C9%D7%A8%D3%C3402?from=zhidao" target="_blank" class="f-light nod" data-log="pos:un,si:1">报吧专用402</a>
//		</span>
//		<span class="mr-8">
//		<a href="http://zhidao.baidu.com/question/2009691234804769108.html" target="_blank" class="f-light nod" data-log="pos:ans,si:1">2个回答</a>
//		</span>
//		<span class="ml-10 f-black">
//		<i class="i-agree"></i>9
//		</span>
//		</dd>
//		</dl>*/
		Elements resultItems = doc.getElementsByAttributeValue("class", "dl"); 
		Log.debug("the number items are " + resultItems.size());
		
		for(Element e :resultItems){
			
			Item item = new Item();
			
			//问句的link
			Element url = e.getElementsByTag("a").first();
			item.setUrl(url.attr("href"));
			//查询ID
			String qid = getId(item.getUrl());
			
			//不要插入重复的答案,可以通过query的id来判重
			boolean flag = false;
			for(Item tmp : itemList){
				if(tmp.getId().equals(qid)){
					flag = true;
					break;
				}
			}
			
			if(flag == true)
				continue;
			
			item.setId(qid);
			
			//下载时间
			item.setDowndate(downloadTime);
			//百度的排序id
			item.setRankid(count++);
			//名字
			Element title = e.getElementsByAttributeValue("class", "ti").first();
			//
			item.setTitle(title.text());
		
			//回答得信息
			Element resultInfo = e.getElementsByAttributeValue("class", "dd answer").first();
//			Log.debug(resultInfo.ownText());
			item.setAnswer(resultInfo.ownText());
//			Elements answer = resultInfo.getElementsByTag("p");
//			for(Element ans : answer){
//				if(ans.text().contains("答")){
//					item.setAnswer(ans.ownText());
//					break;
//				}
//			}
			//回答日期
			Element date = e.getElementsByAttributeValue("alog-group", "result-userinfo").first();
			item.setDate(date.ownText());
			
			itemList.add(item);
		}
		
		return itemList;
		
	}

	/**
	 * 从URl中匹配最后的id
	 * @param url
	 * @return
	 */
	public String getId(String url) {
		String regex = "\\d+";
		Pattern pa = Pattern.compile(regex);
		Matcher ma = pa.matcher(url);
		
		String id = null;
		
		if(ma.find()){
			id = ma.group();
		}
		
		return id;
	}
}
