<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html"/>
    <xsl:template match="/suiteList">
        <html>
            <head>
                <META HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
                <title><xsl:value-of select="@title"/></title>
                <LINK href="testSuite.css" rel="stylesheet" type="text/css"/>
            </head>
            <body>
                <h2><xsl:value-of select="@title"/></h2>
                <table class='fullWidth'>
                    <tr><th/><th/><th colspan='3'>Features:</th><th/><th/></tr>
                    <tr>
                        <th>Suite</th>
                        <th>End State</th>
                        <th>Passed</th>
                        <th>Failed</th>
                        <th>Pending</th>
                        <th>Run Time (s)</th>
                        <th>Host</th>
                    </tr>
                    <xsl:apply-templates select="suite"/>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="suite">
        <tr>
            <td>
                <xsl:element name="a">
                    <xsl:attribute name="href">
                        <xsl:value-of select="@link"/>
                    </xsl:attribute>
                    <xsl:value-of select="@title"/>
                </xsl:element>
            </td>
            <td><xsl:value-of select="@endState"/></td>
            <td class='pass'><xsl:value-of select="resultSummaryBean/@featuresPassed"/></td>
            <td class='fail'><xsl:value-of select="resultSummaryBean/@featuresFailed"/></td>
            <td class='other'><xsl:value-of select="resultSummaryBean/@featuresPending"/></td>
            <td><xsl:value-of select="resultSummaryBean/@timeTakenSeconds"/></td>
            <td><xsl:value-of select="@executionHost"/></td>
        </tr>
    </xsl:template>


</xsl:stylesheet>