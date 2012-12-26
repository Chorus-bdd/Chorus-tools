package org.chorusbdd.chorus.tools.webagent;

import org.chorusbdd.chorus.executionlistener.ExecutionListenerAdapter;
import org.chorusbdd.chorus.results.ExecutionToken;
import org.chorusbdd.chorus.results.FeatureToken;
import org.chorusbdd.chorus.tools.xml.writer.TestSuite;

import java.util.LinkedList;
import java.util.List;
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

    private final LinkedList<TestSuite> cachedSuites = new LinkedList<TestSuite>();
    private final AtomicLong suitesReceived = new AtomicLong();
    private final String cacheName;
    private volatile int maxSuiteHistory;

    public WebAgentFeatureCache(String cacheName, int maxSuiteHistory) {
        this.cacheName = cacheName;
        this.maxSuiteHistory = maxSuiteHistory;
    }

    public void testsCompleted(ExecutionToken testExecutionToken, List<FeatureToken> features) {
        TestSuite testSuite = new TestSuite(testExecutionToken, features);
        addToCachedSuites(testSuite);
    }

    private void addToCachedSuites(TestSuite testSuite) {
        synchronized ( cachedSuites ) {
            cachedSuites.add(testSuite);
            suitesReceived.incrementAndGet();
            if ( cachedSuites.size() > maxSuiteHistory) {
                cachedSuites.removeLast();
            }
        }
    }

    @Override
    public String toString() {
        return "WebAgentFeatureCache{" +
                "cacheName='" + cacheName + '\'' +
                ", maxSuiteHistory=" + maxSuiteHistory +
                '}';
    }

    public int size() {
        synchronized (cachedSuites) {
            return cachedSuites.size();
        }
    }

    public void setMaxHistory(int maxHistory) {
        this.maxSuiteHistory = maxHistory;
        synchronized (cachedSuites) {
            while ( cachedSuites.size() > 0 && cachedSuites.size() > maxHistory) {
                cachedSuites.removeLast();
            }
        }
    }

    public int getSuitesReceived() {
        return suitesReceived.intValue();
    }
}
