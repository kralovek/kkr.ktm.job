package kkr.job.file2base.batchs.file2base;
import java.io.File;
import java.io.FileFilter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import kkr.common.errors.BaseException;
import kkr.common.errors.DatabaseException;
import kkr.common.errors.DatabaseQueryException;
import kkr.common.errors.FunctionalException;
import kkr.common.errors.TechnicalException;
import kkr.common.utils.UtilsDatabase;
import kkr.common.utils.UtilsResource;
import kkr.job.file2base.domains.common.components.iterableinput.IterableInput;
import kkr.job.file2base.domains.common.components.iterableinput.IterableInputException;
import kkr.job.file2base.domains.common.components.validator.ValidatorItemError;
import kkr.job.file2base.domains.common.components.validator.ValidatorResult;

public class BatchJobFile2Base extends BatchJobFile2BaseFwk {
	private static final Logger LOG = Logger.getLogger(BatchJobFile2Base.class);

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");

	private static final String COLUMN_STRING = "COLUMN_STRING";
	private static final String COLUMN_INTEGER = "COLUMN_INTEGER";
	private static final String COLUMN_DOUBLE = "COLUMN_DOUBLE";
	private static final String COLUMN_DATE = "COLUMN_DATE";

	private static final FileFilter FILE_FILTER = new FileFilter() {
		public boolean accept(File file) {
			if (!file.isFile()) {
				return false;
			}
			return file.getName().toLowerCase().endsWith(".csv");
		}
	};

	public void run() throws BaseException {
		LOG.trace("BEGIN");
		try {
			String batchId = DATE_FORMAT.format(new Date());

			Connection connection = null;
			try {
				connection = dataSource.getConnection();

				Collection<File> files = loadFiles();

				for (File file : files) {
					workFile(batchId, connection, file);
				}

				connection.close();
				connection = null;
			} catch (SQLException ex) {
				throw new DatabaseException(dataSource, "Cannot connect to database", ex);
			} finally {
				UtilsResource.closeResource(connection);
			}

			LOG.trace("OK");
		} finally {
			LOG.trace("END");
		}
	}

	private void workFile(String batchId, Connection connection, File file) throws BaseException {
		LOG.trace("BEGIN: " + file.getAbsolutePath());
		try {
			IterableInput<String> iterableInput = iterableInputFactory.createInstance(file.getAbsolutePath());

			int iLine = 0;
			int iLineKo = 0;

			try {
				iterableInput.open();

				for (iLine = 0; iterableInput.hasNext(); iLine++) {
					Map<String, String> data = iterableInput.readNext();
					if (!workLine(batchId, connection, file, iLine, data)) {
						iLineKo++;
					}
				}

				iterableInput.close();
			} catch (IterableInputException ex) {
				throw new TechnicalException("Cannot read the file: " + file.getAbsolutePath(), ex);
			} finally {
				iterableInput.close();

			}

			reportFileResult(batchId, connection, file, iLine, iLineKo);
			moveFile(batchId, file, iLineKo == 0);

			LOG.trace("OK");
		} finally {
			LOG.trace("END: " + file.getAbsolutePath());
		}
	}

	private boolean workLine(String batchId, Connection connection, File file, int iLine, Map<String, String> data) throws BaseException {
		LOG.trace("BEGIN");
		try {
			ValidatorResult validatorResult = validator.validate(data);

			boolean retval = validatorResult.getErrors().isEmpty();

			if (retval) {
				Integer itemInteger = null;
				try {
					itemInteger = Integer.parseInt(data.get(COLUMN_INTEGER));
				} catch (Exception ex) {
					throw new IllegalStateException("Unexpected data conversion problem", ex);
				}

				if (checkUnicity(connection, itemInteger)) {
					writeData(batchId, connection, data);
				} else {
					retval = false;
					reportLineError(batchId, connection, file, iLine, COLUMN_INTEGER + " is not unique: " + itemInteger);
				}
			} else {
				for (Map.Entry<String, ValidatorItemError> entry : validatorResult.getErrors().entrySet()) {
					reportItemError(batchId, connection, file, iLine, entry.getKey(), entry.getValue().getValue(), entry.getValue().getReason());
				}
			}
			LOG.trace("OK");
			return retval;
		} finally {
			LOG.trace("END");
		}
	}

