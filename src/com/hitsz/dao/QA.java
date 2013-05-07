package com.hitsz.dao;

public class QA {

	/**
	 * 问句标题
	 */
	private String title;
	/**
	 * 问句描述
	 */
	private String question;
	
	

	/**
	 * 问句类别
	 */
	private String category;

	/**
	 * 提问者ID
	 */
	private String qId;

	/**
	 * 提问时间
	 */
	private String qDate;
	/**
	 * 采纳答案
	 */
	private String answer;
	/**
	 * 答案提交时间
	 */
	private String aDate;
	/**
	 * 答案者ID
	 * 
	 */
	private String aId;
	
	/**
	 * 回答者等级
	 */
	private String aLevel;
	/**
	 * 回答者的擅长
	 */
	private String aExpert;
	
	public QA(String title, String question, String category, String qId,
			String qDate) {
		super();
		this.title = title;
		this.question = question;
		this.category = category;
		this.qId = qId;
		this.qDate = qDate;
	}

	public QA(String title, String question, String category, String qId,
			String qDate, String answer, String aDate, String aId,
			String aLevel, String aExpert) {
		super();
		this.title = title;
		this.question = question;
		this.category = category;
		this.qId = qId;
		this.qDate = qDate;
		this.answer = answer;
		this.aDate = aDate;
		this.aId = aId;
		this.aLevel = aLevel;
		this.aExpert = aExpert;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getqId() {
		return qId;
	}
	public void setqId(String qId) {
		this.qId = qId;
	}
	public String getqDate() {
		return qDate;
	}
	public void setqDate(String qDate) {
		this.qDate = qDate;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getaDate() {
		return aDate;
	}
	public void setaDate(String aDate) {
		this.aDate = aDate;
	}
	public String getaId() {
		return aId;
	}
	public void setaId(String aId) {
		this.aId = aId;
	}
	public String getaLevel() {
		return aLevel;
	}
	public void setaLevel(String aLevel) {
		this.aLevel = aLevel;
	}
	public String getaExpert() {
		return aExpert;
	}
	public void setaExpert(String aExpert) {
		this.aExpert = aExpert;
	}
	
	@Override
	public String toString() {
		return "QA [title=" + title + ", \nquestion=" + question + ", \ncategory="
				+ category + ", \nqId=" + qId + ", \nqDate=" + qDate + ", \nanswer="
				+ answer + ", \naDate=" + aDate + ", \naId=" + aId + ", \naLevel="
				+ aLevel + ", \naExpert=" + aExpert + "]\n";
	}
}
