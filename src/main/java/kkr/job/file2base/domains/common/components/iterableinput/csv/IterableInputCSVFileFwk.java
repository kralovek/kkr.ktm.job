package kkr.job.file2base.domains.common.components.iterableinput.csv;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kkr.common.errors.ConfigurationException;

public abstract class IterableInputCSVFileFwk {
	protected static final char SYMBOL_SEPARATOR = ';';
	protected static final char SYMBOL_COMMENT = '#';

	private boolean configured;

	protected File file;

	/**
	 * Number of the first lines to ignore
	 */
	protected Integer ignoredLines;

	/**
	 * Index (start 1) of the header line. Must not be in the ignored block.
	 * May not be defined together with 'header' parameter
	 */
	protected Boolean hasHeader;

	/**
	 * If the file does not contain the header, this list specifies the header (column names)
	 */
	private List<String> header;

	//
	// INNER PARAMETERS
	//

	protected String[] headerArray;
	protected Map<String, Integer> headerOrder;

	protected void config() throws ConfigurationException {
		configured = false;
		if (file == null) {
			throw new ConfigurationException("Parameter 'file' is not configured");
		}
		if (ignoredLines == null) {
			ignoredLines = 0;
		} else if (ignoredLines < 0) {
			throw new ConfigurationException(this.getClass().getName() + ": Le param�tre 'ignoredLines' must be a non negativ integer.");
		}
		if (hasHeader == null) {
			hasHeader = false;
		}
		if (header != null && header.isEmpty()) {
			throw new ConfigurationException(this.getClass().getName() + ": If the parameter 'header' is specified it may not be empty.");
		}
		if (header != null && hasHeader) {
			throw new ConfigurationException(this.getClass().getName()
					+ ": If the parameter 'hasHeader' indicates the presence of the header, the parameter 'header' may not be defined.");
		} else if (header == null && !hasHeader) {
			throw new ConfigurationException(this.getClass().getName()
					+ ": If the parameter 'hasHeader' indicates no presence of the header, the header must be specified by the parameter 'headerLine'.");
		} else if (header != null) {
			headerArray = new String[header.size()];
			headerOrder = new HashMap<String, Integer>();
			for (int i = 0; i < header.size(); i++) {
				headerArray[i] = header.get(i);
				headerOrder.put(header.get(i), i);
			}
		}
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": Le composant n'est pas configur�.");
		}
	}

	public Integer getIgnoredLines() {
		return ignoredLines;
	}

	public void setIgnoredLines(Integer ignoredLines) {
		this.ignoredLines = ignoredLines;
	}

	public Boolean getHasHeader() {
		return hasHeader;
	}

	public void setHasHeader(Boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

	public List<String> getHeader() {
		return header;
	}

	public void setHeader(List<String> header) {
		this.header = header;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
