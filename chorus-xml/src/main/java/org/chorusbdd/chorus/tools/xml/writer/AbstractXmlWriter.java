package org.chorusbdd.chorus.tools.xml.writer;

import org.chorusbdd.chorus.results.ResultsSummary;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.stream.XMLStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 10/01/13
 * Time: 22:35
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractXmlWriter<E> {

    private Map<String, Object> marshallerProperties = new HashMap<String, Object>();
    private Class beanClass;

    public AbstractXmlWriter(Class beanClass) {
        this.beanClass = beanClass;
        addDefaultMarshallerProperties();
    }

    protected void addDefaultMarshallerProperties() {
        marshallerProperties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    }

    public void addMarshallerProperty(String key, Object value) {
        marshallerProperties.put(key, value);
    }

    protected Marshaller getMarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(beanClass);
        Marshaller marshaller = context.createMarshaller();
        addMarshallerProperties(marshaller);
        return marshaller;
    }

    private void addMarshallerProperties(Marshaller marshaller) throws PropertyException {
        for(Map.Entry<String, Object> e : marshallerProperties.entrySet()) {
            marshaller.setProperty(e.getKey(), e.getValue());
        }
    }

    public abstract void write(XMLStreamWriter writer, E summary) throws Exception;

    public abstract void write(Writer writer, E summary) throws Exception;
}
