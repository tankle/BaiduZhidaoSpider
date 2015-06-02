package com.hitsz.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * 配置文件单例类，用于获取系统的配置项
 * @author wenbo
 *
 */
public class Configuration {

	private static Configuration config = null;
	private static Properties properties = null;
	//配置文件名，无需.properties后缀，不可随意更改（ /config/config.properties）
	private static final String  CONFIG_FILE =  "config" + File.separator + "url.properties";
//	private static final String  CONFIG_FILE =  "url.properties";
	
	private Configuration()
	{
		init();
	}
	
	public static synchronized Configuration getInstance()
	{
		if(config == null)
		{
			config = new Configuration();
		}
		return config;
	}
	
	/**
	 * 获取properties文件中设置的属性值
	 * @param prop
	 * @return prop对应的属性值，如果没有设置则返回null
	 */
	public String getPropValue(String prop)
	{
		return (properties.getProperty(prop));
	}
	
	/**
	 * 获取properties文件中所有的属性名
	 * @return
	 */
	public Set getPropSet()
	{
		return (properties.keySet());
	}
	
	/**
	 * 初始化配置类
	 *
	 */
	private void init(){

		try {
			String filePath = System.getProperty("user.dir")  + File.separator + CONFIG_FILE;
			properties = new Properties();
			InputStream  in = new FileInputStream(filePath);
			properties.load(in);
			
		} catch (FileNotFoundException e) {
			System.err.println("配置文件config.properties不存在！");
		} catch (IOException e) {
			System.err.println("配置文件config.properties加载错误！");;
		}

	}
	
	/**
	 * 测试用
	 * @param args
	 */
	public static void main(String[] args)
	{
		Configuration configs = Configuration.getInstance();
		Set props = configs.getPropSet();
		for(Object prop : props)
		{
			System.out.println(prop + " : " + configs.getPropValue(prop.toString()));
		}			
	}
}
