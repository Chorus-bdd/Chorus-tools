package org.chorusbdd.chorus.tools.xml.adapter;

import org.chorusbdd.chorus.results.TestSuite;
import org.chorusbdd.chorus.tools.xml.beans.TestSuiteBean;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 12/01/13
 * Time: 17:56
 * To change this template use File | Settings | File Templates.
 */
public class TestSuiteAdapter extends XmlAdapter<TestSuiteBean, TestSuite> {

    @Override
    public TestSuiteBean marshal(TestSuite v) throws Exception {
        TestSuiteBean s = new TestSuiteBean();
        s.setExecution(v.getExecutionToken());
        s.setFeatures(v.getFeatureList());
        s.setSuiteName(v.getTestSuiteName());
        return s;
    }

    @Override
    public TestSuite unmarshal(TestSuiteBean v) throws Exception {
        return new TestSuite(v.getExecution(), v.getFeatures());
    }
}
