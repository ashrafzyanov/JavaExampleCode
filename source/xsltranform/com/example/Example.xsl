<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema">

    <xsl:output indent="yes" />
    <xsl:output omit-xml-declaration="yes"/>


    <xsl:template match="/">
        <html><body>
            <xsl:apply-templates />
        </body></html>
    </xsl:template>

    <xsl:template match="/Country/name">
        <i><xsl:apply-templates /></i>
    </xsl:template>

    <xsl:template match="/Country/goverment">
        <b><xsl:apply-templates /></b>
    </xsl:template>

    <xsl:template match="/Country/sience">
        <a href=""><xsl:apply-templates /></a>
    </xsl:template>

    <xsl:template match="/Country/countOfPeple">
        <i><b><xsl:apply-templates /></b></i>
    </xsl:template>



</xsl:stylesheet>