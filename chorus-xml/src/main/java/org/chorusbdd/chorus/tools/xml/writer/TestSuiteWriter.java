package org.chorusbdd.chorus.tools.xml.writer;


import org.chorusbdd.chorus.tools.xml.beans.TestSuiteBean;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;


public class TestSuiteWriter {

	public TestSuiteWriter() {}

	public void write(StringWriter writer, TestSuiteBean suite) throws JAXBException, XMLStreamException {
			final JAXBContext context = JAXBContext.newInstance(TestSuiteBean.class);
			final Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(suite, writer);
	}

}
