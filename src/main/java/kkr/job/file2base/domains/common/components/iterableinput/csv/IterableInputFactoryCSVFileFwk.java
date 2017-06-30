package kkr.job.file2base.domains.common.components.iterableinput.csv;

import java.io.File;

import kkr.common.errors.ConfigurationException;

public abstract class IterableInputFactoryCSVFileFwk extends IterableInputCSVFileFwk {
	private boolean configured;

	public void config() throws ConfigurationException {
		configured = false;
		File orgFile = file;
		file = new File("x");
		super.config();
		file = orgFile;
		configured = true;
	}

	public void testConfigured() {
		super.testConfigured();
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": The component is not configured");
		}
	}
}
