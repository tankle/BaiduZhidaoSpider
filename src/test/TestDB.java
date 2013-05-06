package test;

import java.sql.Connection;

import org.junit.Assert;
import org.junit.Test;

import com.hitsz.util.DBUtils;

/**
 * 
 * @author JasonTan
 * E-mail: tankle120@gmail.com
 * Create on: 2013-5-6обнГ03:54:37
 *
 */

public class TestDB {
	
	@Test
	public void testDB(){
		Connection conn = DBUtils.getDBConnection();
		
		
		
	}
}
