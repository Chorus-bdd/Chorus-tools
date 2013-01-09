package org.chorusbdd.chorus.tools.webagent.testsuite;

import junit.framework.Assert;
import org.chorusbdd.chorus.annotations.Handler;
import org.chorusbdd.chorus.annotations.Step;

/**
 * User: nick
 * Date: 08/01/13
 * Time: 08:53
 */
@Handler("Web Agent Self Test")
public class WebAgentSelfTestHandler extends Assert {

    @Step("I run a scenario with several steps")
    public void runAScenarioWithSeveralSteps() {

    }

    @Step("a step fails an assertion")
    public void failAnAssertion() {
        assertTrue("Fail an assertion", false);
    }

    @Step("chorus scenario timeout is set to 2 seconds")
    public void timeoutSet() {
    }

    @Step("I wait for four seconds for timeout")
    public void waitForFour() throws InterruptedException {
        Thread.sleep(4000);
    }

}


