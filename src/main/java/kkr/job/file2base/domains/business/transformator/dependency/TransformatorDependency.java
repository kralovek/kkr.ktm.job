package kkr.job.file2base.domains.business.transformator.dependency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import kkr.common.components.datasource.DataSource;
import kkr.common.errors.BaseException;
import kkr.common.errors.DatabaseQueryException;
import kkr.common.utils.UtilsDatabase;
import kkr.common.utils.UtilsResource;
import kkr.job.file2base.domains.business.transformator.Transformator;
import kkr.job.file2base.errors.MissingDependencyException;
import kkr.job.file2base.errors.NotUniqueException;

public class TransformatorDependency extends TransformatorDependencyFwk implements Transformator {
	private static final Logger LOG = Logger.getLogger(TransformatorDependency.class);

	private static final String COLUMN_STRING = "COLUMN_STRING";
	private static final String COLUMN_INTEGER_FK = "COLUMN_INTEGER_FK";

	public void writeData(String batchId, DataSource dataSource, Connection connection, Map<String, String> data) throws BaseException {
		LOG.trace("BEGIN");
		try {
			Integer itemInteger = null;
			Integer itemIntegerFk = null;
			String itemString = null;

			try {
				itemInteger = Integer.parseInt(data.get(columnUnique));
				itemIntegerFk = Integer.parseInt(data.get(COLUMN_INTEGER_FK));
				itemString = data.get(COLUMN_STRING);
			} catch (Exception ex) {
				throw new IllegalStateException("Unexpected data conversion problem", ex);
			}

			if (exists(dataSource, connection, table, columnUnique, itemInteger)) {
				throw new NotUniqueException(table, columnUnique, itemInteger);
			}

			if (!exists(dataSource, connection, "TABLE_DATA", "COLUMN_INTEGER", itemIntegerFk)) {
				throw new MissingDependencyException("TABLE_DATA", "COLUMN_INTEGER", table, COLUMN_INTEGER_FK, itemIntegerFk);
			}

			String query = "INSERT INTO " + table + " (\"ID\", \"BATCH_ID\", \"DATE\", \"" + columnUnique + "\", \"" + COLUMN_STRING + "\", \""
					+ COLUMN_INTEGER_FK + "\") VALUES (SEQ_DEPENDENCY.NEXTVAL, ?, ?, ?, ?, ?)";
			String queryLog = query;

			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = connection.prepareStatement(query);

				Date date = new Date();
				int iItem = 0;

				preparedStatement.setString(++iItem, batchId);
				preparedStatement.setTimestamp(++iItem, new Timestamp(date.getTime()));
				preparedStatement.setInt(++iItem, itemInteger);
				if (itemString != null) {
					preparedStatement.setString(++iItem, itemString);
				} else {
					preparedStatement.setNull(++iItem, Types.VARCHAR);
				}
				preparedStatement.setInt(++iItem, itemIntegerFk);

				queryLog = UtilsDatabase.adaptQueryForLog(query, // 
						batchId, //
						date, //
						itemInteger, //
						itemString, //
						itemIntegerFk);

				LOG.debug("QUERY: " + queryLog);

				int affectedLines = preparedStatement.executeUpdate();

				LOG.debug("Inserted lines: " + affectedLines);

				preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException ex) {
				throw new DatabaseQueryException(dataSource, queryLog, "Cannot insert line to error table", ex);
			} finally {
				UtilsResource.closeResource(preparedStatement);
			}
			LOG.trace("OK");
		} finally {
			LOG.trace("END");
		}
	}
}
