package org.chorusbdd.chorus.tools.xml.beans;

import org.chorusbdd.chorus.results.ExecutionToken;
import org.chorusbdd.chorus.results.FeatureToken;
import org.chorusbdd.chorus.results.TestSuite;
import org.chorusbdd.chorus.tools.xml.adapter.ExecutionTokenAdapter;
import org.chorusbdd.chorus.tools.xml.adapter.FeatureTokenAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nick
 * Date: 17/10/12
 * Time: 17:53
 *
 * A wrapper around the tokens which represent the state of a test suite
 * either in progress or completed
 */
@XmlRootElement(name="testSuite")
public class TestSuiteBean {

    private TestSuite testSuite;

    public TestSuiteBean() {}

    public TestSuiteBean(TestSuite testSuite) {
        this.testSuite = testSuite;
    }

    public void setTestSuite(TestSuite testSuite) {
        this.testSuite = testSuite;
    }

    @XmlJavaTypeAdapter(ExecutionTokenAdapter.class)
    public ExecutionToken getExecution() {
        return testSuite.getExecutionToken();
    }

    @XmlElementWrapper(name="features")
    @XmlJavaTypeAdapter(FeatureTokenAdapter.class)
    public List<FeatureToken> getFeature() {
        return testSuite.getFeatureList();
    }

    @XmlAttribute
    public String getSuiteName() {
        return testSuite.getTestSuiteName();
    }
}
