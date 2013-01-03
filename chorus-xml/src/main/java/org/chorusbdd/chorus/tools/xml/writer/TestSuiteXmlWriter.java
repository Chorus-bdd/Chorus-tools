package org.chorusbdd.chorus.tools.xml.writer;


import org.chorusbdd.chorus.results.TestSuite;
import org.chorusbdd.chorus.tools.xml.beans.TestSuiteBean;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.stream.XMLStreamException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


public class TestSuiteXmlWriter {

    private Map<String, Object> marshallerProperties = new HashMap<String, Object>();

    public TestSuiteXmlWriter() {
        marshallerProperties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    }

    public void addMarshallerProperty(String key, Object value) {
        marshallerProperties.put(key, value);
    }

    public void write(Writer writer, TestSuite suite) throws JAXBException, XMLStreamException {
        TestSuiteBean testSuiteBean = new TestSuiteBean(suite);
        JAXBContext context = JAXBContext.newInstance(TestSuiteBean.class);
        Marshaller marshaller = context.createMarshaller();
        addMarshallerProperties(marshaller);
        marshaller.marshal(testSuiteBean, writer);
	}

    private void addMarshallerProperties(Marshaller marshaller) throws PropertyException {
        for(Map.Entry<String, Object> e : marshallerProperties.entrySet()) {
            marshaller.setProperty(e.getKey(), e.getValue());
        }
    }

}
