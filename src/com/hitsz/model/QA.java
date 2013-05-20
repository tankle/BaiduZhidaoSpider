package com.hitsz.model;

public class QA {

	/**
	 * 百度中的查询id，即
	 * http://zhidao.baidu.com/question/39038584.html
	 * 中的39038584这串数字
	 * 
	 */
	private String qid;

	/**
	 * 问句标题
	 */
	private String question;
	/**
	 * 问句描述
	 */
	private String description;

	/**
	 * 问句类别
	 */
	private String category;
	
	/**
	 * 提问时间
	 */
	private String questionDate;
//
//	/**
//	 * 是否被采纳的标记
//	 *  = 1 表示被采纳
//	 *  = 0 表示未被采纳
//	 */
//	private int isAdopted;
	
	/**
	 * 提问者ID
	 */
	private String questionId;

	
	/**
	 * 采纳答案
	 */
	private String answer;
	/**
	 * 答案提交时间
	 */
	private String answerDate;
	
	/**
	 * 是否是最好的回答
	 * 百度知道一般是提问者采纳了该答案，则有该标记
	 *  = 1 表示是最好的，或者提问者已经采纳
	 *  = 0 无此标记，没有被采纳
	 */
	private int isBest;
	
	/**
	 * 网页下载时间
	 */
	private String downDate;
	
	/**
	 * 回答者的一些基本信息
	 */
	private BaiduUser baiduUser;
	
	

	
	@Override
	public String toString() {
		return "QA [question=" + question + "\n description=" + description + "\n category="
				+ category + "\n questionId=" + questionId + "\n questionDate=" +
					questionDate + "\n answer="
				+ answer + "\n answerDate=" + answerDate +"\n isBest="+isBest+
				"\n downDate=" + downDate +"\n baiduUser=" + baiduUser
				+ "]";
	}

	
	public String getDownDate() {
		return downDate;
	}

	public void setDownDate(String downDate) {
		this.downDate = downDate;
	}
	
	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getQuestionDate() {
		return questionDate;
	}

	public void setQuestionDate(String questionDate) {
		this.questionDate = questionDate;
	}

//	public int getIsAdopted() {
//		return isAdopted;
//	}
//
//	public void setIsAdopted(int isAdopted) {
//		this.isAdopted = isAdopted;
//	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAnswerDate() {
		return answerDate;
	}

	public void setAnswerDate(String answerDate) {
		this.answerDate = answerDate;
	}

	public int getIsBest() {
		return isBest;
	}

	public void setIsBest(int isBest) {
		this.isBest = isBest;
	}

	public BaiduUser getBaiduUser() {
		return baiduUser;
	}


	public void setBaiduUser(BaiduUser baiduUser) {
		this.baiduUser = baiduUser;
	}


}
