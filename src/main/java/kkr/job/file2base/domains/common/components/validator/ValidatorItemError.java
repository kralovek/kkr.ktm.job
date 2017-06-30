package kkr.job.file2base.domains.common.components.validator;

public class ValidatorItemError {

	private String value;
	private String reason;

	public ValidatorItemError(String value, String reason) {
		super();
		this.value = value;
		this.reason = reason;
	}

	public String getValue() {
		return value;
	}

	public String getReason() {
		return reason;
	}
}
