<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/testSuite">
        <html>
            <head>
                <META HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
                <title><xsl:value-of select="@title"/></title>
            </head>
            <body>
             <div class="suiteDetails">
                 <span class="suiteName">Test Suite: <xsl:value-of select="@title"/></span>
                <div class="suiteTime"> <xsl:value-of select="@suiteTime"/>
                <xsl:apply-templates select="execution"/>
                </div>
            </div>

        </body>
        </html>
    </xsl:template>

    <xsl:template match="execution">
        <span class="suiteTimeTaken">Run time: <xsl:value-of select="@timeTakenSeconds"/> seconds</span>
    </xsl:template>

</xsl:stylesheet>