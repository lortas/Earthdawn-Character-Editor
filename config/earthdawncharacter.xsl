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

            <!-- Characteristics -->
            <div class="edCharacteristics">
                <xsl:call-template name="characteristics"/>
            </div>
            
            <!-- Discipline -->
            <div class="edDiscipline">
                <xsl:call-template name="discipline"/>
            </div>
            
            <!-- Weapons -->
            <div class="edWeapons">
                <xsl:call-template name="weapons" />
            </div>

            <!-- Skills -->
            <div class="edSkills">
                <xsl:call-template name="skills" />
            </div>

            <!-- Racial Abilities -->
            <div class="edRaceabilities">
                <xsl:call-template name="raceabilities" />
            </div>

            <!-- Experience -->
            <div class="edExperience">
                <xsl:call-template name="experience" />
            </div>
            
            <!-- Equipment -->
            <div class="edEquipment">
                <xsl:call-template name="equipment" />
            </div>
            
            <!-- Coins -->
            <div class="edCoins">
                <xsl:call-template name="coins" />
            </div>

            <!-- Magic Items -->
            <div class="edMagicItems">
                <xsl:call-template name="magicItems" />
            </div>

            <!-- Pattern Items -->
            <div class="edPatternItems">
                <xsl:call-template name="patternItems" />
            </div>

        </div>
    </body> 
</html> 
</xsl:template>

<xsl:template name="miscellaneous">

    <div class="edSubHeader">General Information</div>

    <table border="1">
        <!-- Name -->
        <tr>
            <td>Name</td>
            <td colspan="5"><xsl:value-of select="/edc:EDCHARACTER/@name" /></td>
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
    <div class="edSubHeader">Attributes</div>
    
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

<xsl:template name="characteristics">
    <div class="edSubHeader">Characteristics</div>
    
    <!-- Movement -->
    <div class="edMovement">
        <xsl:apply-templates select="//edc:MOVEMENT"/>    
    </div>
     
    <!-- Karma -->
    <div class="edKarma">
        <xsl:apply-templates select="//edc:KARMA"/>    
    </div>
     
    <!-- Initiative -->
    <div class="edInitiative">
        <xsl:apply-templates select="//edc:INITIATIVE"/>    
    </div>
     
    <!-- Defense -->
    <div class="edDefense">
        <xsl:apply-templates select="//edc:DEFENSE"/>    
    </div>
    
    <!-- Armor -->
    <div class="edPROTECTION">
        <xsl:apply-templates select="//edc:PROTECTION"/>    
    </div>
     
    <!-- Health -->
    <div class="edHealth">
        <xsl:apply-templates select="//edc:HEALTH"/>    
    </div>     
     
</xsl:template>

<xsl:template match="//edc:MOVEMENT">
    <hr/>
    Movement:<br/>
    Ground=<xsl:value-of select="@ground" /><br/>
    Flight=<xsl:value-of select="@flight" />
    
</xsl:template>

<xsl:template match="//edc:KARMA">
    <hr/>
    Karma:<br/>
    Step=<xsl:value-of select="@current" /><br/>
    Dice=<xsl:value-of select="@max" />
</xsl:template>

<xsl:template match="//edc:INITIATIVE">
    <hr/>
    Initiative:<br/>
    Karma Points=<xsl:value-of select="@step" /><br/>
    Max=<xsl:value-of select="@dice" />
</xsl:template>

<xsl:template match="//edc:DEFENSE">
    <hr/>
    Defense Ratings:<br/>
    Social Defense=<xsl:value-of select="@social" /><br/>
    Spell Defense=<xsl:value-of select="@spell" /><br/>
    Physical Defense=<xsl:value-of select="@physical" />
</xsl:template>

<xsl:template match="//edc:PROTECTION">
    <hr/>
    Armor Ratings:<br/>
    Mystic Armor=<xsl:value-of select="@mysticarmor" /><br/>
    Physical Armor=<xsl:value-of select="@physicalarmor" />
    
    <xsl:apply-templates select="//edt:ARMOR"/>
    <xsl:apply-templates select="//edt:SHIELD"/>
    
</xsl:template>

<xsl:template match="//edt:ARMOR">
    <div>
        Armor:<br />
        Name=<xsl:value-of select="@name" /><br/>
    </div>
</xsl:template>

<xsl:template match="//edt:SHIELD">
    Shield:<br />
    <div>
        Name=<xsl:value-of select="@name" /><br/>
        Deflection Bonus=<xsl:value-of select="@deflectionbonus" /><br/>
    </div>
</xsl:template>

<xsl:template match="//edc:HEALTH">
    <hr/>
    Health:<br/>
    Current Damage=<xsl:value-of select="@damage" /><br/>
    
    <xsl:apply-templates select="//edt:RECOVERY"/>
    <xsl:apply-templates select="//edt:UNCONSCIOUSNESS"/>    
    <xsl:apply-templates select="//edt:DEATH"/>
    <xsl:apply-templates select="//edt:WOUNDS"/>
    
