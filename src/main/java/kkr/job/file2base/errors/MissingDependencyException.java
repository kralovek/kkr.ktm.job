package kkr.job.file2base.errors;

import kkr.common.errors.BaseException;

public class MissingDependencyException extends BaseException {

	private String tablePk;
	private String columnPk;
	private String tableFk;
	private String columnFk;
	private Object value;;

	public MissingDependencyException(String tablePk, String columnPk, String tableFk, String columnFk, Object value) {
		super("Missing dependency. " + tableFk + "." + columnFk + " is not referenced in " + tablePk + "." + columnPk + ": " + value);
		this.tablePk = tablePk;
		this.columnPk = columnPk;
		this.tableFk = tableFk;
		this.columnFk = columnFk;
		this.value = value;
	}

	public String getTablePk() {
		return tablePk;
	}

	public String getColumnPk() {
		return columnPk;
	}

	public String getTableFk() {
		return tableFk;
	}

	public String getColumnFk() {
		return columnFk;
	}

	public Object getValue() {
		return value;
	}
}
