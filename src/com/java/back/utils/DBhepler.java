package com.java.back.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.HibernateException;

public class DBhepler {

	private static Connection con = null;
	private static String username = null;
	private static String password = null;
	private static String driver = null;
	private static String url = null;
	static {
		Properties ps = new Properties();
		try {
			ps.load(DBhepler.class.getResourceAsStream("/db.properties"));
			driver = ps.getProperty("jdbc.driver");
			url = ps.getProperty("jdbc.url");
			username = ps.getProperty("jdbc.username");
			password = ps.getProperty("jdbc.password");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		try {
			if (con == null) {
				synchronized (Connection.class) {
					if (con == null) {
						Class.forName(driver);
						con = DriverManager.getConnection(url, username,
								password);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}

	public static void close(ResultSet rs, Statement stat, Connection conn)
			throws Exception {
		if (rs != null) {
			rs.close();
		}
		if (stat != null) {
			stat.close();
		}
		if (conn != null) {
			conn.close();
		}
	}

	public static void close(Statement stat, Connection conn) throws Exception {
		if (stat != null) {
			stat.close();
		}
		if (conn != null) {
			conn.close();
		}
	}

	/**
	 * 增删修改
	 * 
	 * @param sql
	 * @param str
	 * @return
	 */
	public static int execute(String sql, String str[]) {
		int a = 0;
		try {
			PreparedStatement pst = getConnection().prepareStatement(sql);
			if (str != null) {
				for (int i = 0; i < str.length; i++) {
					pst.setString(i + 1, str[i]);
				}
			}
			a = pst.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return a;
	}

	/**
	 * 执行sql语句查询
	 * 
	 * @param sql
	 * @return
	 */
	public static ResultSet query(String sql) {
		ResultSet rs = null;
		try {
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			rs = stmt.executeQuery();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * 执行sql查询语句，带有参数
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static ResultSet query(String sql, Object[] params) {
		ResultSet rs = null;
		try {
			PreparedStatement stmt = getConnection().prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				stmt.setObject(i + 1, params[i]);
			}
			rs = stmt.executeQuery();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * result转换list
	 * 
	 * @param rs
	 * @return
	 */
	public static List transformResultSetToMapList(ResultSet rs) {
		List list = new ArrayList();
		try {
			Object recordValue = null;
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rs.next()) {
				Map map = new HashMap();
				for (int i = 1; i < columnCount + 1; i++) {
					String columnName = rsmd.getColumnName(i);
					if (rs.getObject(columnName) != null) {
						recordValue = rs.getObject(columnName);
						if (recordValue instanceof BigDecimal) {
							// recordValue =new Long(rs.getLong(columnName));
						} else if (recordValue instanceof Integer) {
							recordValue = new Integer(rs.getInt(columnName));
						} else if (recordValue instanceof java.sql.Date) {
							recordValue = new Date(rs.getDate(columnName)
									.getTime());
						} else if (recordValue instanceof Blob
								|| recordValue instanceof Clob) {
							BufferedReader bf = null;
							if (recordValue instanceof Blob) {
								java.sql.Blob blob = (java.sql.Blob) rs
										.getBlob(columnName);
								bf = new BufferedReader(new InputStreamReader(
										blob.getBinaryStream()));
								String temp = "";
								StringBuffer sb = new StringBuffer();
								while ((temp = bf.readLine()) != null) {
									sb.append(temp);
								}
								recordValue = sb.toString();
							} else {
								java.sql.Clob clob = (java.sql.Clob) rs
										.getClob(columnName);
								recordValue = clob == null ? null : clob
										.getSubString(1L, (int) clob.length());
							}
						}
					} else {
						recordValue = "";
					}
					map.put(columnName, recordValue);
				}
				list.add(map);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 开始事物：取消默认提交
	public static void setAutoCommit(Connection connection) {
		if (connection != null) {
			try {
				connection.setAutoCommit(false);
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}

	// 都成功提交事物
	public static void commit(Connection connection) {
		if (connection != null) {
			try {
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	// 回滚事物
	public static void rollbank(Connection connection) {
		if (connection != null) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void deleteByid(String id) {
		String sql = "DELETE FROM  TEST WHERE ID=?";
		String[] params = { id };
		int i = execute(sql, params);// 大于0 删除成功
		System.out.println(i);
	}

	public static void add(int id, String name) {
		String sql = "INSERT INTO TEST VALUES (?,?) ";
		PreparedStatement pst;
		try {
			pst = getConnection().prepareStatement(sql);
			pst.setInt(1, id);
			pst.setString(2, name);
			int a = pst.executeUpdate();
			System.out.println(a);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 调用存储过程的查询
	 * 
	 * @return
	 */
	public static List prepareCall() {
		try {
			CallableStatement proc = null;
			proc = getConnection().prepareCall("{ call myproc (?) }");
			proc.setInt(1, 20);
			proc.execute();
			ResultSet resultSet = proc.getResultSet();
			List list = transformResultSetToMapList(resultSet);// 查询结果集
			System.out.println(list.size());
			return list;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


//	DROP PROCEDURE
//	IF EXISTS myproc;
//	CREATE PROCEDURE myproc (
//		IN m_price INT (20)
//	)
//	BEGIN
//
//	IF m_price IS NULL THEN
//
//	SET m_price = 0;
//
//
//	END
//	IF;
//
//	SELECT
//		id,
//		price
//	FROM
//		te_member_card md
//	WHERE
//		md.price >= m_price;
//
//
//	END
	
	/**
	 * 存储过程查询分页条数
	 * 
	 * @param t_table
	 * @param t_where
	 * @return
	 */
	public static int getCallInt(String t_table, String t_where) {
		try {
			CallableStatement statement = getConnection().prepareCall(
					"{call mycount(?, ?,? )}");
			statement.setString(1, t_table);
			statement.setString(2, t_where);
			statement.registerOutParameter(3, Types.INTEGER); // 返回的值
			statement.execute();
			int a = statement.getInt(3);
			return a;
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static void main(String[] args) throws Exception {
		// deleteByid("1");

		prepareCall();

		// Connection conn = DBhepler.getConnection();
		// String sql = "UPDATE TEST SET NAME=? WHERE ID=?";
		// conn.setAutoCommit(false);
		// PreparedStatement ps = conn.prepareStatement(sql);
		// for (int i = 0; i < 3; i++) {
		// ps.setString(1, i + "");
		// ps.setInt(2, i);
		// ps.addBatch();
		// }
		// try {
		// ps.executeBatch();
		// conn.commit();
		// ps.close();
		// conn.close();
		// } catch (Exception e) {
		// // TODO: handle exception
		// conn.rollback();
		// ps.close();
		// conn.close();
		// }

		// Connection connection = null;
		// String sql = "select now();";
		// try {
		// connection = getConnection();
		// System.out.println(connection);
		// ResultSet resultSet=query(sql);
		// while (resultSet.next()) {
		// String str = resultSet.getString(1);
		// System.err.println(str);
		//
		// }
		// } catch (Exception e) {
		//
		// }
	}
}
