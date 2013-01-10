package org.chorusbdd.chorus.tools.xml.util;

import javanet.staxutils.IndentingXMLStreamWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 10/01/13
 * Time: 22:22
 * To change this template use File | Settings | File Templates.
 */
public class XmlUtils {

    /**
     * Return an XMLStreamWriter which indents the xml
     * (ideally XMLOutputFactory would support a property which would set this up for us to avoid a dependency
     * on a com.sun.xml class, but it doesn't for the present time appear to do so)
     */
    public static XMLStreamWriter getIndentingXmlStreamWriter(Writer writer) throws XMLStreamException, IOException {
        XMLOutputFactory f = XMLOutputFactory.newInstance();
        return new IndentingXMLStreamWriter(f.createXMLStreamWriter(writer));
    }
}
