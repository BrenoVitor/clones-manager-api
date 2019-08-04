package br.com.liax.clonesManager.bos;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.liax.clonesManager.daos.AdditionalDAO;
import br.com.liax.clonesManager.exceptions.AgeNotIntervalException;
import br.com.liax.clonesManager.exceptions.NameNotMatchedException;
import br.com.liax.clonesManager.models.Additional;

public class AdditionalBO {

	private static final Logger LOGGER = LogManager.getLogger(AdditionalBO.class);

	public static long create(Additional additional)
			throws ClassNotFoundException, SQLException, NameNotMatchedException, AgeNotIntervalException, PropertyVetoException {
		LOGGER.info("Begin process BO create");
		long id = AdditionalDAO.create(additional);
		LOGGER.info("End process BO create");
		return id;
	}

	public static long update(Additional additional)
			throws ClassNotFoundException, SQLException, NameNotMatchedException, AgeNotIntervalException, PropertyVetoException {
		LOGGER.info("Begin process BO update");
		long rowsChanged = AdditionalDAO.update(additional);
		LOGGER.info("End process BO update");
		return rowsChanged;
	}

	public static long delete(long id) throws ClassNotFoundException, SQLException, PropertyVetoException {
		LOGGER.info("Begin process BO delete");
		long rowsChanged = AdditionalDAO.delete(id);
		LOGGER.info("End process BO delete");
		return rowsChanged;
	}

	public static Additional selectById(long id) throws ClassNotFoundException, SQLException, PropertyVetoException {
		LOGGER.info("Begin process BO selectById");
		Additional additional = AdditionalDAO.selectById(id);
		LOGGER.info("End process BO selectById");
		return additional;
	}

	public static Collection<Additional> selectAll() throws ClassNotFoundException, SQLException, PropertyVetoException {
		LOGGER.info("Begin process BO selectAll");
		Collection<Additional> additionals = AdditionalDAO.selectAll();
		LOGGER.info("End process BO selectAll");
		return additionals;
	}

}
