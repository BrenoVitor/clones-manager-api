package br.com.liax.clonesManager.daos;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.liax.clonesManager.handlers.AdditionalRowHandler;
import br.com.liax.clonesManager.models.Additional;
import br.com.liax.clonesManager.utils.ConnectionManager;

public class AdditionalDAO {

	private final static String INSERT = " INSERT INTO ADDITIONAL(NAME) VALUES(?) ";

	private final static String UPDATE = " UPDATE ADDITIONAL A SET A.NAME = ? WHERE A.ID = ? ";

	private final static String SELECT_BY_ID = " SELECT * FROM ADDITIONAL A WHERE A.ID = ? ";

	private final static String SELECT_ALL = " SELECT * FROM ADDITIONAL A ";

	private final static String DELETE = " DELETE FROM ADDITIONAL WHERE ID = ? ";

	private static final Logger LOGGER = LogManager.getLogger(AdditionalDAO.class);

	public static long create(Additional additional) throws ClassNotFoundException, SQLException, PropertyVetoException {
		LOGGER.info("Begin process DAO create");
		long id = 0;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			PreparedStatement prepareStatement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			prepareStatement.setString(1, additional.getName());
			prepareStatement.executeUpdate();
			ResultSet resultSet = prepareStatement.getGeneratedKeys();
			if (resultSet.next()) {
				id = resultSet.getLong(1);
			}
			ConnectionManager.commit(connection);
		} catch (Exception e) {
			ConnectionManager.rollback(connection);
			throw e;
		} finally {
			ConnectionManager.closeConnection(connection);
		}
		LOGGER.info("End process DAO create");
		return id;
	}

	public static long update(Additional additional) throws ClassNotFoundException, SQLException , PropertyVetoException{
		LOGGER.info("Begin process DAO update");
		Connection connection = null;
		long rowsChanged = 0;
		try {
			connection = ConnectionManager.getConnection();
			PreparedStatement prepareStatement = connection.prepareStatement(UPDATE);
			prepareStatement.setString(1, additional.getName());
			prepareStatement.setLong(2, additional.getId());
			rowsChanged = prepareStatement.executeUpdate();
			ConnectionManager.commit(connection);
		} catch (Exception e) {
			ConnectionManager.rollback(connection);
			throw e;
		} finally {
			ConnectionManager.closeConnection(connection);
		}
		LOGGER.info("End process DAO update");
		return rowsChanged;
	}

	public static long delete(long id) throws ClassNotFoundException, SQLException, PropertyVetoException {
		LOGGER.info("Begin process DAO delete");
		Connection connection = null;
		long rowsChanged = 0;
		try {
			connection = ConnectionManager.getConnection();
			PreparedStatement prepareStatement = connection.prepareStatement(DELETE);
			prepareStatement.setLong(1, id);
			rowsChanged = prepareStatement.executeUpdate();
			ConnectionManager.commit(connection);
		} catch (Exception e) {
			ConnectionManager.rollback(connection);
			throw e;
		} finally {
			ConnectionManager.closeConnection(connection);
		}
		LOGGER.info("End process DAO delete");
		return rowsChanged;
	}

	public static Additional selectById(long id) throws ClassNotFoundException, SQLException, PropertyVetoException {
		LOGGER.info("Begin process DAO selectById");
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement prepareStatement = connection.prepareStatement(SELECT_BY_ID);) {
			Additional additional = null;
			prepareStatement.setLong(1, id);
			ResultSet resultSet = prepareStatement.executeQuery();
			if (resultSet.next()) {
				additional = new AdditionalRowHandler().handler(resultSet);
			}
			LOGGER.info("End process DAO selectById");
			return additional;
		}
	}

	public static Collection<Additional> selectAll() throws ClassNotFoundException, SQLException, PropertyVetoException {
		LOGGER.info("Begin process DAO selectAll");
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement prepareStatement = connection.prepareStatement(SELECT_ALL);
				ResultSet resultSet = prepareStatement.executeQuery();) {
			Collection<Additional> additionals = new ArrayList<>();
			while (resultSet.next()) {
				additionals.add(new AdditionalRowHandler().handler(resultSet));
			}
			LOGGER.info("End process DAO selectAll");
			return additionals;
		}
	}
}
