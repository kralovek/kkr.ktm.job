package kkr.job.file2base.errors;

import kkr.common.errors.BaseException;

public class NotUniqueException extends BaseException {

	private String table;
	private String column;
	private Object value;

	public NotUniqueException(String table, String column, Object value) {
		super("Not unique item. " + table + "." + column + ": " + value);
		this.table = table;
		this.column = column;
		this.value = value;
	}

	public String getTable() {
		return table;
	}

	public String getColumn() {
		return column;
	}

	public Object getValue() {
		return value;
	}
}
