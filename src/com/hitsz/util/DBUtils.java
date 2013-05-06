package com.hitsz.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.hitsz.util.config.ConfigUtil;
import com.hitsz.util.config.PropNotConfigedException;

public class DBUtils {

	public static Connection getDBConnection(){
		String conUrl = "";
		String DBDriver = "";
		String user = "";
		String pwd = "";
		try {
			conUrl = ConfigUtil.getPropValue("qaURL");
			DBDriver = ConfigUtil.getPropValue("qaDriver");
			user = ConfigUtil.getPropValue("qaUser");
			pwd = ConfigUtil.getPropValue("qaPwd");
		} catch (PropNotConfigedException e) {
			System.out.println(e.getMessage());
		}
		
		Connection conn = null;
		try {
			Class.forName(DBDriver);
			conn = DriverManager.getConnection(conUrl, user, pwd);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}      
		return conn;
	}

	public static boolean insert(Connection conn, String sql){
		try {
			Statement st = conn.createStatement();
			st.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean insert(Connection conn, List<String> sqlList){
		String temp = "";
		try {
			Statement st = conn.createStatement();
			for(String sql : sqlList){
				temp = sql;
				st.executeUpdate(sql);
			}

		} catch (SQLException e) {
			System.out.println(temp);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static int getMaxId(){
		Connection	conn = getDBConnection();
		String sql = "select max(id) from qaset";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				return (rs.getInt("max(id)"));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeDBConnection(conn);
		return 0;
	}

	public static void closeDBConnection(Connection conn){
		try {
			if(conn != null && !conn.isClosed())
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
