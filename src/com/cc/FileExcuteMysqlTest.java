package com.cc;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FileExcuteMysqlTest {
	
	public String s = "mysql";   //1个成功 1个处理中
	public String sqlText = "E:/mysql.txt";
	public String resultText = "E:/"+ s + "__结果后";

	Font font = new Font("宋体", Font.BOLD, 16);

	public void analysisSql() throws IOException, SQLException {
		List<String> sqlContents = getSqlContents();

		List<List<String>> sqlAll = new ArrayList<List<String>>();
		List<String> sqlpart = new ArrayList<String>();
		for (String string : sqlContents) {
			if (string.length() > 5 && string.substring(0, 5).contains("--")) {
				sqlpart = new ArrayList<String>();
				sqlAll.add(sqlpart);
			}
			sqlpart.add(string);
		}

		StringBuilder sqlResult = new StringBuilder();
		for (List<String> sqlpar : sqlAll) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < sqlpar.size(); i++) {
				if (i == 0) {
					sqlResult.append(sqlpar.get(0)).append("\n");
				} else {
					sb.append(sqlpar.get(i));
				}
			}

			String sql = sb.toString().replaceAll(";", "");
			if (sql.length() > 0 && !sql.equals("")) {
				sqlResult.append(sql).append("\n").append("\n");
				List<List<String>> queryForList = SQLExcute(sb.toString()
						.replaceAll(";", ""));
				for (List<String> string : queryForList) {
					// sqlResult.append(string).append("\n");
					for (String str : string) {
						sqlResult.append(str).append("\n");
					}
					sqlResult.append("\n");
				}
				sqlResult.append("\n");
				sqlResult
						.append("---------------------------------------------------------------------------------------------------------")
						.append("\n");
			}
		}

		// FileUtils.writeStringToFile(new File("d:/result-" +
		// System.currentTimeMillis()), sqlResult.toString());
		FileUtils.writeStringToFile(
				new File(resultText + System.currentTimeMillis()),
				sqlResult.toString());

	}

	private List<String> getSqlContents() throws IOException {
		List<String> readLines = FileUtils.readLines(new File(sqlText), "GBK");
		List<String> sqlcontents = new ArrayList<String>();
		for (String string : readLines) {
			if (!string.equals("")) {
				sqlcontents.add(string);
			}
		}
		return sqlcontents;
	}

	public List<List<String>> SQLExcute(String sql) throws SQLException {
		List<List<String>> lists = new ArrayList<List<String>>();
		Connection conn = DBUtilsMysql.getConn();
		Statement statement = DBUtilsMysql.getStatement(conn);
		ResultSet resultSet = DBUtilsMysql.getResultSet(statement, sql);
		ResultSetMetaData rsm = resultSet.getMetaData(); //
		int col = rsm.getColumnCount(); //
		String colName[] = new String[col];
		while (resultSet.next()) {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < col; i++) {
				colName[i] = rsm.getColumnName(i + 1);
				System.out.println(colName[i] + ":"
						+ resultSet.getString(colName[i]));
				list.add(colName[i] + " :" + resultSet.getString(colName[i]));
				System.out.println();
			}// End for

			lists.add(list);
		}

		// 鍙栫粨鏋滈泦涓殑琛ㄥご鍚嶇О, 鏀惧湪colName鏁扮粍涓
		DBUtils.close(conn, statement, resultSet);
		return lists;
	}

	public static void main(String[] args) throws SQLException, IOException {
		FileExcuteMysqlTest fe = new FileExcuteMysqlTest();
		fe.analysisSql();
	}
}
