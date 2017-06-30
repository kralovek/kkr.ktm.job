package kkr.job.file2base.domains.common.components.validatoritem.mandatory;

import org.apache.log4j.Logger;

import kkr.job.file2base.domains.common.components.validatoritem.ValidatorItem;
import kkr.common.errors.BaseException;

public class ValidatorMandatory extends ValidatorMandatoryFwk implements ValidatorItem {
	private static final Logger LOG = Logger.getLogger(ValidatorMandatory.class);

	public boolean validate(String value) throws BaseException {
		testConfigured();
		return value != null && !value.isEmpty();
	}
}
