package org.chorusbdd.chorus.tools.xml.writer;


import org.chorusbdd.chorus.results.TestSuite;
import org.chorusbdd.chorus.tools.xml.beans.TestSuiteBean;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;
import java.io.Writer;


public class TestSuiteXmlWriter {

	public TestSuiteXmlWriter() {}

	public void write(Writer writer, TestSuite suite) throws JAXBException, XMLStreamException {
        TestSuiteBean testSuiteBean = new TestSuiteBean(suite);
        JAXBContext context = JAXBContext.newInstance(TestSuiteBean.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(testSuiteBean, writer);
	}

}
