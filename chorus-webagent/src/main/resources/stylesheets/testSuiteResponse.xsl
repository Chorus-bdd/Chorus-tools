<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/testSuite">
        <html>
            <head>
                <META HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
                <title><xsl:value-of select="@suiteName"/></title>
                <LINK href="../testSuite.css" rel="stylesheet" type="text/css"/>
            </head>
            <body>
             <div class="suiteDetails">
                 <span class="suiteName">Test Suite: <xsl:value-of select="@suiteName"/></span>
                <div class="suiteTime"><xsl:value-of select="execution/@executionStartTime"/>
                <span class="suiteTimeTaken">
                    Host: <xsl:value-of select="execution/@executionHost"/>&#160;&#160;
                    Run time: <xsl:value-of select="execution/resultsSummary/@timeTakenSeconds"/> seconds
                </span>
                </div>
                 <xsl:apply-templates select="execution"/>
            </div>
            <xsl:apply-templates select="features/feature"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="execution">
        <div class="results">
            <table class="resultsTable">
                <tr>
                    <th scope="row"></th>
                    <th scope="col">Passed</th>
                    <th scope="col">Failed</th>
                    <th scope="col">Pending</th>
                    <th scope="col">Skipped</th>
                    <th scope="col">Undefined</th>
                </tr>
                <tr>
                    <th scope="row">Feature</th>
                    <td class="pass"><xsl:value-of select="resultsSummary/@featuresPassed"/></td>
                    <td class="fail"><xsl:value-of select="resultsSummary/@featuresFailed"/></td>
                    <td/>
                    <td/>
                    <td/>
                </tr>
                <tr>
                    <th scope="row">Scenario</th>
                    <td class="pass"><xsl:value-of select="resultsSummary/@scenariosPassed"/></td>
                    <td class="fail"><xsl:value-of select="resultsSummary/@scenariosFailed"/></td>
                    <td/>
                    <td/>
                    <td/>
                </tr>
                <tr>
                    <th scope="row">Step</th>
                    <td class="pass"><xsl:value-of select="resultsSummary/@stepsPassed"/></td>
                    <td class="fail"><xsl:value-of select="resultsSummary/@stepsFailed"/></td>
                    <td><xsl:value-of select="resultsSummary/@stepsPending"/></td>
                    <td><xsl:value-of select="resultsSummary/@stepsSkipped"/></td>
                    <td><xsl:value-of select="resultsSummary/@stepsUndefined"/></td>
                </tr>
            </table>
        </div>
    </xsl:template>

    <xsl:template match="feature">
        <div class="featureTitle"><xsl:value-of select="@name"/></div>
        <div class="featureDescription">
        <xsl:value-of select="description"/>
        </div>
        <xsl:apply-templates select="scenarios/scenario"/>
    </xsl:template>

    <xsl:template match="scenario">
        <div class="scenarioTitle"><xsl:value-of select="@name"/></div>
        <div class="steps">
            <xsl:apply-templates select="steps/step"/>
        </div>
    </xsl:template>

    <xsl:template match="step">
        <div class="step">
            <xsl:variable name="stepClass">
                <xsl:choose>
                    <xsl:when test="@endState='PASSED'">
                        <xsl:value-of select="'OK'"/>
                    </xsl:when>
                    <xsl:when test="@endState='FAILED'">
                        <xsl:value-of select="'FAIL'"/>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="'PEND'"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:variable>
            <span class="stepText{$stepClass}"><xsl:value-of select="@type"/>&#160;<xsl:value-of select="@action"/></span>
            <span class="resultText{$stepClass}"><xsl:value-of select="@endState"/></span>
            <span class="timeTaken">(<xsl:value-of select="@timeTakenSeconds"/>s)</span>
        </div>
    </xsl:template>

</xsl:stylesheet>