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
    <table border="1">
        <tr>
            <td>Name</td>
            <td colspan="5"><xsl:value-of select="/edc:EDCHARACTER/@name" /></td>
        </tr>
    
        <!-- Discipline -->
        <tr>
            <td>Discipline</td>
            <td><xsl:value-of select="//edc:DISCIPLINE/@name" /></td>
            
            <td>Circle</td>
            <td colspan="3"><xsl:value-of select="//edc:DISCIPLINE/@circle"/></td>
        </tr>

        <!-- Appearance -->
        <xsl:apply-templates select="//edc:APPEARANCE"/>

    </table>

</xsl:template>

<xsl:template match="//edc:APPEARANCE"> 
    <tr>
        <td>Race</td>
        <td><xsl:value-of select="@race" /></td>

        <td>Hair</td>
        <td><xsl:value-of select="@hair" /></td>

        <td>Skin</td>
        <td><xsl:value-of select="@skin" /></td>
    </tr>
            
    <tr>
        <td>Age</td>
        <td><xsl:value-of select="@age" /></td>
                
        <td>Height</td>
        <td><xsl:value-of select="@height" /></td>
                    
        <td>Weight</td>
        <td><xsl:value-of select="@weight" /></td>
    </tr>
    
    <tr>
        <td>Eyes</td>
        <td><xsl:value-of select="@eyes" /></td>
    </tr>
    
</xsl:template>
  
</xsl:stylesheet>