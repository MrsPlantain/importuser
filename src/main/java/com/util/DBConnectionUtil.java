
package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DBConnectionUtil {

	private static Connection conn;
	private static Queue<Connection> connections_unused = new ConcurrentLinkedQueue<Connection>();
	private static Queue<Connection> connections_used = new ConcurrentLinkedQueue<Connection>();

	static {
		try {
			Class.forName(PropertiesUtil.get("jdbc.driver"));
			for (int i = 0; i < 10; i++) {
				connections_unused.add(createConnection());
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static Connection conn() throws InterruptedException {
		Connection connection = connections_unused.poll();
		while (connection == null){
			connection = connections_unused.poll();
		}
		connections_used.add(connection);
		System.out.println(connection);
		return connection;
	}
	public static Connection connection() {
		if (conn == null) {
			try {
				conn = createConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}

	public static void rollback() throws SQLException {
		conn.rollback();
	}

	public static void commit() throws SQLException {
		conn.commit();
	}

	public static void close() throws SQLException {
		conn.close();
	}

	public static void close(Connection connection){
		connections_unused.add(connection);
		connections_used.remove(connection);
	}

	private static Connection createConnection() throws SQLException {
		return DriverManager.getConnection(PropertiesUtil.get("jdbc.url"), PropertiesUtil.get("jdbc.username"),
				PropertiesUtil.get("jdbc.password"));
	}
	
}
