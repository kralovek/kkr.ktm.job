package kkr.job.file2base.domains.common.components.validatoritem.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import kkr.common.errors.ConfigurationException;
import kkr.job.file2base.domains.common.components.validatoritem.base.ValidatorBase;

public abstract class ValidatorItemDateFwk extends ValidatorBase {
	private boolean configured;

	protected String _pattern;
	protected DateFormat pattern;

	public void config() throws ConfigurationException {
		configured = false;
		super.config();

		if (_pattern == null) {
			throw new ConfigurationException("Parameter '_pattern' is not configured");
		} else {
			try {
				pattern = new SimpleDateFormat(_pattern);
				pattern.format(new Date());
			} catch (Exception ex) {
				throw new ConfigurationException("Parameter '_pattern' has bad value: " + _pattern);
			}
		}

		configured = true;
	}

	public void testConfigured() {
		super.testConfigured();
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
