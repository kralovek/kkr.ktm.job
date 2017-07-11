package kkr.job.file2base.batchs.file2base;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

import kkr.common.components.datasource.DataSource;
import kkr.common.errors.ConfigurationException;
import kkr.common.utils.UtilsConfig;
import kkr.job.file2base.domains.business.transformator.Transformator;
import kkr.job.file2base.domains.common.components.iterableinput.IterableInputFactory;
import kkr.job.file2base.domains.common.components.validator.Validator;

public abstract class BatchJobFile2BaseFwk {
	private boolean configured;

	protected File dirInput;
	protected File dirOutput;
	protected File dirError;
	protected IterableInputFactory<String> iterableInputFactory;
	protected Validator validator;
	protected DataSource dataSource;
	protected Transformator transformator;

	protected String _pattern;
	protected FileFilter fileFilter;

	private static class FileFilterPattern implements FileFilter {
		private Pattern pattern;
		public FileFilterPattern(Pattern pattern) {
			this.pattern = pattern;
		}
		public boolean accept(File file) {
			if (!file.isFile()) {
				return false;
			}
			return (pattern.matcher(file.getName()).matches());
		}
	};

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

		if (transformator == null) {
			throw new ConfigurationException("Parameter 'transformator' is not configured");
		}
		if (_pattern == null) {
			throw new ConfigurationException("Parameter 'pattern' is not configured");
		} else {
			Pattern pattern = UtilsConfig.checkPattern(_pattern, "pattern");
			fileFilter = new FileFilterPattern(pattern);
		}

		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": The component is not configured");
		}
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

	public IterableInputFactory<String> getIterableInputFactory() {
		return iterableInputFactory;
	}

	public void setIterableInputFactory(IterableInputFactory<String> iterableInputFactory) {
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

	public Transformator getTransformator() {
		return transformator;
	}

	public void setTransformator(Transformator transformator) {
		this.transformator = transformator;
	}

	public String getPattern() {
		return _pattern;
	}

	public void setPattern(String pattern) {
		this._pattern = pattern;
	}
}
