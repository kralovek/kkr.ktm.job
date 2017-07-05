package kkr.job.file2base.domains.common.components.iterableinput;

import java.util.Map;

import kkr.common.errors.BaseException;

public interface IterableInput<T> {

	String getSource();

	void open() throws BaseException, IterableInputException;

	void close();

	boolean isOpened();

	Map<String, T> readNext() throws BaseException;

	boolean hasNext() throws BaseException;
}
