package kkr.job.file2base.domains.common.components.validatoritem.mandatory;

import kkr.job.file2base.domains.common.components.validatoritem.base.ValidatorBase;
import kkr.common.errors.ConfigurationException;

public abstract class ValidatorMandatoryFwk extends ValidatorBase {
	private boolean configured;

	public void config() throws ConfigurationException {
		configured = false;
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": The component is not configured");
		}
	}
}
