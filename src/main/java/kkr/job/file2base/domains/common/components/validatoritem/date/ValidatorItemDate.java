package kkr.job.file2base.domains.common.components.validatoritem.date;

import java.text.ParseException;
import java.util.Date;

import kkr.common.errors.BaseException;
import kkr.job.file2base.domains.common.components.validatoritem.ValidatorItem;

public class ValidatorItemDate extends ValidatorItemDateFwk implements ValidatorItem {

	public boolean validate(String value) throws BaseException {
		try {
			Date valueDate = pattern.parse(value);
			String valueString = pattern.format(valueDate);
			return value.equals(valueString);
		} catch (ParseException e) {
			return false;
		}
	}
}
