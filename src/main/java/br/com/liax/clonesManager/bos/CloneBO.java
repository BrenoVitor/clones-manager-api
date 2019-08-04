package br.com.liax.clonesManager.bos;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.liax.clonesManager.daos.CloneDAO;
import br.com.liax.clonesManager.exceptions.AgeNotIntervalException;
import br.com.liax.clonesManager.exceptions.NameNotMatchedException;
import br.com.liax.clonesManager.models.Clone;
import br.com.liax.clonesManager.utils.LoadProperties;

public class CloneBO {
	
	private static final Logger LOGGER = LogManager.getLogger(CloneBO.class);

	private static final String AGE_NOT_INTERVAL_DEFAULT_MESSAGE = "A idade do clone está na forma correta, por favor informa uma idade de no minimo => [ %d ] e no máximo => [ %d ]";

	private static final String AGE_NOT_INTERVAL_MESSAGE = "age.not.interval";

	private static final String NAME_NOT_MATCHED_DEFAULT_MESSAGE = "O nome do clone não está na forma correta, por favor utilize o formato => [ %s ]";

	private static final String NAME_NOT_MATCHED_MESSAGE = "name.not.macthed";

	private static final String DEAFULT_CLONE_NAME_REGEX = "[A-Z]{3}[0-9]{4}";

	private static final String DEFAULT_CLONE_MAX_AGE = "20";

	private static final String CLONE_MAX_AGE = "clone.max.age";

	private static final String DEFAULT_CLONE_MIN_AGE = "10";

	private static final String CLONE_MIN_AGE = "clone.min.age";

	private static final String CLONE_NAME_REGEX = "clone.name.regex";

	private static final Properties PROPERTIES = LoadProperties.getProperties();

	public static long create(final Clone clone)
			throws ClassNotFoundException, SQLException, NameNotMatchedException, AgeNotIntervalException, PropertyVetoException {
		LOGGER.info("Begin process BO create");
		generateDate(clone);
		long id = isCloneValid(clone) ? CloneDAO.create(clone) : 0;
		LOGGER.info("End process BO create");
		return id;
	}

	public static long update(final Clone clone)
			throws ClassNotFoundException, SQLException, NameNotMatchedException, AgeNotIntervalException, PropertyVetoException {
		LOGGER.info("Begin process BO update");
		long rowsChanged = isCloneValid(clone) ? CloneDAO.update(clone) : 0;
		LOGGER.info("End process BO update");
		return rowsChanged;
	}

	public static long delete(final long id) throws ClassNotFoundException, SQLException, PropertyVetoException {
		LOGGER.info("Begin process BO delete");
		long rowsChanged = CloneDAO.delete(id);
		LOGGER.info("End process BO delete");
		return rowsChanged;
	}

	public static Clone selectById(final long id) throws ClassNotFoundException, SQLException, PropertyVetoException {
		LOGGER.info("Begin process BO selectById");
		Clone clone = CloneDAO.selectById(id);
		LOGGER.info("End process BO selectById");
		return clone;
	}

	public static Collection<Clone> selectAll() throws ClassNotFoundException, SQLException, PropertyVetoException {
		LOGGER.info("Begin process BO selectAll");
		Collection<Clone> clones = CloneDAO.selectAll();
		LOGGER.info("End process BO selectAll");
		return clones;
	}

	private static void generateDate(final Clone clone) {
		LOGGER.info("Begin process BO generateDate");
		clone.setCreatedAt(new Date());
		LOGGER.info("End process BO generateDate");
	}

	private static boolean isNameValid(final Clone clone) throws NameNotMatchedException {
		LOGGER.info("Begin process BO isNameValid");
		final String REGEX_NAME = PROPERTIES.getProperty(CLONE_NAME_REGEX, DEAFULT_CLONE_NAME_REGEX);
		Pattern pattern = Pattern.compile(REGEX_NAME);
		Matcher matcher = pattern.matcher(clone.getName());
		boolean isMatch = matcher.find();
		if (!isMatch) {
			throw new NameNotMatchedException(String.format(
					PROPERTIES.getProperty(NAME_NOT_MATCHED_MESSAGE, NAME_NOT_MATCHED_DEFAULT_MESSAGE), REGEX_NAME));
		}
		LOGGER.info("End process BO isNameValid");
		return isMatch;
	}

	private static boolean isCloneValid(final Clone clone) throws NameNotMatchedException, AgeNotIntervalException {
		LOGGER.info("Begin process BO isCloneValid");
		boolean isCloneValid = isNameValid(clone) && isAgeValid(clone);
		LOGGER.info("End process BO isCloneValid");
		return isCloneValid;
	}

	private static boolean isAgeValid(final Clone clone) throws AgeNotIntervalException {
		LOGGER.info("Begin process BO isAgeValid");
		final String MIN_AGE = PROPERTIES.getProperty(CLONE_MIN_AGE, DEFAULT_CLONE_MIN_AGE);
		final String MAX_AGE = PROPERTIES.getProperty(CLONE_MAX_AGE, DEFAULT_CLONE_MAX_AGE);
		boolean isAgeValid = clone.getAge() >= Integer.parseInt(MIN_AGE) && clone.getAge() <= Integer.parseInt(MAX_AGE);
		if (!isAgeValid) {
			throw new AgeNotIntervalException(
					String.format(PROPERTIES.getProperty(AGE_NOT_INTERVAL_MESSAGE, AGE_NOT_INTERVAL_DEFAULT_MESSAGE),
					MIN_AGE, MAX_AGE));
		}
		LOGGER.info("End process BO isAgeValid");
		return isAgeValid;
	}

}
