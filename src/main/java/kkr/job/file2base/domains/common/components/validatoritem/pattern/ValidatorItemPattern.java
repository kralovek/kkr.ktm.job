package kkr.job.file2base.domains.common.components.validatoritem.pattern;

import org.apache.log4j.Logger;

import kkr.job.file2base.domains.common.components.validatoritem.ValidatorItem;
import kkr.common.errors.BaseException;

public class ValidatorItemPattern extends ValidatorItemPatternFwk implements ValidatorItem {
	private static final Logger LOG = Logger.getLogger(ValidatorItemPattern.class);

	public boolean validate(String value) throws BaseException {
		if (value == null || value.isEmpty()) {
			return true;
		}
		return pattern.matcher(value).matches();
	}
}
