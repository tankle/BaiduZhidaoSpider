package com.hitsz.dao;

import java.io.Serializable;

public class Item implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7868017331860962546L;

	/**
	 * 网页排序id
	 */
	private int rankid;
	
	/**
	 * 网页下载时间
	 */
	private String downdate;
	
	/**
	 * 序号
	 */
	private int id;
	/**
	 * 网址
	 */
	private String url;
	/**
	 * 标题
	 */

	private String title;
	
	/**
	 * 问句答案
	 *
	 */
	private String answer;
	
	/**
	 * 问句提问时间
	 */
	private String date;
	
	
	@Override
	public String toString() {
		return "Term [id=" + id + "\r\n"+
				"rankid="+rankid+"\r\n"+
				"downdate=" + downdate +"\r\n"+
				"url=" + url +"\r\n"+
				"title=" + title+ "\r\n"
				+ "answer=" + answer +"\r\n" 
				+"date=" + date + "]";
	}

	public Item(int id, String url, String title, String answer, String date) {
		super();
		this.id = id;
		this.url = url;
		this.title = title;
		this.answer = answer;
		this.date = date;
	}

	public Item() {
	}

	public String getDowndate() {
		return downdate;
	}

	public void setDowndate(String downdate) {
		this.downdate = downdate;
	}

	public int getRankid() {
		return rankid;
	}

	public void setRankid(int rankid) {
		this.rankid = rankid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	
}
