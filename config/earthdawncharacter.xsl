<?xml version="1.0" encoding="UTF-8"?>

<xsl:stylesheet 
    version = "1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:edc="http://earthdawn.com/character"
    xmlns:edt="http://earthdawn.com/datatypes"
    exclude-result-prefixes="edc edt"
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
            <div class="edAttributes">
                <xsl:call-template name="attributes"/>
            </div>
            
            <!-- Discipline Talents -->
            <div class="edDisciplineTalents">
                <xsl:call-template name="disziplintalents"/>
            </div>

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

    <div class="subHeader">General Information</div>

    <table border="1">
        <!-- Name -->
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
  
<xsl:template name="attributes">
    <div class="subHeader">Attributes</div>
    
    <table border="1">
        <tr>
            <td><!-- Leer --></td>
            <td>Base Value</td>
            <td>LP Increase</td>
            <td>Current Value</td>
            <td>Step</td>
            <td>Action Dice</td>
        </tr>
        
        <xsl:apply-templates select="//edc:ATTRIBUTE"/>

    </table>
    
</xsl:template>
  
<xsl:template match="//edc:ATTRIBUTE">
    <tr>
        <td><xsl:value-of select="@name"/></td>
        <td><xsl:value-of select="@racevalue"/></td>
        <td><xsl:value-of select="@lpincrease"/></td>
        <td><xsl:value-of select="@currentvalue"/></td>
        <td><xsl:value-of select="@step"/></td>
        <td><xsl:value-of select="@dice"/></td>   
    </tr>
</xsl:template>


<xsl:template name="disziplintalents">
    <div class="subHeader">Discipline Talents</div>
    
    <table border="1">
        <tr>
            <td>Talentname</td>
            <td>Action</td>
            <td>Strain</td>
            <td>Attibute</td>
            <td>Rank</td>
            <td>Step</td>
            <td>Action Dice</td>
            
            <xsl:apply-templates select="//edc:DISZIPLINETALENT"/>
            
        </tr>
    </table>
</xsl:template>

<xsl:template match="//edc:DISZIPLINETALENT">
    <tr>
        <td><xsl:value-of select="@name"/></td>
        <td><xsl:value-of select="@action"/></td>
        <td>TODO</td>
        <td><xsl:value-of select="@attribute"/></td>
        <td><xsl:value-of select="./edt:RANK/@rank"/></td>
        <td><xsl:value-of select="./edt:RANK/@step"/></td>
        <td><xsl:value-of select="./edt:RANK/@dice"/></td>   
    </tr>
</xsl:template>

</xsl:stylesheet>