</xsl:template>

<xsl:template match="//edt:RECOVERY">
    <div>
        Recovery:<br />
        Test per day=<xsl:value-of select="@testsperday" /><br /> 
        Dice=<xsl:value-of select="@dice" /><br />
        Step=<xsl:value-of select="@step" />
    </div>
</xsl:template>

<xsl:template match="//edt:UNCONSCIOUSNESS">
    <div>
        Unconscieousness:<br />
        Base=<xsl:value-of select="@base" /><br />
        Adjustment=<xsl:value-of select="@adjustment" /><br />
        Current Value=<xsl:value-of select="@base" />
    </div>
</xsl:template>

<xsl:template match="//edt:DEATH">
    <div>
        Death:<br />
        Base=<xsl:value-of select="@base" /><br />
        Adjustment=<xsl:value-of select="@adjustment" /><br />
        Current Value=<xsl:value-of select="@base" />
    </div>
</xsl:template>

<xsl:template match="//edt:WOUNDS">
    <div>
        Wounds:<br />   
        Threshold=<xsl:value-of select="@threshold" /><br />
    </div>
</xsl:template>

<xsl:template name="discipline">

    <xsl:apply-templates select="//edc:DISCIPLINE"/>

</xsl:template>

<xsl:template match="//edc:DISCIPLINE">

    <div class="edSubHeader">Discipline <xsl:value-of select="@name" /></div>

    <!-- Discipline -->
    <table border="1">
        <tr>
            <td>Discipline</td>
            <td><xsl:value-of select="//edc:DISCIPLINE/@name" /></td>
            
            <td>Circle</td>
            <td colspan="3"><xsl:value-of select="//edc:DISCIPLINE/@circle"/></td>
        </tr>
    </table>

    <!-- Discipline Bonuses -->

    <!-- Discipline Talents -->
    <div class="edDisciplineTalents">
        <xsl:call-template name="disziplinTalents">
            <xsl:with-param name="disciplineName" select="@name" />
        </xsl:call-template>
    </div>
    
    <!-- Other Talents -->
    <div class="edOtherTalents">
        <xsl:call-template name="optionalTalents">
            <xsl:with-param name="disciplineName" select="@name" />
        </xsl:call-template>
    </div>
            
    <!-- Spells -->
    <div class="edSpells">
        <xsl:call-template name="spells">
            <xsl:with-param name="disciplineName" select="@name" />
        </xsl:call-template>
    </div>

</xsl:template>
  
<xsl:template name="disziplinTalents">
    <xsl:param name="disciplineName" />

    <div class="edSubSubHeader">Discipline Talents</div>
    
    <table border="1">
        <tr>
            <td>Talentname</td>
            <td>Action</td>
            <td>Strain</td>
            <td>Attibute</td>
            <td>Rank</td>
            <td>Step</td>
            <td>Action Dice</td>
        </tr>

        <xsl:apply-templates select="//edc:TALENTS[@discipline=$disciplineName]/edc:DISZIPLINETALENT"/>

    </table>
</xsl:template>

<xsl:template match="//edc:DISZIPLINETALENT">
    <tr>
        <td><xsl:value-of select="@name"/></td>
        <td><xsl:value-of select="@action"/></td>
        <td><xsl:value-of select="@strain"/></td>
        <td><xsl:value-of select="@attribute"/></td>
        <td><xsl:value-of select="./edt:RANK/@rank"/></td>
        <td><xsl:value-of select="./edt:RANK/@step"/></td>
        <td><xsl:value-of select="./edt:RANK/@dice"/></td>   
    </tr>
</xsl:template>

<xsl:template name="optionalTalents">
    <xsl:param name="disciplineName" />

    <div class="edSubSubHeader">Optional Talents</div>
    <table border="1">
        <tr>
            <td>Talent Name</td>
            <td>Karma?</td>
            <td>Action</td>
            <td>Strain</td>
            <td>Attribute</td>
            <td>Rank</td>
            <td>Step</td>
            <td>Action Dice</td>
        </tr>

        <xsl:apply-templates select="//edc:TALENTS[@discipline=$disciplineName]/edc:OPTIONALTALENT"/>

    </table>
</xsl:template>

<xsl:template match="//edc:OPTIONALTALENT">
    <tr>
        <td><xsl:value-of select="@name"/></td>
        <td><xsl:value-of select="@karma"/></td>
        <td><xsl:value-of select="@action"/></td>
        <td><xsl:value-of select="@attribute"/></td>
        <td><xsl:value-of select="@attribute"/></td>
        <td><xsl:value-of select="./edt:RANK/@rank"/></td>
        <td><xsl:value-of select="./edt:RANK/@step"/></td>
        <td><xsl:value-of select="./edt:RANK/@dice"/></td>
    </tr>
