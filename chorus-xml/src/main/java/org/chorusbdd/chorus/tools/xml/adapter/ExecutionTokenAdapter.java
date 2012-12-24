package org.chorusbdd.chorus.tools.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.chorusbdd.chorus.results.ExecutionToken;
import org.chorusbdd.chorus.tools.xml.beans.ExecutionTokenBean;

public class ExecutionTokenAdapter extends XmlAdapter<ExecutionTokenBean, ExecutionToken>{

	@Override
	public ExecutionTokenBean marshal(ExecutionToken arg0) throws Exception {
		ExecutionTokenBean ret = new ExecutionTokenBean();
		//ret.setExecutionStartTime(arg0.getExecutionStartTime());
		ret.setResultsSummary(arg0.getResultsSummary());
		ret.setTestSuiteName(arg0.getTestSuiteName());
		return ret;
	}

	@Override
	public ExecutionToken unmarshal(ExecutionTokenBean arg0) throws Exception {
		return new ExecutionToken(arg0.getTestSuiteName());
	}
	
}
