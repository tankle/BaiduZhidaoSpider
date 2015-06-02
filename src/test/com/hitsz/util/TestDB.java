package test.com.hitsz.util;

import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;

import com.hitsz.util.DBUtil;


/**
 * 
 * @author JasonTan
 * E-mail: tankle120@gmail.com
 * Create on：2013-5-6 下午10:14:31 
 *
 */

public class TestDB {
	
	@Test
	public void testDB(){
		Connection conn = DBUtil.getDBConnection();
		
		assertNotNull(conn);
		
		Statement stmt = DBUtil.getStatment(conn);
		
		assertNotNull(stmt);
		
		String sql = "select * from query";
		
		ResultSet rs = null;
		
		try {
			rs = stmt.executeQuery(sql);
			
//			assertNotNull(rs);
			
			while(rs.next()){
				System.out.println("id is " + rs.getString("id") + " queryid is "+
						rs.getString(2) + " query is " + rs.getString("query"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
//				DBUtils.close(rs);
//				rs = null;
//				DBUtils.close(stmt);
//				stmt = null;
//				DBUtils.close(conn);
//				conn = null;
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		assertNull(conn);
		
		assertNull(stmt);
		
		assertNull(rs);
	}
	
	
	@Test
	public void testInsert(){
		//insert into `qapair_resultlist`(`id`,`qid`,`rankid`,`title`,`answer`,`answer_time`,`link`,`querytime`) values();
		String sql = "INSERT INTO `qapair_resultlist` (`qid` , `rankid` , `title` , `answer` , " +
				"										`answer_time` , `link` , `querytime` )" +
				"VALUES ('123', 456, 'test标题', '回答', '2013-5-6', 'http://zhidao.baidu.com', '2013-5-13')";
		
		Connection conn = DBUtil.getDBConnection();

		assertTrue(DBUtil.insert(conn, sql));
		
		
	}
}
