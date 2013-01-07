package org.chorusbdd.chorus.tools.webagent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chorusbdd.chorus.executionlistener.ExecutionListenerAdapter;
import org.chorusbdd.chorus.results.ExecutionToken;
import org.chorusbdd.chorus.results.FeatureToken;
import org.chorusbdd.chorus.tools.webagent.util.WebAgentUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: nick
 * Date: 24/12/12
 * Time: 22:39
 *
 * Cache containing a history of TestSuite metadata tokens by time
 * Limited to a preset maximum size
 */
public class WebAgentFeatureCache extends ExecutionListenerAdapter {

    private static final Log log = LogFactory.getLog(JmxManagementServerExporter.class);

    //a size restricted linked hash map
    private final LinkedHashMap<String, WebAgentTestSuite> cachedSuites = new LinkedHashMap<String,WebAgentTestSuite>() {
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > maxSuiteHistory;
         }
    };
    private final AtomicLong suitesReceived = new AtomicLong();
    private final String cacheName;
    private String httpName;
    private volatile int maxSuiteHistory;

    public WebAgentFeatureCache(String cacheName, int maxSuiteHistory) {
        this.cacheName = cacheName;
        this.maxSuiteHistory = maxSuiteHistory;
        setHttpName();
    }

    public void testsCompleted(ExecutionToken testExecutionToken, List<FeatureToken> features) {
        WebAgentTestSuite testSuite = new WebAgentTestSuite(testExecutionToken, features);
        addToCachedSuites(testSuite);
    }

    private void addToCachedSuites(WebAgentTestSuite testSuite) {
        synchronized ( cachedSuites ) {
            cachedSuites.put(testSuite.getSuiteId(), testSuite);
            suitesReceived.incrementAndGet();
        }
    }

    @Override
    public String toString() {
        return "WebAgentFeatureCache{" +
                "cacheName='" + cacheName + '\'' +
                ", maxSuiteHistory=" + maxSuiteHistory +
                '}';
    }

    public int getNumberOfTestSuites() {
        synchronized (cachedSuites) {
            return cachedSuites.size();
        }
    }

    public void setMaxHistory(int maxHistory) {
        this.maxSuiteHistory = maxHistory;
        synchronized (cachedSuites) {
            while ( cachedSuites.size() > 0 && cachedSuites.size() > maxHistory) {
                removeExcessEntries(maxHistory);
            }
        }
    }

    private void removeExcessEntries(int maxHistory) {
        Iterator<Map.Entry<String, WebAgentTestSuite>> i = cachedSuites.entrySet().iterator();
        int count = 0;
        while(i.hasNext()) {
            i.next();
            if ( ++count > maxHistory) {
                i.remove();
            }
        }
    }

    public Object getMaxHistory() {
        return maxSuiteHistory;
    }

    public int getSuitesReceived() {
        return suitesReceived.intValue();
    }

    public String getName() {
        return cacheName;
    }

    public void setHttpName() {
        httpName = WebAgentUtil.urlEncode(cacheName);
    }

    public String getHttpName() {
        return httpName;
    }

    public List<WebAgentTestSuite> getSuites() {
        return getSuites(TestSuiteFilter.ALL_SUITES);
    }

    public List<WebAgentTestSuite> getSuites(TestSuiteFilter testSuiteFilter) {
        List<WebAgentTestSuite> l = new LinkedList<>();
        synchronized (cachedSuites) {
            for ( WebAgentTestSuite s : cachedSuites.values()) {
                if ( testSuiteFilter.accept(s)) {
                    l.add(s);
                }
            }
        }
        return l;
    }

    public WebAgentTestSuite getSuite(String suiteId) {
        return cachedSuites.get(suiteId);
    }

    /**
     * A testing hook to set the cached suites to have predictable keys
     */
    public void setSuiteIdsUsingZeroBasedIndex() {
        synchronized (cachedSuites) {
            List<WebAgentTestSuite> s = new ArrayList<>(cachedSuites.values());
            cachedSuites.clear();
            for ( int index=0; index < s.size(); index++) {
                WebAgentTestSuite suite = s.get(index);
                cachedSuites.put(suite.getTestSuiteName() + "-" + index, suite);
            }
        }
    }
}
