package org.chorusbdd.chorus.tools.xml.writer;

import javax.xml.bind.*;
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
public abstract class AbstractXmlMarshaller<E> {

    private Map<String, Object> marshallerProperties = new HashMap<String, Object>();
    private Map<String, Object> unmarshallerProperties = new HashMap<String, Object>();
    private Class beanClass;

    public AbstractXmlMarshaller(Class beanClass) {
        this.beanClass = beanClass;
        addDefaultMarshallerProperties();
        addDefaultUnmarshallerProperties();
    }

    protected void addDefaultMarshallerProperties() {
        marshallerProperties.put(Marshaller.JAXB_FORMATTED_OUTPUT, true);
    }

    protected void addDefaultUnmarshallerProperties() {
    }

    public void addMarshallerProperty(String key, Object value) {
        marshallerProperties.put(key, value);
    }

    public void addUnmarshallerProperty(String key, Object value) {
        unmarshallerProperties.put(key, value);
    }

    protected Marshaller getMarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(beanClass);
        Marshaller marshaller = context.createMarshaller();
        addMarshallerProperties(marshaller);
        return marshaller;
    }

    protected Unmarshaller getUnmarshaller() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(beanClass);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        addUnmarshallerProperties(unmarshaller);
        return unmarshaller;
    }

    protected void addUnmarshallerProperties(Unmarshaller unmarshaller) throws PropertyException {
        for(Map.Entry<String, Object> e : unmarshallerProperties.entrySet()) {
            unmarshaller.setProperty(e.getKey(), e.getValue());
        }
    }

    protected void addMarshallerProperties(Marshaller marshaller) throws PropertyException {
        for(Map.Entry<String, Object> e : marshallerProperties.entrySet()) {
            marshaller.setProperty(e.getKey(), e.getValue());
        }
    }

    public abstract void write(XMLStreamWriter writer, E summary) throws Exception;

    public abstract void write(Writer writer, E summary) throws Exception;
}
