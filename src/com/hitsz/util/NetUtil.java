package com.hitsz.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import com.hitsz.util.config.ConfigUtil;
import com.hitsz.util.config.PropNotConfigedException;


/**
 * 网络单例
 * 
 * @author JasonTan
 * E-mail: tankle120@gmail.com
 * Create on：2013-5-6 下午10:15:05 
 *
 */
public class NetUtil{
	
	private static NetUtil netutil = null;
	
	private NetUtil(){
		/*
		 * 设置代理
		 */
		if(Constants.ISPROXY)
				setProxy();
	}
	
	public static NetUtil getInstance(){
		if(null == netutil){
			netutil = new NetUtil();
		}
		return netutil;
	}
	
	
	/**
	 * set web proxy
	 */
	public static void setProxy(){
		
		String strProxy = "";
		String strPort = "";
		//设置代理
		
		try {
			strProxy =  ConfigUtil.getPropValue("Spider.ProxyIP");
			strPort =  ConfigUtil.getPropValue("Spider.ProxyPort");
		} catch (PropNotConfigedException e) {
			e.printStackTrace();
		}
		

		Properties systemProperties = System.getProperties(); 
		systemProperties.setProperty("http.proxyHost",strProxy); 
		systemProperties.setProperty("http.proxyPort",strPort); 
	}
	
	
	/**
	 * 使用默认编码gbk
	 * @param htmlurl 问题的url
	 * @return
	 * @throws IOException 
	 */
	public  String getHtml(String htmlurl) throws IOException{
		return getHtml(htmlurl,Constants.ENCODING);
	}
	
	/**
	 * 读取网页的内容
	 * @param htmlurl	
	 * @param encoding	网页编码
	 * @return
	 * @throws IOException 
	 */
	public String getHtml(String htmlurl,String encoding) throws IOException{
		URL url;
        String temp = null;
        StringBuffer sb = new StringBuffer();
        try {
        	Log.info("loading the: " + htmlurl);
        	
            url = new URL(htmlurl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url
                    .openStream(), encoding));// 读取网页全部内容
            while ((temp = in.readLine()) != null) {
                sb.append(temp+"\r\n");
            }
            in.close();
            
            Log.info("loading end ....");
        }catch(MalformedURLException me){
            Log.info("你输入的URL格式有问题！请仔细输入");
            me.getMessage();
           throw me;
        }catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return sb.toString();
	}

}
