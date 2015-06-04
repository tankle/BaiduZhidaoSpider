package com.hitsz.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class testJson {

	public static void main(String[] args){
		String text = "{imId:\"94d415255f72f\",uid:\"123456\",uid:\"804762996\",carefield: [{cid:\"187\",cname:\"外语学习\"}]}";
		JSONObject job = JSON.parseObject(text);
		System.out.println(job.get("uid"));
		
		JSONArray ja = job.getJSONArray("carefield");
	}
}
