package org.chorusbdd.chorus.tools.webagent.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;

/**
 * User: nick
 * Date: 24/12/12
 * Time: 14:09
 *
 * Create and export a jmx management server with unencrypted sockets connection
 *
 * See http://docs.oracle.com/javase/6/docs/technotes/guides/management/agent.html
 */
public class JmxManagementServerExporter {

    private static final Log log = LogFactory.getLog(JmxManagementServerExporter.class);

    private int port;
    private boolean usePlatformMBeanServer;
    private JMXConnectorServer jmxConnectorServer;
    private MBeanServer mBeanServer;

    public JmxManagementServerExporter(int port, boolean usePlatformMBeanServer) {
        this.port = port;
        this.usePlatformMBeanServer = usePlatformMBeanServer;
    }

    public void startServer() throws Exception {

        // Ensure cryptographically strong random number generator used
        // to choose the object number - see java.rmi.server.ObjID
        //
        //System.setProperty("java.rmi.server.randomIDs", "true");

        // Start an RMI registry on port 3000.
        //
        log.info("Creating RMI registry on port " + port);
        LocateRegistry.createRegistry(port);

        // Retrieve the PlatformMBeanServer.
        //
        log.info(usePlatformMBeanServer ? "Using Platform MBean Server" : "Creating the MBean server");
        mBeanServer = usePlatformMBeanServer ?
            ManagementFactory.getPlatformMBeanServer() :
            MBeanServerFactory.createMBeanServer();

        // Environment map.
        //
        log.info("Initialize the environment map");
        HashMap<String,Object> env = new HashMap<String,Object>();

        // Provide SSL-based RMI socket factories.
        //
        // The protocol and cipher suites to be enabled will be the ones
        // defined by the default JSSE implementation and only server
        // authentication will be required.
        //
        //SslRMIClientSocketFactory csf = new SslRMIClientSocketFactory();
        //SslRMIServerSocketFactory ssf = new SslRMIServerSocketFactory();
        //env.put(RMIConnectorServer.RMI_CLIENT_SOCKET_FACTORY_ATTRIBUTE, csf);
        //env.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE, ssf);

        // Provide the password file used by the connector server to
        // perform user authentication. The password file is a properties
        // based text file specifying username/password pairs.
        //
        //env.put("jmx.remote.x.password.file", "password.properties");

        // Provide the access level file used by the connector server to
        // perform user authorization. The access level file is a properties
        // based text file specifying username/access level pairs where
        // access level is either "readonly" or "readwrite" access to the
        // MBeanServer operations.
        //
        //env.put("jmx.remote.x.access.file", "access.properties");

        // Create an RMI connector server.
        //
        // As specified in the JMXServiceURL the RMIServer stub will be
        // registered in the RMI registry running in the local host on
        // port 3000 with the name "jmxrmi". This is the same name the
        // out-of-the-box management agent uses to register the RMIServer
        // stub too.
        //
        log.info("Create an RMI connector server");
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:" + port + "/jmxrmi");
        jmxConnectorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, env, mBeanServer);

        // Start the RMI connector server.
        //
        log.info("Start the RMI connector server on port " + port);
        jmxConnectorServer.start();
    }

    public MBeanServer getmBeanServer() {
        return mBeanServer;
    }
}