</xsl:template>

<xsl:template name="spells">
    <xsl:param name="disciplineName" />

    <div class="edSubSubHeader">Spells</div>
    
    <table border="1">
        <tr>
            <td>Spellname</td>
            <td>In Matrix?</td>
            <td>Type</td>
            <td>Circle</td>
            <td>Threads</td>
            <td>Weaving Difficulty</td>
            <td>Reattuning Difficulty</td>
            <td>Casting Difficulty</td>
            <td>Range</td>
            <td>Duration</td>
            <td>Effect</td>
        </tr>

        <xsl:apply-templates select="//edc:SPELLS[@discipline=$disciplineName]/edc:SPELL"/>

    </table>
</xsl:template>

<xsl:template match="//edc:SPELL">
    <tr>
        <td><xsl:value-of select="@name" /></td>
        <td>TODO</td>
        <td><xsl:value-of select="@type" /></td>
        <td><xsl:value-of select="@circle" /></td>
        <td><xsl:value-of select="@threads" /></td>
        <td><xsl:value-of select="@weavingdifficulty" /></td>
        <td><xsl:value-of select="@reattuningdifficulty" /></td>
        <td><xsl:value-of select="@castingdifficulty" /></td>
        <td><xsl:value-of select="@range" /></td>
        <td><xsl:value-of select="@duration" /></td>
        <td><xsl:value-of select="@effect" /></td>
    </tr>
</xsl:template>

<xsl:template name="weapons">
    <div class="edSubHeader">Weapons</div>
    <xsl:apply-templates select="//edc:WEAPON"/>
</xsl:template>

<xsl:template match="//edc:WEAPON">
    Name=<xsl:value-of select="@name" /><br />
    Damage Step=<xsl:value-of select="@damagestep" /><br />
    Size=<xsl:value-of select="@size" /><br />
    Times forged=<xsl:value-of select="@timesforged" /><br />
    Short=<xsl:value-of select="@shortrange" /><br />
    Medium=<xsl:value-of select="@mediumrange" /><br />
    Long=<xsl:value-of select="@longrange" /><br />
</xsl:template>

<xsl:template name="skills">
    <div class="edSubHeader">Skills</div>
    
    <table border="1">
        <tr>
            <td>Skillname</td>
            <td>Action</td>
            <td>Strain</td>
            <td>Attibute</td>
            <td>Rank</td>
            <td>Step</td>
            <td>Action Dice</td>
        </tr>

        <xsl:apply-templates select="//edc:SKILL"/>

    </table>
</xsl:template>

<xsl:template match="//edc:SKILL">
    <tr>
        <td><xsl:value-of select="@name"/></td>
        <td><xsl:value-of select="@action"/></td>
        <td><xsl:value-of select="@strain"/></td>
        <td><xsl:value-of select="@attribute"/></td>
        <td><xsl:value-of select="./edt:RANK/@rank"/></td>
        <td><xsl:value-of select="./edt:RANK/@step"/></td>
        <td><xsl:value-of select="./edt:RANK/@dice"/></td>   
    </tr>
</xsl:template>

<xsl:template name="raceabilities">
    <div class="edSubHeader">Racial Abilities</div>
    
        <table border="1">
            <xsl:apply-templates select="//edc:RACEABILITES"/>
        </table>
</xsl:template>

<xsl:template match="//edc:RACEABILITES">
    <tr>
        <td><xsl:value-of select="."/></td>
    </tr>
</xsl:template>

<xsl:template name="experience">
    <div class="edSubHeader">Experience</div>

    <div>            
        Reputation=<xsl:value-of select="//edc:EXPERIENCE/@reputation" /><br />
        Renown=<xsl:value-of select="//edc:EXPERIENCE/@renown" /><br />
        Currentledgend Points=<xsl:value-of select="//edc:EXPERIENCE/@currentlegendpoints" /><br />
        Totalledgend Points=<xsl:value-of select="//edc:EXPERIENCE/@totallegendpoints" />
    </div>   
            
</xsl:template>

<xsl:template name="equipment">
    <div class="edSubHeader">Equipment</div>
    
    <xsl:apply-templates select="//edc:ITEM"/>

</xsl:template>

<xsl:template match="//edc:ITEM">
    <hr />
    
    Name=<xsl:value-of select="@name" />
    Weight=<xsl:value-of select="@weight" />
    
</xsl:template>

<xsl:template name="coins">
    <div class="edSubHeader">Coins</div>

    TODO

</xsl:template>

<xsl:template name="patternItems">
    <div class="edSubHeader">Pattern Items</div>
    
    TODO

</xsl:template>

<xsl:template name="magicItems">
    <div class="edSubHeader">Magic Items</div>

    TODO
</xsl:template>

</xsl:stylesheet>