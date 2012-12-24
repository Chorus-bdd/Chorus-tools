package org.chorusbdd.chorus.tools.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.chorusbdd.chorus.results.StepToken;
import org.chorusbdd.chorus.tools.xml.beans.StepTokenBean;

public class StepTokenAdapter extends XmlAdapter<StepTokenBean, StepToken>{

	@Override
	public StepTokenBean marshal(StepToken v) throws Exception {
		StepTokenBean toRet = new StepTokenBean();
		
		toRet.setType(v.getType());
	    toRet.setAction(v.getAction());
	    toRet.setEndState(v.getEndState());
	    toRet.setMessage(v.getMessage());
	    if (v.getThrowable()!=null)
	    {	
	    	toRet.setThrowable(v.getThrowable().toString());
	    }	    
		return toRet;
	}

	@Override
	public StepToken unmarshal(StepTokenBean v) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
