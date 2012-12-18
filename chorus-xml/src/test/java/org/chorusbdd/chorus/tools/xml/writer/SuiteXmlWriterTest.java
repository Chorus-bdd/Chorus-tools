package org.chorusbdd.chorus.tools.xml.writer;

import junit.framework.Assert;
import org.chorusbdd.chorus.Chorus;
import org.chorusbdd.chorus.core.interpreter.ExecutionListener;
import org.chorusbdd.chorus.core.interpreter.results.ExecutionToken;
import org.chorusbdd.chorus.core.interpreter.results.FeatureToken;
import org.chorusbdd.chorus.core.interpreter.results.ScenarioToken;
import org.chorusbdd.chorus.core.interpreter.results.StepToken;
import org.chorusbdd.chorus.util.config.InterpreterPropertyException;
import org.chorusbdd.chorus.util.logging.ChorusOut;
import org.junit.Test;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 29/10/12
 * Time: 08:55
 * To change this template use File | Settings | File Templates.
 */
public class SuiteXmlWriterTest extends Assert {

    @Test
    public void testWriteSuite() throws Exception {
        ExecutionToken executionToken = new ExecutionToken("testSuite");
        TestSuite suite = getTestSuite();

        XmlElementWriterFactory f = new DefaultXmlWriterFactory();
        TestSuiteWriter w = new TestSuiteWriter(f);

        XMLOutputFactory out = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = out.createXMLStreamWriter(System.out);

        w.write(writer, suite);
        assertTrue(true);
    }

    private TestSuite getTestSuite() throws Exception {
        String featureDirPath = System.getProperty("user.dir");
        File f = new File(featureDirPath, "chorus-xml" + File.separator + "src" + File.separator + "test");
        System.out.println(f.getAbsolutePath());
        Chorus chorus = new Chorus(new String[] {"-f", f.getAbsolutePath(), "-h", "org.chorusbdd.chorus.tools.xml"});
        MockExecutionListener l = new MockExecutionListener();
        chorus.addExecutionListener(l);
        chorus.run();
        System.out.println(f.getAbsolutePath());
        return new TestSuite(l.testExecutionToken, l.featureTokens);
    }

    private static class MockExecutionListener implements ExecutionListener {

        ExecutionToken testExecutionToken;
        List<FeatureToken> featureTokens;

        public void testsStarted(ExecutionToken testExecutionToken) {
        }

        public void testsCompleted(ExecutionToken testExecutionToken, List<FeatureToken> features) {
            this.testExecutionToken = testExecutionToken;
            this.featureTokens = features;
        }

        public void featureStarted(ExecutionToken testExecutionToken, FeatureToken feature) {
        }

        public void featureCompleted(ExecutionToken testExecutionToken, FeatureToken feature) {
        }

        public void scenarioStarted(ExecutionToken testExecutionToken, ScenarioToken scenario) {
        }

        public void scenarioCompleted(ExecutionToken testExecutionToken, ScenarioToken scenario) {
        }

        public void stepStarted(ExecutionToken testExecutionToken, StepToken step) {
        }

        public void stepCompleted(ExecutionToken testExecutionToken, StepToken step) {
        }


    }
}
