package org.chorusbdd.chorus.tools.webagent.filter;

import javax.servlet.http.HttpServletRequest;

/**
 * User: nick
 * Date: 12/09/13
 * Time: 18:06
 */
public interface FilterFactory {
    
    TestSuiteFilter createFilter(String target, HttpServletRequest request);
}
