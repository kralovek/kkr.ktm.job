package kkr.job.file2base.domains.business.transformator.data;

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
import kkr.common.utils.UtilsString;
import kkr.job.file2base.domains.business.transformator.Transformator;
import kkr.job.file2base.errors.NotUniqueException;

public class TransformatorData extends TransformatorDataFwk implements Transformator {
	private static final Logger LOG = Logger.getLogger(TransformatorData.class);

	private static final String COLUMN_STRING = "COLUMN_STRING";
	private static final String COLUMN_DOUBLE = "COLUMN_DOUBLE";
	private static final String COLUMN_DATE = "COLUMN_DATE";

	public void writeData(String batchId, DataSource dataSource, Connection connection, Map<String, String> data) throws BaseException {
		LOG.trace("BEGIN");
		try {
			Integer itemInteger = null;
			String itemString = null;
			Double itemDouble = null;
			Date itemDate = null;

			try {
				String valueInteger = data.get(columnUnique);
				String valueString = data.get(COLUMN_STRING);
				String valueDouble = data.get(COLUMN_DOUBLE);
				String valueDate = data.get(COLUMN_DATE);

				itemInteger = Integer.parseInt(valueInteger);
				itemString = valueString;
				if (!UtilsString.isEmpty(valueDouble)) {
					itemDouble = Double.parseDouble(valueDouble);
				}
				if (!UtilsString.isEmpty(valueDate)) {
					itemDate = dateFormat.parse(valueDate);
				}
			} catch (Exception ex) {
				throw new IllegalStateException("Unexpected data conversion problem", ex);
			}

			if (exists(dataSource, connection, table, columnUnique, itemInteger)) {
				throw new NotUniqueException(table, columnUnique, itemInteger);
			}

			String query = "INSERT INTO " + table + " (\"ID\", \"BATCH_ID\", \"DATE\", \"" + columnUnique + "\", \"" + COLUMN_STRING + "\", \""
					+ COLUMN_DOUBLE + "\", \"" + COLUMN_DATE + "\") VALUES (SEQ_DATA.NEXTVAL, ?, ?, ?, ?, ?, ?)";
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
				if (itemDouble != null) {
					preparedStatement.setDouble(++iItem, itemDouble);
				} else {
					preparedStatement.setNull(++iItem, Types.DOUBLE);
				}
				if (itemDate != null) {
					preparedStatement.setTimestamp(++iItem, new Timestamp(itemDate.getTime()));
				} else {
					preparedStatement.setNull(++iItem, Types.TIMESTAMP);
				}

				queryLog = UtilsDatabase.adaptQueryForLog(query, // 
						batchId, //
						date, //
						itemInteger, //
						itemString, //
						itemDouble, //
						itemDate);

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
