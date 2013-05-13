package com.hitsz.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.hitsz.dao.BaiduUser;
import com.hitsz.dao.Item;
import com.hitsz.dao.QA;

/**
 * 
 * @author JasonTan
 * E-mail: tankle120@gmail.com
 * Create on：2013-5-13 上午9:49:13 
 *
 */
public class QADBUtil {
	
	/**
	 * 从数据库中读取所有问句
	 */
	public static List<String> getQueryListFromDB() {
		
		Connection conn = DBUtil.getDBConnection();	
		Statement stmt = DBUtil.getStatment(conn);

		String sql = "select * from query";
		
		ResultSet rs = null;
		
		List<String> querys = new ArrayList<String>();
		
		try {
			rs = stmt.executeQuery(sql);
						
			while(rs.next()){
				String query = rs.getString("query");

				querys.add(query);
				
				System.out.println("id is " + rs.getString("id") + " queryid is "+
						rs.getString(2) + " query is " + rs.getString("query"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(stmt != null){			
					stmt.close();
					stmt = null;
				}				
				if(null != conn){
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return querys;
	}

	/**
	 * qapair_resultlist表中的字段
	 * id,qid,rankid,title,answer,answer_time,link,querytime
	 * 
	 * 		String sql = "INSERT INTO `qapair_resultlist` (`qid` , `rankid` , `title` , `answer` , " +
	 * 		"`answer_time` , `link` , `querytime`,`queryid` )" +
	 * 		"VALUES ('123', 456, 'test标题', '回答', '2013-5-6', 'http://zhidao.baidu.com', '2013-5-13','0001')";
	 * 
	 * @param list
	 * @param query 
	 */
	public static void saveTermList(List<Item> list, String query) {
		Connection conn = DBUtil.getDBConnection();	
		
		/**
		 *   根据问句获得问句的queryid
		 */
		String sqlid = "select `queryid` from `query` where query = \"" + query + "\"";
		
		ResultSet rs = DBUtil.getResultSet(conn, sqlid);
		
		String queryid = null;
		
		try {
			if(rs.next()){
				queryid = rs.getString("queryid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		/**
		 * 保存所有的查询结果
		 */
		List<String> sqls = new LinkedList<String>();
		
		for (int i = 0; i < list.size(); i++) {

			Item term = list.get(i);
			String sql = "INSERT INTO `qapair_resultlist` (`qid` , `rankid` , `title` , `answer` , " +
					 			"`answer_time` , `link` , `querytime` ,`queryid`) VALUES " +
					 			"('"+term.getId()+"'," + term.getRankid()+", '"+
					 			term.getTitle()+"', '"+ term.getAnswer() +"', '"+term.getDate()+"', " +
					 					"'"+term.getUrl()+"', '"+term.getDowndate()+"','"+queryid+"')";
			sqls.add(sql);

		}
		
		DBUtil.insert(conn, sqls);
		
		DBUtil.close(conn);
		
	}

	public static List<String> getUrlsFromQAPair() {
		Connection conn = DBUtil.getDBConnection();	
		String sql;
		if(Constants.DEBUG)
			 sql = "select link from `qapair_resultlist` where id <= 5";
		else
			sql = "select link from `qapair_resultlist`";
		
		System.out.println("execute sql :" + sql);
		
		ResultSet rs = DBUtil.getResultSet(conn, sql);
		
		List<String> urls = new LinkedList<String>();
		int i = 1;
		try {
			while(rs.next()){
				String url = rs.getString("link");
				System.out.println("The "+(i++) +" url is " + url);
				urls.add(url);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(conn);
			
			DBUtil.close(rs);
		}
		
		return urls;
	}

	public static List<String> getTermIDsFromDB() {
		
		Connection conn = DBUtil.getDBConnection();	

		String sql;
		if(Constants.DEBUG)
			 sql = "select qid from qapair_resultlist where id <= 5";
		else
			sql = "select qid from qapair_resultlist";
		
		System.out.println("execute sql :" + sql);
		
		ResultSet rs = DBUtil.getResultSet(conn, sql);
		
		List<String> querys = new LinkedList<String>();
		
		try {
						
			while(rs.next()){
				String query = rs.getString("qid");

				querys.add(query);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null){
					rs.close();
					rs = null;
				}			
				if(null != conn){
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return querys;
	}

	/**
	 * INSERT INTO `qapair`(`qid`, `question`, `description`, `category`, `q_time`,
	 * 	 `isAdopted`, `asker_id`, `answer`, `comment`, `asker_comment`, `answer_time`, 
	 * `isBest`, `answer_id`, `querytime`, `id`) 
	 * VALUES ([value-1],[value-2],[value-3],[value-4],[value-5],[value-6],[value-7],
	 * [value-8],[value-9],[value-10],[value-11],[value-12],[value-13],[value-14],[value-15],[value-16])
	 * 
	 * INSERT INTO `baiduuser`(`id`, `userName`, `level`, `carefield`, `goodRate`) 
	 * VALUES ([value-1],[value-2],[value-3],[value-4],[value-5]
	 * @param qas
	 */
	public static void saveQAList(List<QA> qas) {
		Connection conn = DBUtil.getDBConnection();	
		
		for(QA qa : qas){
			BaiduUser user = qa.getBaiduUser();
			
			int id = saveUser(conn,user);
		
			String sql = "INSERT INTO `qapair`(`qid`, `question`, `description`, `category`, `q_time`, `asker_id`, `answer`, " +
					"`answer_time`, `answer_id`, `querytime`)"+
					"VALUES ('" + qa.getId() +"','" + qa.getTitle() +"','" + qa.getQuestion()+"','" + qa.getCategory()+"','"+
					qa.getQuestionDate() + "','" + qa.getQuestionId() + "','" + qa.getAnswer() + "','" + qa.getAnswerDate() + 
					"'," +id +",'" +qa.getDownDate()+"')";
			System.out.println("execute sql :" + sql);
			DBUtil.insert(conn, sql);
		}
		try {		
			if(null != conn){
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static int saveUser(Connection conn, BaiduUser user) {
		String sql = "select * from `baiduuser` where `userName` = " +"'"+user.getUsername()+"'";
		
		System.out.println("execute sql :" + sql);
		ResultSet rs = DBUtil.getResultSet(conn, sql);
		
		try {
			if(rs.next()){
				return Integer.parseInt(rs.getString("id"));
			}else{
				sql = "insert into `baiduuser`( `userName`, `level`, `carefield`, `goodRate`) values ('"+
						user.getUsername() +"','"+ user.getLevel()+"','"+user.getCarefield()+"','"+user.getGoodRate()+ "')";
				
				System.out.println("execute sql :" + sql);
				DBUtil.insert(conn, sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sql = "select * from `baiduuser` where `userName` = " +"'"+user.getUsername()+"'";
		System.out.println("execute sql :" + sql);
		rs = DBUtil.getResultSet(conn, sql);
		int id = -1;
		try {
			if(rs.next())
				id = Integer.parseInt(rs.getString("id"));
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(rs != null){
					rs.close();
					rs = null;
				}			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return id;
	}
}
