package br.com.liax.clonesManager.utils;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionFactory {

	private static final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";

	private static final String DEFAULT_AUTOCOMMIT = "false";

	private static final String DEFAULT_PASSWORD = "";

	private static final String DEFAULT_USERNAME = "root";

	private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/CLONESMANAGER";

	private static final String PASSWORD = "password";

	private static final String USERNAME = "username";

	private static final String URL = "url";

	private static final String AUTOCOMMIT = "autocommit";

	private static final String DRIVER = "driver";

	private static Connection connection;
	
	private static ComboPooledDataSource connectionPoolDatasource = null;

	private static final Properties PROPERTIES = LoadProperties.getProperties();

	private static final String IS_CONNECTION_POOL = "is.connection.pool";

	private static final String DEAFULT_IS_CONNECTION_POOL = "true";

	private static final boolean USE_CONNECTION_POOL = PROPERTIES.getProperty(IS_CONNECTION_POOL, DEAFULT_IS_CONNECTION_POOL) == DEAFULT_IS_CONNECTION_POOL;

	private ConnectionFactory() {
		// singleton
	}

	public static Connection getConnection() throws SQLException, ClassNotFoundException, PropertyVetoException {
		if(USE_CONNECTION_POOL && connectionPoolDatasource == null) {
			configPool();
		}
		
		if (!isConnectionNullOrClosed()) {
			return connection;
		}
		
		buildConnection();
		configAutoCommit();
		return connection;
	}

	private static boolean isConnectionNullOrClosed() throws SQLException {
		return connection == null || connection.isClosed();
	}

	private static void buildConnection() throws SQLException, ClassNotFoundException {
		if (USE_CONNECTION_POOL) {
			createConnectionByPoll();
		} else {
			createDriveConnection();
		}
	}

	private static void createConnectionByPoll() throws SQLException {
		connection = connectionPoolDatasource.getConnection();
	}

	private static void createDriveConnection() throws SQLException, ClassNotFoundException {
		createConnectionByPoll();
		loadClassDriver();
		connection = DriverManager.getConnection(PROPERTIES.getProperty(URL, DEFAULT_URL),
				PROPERTIES.getProperty(USERNAME, DEFAULT_USERNAME),
				PROPERTIES.getProperty(PASSWORD, DEFAULT_PASSWORD));
	}

	private static void configPool() throws PropertyVetoException {
		connectionPoolDatasource = new ComboPooledDataSource();
		connectionPoolDatasource.setJdbcUrl(PROPERTIES.getProperty(URL, DEFAULT_URL));
		connectionPoolDatasource.setDriverClass(PROPERTIES.getProperty(DRIVER, DEFAULT_DRIVER));
		connectionPoolDatasource.setMinPoolSize(1);
		connectionPoolDatasource.setAcquireIncrement(1);
		connectionPoolDatasource.setMaxPoolSize(10);
		connectionPoolDatasource.setUser(PROPERTIES.getProperty(USERNAME, DEFAULT_USERNAME));
		connectionPoolDatasource.setPassword(PROPERTIES.getProperty(PASSWORD, DEFAULT_PASSWORD));
	}

	private static void configAutoCommit() throws SQLException {
		if (isAutoCommit()) {
			connection.setAutoCommit(false);
		}
	}

	private static boolean isAutoCommit() {
		return PROPERTIES.getProperty(AUTOCOMMIT, DEFAULT_AUTOCOMMIT) == DEFAULT_AUTOCOMMIT;
	}

	private static void loadClassDriver() throws ClassNotFoundException {
		Class.forName(PROPERTIES.getProperty(DRIVER, DEFAULT_DRIVER));
	}

}
