package com.cc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtilsMysql {
	static {
		try {
			// 鍔犺浇鏁版嵁搴撻┍鍔�
			System.out.print("aaaaaaa");
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 鑾峰彇鏁版嵁搴撹繛鎺ュ璞�
	public static Connection getConn() {
		Connection conn = null;
		try {
			// "jdbc:oracle:thin:@localhost:1521:浣犵殑鏁版嵁搴撳悕瀛�, "鐢ㄦ埛鍚�,"瀵嗙爜"
			//conn = DriverManager.getConnection("jdbc:oracle:thin:@172.16.200.72:1521:test", "poseidon1", "123456");
			conn = DriverManager.getConnection("jdbc:mysql://172.16.200.131:3306/hermes_ttcf_test1?useUnicode=true&characterEncoding=utf-8", "hermes_dev", "123456");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	// 鑾峰彇璇彞鎵ц瀵硅薄
	public static Statement getStatement(Connection conn) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stmt;
	}

	// 鑾峰彇棰勫鐞嗚鍙ユ墽琛屽璞�
	public static PreparedStatement getPreparedStatement(Connection conn, String sql) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pstmt;
	}

	// 鑾峰彇缁撴灉闆嗗璞�
	public static ResultSet getResultSet(PreparedStatement pstmt) {
		ResultSet res = null;
		try {
			res = pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	// 鑾峰彇缁撴灉闆嗗璞�
	public static ResultSet getResultSet(Statement stmt, String sql) {
		ResultSet res = null;
		try {
			res = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	// 鍏抽棴璧勬簮鏂规硶
	public static void close(Connection conn, Statement stmt, ResultSet res) {
		close(res);
		close(stmt);
		close(conn);
	}

	// 灏佽鏂规硶鍏抽棴璇彞瀵硅薄
	private static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			stmt = null;
		}
	}

	// 灏佽鏂规硶鍏抽棴缁撴灉闆嗗璞�
	private static void close(ResultSet res) {
		if (res != null) {
			try {
				res.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			res = null;
		}
	}

	// 灏佽鏂规硶鍏抽棴鏁版嵁搴撹繛鎺ュ璞�
	private static void close(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn = null;
	}

}
