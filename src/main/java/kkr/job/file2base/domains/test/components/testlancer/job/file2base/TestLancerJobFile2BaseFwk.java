package kkr.job.file2base.domains.test.components.testlancer.job.file2base;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import kkr.common.errors.ConfigurationException;
import kkr.job.file2base.batchs.file2base.BatchJobFile2Base;
import kkr.ktm.domains.common.components.diffmanager.DiffManager;

public abstract class TestLancerJobFile2BaseFwk {
	private boolean configured;

	protected BatchJobFile2Base batchJobFile2Base;
	protected String sysPrefix;

	protected Map<String, DiffManager> _diffManagers;
	protected Map<Pattern, DiffManager> diffManagers;

	public void config() throws ConfigurationException {
		configured = false;
		if (batchJobFile2Base == null) {
			throw new ConfigurationException("Parameter 'batchJobFile2Base' is not configured");
		}
		if (sysPrefix == null) {
			throw new ConfigurationException("Parameter '_sysPrefix' is not configured");
		}
		diffManagers = new LinkedHashMap<Pattern, DiffManager>();
		if (diffManagers == null) {
			// OK
		} else {
			int i = 0;
			for (Map.Entry<String, DiffManager> entry : _diffManagers.entrySet()) {
				if (entry.getValue() == null) {
					throw new ConfigurationException("Parameter 'diffManagers[" + i + "]' value is null");
				}
				try {
					Pattern pattern = Pattern.compile(entry.getKey());
					diffManagers.put(pattern, entry.getValue());
				} catch (Exception ex) {
					throw new ConfigurationException("Parameter 'diffManagers[" + i + "]' has bad key: " + entry.getKey());
				}
				i++;
			}
		}

		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": The component is not configured");
		}
	}

	public BatchJobFile2Base getBatchJobFile2Base() {
		return batchJobFile2Base;
	}

	public void setBatchJobFile2Base(BatchJobFile2Base batchJobFile2Base) {
		this.batchJobFile2Base = batchJobFile2Base;
	}
}
