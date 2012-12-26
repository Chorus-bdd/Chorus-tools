package org.chorusbdd.chorus.tools.webagent;

import junit.framework.Assert;
import org.chorusbdd.chorus.annotations.Handler;
import org.chorusbdd.chorus.annotations.Step;
import org.chorusbdd.chorus.handlers.util.PolledAssertion;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;

/**
 * User: nick
 * Date: 26/12/12
 * Time: 11:27
 *
 *
 */
@Handler("Suite Caching")
@ContextConfiguration("SuiteCachingContext.xml")
public class SuiteCachingHandler extends Assert {

    @Resource
    private WebAgentFeatureCache mainFeatureCache;

    @Step("the web agent cache is empty")
    public void testCacheIsEmpty() {
        assertEquals("Expect web agent cache is empty", 0, mainFeatureCache.size());
    }

    @Step("the web agent cache contains (\\d) test suite")
    public void testSuiteCount(final int count) {
        new PolledAssertion() {
            protected void validate() {
                assertEquals("Expect " + count + " items in cache", count, mainFeatureCache.size());
            }
        }.await();
    }

}
