package kkr.job.file2base.domains.common.components.validator;

import java.util.LinkedHashMap;
import java.util.Map;

public class ValidatorResult {

	private Map<String, ValidatorItemError> errors = new LinkedHashMap<String, ValidatorItemError>();

	public ValidatorResult() {
	}

	public Map<String, ValidatorItemError> getErrors() {
		return errors;
	}

	public String toString() {
		return "[ERRORS: " + errors.size() + "]";
	}
}
