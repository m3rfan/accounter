package com.vimukti.accounter.migration;

import org.hibernate.Criteria;
import org.json.JSONException;
import org.json.JSONObject;

import com.vimukti.accounter.core.CompanyPreferences;

public class DefaultFeaturesMigrator implements IMigrator<CompanyPreferences> {

	@Override
	public JSONObject migrate(CompanyPreferences obj, MigratorContext context)
			throws JSONException {
		JSONObject features = new JSONObject();
		features.put("enableLocationTracking", true);
		features.put("enableClassTracking", true);
		// features.put("classTrackingType",
		// obj.isClassPerDetailLine() ? "OnePerDetailLine"
		// : "OnePerTransaction");
		features.put("enableShipping", obj.isDoProductShipMents());
		features.put("projectTracking", obj.isJobTrackingEnabled());
		return features;
	}

	@Override
	public void addRestrictions(Criteria criteria) {
		// TODO Auto-generated method stub
		
	}

}
