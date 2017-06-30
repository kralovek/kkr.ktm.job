package kkr.job.file2base.domains.common.components.validatoritem.base;

import kkr.common.errors.ConfigurationException;

public abstract class ValidatorBaseFwk {
	private boolean configured;

	protected String name;

	public void config() throws ConfigurationException {
		configured = false;

		if (name == null) {
			throw new ConfigurationException("Parameter 'name' is not configured");
		}

		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": The component is not configured");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
