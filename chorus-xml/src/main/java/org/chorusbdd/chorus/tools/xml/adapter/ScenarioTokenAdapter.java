package org.chorusbdd.chorus.tools.xml.adapter;

import org.chorusbdd.chorus.results.ScenarioToken;
import org.chorusbdd.chorus.tools.xml.beans.ScenarioTokenBean;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Arrays;
import java.util.List;

public class ScenarioTokenAdapter extends XmlAdapter<ScenarioTokenBean, ScenarioToken> {

	@Override
	public ScenarioTokenBean marshal(ScenarioToken s) throws Exception {
		ScenarioTokenBean result = new ScenarioTokenBean();
		result.setName(s.getName());
		result.setSteps(s.getSteps());
        List<String> tags = s.getTags();
        result.setTags(tags.size() == 0 ? null : Arrays.toString(tags.toArray()));
        result.setEndState(s.getEndState());
		return result;
	}

	@Override
	public ScenarioToken unmarshal(ScenarioTokenBean v) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
