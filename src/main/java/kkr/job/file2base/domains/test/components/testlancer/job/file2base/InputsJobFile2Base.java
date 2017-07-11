package kkr.job.file2base.domains.test.components.testlancer.job.file2base;

import org.apache.log4j.Logger;

import kkr.ktm.domains.tests.data.TestInput;
import kkr.ktm.domains.tests.errors.TestException;
import kkr.ktm.utils.UtilsKtm;

public class InputsJobFile2Base {
	private static final Logger LOG = Logger.getLogger(InputsJobFile2Base.class);

	private static final String PARAM_IN_FILE = "FILE";
	private static final String PARAM_NO_FILE = "NO_FILE";
	private static final String PARAM_NO_RUN = "NO_RUN";

	private String filename;
	private boolean noFile;
	private boolean noRun;
	private String sysPrefix;

	public InputsJobFile2Base(String sysPrefix) {
		this.sysPrefix = sysPrefix;
	}

	public void config(TestInput testInput) throws TestException {
		LOG.trace("BEGIN");
		try {
			//
			// FILENAME
			//
			filename = UtilsKtm.checkInputString(testInput, sysPrefix + PARAM_IN_FILE);
			if (filename == null) {
				throw new TestException(testInput, "Missing parameter: " + (sysPrefix + PARAM_IN_FILE));
			}

			//
			// NOFILE
			//
			Boolean bNoFile = UtilsKtm.checkInputBoolean(testInput, sysPrefix + PARAM_NO_FILE);
			noFile = bNoFile != null ? bNoFile : false;

			//
			// NORUN
			//
			Boolean bNoRun = UtilsKtm.checkInputBoolean(testInput, sysPrefix + PARAM_NO_RUN);
			noFile = bNoRun != null ? bNoRun : false;

			LOG.trace("OK");
		} finally {
			LOG.trace("END");
		}
	}

	public String getFilename() {
		return filename;
	}

	public boolean isNoFile() {
		return noFile;
	}

	public boolean isNoRun() {
		return noRun;
	}
}
