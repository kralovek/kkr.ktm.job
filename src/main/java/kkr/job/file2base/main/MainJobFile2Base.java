package kkr.job.file2base.main;

import org.apache.log4j.Logger;

import kkr.common.errors.BaseException;
import kkr.common.main.AbstractMain;
import kkr.common.main.Config;
import kkr.job.file2base.batchs.file2base.BatchJobFile2Base;

public class MainJobFile2Base extends AbstractMain {

	private static final Logger LOG = Logger.getLogger(MainJobFile2Base.class);

	private static final String ID_BEAN_DEFAULT = "batchJobFile2Base";
	private static final String CONFIG_DEFAULT = "spring-main-job-file2base.xml";

	public static final void main(String[] args) throws BaseException {
		LOG.trace("BEGIN");
		try {
			MainJobFile2Base mainTravel = new MainJobFile2Base();
			mainTravel.run(args);
			LOG.trace("OK");
		} finally {
			LOG.trace("END");
		}
	}

	private void run(String[] args) throws BaseException {
		Config config = config(getClass(), Config.class, args);
		LOG.trace("BEGIN");
		try {
			BatchJobFile2Base batch = createBean(config, BatchJobFile2Base.class, CONFIG_DEFAULT, ID_BEAN_DEFAULT);
			batch.run();
			LOG.trace("OK");
		} finally {
			LOG.trace("END");
		}
	}
}
