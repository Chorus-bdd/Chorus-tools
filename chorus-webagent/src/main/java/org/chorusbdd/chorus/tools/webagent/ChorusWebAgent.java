package org.chorusbdd.chorus.tools.webagent;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 24/12/12
 * Time: 13:25
 *
 * A web agent for chorus which can publish the results of running Chorus test suites in various representations over HTTP
 *
 * This is run as a remote execution listener for Chorus test suites
 * (use command line switch -r host:port when running Chorus to publish such events to the agent)
 *
 * Uses an embedded jetty instance to publish details of executed test suites
 */
public class ChorusWebAgent {

    public ChorusWebAgent() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext.xml");
    }

    public static void main(String[] args) throws Exception {
        new ChorusWebAgent();
    }

}
