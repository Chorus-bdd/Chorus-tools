package org.chorusbdd.chorus.tools.xml.writer;

import junit.framework.Assert;
import org.chorusbdd.chorus.Chorus;
import org.chorusbdd.chorus.core.interpreter.ExecutionListener;
import org.chorusbdd.chorus.core.interpreter.results.ExecutionToken;
import org.chorusbdd.chorus.core.interpreter.results.FeatureToken;
import org.chorusbdd.chorus.core.interpreter.results.ScenarioToken;
import org.chorusbdd.chorus.core.interpreter.results.StepToken;
import org.chorusbdd.chorus.tools.xml.util.FileUtil;
import org.junit.Test;

import com.sun.xml.internal.txw2.output.IndentingXMLStreamWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.*;
import java.net.URL;
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
        StringWriter out = new StringWriter();
        TestSuiteWriter w = new TestSuiteWriter();
        w.write(out, getTestSuite());
        out.flush();
        out.close();
        
        System.out.println();
        System.out.println("This is the output as XML --->");
        System.out.println(out.toString());
        

        URL expectedOutputResource = getClass().getResource("featureOneExpectedOutput.xml");
        String expectedXml = FileUtil.readToString(expectedOutputResource.openStream());
        assertEquals("Check Expected XML", expectedXml.trim(), out.toString().trim());
    }

    private TestSuite getTestSuite() throws Exception {
        String baseDir = findChorusXmlModuleRoot();
        String featureDirPath = baseDir + File.separator + "src" + File.separator + "test";
        Chorus chorus = new Chorus(new String[] {"-f", featureDirPath, "-h", "org.chorusbdd.chorus.tools.xml","-l", "debug"});
        MockExecutionListener l = new MockExecutionListener();
        chorus.addExecutionListener(l);
        chorus.run();
        TestSuite t = new TestSuite();
        
        return new TestSuite(l.testExecutionToken, l.featureTokens);
    }

    private String findChorusXmlModuleRoot() {
        String baseDir = System.getProperty("user.dir");
        File f = new File(baseDir, "chorus-xml");
        if ( f.exists() ) {
            baseDir = f.getPath();
        }
        return baseDir;
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
