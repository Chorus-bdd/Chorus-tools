package org.chorusbdd.chorus.tools.webagent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chorusbdd.chorus.tools.webagent.jettyhandler.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;

import java.util.List;

/**
 * User: nick
 * Date: 27/12/12
 * Time: 09:42
 */
public class WebAgentHttpConnector {

    private static final Log log = LogFactory.getLog(WebAgentHttpConnector.class);

    private int localPort;
    private List<WebAgentFeatureCache> cacheList;
    private final Server server;

    public WebAgentHttpConnector(int localPort, List<WebAgentFeatureCache> cacheList) {
        this.localPort = localPort;
        this.cacheList = cacheList;
        server = new Server(localPort);
    }

    public void start() throws Exception {
        log.info("Starting Jetty HTTPD on port " + localPort + " for feature caches " + cacheList);
        addHandlers(server);
        server.start();
    }

    public void stop() throws Exception {
        server.stop();
    }

    private void addHandlers(Server server) {
        HandlerList l = new HandlerList();
        l.addHandler(new IndexHandler(cacheList));
        l.addHandler(new StyleSheetResourceHandler());
        for ( WebAgentFeatureCache c : cacheList) {
            l.addHandler(new CacheIndexHandler(c));
            l.addHandler(new Rss2SuiteListHandler(
                c,
                TestSuiteFilter.ALL_SUITES,
                "/" + c.getHttpName() + "/allTestSuites",
                ".rss",
                "All Test Suites in " + c.getName(),
                "All the test suites which are available from ChorusWebAgent " + c.getName() + " cache",
                localPort)
            );

            l.addHandler(new XmlSuiteListHandler(
                c,
                TestSuiteFilter.ALL_SUITES,
                "/" + c.getHttpName() + "/allTestSuites",
                ".xml",
                "All Test Suites in " + c.getName(),
                localPort)
            );
            l.addHandler(new TestSuiteHandler(c));
        }
        server.setHandler(l);
    }


}
