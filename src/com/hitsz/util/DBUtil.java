package com.hitsz.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.hitsz.util.config.ConfigUtil;
import com.hitsz.util.config.PropNotConfigedException;

/**
 * 
 * @author JasonTan
 * E-mail: tankle120@gmail.com
 * Create on：2013-5-7 上午10:39:30 
 *
 */
public class DBUtil {

	/**
	 * 获得链接数据库的Connection
	 * @return
	 */
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

	/**
	 * 执行单条sql
	 * @param conn
	 * @param sql
	 * @return
	 */
	public static boolean insert(Connection conn, String sql){
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 执行多条sql
	 * @param conn
	 * @param sqlList
	 * @return
	 */
	public static boolean insert(Connection conn, List<String> sqlList){
		String temp = "";
		try {
			Statement st = conn.createStatement();
			for(String sql : sqlList){
				System.out.println("insert qapair_list:\n"+sql);
				
				temp = sql;
				st.executeUpdate(sql);
			}
			if(null != st){
				st.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

//	/**
//	 * 获得最大的
//	 * @return
//	 */
//	public static int getMaxId(){
//		Connection	conn = getDBConnection();
//		String sql = "select max(id) from qaset";
//		try {
//			Statement stmt = conn.createStatement();
//			ResultSet rs = stmt.executeQuery(sql);
//			if(rs.next()){
//				return (rs.getInt("max(id)"));
//			}
//			conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		closeDBConnection(conn);
//		return 0;
//	}

//	/**
//	 * 关闭链接
//	 * @param conn
//	 */
//	public static void closeDBConnection(Connection conn){
//		try {
//			if(conn != null && !conn.isClosed())
//				conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * 获取
	 * @param conn
	 * @return
	 */
	public static Statement getStatment(Connection conn){
		Statement stmt = null;
		try{
			stmt = conn.createStatement();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return stmt;
	}
	
	/**
	 * 
	 * @param stmt
	 * @param sql
	 * @return
	 */
	public static ResultSet getResultSet(Statement stmt, String sql){
		ResultSet rs = null;
		try{
			rs = stmt.executeQuery(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 
	 * @param conn
	 * @param sql
	 * @return
	 */
	public static ResultSet getResultSet(Connection conn, String sql){
		Statement stmt = getStatment(conn);
		ResultSet rs = null;
		try{
			rs = stmt.executeQuery(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * close the connection
	 * @param conn
	 */
	public static void close(Connection conn){
		try {
			if(conn != null){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * close the statement
	 * @param stmt
	 */
	public static void close(Statement stmt){
		try {
			if(stmt != null )
				stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * close resultset
	 * @param rs
	 */
	public static void close(ResultSet rs){
		try {
			if(rs != null )
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
