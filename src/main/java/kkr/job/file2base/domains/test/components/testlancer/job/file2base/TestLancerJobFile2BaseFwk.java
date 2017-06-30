package kkr.job.file2base.domains.test.components.testlancer.job.file2base;

import kkr.common.errors.ConfigurationException;

public abstract class TestLancerJobFile2BaseFwk {
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
