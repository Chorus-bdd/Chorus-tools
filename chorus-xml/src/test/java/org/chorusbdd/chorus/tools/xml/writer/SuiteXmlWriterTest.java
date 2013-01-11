package org.chorusbdd.chorus.tools.xml.writer;

import junit.framework.Assert;
import org.chorusbdd.chorus.Chorus;
import org.chorusbdd.chorus.executionlistener.ExecutionListener;
import org.chorusbdd.chorus.results.*;
import org.chorusbdd.chorus.tools.xml.beans.TestSuiteBean;
import org.chorusbdd.chorus.tools.xml.util.FileUtil;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.DifferenceListener;
import org.custommonkey.xmlunit.XMLTestCase;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import java.io.*;
import java.net.URL;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 29/10/12
 * Time: 08:55
 *
 * Test that we can serialize chorus interpreter tokens to xml
 *
 * Here we run a feature by executing the interpreter
 * and add a local observer for execution events, which allow us to pick
 * up the interpreter tokens which give feature/scenario state during and
 * after execution
 */
public class SuiteXmlWriterTest extends XMLTestCase {

    @Test
    public void testWriteSuite() throws Exception {
        StringWriter out = new StringWriter();
        TestSuiteXmlWriter w = new TestSuiteXmlWriter();
        w.write(out, getTestSuite());
        out.flush();
        out.close();
        
        System.out.println();
        System.out.println("This is the output as XML --->");
        System.out.println(out.toString());


        URL expectedOutputResource = getClass().getResource("featureOneExpectedOutput.xml");
        String expectedXml = FileUtil.readToString(expectedOutputResource.openStream());
        String actualXml = out.toString().trim();
        actualXml = removeVariableContent(actualXml);
        XMLUnit.setNormalizeWhitespace(true);

        //DifferenceListener myDifferenceListener = new IgnoreTextAndAttributeValuesDifferenceListener();
        //myDiff.overrideDifferenceListener(myDifferenceListener);
        Diff myDiff = new Diff(expectedXml, actualXml);
        assertTrue("test XML identical " + myDiff, myDiff.identical());
    }

    //some contents is variable based on time and date, replace this
    private String removeVariableContent(String xml) {
        xml = xml.replaceAll("timeTaken=\"\\d{0,5}\"", "timeTaken=\"{TIMETAKEN}\"");
        xml = xml.replaceAll("timeTakenSeconds=\"\\d{0,5}\\.?\\d?\"", "timeTakenSeconds=\"{TIMETAKEN_SECONDS}\"");
        xml = xml.replaceAll("executionStartTime=\".*\"", "executionStartTime=\"{STARTTIME}\"");
        xml = xml.replaceAll("executionStartTimestamp=\".*\"", "executionStartTimestamp=\"{STARTTIMSTAMP}\"");
        xml = xml.replaceAll("executionHost=\".*\"", "executionHost=\"{EXECUTIONHOST}\"");
        return xml;
    }

    private TestSuite getTestSuite() throws Exception {
        String baseDir = findChorusXmlModuleRoot();
        String featureDirPath = baseDir + File.separator + "src" + File.separator + "test";
        Chorus chorus = new Chorus(new String[] {"-f", featureDirPath, "-h", "org.chorusbdd.chorus.tools.xml","-l", "debug"});
        MockExecutionListener l = new MockExecutionListener();
        chorus.addExecutionListener(l);
        chorus.run();
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
