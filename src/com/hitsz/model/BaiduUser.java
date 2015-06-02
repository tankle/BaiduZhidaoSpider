package com.hitsz.model;

public class BaiduUser {

	/**
	 *  用户名字
	 */
	private String username;
	
	/**
	 * 用户等级
	 */
	private int gradeIndex;
	
	/**
	 * 用户的采纳数
	 */
	private int grAnswerNum;

	/**
	 * 用户的擅长领域
	 */
	private String carefield;
	
	/**
	 * 用户的被采纳率
	 */
	private int goodRate;

	
	@Override
	public String toString() {
		return "BaiduUser [username=" + username + "\n gradeIndex=" + gradeIndex
				+"\n grAnswerNum=" + grAnswerNum+ 
				"\n carefield=" + carefield + "\n goodRate=" + goodRate + "]";
	}

	public int getGrAnswerNum() {
		return grAnswerNum;
	}

	public void setGrAnswerNum(int grAnswerNum) {
		this.grAnswerNum = grAnswerNum;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getGradeIndex() {
		return gradeIndex;
	}

	public void setGradeIndex(int gradeIndex) {
		this.gradeIndex = gradeIndex;
	}

	public String getCarefield() {
		return carefield;
	}

	public void setCarefield(String carefield) {
		this.carefield = carefield;
	}

	public int getGoodRate() {
		return goodRate;
	}

	public void setGoodRate(int goodRate) {
		this.goodRate = goodRate;
	}
	
}
