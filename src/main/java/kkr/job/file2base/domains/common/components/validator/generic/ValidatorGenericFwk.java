package kkr.job.file2base.domains.common.components.validator.generic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import kkr.common.errors.ConfigurationException;
import kkr.job.file2base.domains.common.components.validatoritem.ValidatorItem;

public abstract class ValidatorGenericFwk {
	private boolean configured;

	protected Map<String, Collection<ValidatorItem>> validatorItems;

	public void config() throws ConfigurationException {
		configured = false;
		if (validatorItems == null) {
			validatorItems = new HashMap<String, Collection<ValidatorItem>>();
		}
		configured = true;
	}

	public void testConfigured() {
		if (!configured) {
			throw new IllegalStateException(this.getClass().getName() + ": The component is not configured");
		}
	}

	public Map<String, Collection<ValidatorItem>> getValidatorItems() {
		return validatorItems;
	}

	public void setValidatorItems(Map<String, Collection<ValidatorItem>> validatorItems) {
		this.validatorItems = validatorItems;
	}
}
