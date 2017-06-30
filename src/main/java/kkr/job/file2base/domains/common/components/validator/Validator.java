package kkr.job.file2base.domains.common.components.validator;

import java.util.Map;

import kkr.common.errors.BaseException;

public interface Validator {
	ValidatorResult validate(Map<String, String> data) throws BaseException;

}
