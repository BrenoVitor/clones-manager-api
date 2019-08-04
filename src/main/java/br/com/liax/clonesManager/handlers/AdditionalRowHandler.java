package br.com.liax.clonesManager.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.liax.clonesManager.models.Additional;

public class AdditionalRowHandler implements RowHandler<Additional> {
	
	private static final String NAME = "A.NAME";
	private static final String ID = "A.ID";

	@Override
	public Additional handler(ResultSet resultSet) throws SQLException {
		Additional additional = new Additional();
		additional.setId(resultSet.getLong(ID));
		additional.setName(resultSet.getString(NAME));
		return additional;
	}

}
