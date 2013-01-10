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
                <xsl:apply-templates select="suite"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="suite">
        <div>
            <xsl:element name="a">
                <xsl:attribute name="href">
                    <xsl:value-of select="@link"/>
                </xsl:attribute>
                <xsl:value-of select="@name"/>
            </xsl:element>
        </div>
    </xsl:template>


</xsl:stylesheet>