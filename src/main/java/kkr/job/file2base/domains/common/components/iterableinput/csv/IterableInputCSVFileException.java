package kkr.job.file2base.domains.common.components.iterableinput.csv;

import java.io.File;

import kkr.job.file2base.domains.common.components.iterableinput.IterableInputException;

public class IterableInputCSVFileException extends IterableInputException {

	private Integer line;
	private File file;

	public IterableInputCSVFileException() {
		super();
	}

	public IterableInputCSVFileException(String message, Throwable cause) {
		super(message, cause);
	}

	public IterableInputCSVFileException(String message) {
		super(message);
	}

	public IterableInputCSVFileException(Throwable cause) {
		super(cause);
	}

	public Integer getLine() {
		return line;
	}

	public void setLine(Integer line) {
		this.line = line;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