	private void writeData(String batchId, Connection connection, Map<String, String> data) throws BaseException {
		LOG.trace("BEGIN");
		try {
			Integer itemInteger = null;
			String itemString = null;
			Double itemDouble = null;
			Date itemDate = null;

			try {
				itemInteger = Integer.parseInt(data.get(COLUMN_INTEGER));
				itemString = data.get(COLUMN_STRING);
				itemDouble = Double.parseDouble(data.get(COLUMN_DOUBLE));
				itemDate = dateFormat.parse(data.get(COLUMN_DATE));
			} catch (Exception ex) {
				throw new IllegalStateException("Unexpected data conversion problem", ex);
			}

			String query = "INSERT INTO TABLE_DATA (\"ID\", \"BATCH_ID\", \"DATE\", \"" + COLUMN_INTEGER + "\", \"" + COLUMN_STRING + "\", \""
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

				LOG.info("QUERY: " + queryLog);

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

	private boolean checkUnicity(Connection connection, int id) throws BaseException {
		LOG.trace("BEGIN");
		try {
			boolean retval;
			String query = "SELECT COUNT(*) FROM TABLE_DATA WHERE \"" + COLUMN_INTEGER + "\" = ?";
			String queryLog = query;

			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			try {
				preparedStatement = connection.prepareStatement(query);

				int iItem = 0;
				preparedStatement.setInt(++iItem, id);

				queryLog = UtilsDatabase.adaptQueryForLog(query, id);

				LOG.info("QUERY: " + queryLog);

				resultSet = preparedStatement.executeQuery();
				resultSet.next();
				int count = resultSet.getInt(1);
				retval = count == 0;

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

	private void reportLineError(String batchId, Connection connection, File file, int iLine, String reason) throws BaseException {
		LOG.trace("BEGIN");
		try {
			String query = "INSERT INTO TABLE_ERROR (\"ID\", \"BATCH_ID\", \"DATE\", \"FILE\", \"LINE\", \"REASON\") VALUES (SEQ_DATA.NEXTVAL, ?, ?, ?, ?, ?)";
			String queryLog = query;

			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = connection.prepareStatement(query);

				Date date = new Date();
				int iItem = 0;
				preparedStatement.setString(++iItem, batchId);
				preparedStatement.setTimestamp(++iItem, new Timestamp(date.getTime()));
				preparedStatement.setString(++iItem, file.getName());
				preparedStatement.setInt(++iItem, iLine);
				preparedStatement.setString(++iItem, reason);

				queryLog = UtilsDatabase.adaptQueryForLog(query, //
						batchId, //
						date, //
						file.getName(), //
						iLine, //
						reason);

				LOG.info("QUERY: " + queryLog);

				int affectedLines = preparedStatement.executeUpdate();

				LOG.debug("Inserted lines: " + affectedLines);

				preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException ex) {
				new DatabaseQueryException(dataSource, queryLog, "Cannot insert line to error table", ex);
			} finally {
				UtilsResource.closeResource(preparedStatement);
			}
			LOG.trace("OK");
		} finally {
			LOG.trace("END");
		}
	}

	private void reportItemError(String batchId, Connection connection, File file, int iLine, String name, String value, String reason)
			throws BaseException {
		LOG.trace("BEGIN");
		try {
			String query = "INSERT INTO TABLE_ERROR (\"ID\", \"BATCH_ID\", \"DATE\", \"FILE\", \"LINE\", \"ITEM\", \"VALUE\", \"REASON\") VALUES (SEQ_DATA.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
			String queryLog = query;

			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = connection.prepareStatement(query);

				Date date = new Date();
				int iItem = 0;
				preparedStatement.setString(++iItem, batchId);
				preparedStatement.setTimestamp(++iItem, new Timestamp(date.getTime()));
				preparedStatement.setString(++iItem, file.getName());
				preparedStatement.setInt(++iItem, iLine);
				preparedStatement.setString(++iItem, name);
				preparedStatement.setString(++iItem, value);
				preparedStatement.setString(++iItem, reason);

				queryLog = UtilsDatabase.adaptQueryForLog(query, //
						batchId, //
						date, //
						file.getName(), //
						iLine, //
						name, //
						value, //
						reason);

				LOG.info("QUERY: " + queryLog);

				int affectedLines = preparedStatement.executeUpdate();

				LOG.debug("Inserted lines: " + affectedLines);

				preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException ex) {
				new DatabaseQueryException(dataSource, queryLog, "Cannot insert line to error table", ex);
			} finally {
				UtilsResource.closeResource(preparedStatement);
			}
			LOG.trace("OK");
		} finally {
			LOG.trace("END");
		}
	}

	private void reportFileResult(String batchId, Connection connection, File file, int countLines, int countLinesError) throws BaseException {
		LOG.trace("BEGIN");
		try {
			String query = "INSERT INTO TABLE_FILE (\"ID\", \"BATCH_ID\", \"DATE\", \"FILE\", \"COUNT_LINES\", \"COUNT_LINES_ERROR\") VALUES (SEQ_DATA.NEXTVAL, ?, ?, ?, ?, ?)";
			String queryLog = query;

			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = connection.prepareStatement(query);

				Date date = new Date();
				int iItem = 0;
				preparedStatement.setString(++iItem, batchId);
				preparedStatement.setTimestamp(++iItem, new Timestamp(date.getTime()));
				preparedStatement.setString(++iItem, file.getName());
				preparedStatement.setInt(++iItem, countLines);
				preparedStatement.setInt(++iItem, countLinesError);

				queryLog = UtilsDatabase.adaptQueryForLog(query, // 
						batchId, //
						date, //
						file.getName(), //
						countLines, //
						countLinesError);

				LOG.info("QUERY: " + queryLog);

				int affectedLines = preparedStatement.executeUpdate();

				LOG.debug("Inserted lines: " + affectedLines);

				preparedStatement.close();
				preparedStatement = null;
			} catch (SQLException ex) {
				new DatabaseQueryException(dataSource, queryLog, "Cannot insert line to error table", ex);
			} finally {
				UtilsResource.closeResource(preparedStatement);
			}
			LOG.trace("OK");
		} finally {
			LOG.trace("END");
		}
	}

	private void moveFile(String batchId, File file, boolean ok) throws BaseException {
		LOG.trace("BEGIN");
		try {
			File fileTarget;

			if (ok) {
				LOG.info("Moving file to Output directory: " + file.getName());
				fileTarget = new File(dirOutput, batchId + ".OK." + file.getName());
			} else {
				LOG.info("Moving file to Error directory: " + file.getName());
				fileTarget = new File(dirError, batchId + ".ERROR." + file.getName());
			}

			if (!file.renameTo(fileTarget)) {
				throw new TechnicalException("Cannot move file " + file.getAbsolutePath() + " to " + fileTarget.getAbsolutePath());
			}

			LOG.trace("OK");
		} finally {
			LOG.trace("END");
		}
	}

	private Collection<File> loadFiles() throws BaseException {
		LOG.trace("BEGIN");
		try {
			if (!dirInput.isDirectory()) {
				throw new FunctionalException("Directory does not exist: " + dirInput.getAbsolutePath());
			}

			File[] files = dirInput.listFiles(FILE_FILTER);

			if (files == null) {
				throw new TechnicalException("Cannot access the directory: " + dirInput.getAbsolutePath());
			}

			Collection<File> retval = new ArrayList<File>();

			for (File file : files) {
				retval.add(file);
			}

			LOG.trace("OK");
			return retval;
		} finally {
			LOG.trace("END");
		}
	}
}
