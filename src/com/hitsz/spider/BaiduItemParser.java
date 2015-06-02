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
		
		Elements resultItems = doc.getElementsByAttributeValue("class", "result-item"); 
		for(Element e :resultItems){
			Item item = new Item();
			//下载时间
			item.setDowndate(downloadTime);
			//百度的排序id
			item.setRankid(count++);
			//名字
			Element title = e.getElementsByAttributeValue("class", "result-title").first();
			//
			item.setTitle(title.text());
			//问句的link
			Element url = e.getElementsByTag("a").first();
			item.setUrl(url.attr("href"));
			item.setId(getId(item.getUrl()));
			
			Element resultInfo = e.getElementsByAttributeValue("class", "result-info").first();
			Elements answer = resultInfo.getElementsByTag("p");
			for(Element ans : answer){
				if(ans.text().contains("答")){
					item.setAnswer(ans.ownText());
					break;
				}
			}
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
	public int getId(String url) {
		String regex = "\\d+";
		Pattern pa = Pattern.compile(regex);
		Matcher ma = pa.matcher(url);
		
		String id = null;
		
		if(ma.find()){
			id = ma.group();
		}
		
		return Integer.parseInt(id);
	}
}
