<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/cache">
        <html>
            <head>
                <META HTTP-EQUIV="Pragma" CONTENT="no-cache"/>
                <title>Chorus Web Agent - Index of Feature Cache <xsl:value-of select="@name"/></title>
                <style type="text/css"/>
            </head>
            <body>
                <h2>Index of <xsl:value-of select="@name"/></h2>
                <xsl:apply-templates select="resource"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="resource">
        <div>
            <xsl:element name="a">
                <xsl:attribute name="href">
                    <xsl:value-of select="@xmlLink"/>
                </xsl:attribute>
                <xsl:value-of select="@name"/> XML/HTML </xsl:element>
            <span/><br/>
            <xsl:element name="a">
                <xsl:attribute name="href">
                    <xsl:value-of select="@rssLink"/>
                </xsl:attribute>
                <xsl:value-of select="@name"/> RSS </xsl:element>

        </div>
    </xsl:template>

</xsl:stylesheet>