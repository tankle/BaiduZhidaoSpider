package com.hitsz.util.config;

public class ConfigUtil {

	/**
	 * 获取配置
	 * @param prop
	 * @return
	 */
	public static String getPropValue(String prop) throws PropNotConfigedException
	{
		
		Configuration config = Configuration.getInstance();
		String value = config.getPropValue(prop);
		if(value==null)
		{
			throw new PropNotConfigedException("Error: Property [" + prop + "] not Configed,"
					 				+ " please check you configuration!");
		}
		return  value;
	}
	
	/**
	 * 字符串转换为int
	 * @param str
	 * @return
	 */
	public static int strToInt(String str)
	{
		return str==null?0:new Integer(str);
	}
}
