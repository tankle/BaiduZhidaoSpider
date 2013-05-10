package com.hitsz.dao;

public class BaiduUser {

	/**
	 *  用户名字
	 */
	private String username;
	
	/**
	 * 用户等级
	 */
	private String level;
	
	
	/**
	 * 用户的擅长领域
	 */
	private String carefield;
	
	/**
	 * 用户的被采纳率
	 */
	private String goodRate;

	
	@Override
	public String toString() {
		return "BaiduUser [username=" + username + "\n level=" + level
				+ "\n carefield=" + carefield + "\n goodRate=" + goodRate + "]";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getCarefield() {
		return carefield;
	}

	public void setCarefield(String carefield) {
		this.carefield = carefield;
	}

	public String getGoodRate() {
		return goodRate;
	}

	public void setGoodRate(String goodRate) {
		this.goodRate = goodRate;
	}
	
}
