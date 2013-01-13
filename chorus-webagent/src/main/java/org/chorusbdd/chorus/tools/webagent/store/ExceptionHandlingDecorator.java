package org.chorusbdd.chorus.tools.webagent.store;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chorusbdd.chorus.tools.webagent.WebAgentTestSuite;

import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 13/01/13
 * Time: 14:40
 *
 * Catch and handle any exceptions propagating from a wrapped SuiteStore
 */
public class ExceptionHandlingDecorator implements SuiteStore {

    private static final Log log = LogFactory.getLog(ExceptionHandlingDecorator.class);

    private SuiteStore wrappedStore;

    public ExceptionHandlingDecorator(SuiteStore wrappedStore) {
        this.wrappedStore = wrappedStore;
    }

    @Override
    public List<WebAgentTestSuite> loadTestSuites() {
        List<WebAgentTestSuite> storedSuites = Collections.emptyList();
        try {
            storedSuites = wrappedStore.loadTestSuites();
        } catch (Throwable t) {
            log.error("Failed to load suites from cache, no test suite history will be visible", t);
        }
        return storedSuites;
    }

    @Override
    public void addSuiteToStore(WebAgentTestSuite suite) {
        try {
            wrappedStore.addSuiteToStore(suite);
        } catch (Throwable t) {
            log.error("Failed to add suite " + suite + " to suite store " + wrappedStore, t);
        }
    }

    @Override
    public void removeSuiteFromStore(WebAgentTestSuite suite) {
        try {
            wrappedStore.removeSuiteFromStore(suite);
        } catch (Throwable t) {
            log.error("Failed to remove suite " + suite + " from suite store " + wrappedStore, t);
        }
    }
}
