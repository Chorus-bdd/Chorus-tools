<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/index">
        <html>
            <head>
                <META HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
                <title>Chorus Web Agent - Index of Feature Cache</title>
                <style type="text/css"/>
            </head>
            <body>
                <h2>Index of Feature Cache</h2>
                <xsl:apply-templates select="featureCache"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="featureCache">
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