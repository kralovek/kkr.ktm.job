package kkr.job.file2base.domains.test.components.testlancer.job.file2base;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import kkr.common.errors.BaseException;
import kkr.common.utils.UtilsFile;
import kkr.common.utils.UtilsString;
import kkr.ktm.domains.common.components.diffmanager.data.DiffEntity;
import kkr.ktm.domains.common.components.diffmanager.data.DiffItem;
import kkr.ktm.domains.orchestrator.components.testlancer.TestLancer;
import kkr.ktm.domains.orchestrator.data.TestOutputImpl;
import kkr.ktm.domains.tests.data.TestInput;
import kkr.ktm.domains.tests.data.TestOutput;

public class TestLancerJobFile2Base extends TestLancerJobFile2BaseFwk implements TestLancer {
	private static final Logger LOG = Logger.getLogger(TestLancerJobFile2Base.class);

	private static final String PARAM_OUT_TIME_BEGIN = "TIME.BEGIN";
	private static final String PARAM_OUT_TIME_END = "TIME.END";
	private static final String PARAM_OUT_TIME_DELTA = "TIME.DELTA";

	private static final String PARAM_OUT_PARAMETERS = "PARAMETERS";
	private static final String PARAM_OUT_EXCEPTION = "EXCEPTION";
	private static final String PARAM_OUT_EXCEPTION_MESSAGE = "EXCEPTION.MESSAGE";

	public TestOutput lanceTest(TestInput testInput) throws BaseException {
		LOG.trace("BEGIN");
		try {
			TestOutput retval = new TestOutputImpl(testInput);

			InputsJobFile2Base inputs = new InputsJobFile2Base(sysPrefix);
			inputs.config(testInput);

			// Collection<DiffEntity> diffEntitiesBefore =
			// diffManager.loadCurrents(testInput);

			createFile(inputs, testInput, retval);

			// runJob(inputs, retval);

			// Collection<DiffEntity> diffEntitiesAfter = diffManager.loadDiffs(testInput,
			// diffEntitiesBefore);

			// toParametersDiffEntities(retval, diffEntitiesAfter);
			// toParametersSystem(retval);

			LOG.trace("OK");
			return retval;
		} finally {
			LOG.trace("END");
		}
	}

	private void createFile(InputsJobFile2Base inputs, TestInput testInput, TestOutput testOutput)
			throws BaseException {
		LOG.trace("BEGIN");
		try {
			if (inputs.isNoFile()) {
				LOG.trace("OK");
				return;
			}

			File file = new File(dirInput, inputs.getFilename());

			String templateContent = templateLoader.loadTemplate(testInput);

			String fileContent = parametersFormatter.format(templateContent, testInput.getDataInput());

			UtilsFile.contentToFile(fileContent, file);

			LOG.trace("OK");
		} finally {
			LOG.trace("END");
		}
	}

	private void runJob(InputsJobFile2Base inputs, TestOutput testOutput) {
		LOG.trace("BEGIN");
		try {
			if (inputs.isNoRun()) {
				LOG.trace("OK");
				return;
			}
			Date dateBegin = new Date();
			try {
				batchJobFile2Base.run();
			} catch (Throwable ex) {
				String exceptionBody = UtilsString.toStringException(ex);
				testOutput.getDataOutput().put(sysPrefix + PARAM_OUT_EXCEPTION, exceptionBody);
				testOutput.getDataOutput().put(sysPrefix + PARAM_OUT_EXCEPTION_MESSAGE, ex.getMessage());
			}
			Date dateEnd = new Date();
			String dateDetla = UtilsString.toStringDateDelta(dateBegin, dateEnd);

			testOutput.getDataOutput().put(sysPrefix + PARAM_OUT_TIME_BEGIN, dateBegin);
			testOutput.getDataOutput().put(sysPrefix + PARAM_OUT_TIME_END, dateEnd);
			testOutput.getDataOutput().put(sysPrefix + PARAM_OUT_TIME_DELTA, dateDetla);

			LOG.trace("OK");
		} finally {
			LOG.trace("END");
		}
	}

	private void toParametersSystem(TestOutput testOutput) {
		testOutput.getDataOutput().put(sysPrefix + PARAM_OUT_PARAMETERS, null);
		StringBuffer buffer = new StringBuffer();
		Collection<String> keys = new TreeSet<String>(testOutput.getDataOutput().keySet());
		for (String key : keys) {
			if (buffer.length() != 0) {
				buffer.append("\n");
			}
			buffer.append(key);
		}
		testOutput.getDataOutput().put(sysPrefix + PARAM_OUT_PARAMETERS, buffer.toString());
	}

	private void toParametersDiffEntities(TestOutput testOutput, Collection<DiffEntity> diffEntities) {
		LOG.trace("BEGIN");
		try {
			for (DiffEntity diffEntity : diffEntities) {
				String keyName = diffEntity.getName() + ".<NAME>";
				String keyIndex = diffEntity.getName() + ".<INDEX>";
				String keyItem = diffEntity.getName() + ".<ITEM>";
				String keyItemName = diffEntity.getName() + ".<ITEM>.<NAME>";
				String keyItemStatus = diffEntity.getName() + ".<ITEM>.<STATUS>";
				String keyItemIndex = diffEntity.getName() + ".<ITEM>.<INDEX>";

				testOutput.getDataOutput().put(keyName, diffEntity.getName());
				testOutput.getDataOutput().put(keyIndex, diffEntity.getLastIndex().toString());

				if (diffEntity.getItems().size() == 1) {
					DiffItem diffItem = diffEntity.getItems().iterator().next();

					testOutput.getDataOutput().put(keyItemName, diffItem.getName());
					testOutput.getDataOutput().put(keyItemIndex, diffItem.getIndex().toString());
					testOutput.getDataOutput().put(keyItemStatus, diffItem.getStatus().name());

					for (Map.Entry<String, Object> entry : diffItem.getParameters().entrySet()) {
						testOutput.getDataOutput().put(keyItem + "." + entry.getKey(), entry.getValue());
					}
				} else if (diffEntity.getItems().size() > 1) {
					String[] arrayName = new String[diffEntity.getItems().size()];
					String[] arrayIndex = new String[diffEntity.getItems().size()];
					String[] arrayStatus = new String[diffEntity.getItems().size()];
					testOutput.getDataOutput().put(keyItemName, arrayName);
					testOutput.getDataOutput().put(keyItemIndex, arrayIndex);
					testOutput.getDataOutput().put(keyItemStatus, arrayStatus);

					Map<String, Object[]> arraysData = new LinkedHashMap<String, Object[]>();

					int i = 0;
					for (DiffItem diffItem : diffEntity.getItems()) {
						arrayName[i] = diffItem.getName();
						arrayIndex[i] = diffItem.getIndex().toString();
						arrayStatus[i] = diffItem.getStatus().name();

						for (Map.Entry<String, Object> entry : diffItem.getParameters().entrySet()) {
							String keyData = keyItem + "." + entry.getKey();
							Object[] arrayData = arraysData.get(keyData);
							if (arrayData == null) {
								arrayData = new Object[diffEntity.getItems().size()];
								arraysData.put(keyData, arrayData);
								testOutput.getDataOutput().put(keyData, arrayData);
							}
							arrayData[i] = entry.getValue();
						}

						i++;
					}
				}
			}
			LOG.trace("OK");
		} finally {
			LOG.trace("END");
		}
	}
}
