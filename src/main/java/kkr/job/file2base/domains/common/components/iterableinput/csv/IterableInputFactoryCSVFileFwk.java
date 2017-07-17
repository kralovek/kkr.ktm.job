package kkr.job.file2base.domains.common.components.iterableinput.csv;

import kkr.common.errors.ConfigurationException;

public abstract class IterableInputFactoryCSVFileFwk extends IterableInputCSVFileFwk {
	private boolean configured;

	public void config() throws ConfigurationException {
		configured = false;
		super.config();
		configured = true;
	}

	public void configFile() throws ConfigurationException {
		// nothing to do
	}

	public void testConfigured() {
		super.testConfigured();
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": The component is not configured");
		}
	}
}
