package com.hitsz.util;

import org.apache.log4j.Logger;


public class Log {
	static Logger log = Logger.getLogger(Log.class.getName());

    public static void debug(String message) {
    	log.debug(message);
    }

	public static void info(String message) {
		log.info(message);
	}
}
