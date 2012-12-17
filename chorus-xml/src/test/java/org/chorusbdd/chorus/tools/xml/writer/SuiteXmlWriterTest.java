package org.chorusbdd.chorus.tools.xml.writer;

import org.chorusbdd.chorus.core.interpreter.results.ExecutionToken;
import org.chorusbdd.chorus.core.interpreter.results.FeatureToken;
import org.junit.Test;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 29/10/12
 * Time: 08:55
 * To change this template use File | Settings | File Templates.
 */
public class SuiteXmlWriterTest {

    @Test
    public void testWriteSuite() throws XMLStreamException, IOException {
        ExecutionToken executionToken = new ExecutionToken("testSuite");
        TestSuite suite = new TestSuite(executionToken, Collections.<FeatureToken>emptyList());

        XmlElementWriterFactory f = new DefaultXmlWriterFactory();
        TestSuiteWriter w = new TestSuiteWriter(f);

        XMLOutputFactory out = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = out.createXMLStreamWriter(System.out);

        w.write(writer, suite);

    }
}
