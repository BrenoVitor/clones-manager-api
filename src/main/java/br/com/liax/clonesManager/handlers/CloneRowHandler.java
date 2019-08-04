package br.com.liax.clonesManager.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.liax.clonesManager.models.Clone;

public class CloneRowHandler implements RowHandler<Clone> {

	private static final String CREATEDAT = "CREATEDAT";
	private static final String AGE = "AGE";
	private static final String NAME = "NAME";
	private static final String ID = "ID";

	@Override
	public Clone handler(ResultSet resultSet) throws SQLException {
		Clone clone = new Clone();
		clone.setId(resultSet.getLong(ID));
		clone.setName(resultSet.getString(NAME));
		clone.setAge(resultSet.getInt(AGE));
		clone.setCreatedAt(resultSet.getTimestamp(CREATEDAT));
		return clone;
	}

}
