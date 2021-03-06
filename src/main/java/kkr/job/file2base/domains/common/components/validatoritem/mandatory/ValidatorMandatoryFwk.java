package kkr.job.file2base.domains.common.components.validatoritem.mandatory;

import kkr.common.errors.ConfigurationException;
import kkr.job.file2base.domains.common.components.validatoritem.base.ValidatorBase;

public abstract class ValidatorMandatoryFwk extends ValidatorBase {
	private boolean configured;

	public void config() throws ConfigurationException {
		configured = false;
		super.config();
		configured = true;
	}

	public void testConfigured() {
		super.testConfigured();
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": The component is not configured");
		}
	}
}
