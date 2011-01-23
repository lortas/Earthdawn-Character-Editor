<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet 
    version = "1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:edc="http://earthdawn.com/character"
    exclude-result-prefixes="edc"
>

<xsl:output
    method="html"
    encoding="UTF-8"
    doctype-public="-//W3C//DTD HTML 4.01//EN"
    doctype-system="http://www.w3.org/TR/html4/loose.dtd"
    indent="yes"
    omit-xml-declaration="yes"
/>

<xsl:template match="/">
<html>
    <head>
        <title><xsl:value-of select="/edc:EDCHARACTER/@name" /></title>
        <link rel="stylesheet" type="text/css" href="edstyle.css"/>
    </head>
    <body class="edBody">
 
        <!-- div needed for layout purposes -->
        <div class="edWrap">
        
            <!-- Heading -->
            <div class="edHeader">Earthdawn Character Sheet</div>
            
            
            <!-- Miscellaneous -->
            <div class="edMiscellaneous">
                <xsl:call-template name="miscellaneous" />
            </div>
            
            <!-- Attributes -->
            <div class="edAttributes"></div>
            
            <!-- Discipline Talents -->
            <div class="edDisciplineTalents"></div>

            <!-- Characteristics -->
            <div class="edCharacteristics"></div>
            
            <!-- Melee Weapons -->
            <div class="edMeleeWeapons"></div>
            
            <!-- Ranged Weapons -->
            <div class="edRangedWeapons"></div>
            
            <!-- Discipline Bonuses -->
            <div class="edRangedWeapons"></div>

            <!-- Other Talents -->
            <div class="edOtherTalents"></div>

            <!-- Skills -->
            <div class="edSkills"></div>

            <!-- Experience -->
            <div class="edExperience"></div>

            <!-- Spells -->
            <div class="edSpells"></div>
            
            <!-- Equipment -->
            <div class="edEquipment"></div>

            <!-- Blood Magic -->
            <div class="edBloodMagic"></div>

            <!-- Thread Magic -->
            <div class="edThreadMagic"></div>
            
            <!-- Pattern Items -->
            <div class="edThreadMagic"></div>

            <!-- Magical Treasure -->
            <div class="edMagicalTreasure"></div>

            <!-- Additional Discipline -->
            <div class="edAdditionalDiscipline"></div>
            
        </div>
    </body> 
</html> 
</xsl:template>

<xsl:template name="miscellaneous">
    <!-- Name -->
    <div class="edName">
        <xsl:call-template name="keyValuePair">
            <xsl:with-param name="key">Name</xsl:with-param>
            <xsl:with-param name="value" select="/edc:EDCHARACTER/@name" />
        </xsl:call-template>
    </div>
    
    <!-- Discipline -->
    <div class="edDiscipline">
        <xsl:call-template name="keyValuePair">
            <xsl:with-param name="key">Discipline</xsl:with-param>
            <xsl:with-param name="value" select="//edc:DISCIPLINE/@name">Name</xsl:with-param>
        </xsl:call-template>

        <xsl:call-template name="keyValuePair">
            <xsl:with-param name="key">Circle</xsl:with-param>
            <xsl:with-param name="value" select="//edc:DISCIPLINE/@circle"/>
        </xsl:call-template>
    </div>

    <!-- Appearance -->
    <div class="edAppearance">
        <xsl:apply-templates select="//edc:APPEARANCE"/>
    </div>
    
    <!-- TODO -->
</xsl:template>

<xsl:template match="//edc:APPEARANCE">
 
    <xsl:call-template name="keyValuePair">
        <xsl:with-param name="key">Race</xsl:with-param>
        <xsl:with-param name="value" select="@race"></xsl:with-param>
    </xsl:call-template>
    
    <xsl:call-template name="keyValuePair">
        <xsl:with-param name="key">Hair</xsl:with-param>
        <xsl:with-param name="value" select="@hair" />
    </xsl:call-template>
    
    <xsl:call-template name="keyValuePair">
        <xsl:with-param name="key">Skin</xsl:with-param>
        <xsl:with-param name="value" select="@skin" />
    </xsl:call-template>
            
    <xsl:call-template name="keyValuePair">
        <xsl:with-param name="key">Age</xsl:with-param>
        <xsl:with-param name="value" select="@age" />
    </xsl:call-template>
                
    <xsl:call-template name="keyValuePair">
        <xsl:with-param name="key">Height</xsl:with-param>
        <xsl:with-param name="value" select="@height" />
    </xsl:call-template>
                    
    <xsl:call-template name="keyValuePair">
        <xsl:with-param name="key">Weight</xsl:with-param>
        <xsl:with-param name="value" select="@weight" />
    </xsl:call-template>

    <xsl:call-template name="keyValuePair">
        <xsl:with-param name="key">Eyes</xsl:with-param>
        <xsl:with-param name="value" select="@eyes" />
    </xsl:call-template>
    
</xsl:template>

<xsl:template name="keyValuePair">
    <xsl:param name="key" />
    <xsl:param name="value" />
    
    <div class="edPair">
        <span class="edKey"><xsl:value-of select="$key" />: </span>
        <span class="edValue"><xsl:value-of select="$value" /></span>
    </div>
    
</xsl:template>
  
</xsl:stylesheet>