package kkr.job.file2base.domains.business.transformator.dependency;

import kkr.common.errors.ConfigurationException;
import kkr.job.file2base.domains.business.transformator.base.TransformatorBase;

public abstract class TransformatorDependencyFwk extends TransformatorBase {
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
