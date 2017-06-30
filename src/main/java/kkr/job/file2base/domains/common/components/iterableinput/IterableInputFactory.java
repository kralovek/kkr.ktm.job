package kkr.job.file2base.domains.common.components.iterableinput;

import kkr.common.errors.BaseException;

public interface IterableInputFactory {

	IterableInput createInstance(String source) throws BaseException;
}
