package kkr.job.file2base.domains.test.components.testlancer.job.file2base;

import org.apache.log4j.Logger;

import kkr.common.errors.BaseException;
import kkr.ktm.domains.orchestrator.components.testlancer.TestLancer;
import kkr.ktm.domains.tests.data.TestInput;
import kkr.ktm.domains.tests.data.TestOutput;

public class TestLancerJobFile2Base extends TestLancerJobFile2BaseFwk implements TestLancer {
	private static final Logger LOG = Logger.getLogger(TestLancerJobFile2Base.class);

	public TestOutput lanceTest(TestInput testInput) throws BaseException {
		LOG.trace("BEGIN");
		try {
			LOG.trace("OK");
		} finally {
			LOG.trace("END");
		}
		return null;
	}
}
