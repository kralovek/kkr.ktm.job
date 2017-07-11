package kkr.job.file2base.domains.business.transformator;

import java.sql.Connection;
import java.util.Map;

import kkr.common.components.datasource.DataSource;
import kkr.common.errors.BaseException;

public interface Transformator {

	String getTable();

	String getColumnUnique();

	void writeData(String batchId, DataSource dataSource, Connection connection, Map<String, String> data) throws BaseException;
}
