<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html"/>
    <xsl:template match="/cache">
        <html>
            <head>
                <META HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
                <title>Chorus Web Agent - Index of Feature Cache <xsl:value-of select="@name"/></title>
                <LINK href="testSuite.css" rel="stylesheet" type="text/css"/>
            </head>
            <body>
                <h2>Index of Cache - <xsl:value-of select="@name"/></h2>
                <xsl:apply-templates select="resource"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="resource">
        <div>
            <table>
                <tr>
                    <th>Resource</th>
                    <th/>
                    <th/>
                </tr>
                <tr>
                    <td>
                        <xsl:value-of select="@name"/>
                    </td>
                    <td>
                        <xsl:element name="a">
                            <xsl:attribute name="href">
                                <xsl:value-of select="@xmlLink"/>
                            </xsl:attribute>XML/HTML</xsl:element>
                    </td>
                    <td>
                        <xsl:element name="a">
                            <xsl:attribute name="href">
                                <xsl:value-of select="@rssLink"/>
                            </xsl:attribute>RSS</xsl:element>
                    </td>
                </tr>
            </table>
        </div>
    </xsl:template>

</xsl:stylesheet>