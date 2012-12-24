package org.chorusbdd.chorus.tools.webagent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chorusbdd.chorus.executionlistener.ExecutionListener;
import org.chorusbdd.chorus.executionlistener.ExecutionListenerSupport;
import org.chorusbdd.chorus.remoting.jmx.RemoteExecutionListener;
import org.chorusbdd.chorus.remoting.jmx.RemoteExecutionListenerMBean;
import org.chorusbdd.chorus.results.ExecutionToken;
import org.chorusbdd.chorus.results.FeatureToken;
import org.chorusbdd.chorus.results.ScenarioToken;
import org.chorusbdd.chorus.results.StepToken;
import org.chorusbdd.chorus.tools.webagent.JmxManagementServerExporter;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: nick
 * Date: 24/12/12
 * Time: 14:02
 *
 * Create and register a local MBean server to receive test suite events from a remote interpreter
 */
public class WebAgentSuiteListener implements ExecutionListener {

    private static final Log log = LogFactory.getLog(JmxManagementServerExporter.class);

    private static final Executor eventPropagator = Executors.newSingleThreadExecutor();
    private int localPort;
    private final JmxManagementServerExporter jmxManagementServerExporter;
    private ExecutionListenerSupport executionListenerSupport = new ExecutionListenerSupport();

    public WebAgentSuiteListener(int localPort, boolean usePlatformMBeanServer) {
        this.localPort = localPort;
        this.jmxManagementServerExporter = new JmxManagementServerExporter(localPort, usePlatformMBeanServer);
    }

    public void start() throws Exception {
        jmxManagementServerExporter.startServer();
        RemoteExecutionListener r = new RemoteExecutionListener(this);
        MBeanServer mBeanServer = jmxManagementServerExporter.getmBeanServer();
        try {
            mBeanServer.registerMBean(r, new ObjectName(RemoteExecutionListenerMBean.JMX_EXECUTION_LISTENER_NAME));
        } catch (Exception e) {
            log.error("Failed to register jmx execution listener", e);
        }
    }

    public String toString() {
        return getClass().getSimpleName() + " on localhost " + localPort;
    }

    //delegate to  executionListenerSupport to enable adding / removing listeners

    public void addExecutionListener(ExecutionListener... listeners) {
        executionListenerSupport.addExecutionListener(listeners);
    }

    public boolean removeExecutionListener(ExecutionListener... listeners) {
        return executionListenerSupport.removeExecutionListener(listeners);
    }

    public void addExecutionListener(Collection<ExecutionListener> listeners) {
        executionListenerSupport.addExecutionListener(listeners);
    }

    public void removeExecutionListeners(List<ExecutionListener> listeners) {
        executionListenerSupport.removeExecutionListeners(listeners);
    }

    public void setExecutionListener(ExecutionListener... listener) {
        executionListenerSupport.setExecutionListener(listener);
    }

    public List<ExecutionListener> getListeners() {
        return executionListenerSupport.getListeners();
    }

    //////////////// Implement ExecutionListener to forward received events via event propagation thread

    @Override
    public void testsStarted(final ExecutionToken testExecutionToken) {
        eventPropagator.execute(new Runnable() {
            public void run() {
                executionListenerSupport.notifyStartTests(testExecutionToken);
            }
        });
    }

    @Override
    public void testsCompleted(final ExecutionToken testExecutionToken, final List<FeatureToken> features) {
        eventPropagator.execute(new Runnable() {
            public void run() {
                executionListenerSupport.notifyTestsCompleted(testExecutionToken, features);
            }
        });
    }

    @Override
    public void featureStarted(final ExecutionToken testExecutionToken, final FeatureToken feature) {
        eventPropagator.execute(new Runnable() {
            public void run() {
                executionListenerSupport.notifyFeatureStarted(testExecutionToken, feature);
            }
        });
    }

    @Override
    public void featureCompleted(final ExecutionToken testExecutionToken, final FeatureToken feature) {
        eventPropagator.execute(new Runnable() {
            public void run() {
                executionListenerSupport.notifyFeatureCompleted(testExecutionToken, feature);
            }
        });
    }

    @Override
    public void scenarioStarted(final ExecutionToken testExecutionToken, final ScenarioToken scenario) {
        eventPropagator.execute(new Runnable() {
            public void run() {
                executionListenerSupport.notifyScenarioStarted(testExecutionToken, scenario);
            }
        });
    }

    @Override
    public void scenarioCompleted(final ExecutionToken testExecutionToken, final ScenarioToken scenario) {
        eventPropagator.execute(new Runnable() {
            public void run() {
                executionListenerSupport.notifyScenarioCompleted(testExecutionToken, scenario);
            }
        });
    }

    @Override
    public void stepStarted(final ExecutionToken testExecutionToken, final StepToken step) {
        eventPropagator.execute(new Runnable() {
            public void run() {
                executionListenerSupport.notifyStepStarted(testExecutionToken, step);
            }
        });
    }

    @Override
    public void stepCompleted(final ExecutionToken testExecutionToken, final StepToken step) {
        eventPropagator.execute(new Runnable() {
            public void run() {
                executionListenerSupport.notifyStepCompleted(testExecutionToken, step);
            }
        });
    }
}
