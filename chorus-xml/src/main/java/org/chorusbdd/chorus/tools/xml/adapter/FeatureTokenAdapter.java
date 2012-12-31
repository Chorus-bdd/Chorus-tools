package org.chorusbdd.chorus.tools.xml.adapter;

import java.util.Arrays;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.chorusbdd.chorus.results.FeatureToken;
import org.chorusbdd.chorus.tools.xml.beans.FeatureTokenBean;

public class FeatureTokenAdapter  extends XmlAdapter<FeatureTokenBean, FeatureToken>{

	@Override
	public FeatureTokenBean marshal(FeatureToken f) throws Exception {
		FeatureTokenBean result = new FeatureTokenBean();
		result.setName(f.getName());
		if (f.getUsesHandlers()!=null && f.getUsesHandlers().length>0) {
			result.setUsesHandlers(Arrays.toString(f.getUsesHandlers()));
		}
		result.setConfigurationName(f.getConfigurationName());
	    result.setDescription(f.getDescription());
	    result.setScenarios(f.getScenarios());
        result.setEndState(f.getEndState());
		return result;
	}

	@Override
	public FeatureToken unmarshal(FeatureTokenBean arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
