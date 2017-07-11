package kkr.job.file2base.domains.test.components.testlancer.job.file2base;

import java.io.File;

import kkr.common.errors.ConfigurationException;
import kkr.job.file2base.batchs.file2base.BatchJobFile2Base;
import kkr.ktm.common.components.templateloader.TemplateLoader;
import kkr.ktm.domains.common.components.diffmanager.DiffManager;
import kkr.ktm.domains.common.components.parametersformater.ParametersFormatter;
import kkr.ktm.utils.UtilsKtm;

public abstract class TestLancerJobFile2BaseFwk {
	private boolean configured;

	protected File dirInput;
	protected BatchJobFile2Base batchJobFile2Base;

	private String _sysPrefix;
	protected String sysPrefix;

	protected DiffManager diffManager;

	protected TemplateLoader templateLoader;
	protected ParametersFormatter parametersFormatter;

	public void config() throws ConfigurationException {
		configured = false;
		if (dirInput == null) {
			throw new ConfigurationException("Parameter 'dirInput' is not configured");
		}

		if (batchJobFile2Base == null) {
			throw new ConfigurationException("Parameter 'batchJobFile2Base' is not configured");
		}
		sysPrefix = UtilsKtm.checkPrefix(_sysPrefix, "sysPrefix");
		if (diffManager == null) {
			throw new ConfigurationException("Parameter 'diffManager' is not configured");
		}
		if (templateLoader == null) {
			throw new ConfigurationException("Parameter 'templateLoader' is not configured");
		}
		if (parametersFormatter == null) {
			throw new ConfigurationException("Parameter 'parametersFormatter' is not configured");
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

	public DiffManager getDiffManager() {
		return diffManager;
	}

	public void setDiffManager(DiffManager diffManager) {
		this.diffManager = diffManager;
	}

	public String getSysPrefix() {
		return _sysPrefix;
	}

	public void setSysPrefix(String sysPrefix) {
		this._sysPrefix = sysPrefix;
	}

	public TemplateLoader getTemplateLoader() {
		return templateLoader;
	}

	public void setTemplateLoader(TemplateLoader templateLoader) {
		this.templateLoader = templateLoader;
	}

	public ParametersFormatter getParametersFormatter() {
		return parametersFormatter;
	}

	public void setParametersFormatter(ParametersFormatter parametersFormatter) {
		this.parametersFormatter = parametersFormatter;
	}

	public File getDirInput() {
		return dirInput;
	}

	public void setDirInput(File dir) {
		this.dirInput = dir;
	}
}
