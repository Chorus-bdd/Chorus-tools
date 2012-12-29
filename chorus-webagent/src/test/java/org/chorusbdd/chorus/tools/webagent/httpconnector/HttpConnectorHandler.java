package org.chorusbdd.chorus.tools.webagent.httpconnector;

import junit.framework.Assert;
import org.chorusbdd.chorus.annotations.Handler;
import org.chorusbdd.chorus.annotations.Step;
import org.chorusbdd.chorus.handlers.util.PolledAssertion;
import org.chorusbdd.chorus.tools.webagent.WebAgentFeatureCache;
import org.chorusbdd.chorus.tools.webagent.util.FileUtil;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * User: nick
 * Date: 26/12/12
 * Time: 11:27
 *
 *
 */
@Handler("Http Connector")
@ContextConfiguration("HttpConnectorContext.xml")
public class HttpConnectorHandler extends Assert {

    @Resource
    private WebAgentFeatureCache mainFeatureCache;

    @Step(".*(http://.*) matches (.*)")
    public void checkWebContent(final String url, String resource) throws IOException {
        InputStream is = getClass().getResource("expected/" + resource).openStream();
        final String expected = FileUtil.readToString(is);

        new PolledAssertion() {
            @Override
            protected void validate() {
                try {
                    URL u = new URL(url);
                    InputStream urlStream = u.openStream();
                    String actual = FileUtil.readToString(urlStream);

                    String e = replaceAllTimesAndDates(expected);
                    String a = replaceAllTimesAndDates(actual);
                    assertEquals("Check http results", e, a);
                } catch (IOException e) {
                    fail("Could not connect");
                }
            }
        }.await();
    }

    public String replaceAllTimesAndDates(String content) {
        content = content.replaceAll("\\d{8} \\d\\d:\\d\\d:\\d\\d \\w\\w\\w", "DATETIME");
        content = content.replaceAll("-\\d{13}.xml", "TIMESTAMP.xml");
        return content;
    }

}
