package com.hitsz.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.hitsz.model.BaiduUser;
import com.hitsz.model.Item;
import com.hitsz.model.QA;

/**
 * 
 * @author JasonTan
 * E-mail: tankle120@gmail.com
 * Create on：2013-5-13 上午9:49:13 
 *
 */
public class QADBUtil {
	
	/**
	 * qa数据库中各个表的名字
	 */
//	public static final String QUERYTB = "query";
//	public static final String RESULTLISTDB = "qapair_resultlist";
//	public static final String PAIRDB = "qapair";
//	public static final String USER = "baiduuser";
	
	/**
	 * 从数据库中读取所有问句
	 * 从数据库中获取是否有下载过的问句
	 * 	finished = 1 获取已经下载过的
	 * 	finished = 0 没有下载过的
	 * @param finished
	 */
	public static List<String> getQueryListFromDB(int finished) {
		
		Connection conn = DBUtil.getDBConnection();	
		Statement stmt = DBUtil.getStatment(conn);

		String sql = "select * from query where finished = " + finished;
		
		ResultSet rs = null;
		
		List<String> querys = new ArrayList<String>();
		
		try {
			Log.info("execute sql:" + sql);
			
			rs = stmt.executeQuery(sql);
						
			while(rs.next()){
				String query = rs.getString("query");

				querys.add(query);
				
				Log.info("id is " + rs.getString("id") + " queryid is "+
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
		Log.info("execute sql:" + sqlid);
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

	/**
	 * 从数据库中读取所有的未下载的urls
	 * 	根据finished标识，来选取相应的urls
	 * 
	 * @param finished
	 * 			表示该item中的link是否已经下载过
	 * 		 = 1 表示已经下载过
	 * 		 = 0 表示还没有下载 
	 * 
	 * @return
	 */
	public static List<String> getUrlsFromQAPair(int finished) {
		Connection conn = DBUtil.getDBConnection();	
		String sql;
		if(Constants.DEBUG)
			 sql = "select link from `qapair_resultlist` where id <= 5 and finished = " + finished;
		else
			sql = "select link from `qapair_resultlist` where finished = " + finished;
		
		Log.info("execute sql :" + sql);
		
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

	/**
	 * 从数据库中找出那些finished标识位为finished的qids
	 * 
	 * @param finished
	 * 			= 1 表示提取出已经下载过的
	 * 			= 0 表示还没有下载过 
	 * @return
	 */
	public static List<String> getItemIDsFromDB(int finished) {
		
		Connection conn = DBUtil.getDBConnection();	

		String sql;
		if(Constants.DEBUG)
			 sql = "select qid from qapair_resultlist where id <= 5 and finished = " + finished;
		else
			sql = "select qid from qapair_resultlist where finished = " + finished;
		
		Log.info("execute sql :" + sql);
		
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
	 * 
	 * INSERT INTO `qapair`(`id`, `qid`, `question`, `description`, `category`, `q_time`, 
	 * 		`isAdopted`, `asker_id`, `answer`, `comment`, `asker_comment`, 
	 * 			`answer_time`, `isBest`, `answer_id`, `querytime`) VALUES ()

	 * @param qas
	 */
	public static void saveQAList(List<QA> qas) {
		Connection conn = DBUtil.getDBConnection();	
		
		for(QA qa : qas){
			BaiduUser user = qa.getBaiduUser();
			if(user == null){
				System.out.println(qa.getQid());
				continue;
			}
			int id = saveUser(conn,user);
		
			String sql = "INSERT INTO `qapair`(`qid`, `question`, `description`, `category`, `q_time`, `asker_id`, `answer`, " +
					"`answer_time`, `isBest`,`answer_id`, `querytime`)"+
					"VALUES ('" + qa.getQid() +"','" + qa.getQuestion() +"','" + qa.getQuestion()+"','" + qa.getCategory()+"','"+
					qa.getQuestionDate() + "','" + qa.getQuestionId() + "','" + qa.getAnswer()+ "','" + qa.getAnswerDate() + 
					 "',"+qa.getIsBest() + "," +id +",'" +qa.getDownDate()+"')";
			Log.info("execute sql :" + sql);
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

	/**
	 * 保存user的信息
	 * 	INSERT INTO `baiduuser`(`id`, `userName`, `gradeIndex`, `grAnswerNum`, `carefield`, `goodRate`) 
	 * 		VALUES ([value-1],[value-2],[value-3],[value-4],[value-5],[value-6])
	 * @param conn
	 * @param user
	 * @return
	 */
	private static int saveUser(Connection conn, BaiduUser user) {
		System.out.println(user.getUsername());
		String sql = "select * from `baiduuser` where `userName` = " +"'"+user.getUsername()+"'";
		
		Log.info("execute sql :" + sql);
		ResultSet rs = DBUtil.getResultSet(conn, sql);
		
		try {
			if(rs.next()){
				return Integer.parseInt(rs.getString("id"));
			}else{
				sql = "insert into `baiduuser`( `userName`, `gradeIndex`, `grAnswerNum`, `carefield`, `goodRate`) values ('"+
						user.getUsername() +"',"+ user.getGradeIndex()+ ","+user.getGrAnswerNum()+",'"+user.getCarefield()+"',"+user.getGoodRate()+ ")";
				
				Log.info("execute sql :" + sql);
				DBUtil.insert(conn, sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//插入完之后获得 user的id
		sql = "select * from `baiduuser` where `userName` = " +"'"+user.getUsername()+"'";
		Log.info("execute sql :" + sql);
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

	/**
	 * 设置query表中的查询问句query 的finished位为1
	 * @param keyword
	 */
	public static void setQueryFinished(String query) {
		Connection con = DBUtil.getDBConnection();
		String sql = "update query set finished = 1 where query = '" +query+"'";
		
		setFinished(con, sql);
		
		
	}

	private static void setFinished(Connection con, String sql) {
		Statement stat;
		try {
			con.setAutoCommit(false);
			stat  = DBUtil.getStatment(con);
			Log.info("execute sql:" + sql);
			stat.executeUpdate(sql);
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			Log.info("sql is roll back:" + sql);
			e.printStackTrace();
		}finally{
			if(null != con ){
				try {
					con.setAutoCommit(true);
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void setQAPairResultFinished(String url) {
		Connection con = DBUtil.getDBConnection();
		String sql = "update qapair_resultlist set finished = 1 where link = '" +url+"'";
		
		setFinished(con, sql);
		
	}
	
	public static void insertQuery(String query){
		Connection con = DBUtil.getDBConnection();
		int maxid = DBUtil.getMaxId(con);
		String queryid = "000"+(maxid+1);
		String sql = "INSERT INTO `query`(`queryid`, `query`, `finished`) "
				+ "VALUES ('"+queryid+"','"+query+"',0)";
		Log.info(sql);
		DBUtil.insert(con, sql);
	}
}
