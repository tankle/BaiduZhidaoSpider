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
	private String questionId;

	/**
	 * 提问时间
	 */
	private String questionDate;
	/**
	 * 采纳答案
	 */
	private String answer;
	/**
	 * 答案提交时间
	 */
	private String answerDate;
//	/**
//	 * 答案者ID
//	 * 
//	 */
//	private String aId;
//	
//	/**
//	 * 回答者等级
//	 */
//	private String aLevel;
//	/**
//	 * 回答者的擅长
//	 */
//	private String aExpert;
	
	private BaiduUser baiduUser;
	
	
	public QA() {
		
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

	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}


	public String getQuestionId() {
		return questionId;
	}


	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}


	public String getQuestionDate() {
		return questionDate;
	}


	public void setQuestionDate(String questionDate) {
		this.questionDate = questionDate;
	}


	public String getAnswerDate() {
		return answerDate;
	}


	public void setAnswerDate(String answerDate) {
		this.answerDate = answerDate;
	}


	@Override
	public String toString() {
		return "QA [title=" + title + "\n question=" + question + "\n category="
				+ category + "\n questionId=" + questionId + "\n questionDate=" +
					questionDate + "\n answer="
				+ answer + "\n answerDate=" + answerDate + "\n baiduUser=" + baiduUser
				+ "]";
	}


	public BaiduUser getBaiduUser() {
		return baiduUser;
	}


	public void setBaiduUser(BaiduUser baiduUser) {
		this.baiduUser = baiduUser;
	}


}
