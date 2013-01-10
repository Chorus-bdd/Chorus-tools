package org.chorusbdd.chorus.tools.xml.writer;

import org.chorusbdd.chorus.results.ResultsSummary;
import org.chorusbdd.chorus.tools.xml.adapter.ResultsSummaryAdapter;
import org.chorusbdd.chorus.tools.xml.beans.ResultSummaryBean;

import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamWriter;
import java.io.Writer;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 10/01/13
 * Time: 21:53
 * To change this template use File | Settings | File Templates.
 */
public class ResultSummaryXmlWriter extends AbstractXmlWriter<ResultsSummary> {

    public ResultSummaryXmlWriter() {
        super(ResultSummaryBean.class);
    }

    @Override
    public void write(XMLStreamWriter writer, ResultsSummary summary) throws Exception {
        ResultSummaryBean resultSummaryBean = new ResultsSummaryAdapter().marshal(summary);
        Marshaller marshaller = getMarshaller();
        marshaller.marshal(resultSummaryBean, writer);
    }

    @Override
    public void write(Writer writer, ResultsSummary summary) throws Exception {
        ResultSummaryBean resultSummaryBean = new ResultsSummaryAdapter().marshal(summary);
        Marshaller marshaller = getMarshaller();
        marshaller.marshal(resultSummaryBean, writer);
    }

}
