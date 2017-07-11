package kkr.job.file2base.domains.business.transformator.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import kkr.common.components.datasource.DataSource;
import kkr.common.errors.BaseException;
import kkr.common.errors.DatabaseQueryException;
import kkr.common.utils.UtilsDatabase;
import kkr.common.utils.UtilsResource;
import kkr.job.file2base.domains.business.transformator.Transformator;

public abstract class TransformatorBase extends TransformatorBaseFwk implements Transformator {
	private static final Logger LOG = Logger.getLogger(TransformatorBase.class);

	protected boolean exists(DataSource dataSource, Connection connection, String table, String column, int id) throws BaseException {
		LOG.trace("BEGIN");
		try {
			boolean retval;
			String query = "SELECT COUNT(*) FROM " + table + " WHERE \"" + column + "\" = ?";
			String queryLog = query;

			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			try {
				preparedStatement = connection.prepareStatement(query);

				int iItem = 0;
				preparedStatement.setInt(++iItem, id);

				queryLog = UtilsDatabase.adaptQueryForLog(query, id);

				LOG.debug("QUERY: " + queryLog);

				resultSet = preparedStatement.executeQuery();
				resultSet.next();
				int count = resultSet.getInt(1);
				retval = count != 0;

				resultSet.close();
				resultSet = null;

				preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException ex) {
				throw new DatabaseQueryException(dataSource, queryLog, "Cannot insert line to error table", ex);
			} finally {
				UtilsResource.closeResource(resultSet);
				UtilsResource.closeResource(preparedStatement);
			}
			LOG.trace("OK");
			return retval;
		} finally {
			LOG.trace("END");
		}
	}
}
