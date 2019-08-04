package br.com.liax.clonesManager.utils;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {

	public static Connection getConnection() throws ClassNotFoundException, SQLException, PropertyVetoException {
		return ConnectionFactory.getConnection();
	}

	public static void closeConnection(Connection connection) throws SQLException {
		if (!isNullOrClosed(connection)) {
			connection.close();
		}
	}

	private static boolean isNullOrClosed(Connection connection) throws SQLException {
		return connection == null || connection.isClosed();
	}

	public static void commit(Connection connection) throws SQLException {
		if (!isNullOrClosed(connection) && !isAutoCommit(connection)) {
			connection.commit();
		}
	}

	private static boolean isAutoCommit(Connection connection) throws SQLException {
		return connection.getAutoCommit();
	}
	
	public static void rollback(Connection connection) throws SQLException {
		if (!isNullOrClosed(connection) && !isAutoCommit(connection)) {
			connection.rollback();
		}
	}

}
