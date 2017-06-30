package kkr.job.file2base.batchs.file2base;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import kkr.common.components.datasource.DataSource;
import kkr.common.errors.ConfigurationException;
import kkr.job.file2base.domains.common.components.iterableinput.IterableInputFactory;
import kkr.job.file2base.domains.common.components.validator.Validator;

public abstract class BatchJobFile2BaseFwk {
	private boolean configured;

	protected File dirInput;
	protected File dirOutput;
	protected File dirError;
	protected IterableInputFactory iterableInputFactory;
	protected Validator validator;
	protected DataSource dataSource;

	protected String _dateFormat;
	protected DateFormat dateFormat;

	public void config() throws ConfigurationException {
		configured = false;
		if (dirInput == null) {
			throw new ConfigurationException("Parameter 'dirInput' is not configured");
		}
		if (dirOutput == null) {
			throw new ConfigurationException("Parameter 'dirOutput' is not configured");
		}
		if (dirError == null) {
			throw new ConfigurationException("Parameter 'dirError' is not configured");
		}

		if (iterableInputFactory == null) {
			throw new ConfigurationException("Parameter 'iterableInputFactory' is not configured");
		}
		if (validator == null) {
			throw new ConfigurationException("Parameter 'validator' is not configured");
		}
		if (dataSource == null) {
			throw new ConfigurationException("Parameter 'dataSource' is not configured");
		}

		if (_dateFormat == null) {
			throw new ConfigurationException("Parameter 'dateFormat' is not configured");
		} else {
			try {
				dateFormat = new SimpleDateFormat(_dateFormat);
				dateFormat.format(new Date());
			} catch (Exception ex) {
				throw new ConfigurationException("Parameter 'dateFormat' has bad value: " + _dateFormat);
			}
		}

		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": The component is not configured");
		}
	}

	public String getDateFormat() {
		return _dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this._dateFormat = dateFormat;
	}

	public File getDirInput() {
		return dirInput;
	}

	public void setDirInput(File dirInput) {
		this.dirInput = dirInput;
	}

	public File getDirOutput() {
		return dirOutput;
	}

	public void setDirOutput(File dirOutput) {
		this.dirOutput = dirOutput;
	}

	public File getDirError() {
		return dirError;
	}

	public void setDirError(File dirError) {
		this.dirError = dirError;
	}

	public IterableInputFactory getIterableInputFactory() {
		return iterableInputFactory;
	}

	public void setIterableInputFactory(IterableInputFactory iterableInputFactory) {
		this.iterableInputFactory = iterableInputFactory;
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
