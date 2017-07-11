package kkr.job.file2base.domains.business.transformator.base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import kkr.common.errors.ConfigurationException;

public abstract class TransformatorBaseFwk {
	private boolean configured;

	protected String table;
	protected String columnUnique;

	protected String _dateFormat;
	protected DateFormat dateFormat;

	public void config() throws ConfigurationException {
		configured = false;
		if (columnUnique == null) {
			throw new ConfigurationException("Parameter 'columnUnique' is not configured");
		}
		if (table == null) {
			throw new ConfigurationException("Parameter 'table' is not configured");
		}

		if (_dateFormat == null) {
			throw new ConfigurationException("Parameter 'dateFormat' is not configured");
		} else {
			try {
				dateFormat = new SimpleDateFormat(_dateFormat);
				dateFormat.format(new Date());
			} catch (Exception ex) {
				throw new ConfigurationException("Parameter 'dateFormat' has bad value: " + _dateFormat);
			}
		}

		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": The component is not configured");
		}
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getColumnUnique() {
		return columnUnique;
	}

	public void setColumnUnique(String columnUnique) {
		this.columnUnique = columnUnique;
	}

	public String getDateFormat() {
		return _dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this._dateFormat = dateFormat;
	}
}
