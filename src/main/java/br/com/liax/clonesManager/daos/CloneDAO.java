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
import br.com.liax.clonesManager.handlers.CloneRowHandler;
import br.com.liax.clonesManager.models.Additional;
import br.com.liax.clonesManager.models.Clone;
import br.com.liax.clonesManager.utils.ConnectionManager;

public class CloneDAO {

	private final static String INSERT = " INSERT INTO CLONE(NAME, AGE) VALUES(?,?) ";

	private final static String UPDATE = " UPDATE CLONE SET NAME = ?, AGE = ? WHERE ID = ? ";

	private final static String SELECT_BY_ID = " SELECT * FROM CLONE C WHERE C.ID = ? ";

	private final static String SELECT_ALL = " SELECT * FROM CLONE C ";

	private final static String DELETE = " DELETE FROM CLONE WHERE ID = ? ";

	private static final String SELECT_ADDITIONAL_CLONE_BY_CLONE = " SELECT * FROM ADDITIONAL_CLONE AC INNER JOIN CLONE C ON C.ID = AC.CLONE_ID INNER JOIN ADDITIONAL A ON A.ID = AC.ADDITIONAL_ID WHERE C.ID = ? ";

	private final static String INSERT_ADDITIONAL_CLONE = " INSERT INTO ADDITIONAL_CLONE(CLONE_ID, ADDITIONAL_ID) VALUES(?,?) ";

	private final static String DELETE_ADDITIONAL_CLONE = " DELETE FROM ADDITIONAL_CLONE WHERE CLONE_ID = ? ";
	
	private static final Logger LOGGER = LogManager.getLogger(CloneDAO.class);

	public static long create(final Clone clone) throws ClassNotFoundException, SQLException, PropertyVetoException {
		LOGGER.info("Begin process DAO create");
		long id = 0;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			PreparedStatement prepareStatementClone = connection.prepareStatement(INSERT,
					Statement.RETURN_GENERATED_KEYS);
			prepareStatementClone.setString(1, clone.getName());
			prepareStatementClone.setInt(2, clone.getAge());
			prepareStatementClone.executeUpdate();
			ResultSet resultSetClone = prepareStatementClone.getGeneratedKeys();
			if (resultSetClone.next()) {
				id = resultSetClone.getLong(1);
			}
			clone.setId(id);
			createAdditionalClone(clone, connection);
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

	private static void createAdditionalClone(final Clone clone, final Connection connection) throws SQLException {
		try (PreparedStatement prepareStatementAdditionalClone = connection
				.prepareStatement(INSERT_ADDITIONAL_CLONE);) {
			for (Additional additional : clone.getAdditionals()) {
				prepareStatementAdditionalClone.setLong(1, clone.getId());
				prepareStatementAdditionalClone.setLong(2, additional.getId());
				prepareStatementAdditionalClone.executeUpdate();
			}
		}
	}

	public static long update(final Clone clone) throws ClassNotFoundException, SQLException, PropertyVetoException {
		LOGGER.info("Begin process DAO update");
		long rowsChanged = 0;
		Connection connection = null;
		try {
			connection = ConnectionManager.getConnection();
			PreparedStatement prepareStatementClone = connection.prepareStatement(UPDATE);
			prepareStatementClone.setString(1, clone.getName());
			prepareStatementClone.setInt(2, clone.getAge());
			prepareStatementClone.setLong(3, clone.getId());
			rowsChanged = prepareStatementClone.executeUpdate();
			deleteAdditionalClone(clone, connection);
			createAdditionalClone(clone, connection);
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

	private static void deleteAdditionalClone(final Clone clone, final Connection connection) throws SQLException {
		try (PreparedStatement prepareStatementAdditionalClone = connection.prepareStatement(DELETE_ADDITIONAL_CLONE)){
			prepareStatementAdditionalClone.setLong(1, clone.getId());
			prepareStatementAdditionalClone.executeUpdate();
		}
	}

	public static long delete(final long id) throws ClassNotFoundException, SQLException, PropertyVetoException {
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

	public static Clone selectById(final long id) throws ClassNotFoundException, SQLException, PropertyVetoException {
		LOGGER.info("Begin process DAO selectById");
		Clone clone = null;
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement prepareStatementClone = connection.prepareStatement(SELECT_BY_ID);) {

			prepareStatementClone.setLong(1, id);
			ResultSet resultSetClone = prepareStatementClone.executeQuery();

			if (resultSetClone.next()) {
				clone = new CloneRowHandler().handler(resultSetClone);
				clone.setAdditionals(fillAdditionals(id, connection));
			}

			LOGGER.info("End process DAO selectById");
			return clone;
		}
	}

	private static Collection<Additional> fillAdditionals(final long id, final Connection connection) throws SQLException {
		Collection<Additional> additionals;
		try (PreparedStatement prepareStatementAdditionalCLone = connection
				.prepareStatement(SELECT_ADDITIONAL_CLONE_BY_CLONE);) {
			prepareStatementAdditionalCLone.setLong(1, id);
			ResultSet resultSetAdditionalClone = prepareStatementAdditionalCLone.executeQuery();
			additionals = new ArrayList<Additional>();
			while (resultSetAdditionalClone.next()) {
				additionals.add(new AdditionalRowHandler().handler(resultSetAdditionalClone));
			}
		}
		return additionals;
	}

	public static Collection<Clone> selectAll() throws ClassNotFoundException, SQLException, PropertyVetoException {
		LOGGER.info("Begin process DAO selectAll");
		Collection<Clone> cloneCollection = null;
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement prepareStatement = connection.prepareStatement(SELECT_ALL);
				ResultSet resultSet = prepareStatement.executeQuery();) {
			cloneCollection = new ArrayList<>();
			while (resultSet.next()) {
				Clone clone = new CloneRowHandler().handler(resultSet);
				clone.setAdditionals(fillAdditionals(clone.getId(), connection));
				cloneCollection.add(clone);
			}

			LOGGER.info("End process DAO selectAll");
			return cloneCollection;
		}
	}

}
