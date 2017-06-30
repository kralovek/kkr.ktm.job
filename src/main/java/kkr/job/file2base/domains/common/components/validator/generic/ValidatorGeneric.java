package kkr.job.file2base.domains.common.components.validator.generic;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import kkr.common.errors.BaseException;
import kkr.job.file2base.domains.common.components.validator.Validator;
import kkr.job.file2base.domains.common.components.validator.ValidatorItemError;
import kkr.job.file2base.domains.common.components.validator.ValidatorResult;
import kkr.job.file2base.domains.common.components.validatoritem.ValidatorItem;

public class ValidatorGeneric extends ValidatorGenericFwk implements Validator {
	private static final Logger LOG = Logger.getLogger(ValidatorGeneric.class);

	public ValidatorResult validate(Map<String, String> data) throws BaseException {
		LOG.trace("BEGIN");
		try {
			Map<String, String> dataCompleted = completeData(data);

			ValidatorResult retval = new ValidatorResult();
			for (Map.Entry<String, String> entry : dataCompleted.entrySet()) {
				ValidatorItemError error = validateItem(entry.getKey(), entry.getValue());
				if (error != null) {
					retval.getErrors().put(entry.getKey(), error);
				}
			}
			LOG.trace("OK");
			return retval;
		} finally {
			LOG.trace("END");
		}
	}

	private Map<String, String> completeData(Map<String, String> data) {
		Map<String, String> retval = new HashMap<String, String>(data);

		for (String item : validatorItems.keySet()) {
			if (!retval.containsKey(item)) {
				retval.put(item, null);
			}
		}

		return retval;
	}

	public ValidatorItemError validateItem(String name, String value) throws BaseException {
		LOG.trace("BEGIN");
		try {
			Collection<ValidatorItem> typeValidatorItems = validatorItems.get(name);

			if (typeValidatorItems == null) {
				ValidatorItemError error = new ValidatorItemError(value, "Unknown item");
				LOG.trace("OK");
				return error;
			}

			for (ValidatorItem validatorItem : typeValidatorItems) {
				if (!validatorItem.validate(value)) {
					ValidatorItemError error = new ValidatorItemError(value, validatorItem.getName());
					LOG.trace("OK");
					return error;
				}
			}

			LOG.trace("OK");
			return null;
		} finally {
			LOG.trace("END");
		}
	}
}
