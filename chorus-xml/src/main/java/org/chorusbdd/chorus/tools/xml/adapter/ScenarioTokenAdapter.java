package org.chorusbdd.chorus.tools.xml.adapter;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.chorusbdd.chorus.core.interpreter.results.ScenarioToken;
import org.chorusbdd.chorus.tools.xml.beans.ScenarioTokenBean;

public class ScenarioTokenAdapter extends XmlAdapter<ScenarioTokenBean, ScenarioToken> {

	@Override
	public ScenarioTokenBean marshal(ScenarioToken v) throws Exception {
		ScenarioTokenBean toRet = new ScenarioTokenBean();
		toRet.setName(v.getName());
		toRet.setSteps(v.getSteps());
        List<String> tags = v.getTags();
        toRet.setTags(tags.size() == 0 ? null : Arrays.toString(tags.toArray()));
		return toRet;
	}

	@Override
	public ScenarioToken unmarshal(ScenarioTokenBean v) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
