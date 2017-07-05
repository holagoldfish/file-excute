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

public class FileExcuteTest {

	// public String s="F05121304080006-01-013投资";
	// public String s="F05121304080006-01-013投资延期收款SQL";
	// public String s = "外部债权导入SQL";
	// public String s="内部债权导入SQL";
	// public String s="异常债权导入SQL";
	// public String s="新建投资SQL";
	// public String s="交易撮合SQL";
	// public String s="收款SQL";
	// public String s = "回款计划SQL";
	// public String s="付款收款SQL";
	//public String s = "综合a";
	//public String s = "定投";
	
	
	//public String s = "天天3_1"; //成功
	//public String s = "天天3_3";   //成功
	//public String s = "天天3_4";   //1个成功 1个处理中
	//public String s = "天天3_5";   // 1个处理中
	//public String s = "天天3_6";   // 1个处理中
	
	

	//public String s = "SX1"; //成功    定制理财产品后  樱子1000失败 樱子2000失败    樱子3000成功 
	//public String s = "天天5_2"; //成功  1,5千都成功 245772390   交通银行 吉林 2010101010101010
	//public String s = "天天5_3";   //成功  yangxin 1,5 失败,6冻结中  ；||   245客户换银行卡 ，2010101010101020  ,7k失败  2010101010101029
	//public String s = "天天5_4";   //1个成功 1个处理中 shenhao         245772390 变更过银行卡 中国银行  2010101010101029  5000
	//public String s = "天天5_5";   // 1个处理中
	//public String s = "天天5_6";   // 1个处理中
		
	//public String sqlText = "E:/tiantian7/"+s+".sql";
	//public String resultText = "E:/tiantian7/"+ s + "回款结算成功";
	
	public String s = "ebo_tpp_一站式代收成功后代付成功";     // ebo_tpp_一站式代收成功 ebo_tpp_一站式部分代收成功失败  ebo_tpp_一站式拆单全部失败   ebo_tpp_一站式代收成功后代付成功
	// 1个处理中	ebo_tpp_代收成功b7c1d1d41d724321b99c83b88e788a04 ebo_tpp_代收成功752796cd06d849dd80a196c82077c753 d6826e577d2843dea9fb9928c2dbfbf4
	public String sqlText = "E:/ebo_tpp_一站式代收成功/"+s+".sql";
	public String resultText = "E:/ebo_tpp_一站式代收成功/"+ s + "-----后2----";
	
	
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
		Connection conn = DBUtils.getConn();
		Statement statement = DBUtils.getStatement(conn);
		ResultSet resultSet = DBUtils.getResultSet(statement, sql);
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
		FileExcuteTest fe = new FileExcuteTest();
		fe.analysisSql();
	}
}
