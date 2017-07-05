package kkr.job.file2base.domains.common.components.iterableinput.csv;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kkr.common.errors.BaseException;
import kkr.job.file2base.domains.common.components.iterableinput.IterableInputFactory;

public class IterableInputFactoryCSVFile extends IterableInputFactoryCSVFileFwk implements IterableInputFactory<String> {
	private static Log LOGGER = LogFactory.getLog(IterableInputFactoryCSVFile.class);

	public IterableInputCSVFile createInstance(String source) throws BaseException {
		try {
			LOGGER.trace("BEGIN");
			testConfigured();

			if (source == null) {
				throw new IllegalArgumentException("The parameter source may not be null");
			}

			IterableInputCSVFile iterableInputCSVFile = new IterableInputCSVFile();

			iterableInputCSVFile.setFile(new File(source));
			iterableInputCSVFile.setHasHeader(getHasHeader());
			iterableInputCSVFile.setHeader(getHeader());
			iterableInputCSVFile.setIgnoredLines(getIgnoredLines());

			iterableInputCSVFile.config();

			LOGGER.trace("OK");
			return iterableInputCSVFile;
		} finally {
			LOGGER.trace("END");
		}
	}
}
