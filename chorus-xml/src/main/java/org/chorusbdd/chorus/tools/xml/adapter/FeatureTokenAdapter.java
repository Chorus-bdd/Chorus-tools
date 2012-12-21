package org.chorusbdd.chorus.tools.xml.adapter;

import java.util.Arrays;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.chorusbdd.chorus.core.interpreter.results.FeatureToken;
import org.chorusbdd.chorus.tools.xml.beans.FeatureTokenBean;

public class FeatureTokenAdapter  extends XmlAdapter<FeatureTokenBean, FeatureToken>{

	@Override
	public FeatureTokenBean marshal(FeatureToken arg0) throws Exception {
		FeatureTokenBean toRet = new FeatureTokenBean();
		toRet.setName(arg0.getName());
		if (arg0.getUsesHandlers()!=null && arg0.getUsesHandlers().length>0) {
			toRet.setUsesHandlers(Arrays.toString(arg0.getUsesHandlers()));
		}
		toRet.setConfigurationName(arg0.getConfigurationName());
	    toRet.setDescription(arg0.getDescription().toString());
	    toRet.setScenarios(arg0.getScenarios());
		return toRet;
	}

	@Override
	public FeatureToken unmarshal(FeatureTokenBean arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
