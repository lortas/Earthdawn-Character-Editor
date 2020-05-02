<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet
	version = "1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:edc="http://earthdawn.com/character"
	xmlns:edt="http://earthdawn.com/datatypes"
	exclude-result-prefixes="edc edt"
>

<xsl:output
	method="xml"
	encoding="UTF-8"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:edc="http://earthdawn.com/character"
	xmlns:edt="http://earthdawn.com/datatypes"
	indent="yes"
	omit-xml-declaration="no"
	standalone="no"
/>

<xsl:template match="node()|@*">
	<xsl:choose>
		<xsl:when test="local-name()='DISZIPLINETALENT'">
			<xsl:call-template name="CAPABILITY"/>
		</xsl:when>
		<xsl:when test="local-name()='OPTIONALTALENT'">
			<xsl:call-template name="CAPABILITY"/>
		</xsl:when>
		<xsl:when test="local-name()='SKILL'">
			<xsl:call-template name="CAPABILITY"/>
		</xsl:when>
		<xsl:when test="local-name()='xsd-version'">
			<xsl:attribute name='xsd-version'>1.1</xsl:attribute>
		</xsl:when>
		<xsl:when test="node() and local-name()='description'">
			<xsl:call-template name="description"/>
		</xsl:when>
		<xsl:otherwise>
			<xsl:copy>
				<xsl:apply-templates select="@*"/>
				<xsl:apply-templates/>
			</xsl:copy>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="CAPABILITY">
	<xsl:choose>
		<xsl:when test="@limitation!=''">
			<xsl:copy>
				<xsl:call-template name="MOVE_LIMITATION"/>
			</xsl:copy>
		</xsl:when>
		<xsl:otherwise>
			<xsl:copy-of select="."/>
		</xsl:otherwise>
	</xsl:choose>
</xsl:template>

<xsl:template name="MOVE_LIMITATION">
	<xsl:for-each select="@*">
		<xsl:if test="name()!='limitation'">
			<xsl:copy/>
		</xsl:if>
	</xsl:for-each>
	<xsl:element name="edt:LIMITATION"><xsl:value-of select="@limitation"/></xsl:element>
	<xsl:for-each select="node()">
		<xsl:copy-of select="."/>
	</xsl:for-each>
</xsl:template>

<xsl:template name="description">
	<xsl:element name="DESCRIPTION" namespace="{namespace-uri()}">
		<xsl:apply-templates select="@*"/>
		<xsl:apply-templates/>
		<xsl:value-of select="."/>
	</xsl:element>
</xsl:template>

<xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz '" />
<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ_'" />
</xsl:stylesheet>
