package kkr.job.file2base.domains.common.components.validatoritem;

import kkr.common.errors.BaseException;

public interface ValidatorItem {

	String getName();

	boolean validate(String value) throws BaseException;
}
