package org.chorusbdd.chorus.tools.webagent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * User: nick
 * Date: 24/12/12
 * Time: 22:53
 *
 * Tie together a cache with one or more suite listeners - this means the same cache may maintain test suite
 * details received from listeners on one or more ports.
 */
public class WebAgentContext {

    private static final Log log = LogFactory.getLog(WebAgentContext.class);

    private final WebAgentFeatureCache webAgentFeatureCache;
    private final List<WebAgentSuiteListener> suiteListeners;

    public WebAgentContext(WebAgentFeatureCache webAgentFeatureCache, List<WebAgentSuiteListener> suiteListeners) {
        this.webAgentFeatureCache = webAgentFeatureCache;
        this.suiteListeners = suiteListeners;
    }

    public void start() {
        if ( suiteListeners.size() == 0 ) {
            log.warn(webAgentFeatureCache + " is not configured with any suite listeners");
        }
        for (WebAgentSuiteListener l : suiteListeners) {
            log.info(webAgentFeatureCache + " will receive test suites from suite listener " + l);
            l.addExecutionListener(webAgentFeatureCache);
        }
    }
}
