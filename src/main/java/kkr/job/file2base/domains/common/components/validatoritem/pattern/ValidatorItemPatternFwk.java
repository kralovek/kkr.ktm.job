package kkr.job.file2base.domains.common.components.validatoritem.pattern;

import java.util.regex.Pattern;

import kkr.job.file2base.domains.common.components.validatoritem.base.ValidatorBase;
import kkr.common.errors.ConfigurationException;

public abstract class ValidatorItemPatternFwk extends ValidatorBase {
	private boolean configured;

	protected String _pattern;
	protected Pattern pattern;

	public void config() throws ConfigurationException {
		configured = false;
		if (_pattern == null) {
			throw new ConfigurationException("Parameter 'pattern' is not configured");
		} else {
			try {
				pattern = Pattern.compile(_pattern);
			} catch (Exception ex) {
				throw new ConfigurationException("Parameter 'pattern' has bad value: " + _pattern);
			}
		}

		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": The component is not configured");
		}
	}

	public String getPattern() {
		return _pattern;
	}

	public void setPattern(String pattern) {
		this._pattern = pattern;
	}
}
