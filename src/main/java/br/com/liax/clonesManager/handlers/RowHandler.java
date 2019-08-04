package br.com.liax.clonesManager.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowHandler<T> {

	public T handler(ResultSet resultSet) throws SQLException;
}
