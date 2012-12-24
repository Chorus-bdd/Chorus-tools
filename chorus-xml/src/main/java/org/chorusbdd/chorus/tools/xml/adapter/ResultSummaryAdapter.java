package org.chorusbdd.chorus.tools.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.chorusbdd.chorus.results.ResultsSummary;
import org.chorusbdd.chorus.tools.xml.beans.ResultSummaryBean;

public class ResultSummaryAdapter extends XmlAdapter<ResultSummaryBean, ResultsSummary> {

	@Override
	public ResultSummaryBean marshal(ResultsSummary v) throws Exception {
		ResultSummaryBean ret = new ResultSummaryBean();

	    ret.setFeaturesPassed(v.getFeaturesPassed());
	    ret.setFeaturesPending(v.getFeaturesPending());
	    ret.setFeaturesFailed(v.getFeaturesFailed());

	    //stats
	    ret.setScenariosPassed(v.getScenariosPassed());
	    ret.setScenariosPending(v.getScenariosPending());
	    ret.setScenariosFailed(v.getScenariosFailed());
	    ret.setUnavailableHandlers(v.getUnavailableHandlers());

	    ret.setStepsPassed(v.getStepsPassed());
	    ret.setStepsFailed(v.getStepsFailed());
	    ret.setStepsPending(v.getStepsPending());
	    ret.setStepsUndefined(v.getStepsUndefined());
	    ret.setStepsSkipped(v.getStepsSkipped());
	    ret.setTokenId(v.getTokenId());
		return ret;
	}

	@Override
	public ResultsSummary unmarshal(ResultSummaryBean v) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
