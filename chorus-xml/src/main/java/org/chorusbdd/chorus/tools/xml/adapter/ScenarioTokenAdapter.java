package org.chorusbdd.chorus.tools.xml.adapter;

import java.util.Arrays;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.chorusbdd.chorus.core.interpreter.results.ScenarioToken;
import org.chorusbdd.chorus.tools.xml.beans.ScenarioTokenBean;

public class ScenarioTokenAdapter extends XmlAdapter<ScenarioTokenBean, ScenarioToken> {

	@Override
	public ScenarioTokenBean marshal(ScenarioToken v) throws Exception {
		ScenarioTokenBean toRet = new ScenarioTokenBean();
		toRet.setName(v.getName());
		toRet.setSteps(v.getSteps());
		toRet.setTags(Arrays.toString(v.getTags().toArray()));
		return toRet;
	}

	@Override
	public ScenarioToken unmarshal(ScenarioTokenBean v) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
