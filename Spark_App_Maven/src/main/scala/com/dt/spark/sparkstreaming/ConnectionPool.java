package com.dt.spark.sparkstreaming;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;


public class ConnectionPool {

	private static LinkedList<Connection> connectionQueue;
	

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}  
	}

	public synchronized static Connection getConnection() {
		try {
			if(connectionQueue == null) {
				connectionQueue = new LinkedList<Connection>();
				for(int i = 0; i < 5; i++) {
					Connection conn = DriverManager.getConnection(
							"jdbc:mysql://Master:3306/sparkstreaming?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8",
							"root",
							"123456");
					connectionQueue.push(conn);  
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connectionQueue.poll();
	}

	public static void returnConnection(Connection conn) {
		connectionQueue.push(conn);  
	}
	
}
