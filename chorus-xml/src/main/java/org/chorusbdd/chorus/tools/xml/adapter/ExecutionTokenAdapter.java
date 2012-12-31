package org.chorusbdd.chorus.tools.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.chorusbdd.chorus.results.ExecutionToken;
import org.chorusbdd.chorus.tools.xml.beans.ExecutionTokenBean;
import org.chorusbdd.chorus.tools.xml.util.FormattingUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExecutionTokenAdapter extends XmlAdapter<ExecutionTokenBean, ExecutionToken>{

	@Override
	public ExecutionTokenBean marshal(ExecutionToken arg0) throws Exception {
		ExecutionTokenBean ret = new ExecutionTokenBean();
		ret.setExecutionStartTimestamp(arg0.getExecutionStartTime());
        ret.setExecutionStartTime(FormattingUtils.getStartTimeFormatter().format(new Date(arg0.getExecutionStartTime())));
		ret.setResultsSummary(arg0.getResultsSummary());
		ret.setTestSuiteName(arg0.getTestSuiteName());
        ret.setExecutionHost(arg0.getExecutionHost());
		return ret;
	}


	public ExecutionToken unmarshal(ExecutionTokenBean arg0) throws Exception {
		return new ExecutionToken(arg0.getTestSuiteName());
	}
	
}
