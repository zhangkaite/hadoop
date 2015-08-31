package zkt.util;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

/**
 * 创建连接mysql的工具类
 * 
 * @author zkt
 *
 */
public class DBUtil {

	/***
	 * 1、通过静态方法实例化数据库 包括数据库连接 2、
	 */
	private Connection conn = null;

	private PreparedStatement ps = null;

	//private ResultSet rs = null;

	private final String DBDRIVER = "com.mysql.jdbc.Driver";// 驱动类类名

	private final String DBNAME = "statistics";// 数据库名

	private final String DBURL = "jdbc:mysql://192.168.12.101:3306/" + DBNAME;// 连接URL

	private final String DBUSER = "root";// 数据库用户名

	private final String DBPASSWORD = "root";// 数据库密码

	/**
	 * 默认初始化连接数据库
	 */
	public DBUtil() {
		try {
			Class.forName(DBDRIVER);// 注册驱动
			conn = (Connection) DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);// 获得连接对象

		} catch (ClassNotFoundException e) {// 捕获驱动类无法找到异常
			e.printStackTrace();
		} catch (SQLException e) {// 捕获SQL异常

			e.printStackTrace();

		}

	}

	/***
	 * 数据插入操作
	 * 
	 * @throws Exception
	 */
	public void inSertData(String sql) throws Exception {
		try {

			ps = (PreparedStatement) conn.prepareStatement(sql);

			ps.executeUpdate();

		} catch (SQLException sqle) {

			throw new SQLException("insert data Exception: " + sqle.getMessage());

		} finally {

			try {

				if (ps != null) {

					ps.close();

				}

			} catch (Exception e) {

				throw new Exception("ps close exception: " + e.getMessage());

			}

			try {

				if (conn != null) {

					conn.close();

				}

			} catch (Exception e) {

				throw new Exception("conn close exception: " + e.getMessage());

			}

		}

	}
}
