package kkr.job.file2base.domains.test.components.testlancer.job.file2base;

import java.util.Collection;

import org.apache.log4j.Logger;

import kkr.common.errors.BaseException;
import kkr.ktm.domains.common.components.diffmanager.DiffManager;
import kkr.ktm.domains.common.components.diffmanager.data.DiffGroup;
import kkr.ktm.domains.orchestrator.components.testlancer.TestLancer;
import kkr.ktm.domains.orchestrator.data.TestOutputImpl;
import kkr.ktm.domains.tests.data.TestInput;
import kkr.ktm.domains.tests.data.TestOutput;

public class TestLancerJobFile2Base extends TestLancerJobFile2BaseFwk implements TestLancer {
	private static final Logger LOG = Logger.getLogger(TestLancerJobFile2Base.class);

	private static final String PARAM_EXCEPTION = "EXCEPTION";
	private static final String PARAM_EXCEPTION_MESSAGE = "EXCEPTION.MESSAGE";

	public TestOutput lanceTest(TestInput testInput) throws BaseException {
		LOG.trace("BEGIN");
		try {
			TestOutput testOutput = new TestOutputImpl(testInput);

			DiffManager diffManager = diffManagers.get(testInput.getType());

			Collection<DiffGroup> diffGroupsBefore = diffManager.loadCurrents();

			try {
				batchJobFile2Base.run();
			} catch (Exception ex) {
				testOutput.getDataOutput().put(PARAM_EXCEPTION, ex.toString());
				testOutput.getDataOutput().put(PARAM_EXCEPTION_MESSAGE, ex.getMessage());
			}

			Collection<DiffGroup> diffGroupsAfter = diffManager.loadDiffs(diffGroupsBefore);

			LOG.trace("OK");
		} finally {
			LOG.trace("END");
		}
		return null;
	}
}
