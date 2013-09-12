package org.chorusbdd.chorus.tools.webagent.filter;

import javax.servlet.http.HttpServletRequest;

/**
 * User: nick
 * Date: 12/09/13
 * Time: 18:07
 */
public class SuiteListFilterFactory implements FilterFactory {

    private TestSuiteFilter baseFilter;

    public SuiteListFilterFactory(TestSuiteFilter baseFilter) {
        this.baseFilter = baseFilter;
    }
    
    public TestSuiteFilter createFilter(String target, HttpServletRequest request) {

        TestSuiteFilter testSuiteFilter = baseFilter;
        
        //if parameter set, add the filter rule to the rule chain
        if ( request.getParameter(SuiteNameFilter.SUITE_NAME_HTTP_PARAMETER) != null ) {
            String[] suiteNames = request.getParameterValues(SuiteNameFilter.SUITE_NAME_HTTP_PARAMETER);
            testSuiteFilter = new SuiteNameFilter(suiteNames, testSuiteFilter);
        }

        //if parameter set, add the filter rule to the rule chain
        if ( request.getParameter(SuiteEndStateFilter.SUITE_END_STATE_HTTP_PARAMETER) != null) {
            String[] suiteEndStates = request.getParameterValues(SuiteEndStateFilter.SUITE_END_STATE_HTTP_PARAMETER);
            //add the filter rule to the rule chain
            testSuiteFilter = new SuiteEndStateFilter(suiteEndStates, testSuiteFilter);
        }
        return testSuiteFilter;    }
}
