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
        <link rel="stylesheet" type="text/css" href="earthdawncharacter.css"/>
    </head>
    <body class="edBody">
        <!-- div needed for layout purposes -->
        <div class="edWrap">
            <!-- Heading -->
            <div class="edHeader">Earthdawn Character Sheet</div>
            <!-- Miscellaneous -->
            <xsl:call-template name="miscellaneous" />
            <!-- Attributes -->
            <xsl:call-template name="attributes"/>
            <!-- Characteristics -->
            <xsl:call-template name="characteristics"/>
            <!-- Discipline -->
            <xsl:call-template name="discipline"/>
            <!-- Weapons -->
            <xsl:call-template name="weapons" />
            <!-- Skills -->
            <xsl:call-template name="skills" />
            <!-- Racial Abilities -->
            <xsl:call-template name="raceabilities" />
            <!-- Experience -->
            <xsl:call-template name="experience" />
            <!-- Equipment -->
            <xsl:call-template name="equipment" />
            <!-- Coins -->
            <xsl:call-template name="coins" />
            <!-- Magic Items -->
            <xsl:call-template name="magicItems" />
            <!-- Pattern Items -->
            <xsl:call-template name="patternItems" />
        </div>
    </body> 
</html> 
</xsl:template>

<xsl:template name="miscellaneous">
    <div class="edMiscellaneous">
        <div class="edSubHeader">General Information</div>
        <table>
            <!-- Name -->
            <tr>
                <td class="edKeyCell">Name:</td>
                <td colspan="5" class="edValueCell"><xsl:value-of select="/edc:EDCHARACTER/@name" /></td>
            </tr>
            <!-- Appearance -->
            <xsl:apply-templates select="//edc:APPEARANCE"/>
        </table>
    </div>
</xsl:template>

<xsl:template match="//edc:APPEARANCE"> 
    <tr>
        <td class="edKeyCell">Race:</td>
        <td class="edValueCell"><xsl:value-of select="@race" /></td>
        <td class="edKeyCell">Age:</td>
        <td class="edValueCell"><xsl:value-of select="@age" /></td>
        <td class="edKeyCell">Skin:</td>
        <td class="edValueCell"><xsl:value-of select="@skin" /></td>
    </tr>
    <tr>
        <td class="edKeyCell">Hair:</td>
        <td class="edValueCell"><xsl:value-of select="@hair" /></td>        
        <td class="edKeyCell">Height:</td>
        <td class="edValueCell"><xsl:value-of select="@height" /></td>
        <td class="edKeyCell">Weight:</td>
        <td class="edValueCell"><xsl:value-of select="@weight" /></td>
    </tr>
    <tr>
        <td class="edKeyCell">Eyes:</td>
        <td class="edValueCell"><xsl:value-of select="@eyes" /></td>
    </tr>
</xsl:template>
  
<xsl:template name="attributes">
    <div class="edAttributes">
        <div class="edSubHeader">Attributes</div>
        <table>
            <tr>
                <td class="edHeaderCell"><!-- Leer --></td>
                <td class="edHeaderCell">Base Value</td>
                <td class="edHeaderCell">LP Increase</td>
                <td class="edHeaderCell">Current Value</td>
                <td class="edHeaderCell">Step</td>
                <td class="edHeaderCell">Action Dice</td>
            </tr>
            <xsl:apply-templates select="//edc:ATTRIBUTE"/>
        </table>
    </div>
</xsl:template>
  
<xsl:template match="//edc:ATTRIBUTE">
    <tr>
        <td class="edCell"><xsl:value-of select="@name"/></td>
        <td class="edCell"><xsl:value-of select="@racevalue"/></td>
        <td class="edCell"><xsl:value-of select="@lpincrease"/></td>
        <td class="edCell"><xsl:value-of select="@currentvalue"/></td>
        <td class="edCell"><xsl:value-of select="@step"/></td>
        <td class="edCell"><xsl:value-of select="@dice"/></td>   
    </tr>
</xsl:template>

<xsl:template name="characteristics">
    <div class="edCharacteristics">
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
    </div> 
</xsl:template>

<xsl:template match="//edc:MOVEMENT">
    <div class="edSubSubHeader">Movement</div>
    <table>
        <tr>
            <td class="edKeyCell">Ground:</td>
            <td class="edValueCell"><xsl:value-of select="@ground" /></td>
        </tr>
        <tr>
            <td class="edKeyCell">Flight:</td>
            <td class="edValueCell"><xsl:value-of select="@flight" /></td>
        </tr>
    </table>
</xsl:template>

<xsl:template match="//edc:KARMA">
    <div class="edSubSubHeader">Karma</div>
    <table>
        <tr>
            <td class="edKeyCell">Karma Points:</td>
            <td class="edValueCell"><xsl:value-of select="@current" /></td>
        </tr>
        <tr>
            <td class="edKeyCell">Max:</td>
            <td class="edValueCell"><xsl:value-of select="@max" /></td>
        </tr>
    </table>
</xsl:template>

<xsl:template match="//edc:INITIATIVE">
    <div class="edSubSubHeader">Initiative</div>
    <table>
        <tr>
            <td class="edKeyCell">Step:</td>
            <td class="edValueCell"><xsl:value-of select="@step" /></td>
        </tr>
        <tr>
            <td class="edKeyCell">Dice:</td>
            <td class="edValueCell"><xsl:value-of select="@dice" /></td>
        </tr>
    </table>
</xsl:template>

<xsl:template match="//edc:DEFENSE">
    <div class="edSubSubHeader">Defense Ratings</div>
    <table>
        <tr>
            <td class="edKeyCell">Social Defense:</td>
            <td class="edValueCell"><xsl:value-of select="@social" /></td>
        </tr>
        <tr>
            <td class="edKeyCell">Spell Defense:</td>
            <td class="edValueCell"><xsl:value-of select="@spell" /></td>
        </tr>
        <tr>
            <td class="edKeyCell">Physical Defense:</td>
            <td class="edValueCell"><xsl:value-of select="@physical" /></td>
        </tr>
    </table>
</xsl:template>

<xsl:template match="//edc:PROTECTION">
    <div class="edSubSubHeader">Armor Ratings</div>
    <table>
        <tr>
            <td class="edKeyCell">Mystic Armor:</td>
            <td class="edValueCell"><xsl:value-of select="@mysticarmor" /></td>
        </tr>
        <tr>
            <td class="edKeyCell">Physical Armor:</td>
            <td class="edValueCell"><xsl:value-of select="@physicalarmor" /></td>
        </tr>
    </table>
    <!-- TODO: -->
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
    <div class="edDiscipline">
        <div class="edSubHeader">Discipline <xsl:value-of select="@name" /></div>
        <!-- Discipline -->
        <table>
            <tr>
                <td class="edKeyCell">Discipline</td>
                <td class="edValueCell"><xsl:value-of select="@name" /></td>                
                <td class="edKeyCell">Circle</td>
                <td class="edValueCell" colspan="3"><xsl:value-of select="@circle"/></td>
            </tr>
        </table>
        <!-- Discipline Talents -->
        <xsl:call-template name="disziplinTalents">
            <xsl:with-param name="disciplineName" select="@name" />
        </xsl:call-template>
        <!-- Other Talents -->
        <xsl:call-template name="optionalTalents">
            <xsl:with-param name="disciplineName" select="@name" />
        </xsl:call-template>
        <!-- Spells -->
        <xsl:call-template name="spells">
            <xsl:with-param name="disciplineName" select="@name" />
        </xsl:call-template>
        <!-- Discipline Bonuses -->
        <xsl:call-template name="disciplineBonuses"/>
    </div>
</xsl:template>
  
<xsl:template name="disziplinTalents">
    <xsl:param name="disciplineName" />
    <div class="edDisciplineTalents">
        <div class="edSubSubHeader">Discipline Talents</div>
        <table>
            <tr>
                <td class="edHeaderCell">Talentname</td>
                <td class="edHeaderCell">Action</td>
                <td class="edHeaderCell">Strain</td>
                <td class="edHeaderCell">Attibute</td>
                <td class="edHeaderCell">Rank</td>
                <td class="edHeaderCell">Step</td>
                <td class="edHeaderCell">Action Dice</td>
            </tr>
            <xsl:apply-templates select="//edc:TALENTS[@discipline=$disciplineName]/edc:DISZIPLINETALENT"/>
        </table>
    </div>
</xsl:template>

<xsl:template match="//edc:DISZIPLINETALENT">
    <tr>
        <td class="edCell"><xsl:value-of select="@name"/></td>
        <td class="edCell"><xsl:value-of select="@action"/></td>
        <td class="edCell"><xsl:value-of select="@strain"/></td>
        <td class="edCell"><xsl:value-of select="@attribute"/></td>
        <td class="edCell"><xsl:value-of select="./edt:RANK/@rank"/></td>
        <td class="edCell"><xsl:value-of select="./edt:RANK/@step"/></td>
        <td class="edCell"><xsl:value-of select="./edt:RANK/@dice"/></td>   
    </tr>
</xsl:template>

<xsl:template name="optionalTalents">
    <xsl:param name="disciplineName" />
    <xsl:if test="//edc:TALENTS[@discipline=$disciplineName]/edc:OPTIONALTALENT">
        <div class="edOptionalTalents">
            <div class="edSubSubHeader">Optional Talents</div>
            <table>
                <tr>
                    <td class="edHeaderCell">Talent Name</td>
                    <td class="edHeaderCell">Karma?</td>
                    <td class="edHeaderCell">Action</td>
                    <td class="edHeaderCell">Strain</td>
                    <td class="edHeaderCell">Attribute</td>
                    <td class="edHeaderCell">Rank</td>
                    <td class="edHeaderCell">Step</td>
                    <td class="edHeaderCell">Action Dice</td>
                </tr>
                <xsl:apply-templates select="//edc:TALENTS[@discipline=$disciplineName]/edc:OPTIONALTALENT"/>
            </table>
        </div>
    </xsl:if>
</xsl:template>

<xsl:template match="//edc:OPTIONALTALENT">
    <tr>
        <td class="edCell"><xsl:value-of select="@name"/></td>
        <td class="edCell"><xsl:value-of select="@karma"/></td>
        <td class="edCell"><xsl:value-of select="@action"/></td>
        <td class="edCell"><xsl:value-of select="@attribute"/></td>
        <td class="edCell"><xsl:value-of select="@attribute"/></td>
        <td class="edCell"><xsl:value-of select="./edt:RANK/@rank"/></td>
        <td class="edCell"><xsl:value-of select="./edt:RANK/@step"/></td>
        <td class="edCell"><xsl:value-of select="./edt:RANK/@dice"/></td>
    </tr>
</xsl:template>

<xsl:template name="spells">
    <xsl:param name="disciplineName" />
    <xsl:if test="//edc:SPELLS[@discipline=$disciplineName]/edc:SPELL">
        <div class="edSpells">
            <div class="edSubSubHeader">Spells</div>
            <table>
                <tr>
                    <td class="edHeaderCell">Spellname</td>
                    <td class="edHeaderCell">In Matrix?</td>
                    <td class="edHeaderCell">Type</td>
                    <td class="edHeaderCell">Circle</td>
                    <td class="edHeaderCell">Threads</td>
                    <td class="edHeaderCell">Weaving Difficulty</td>
                    <td class="edHeaderCell">Reattuning Difficulty</td>
                    <td class="edHeaderCell">Casting Difficulty</td>
                    <td class="edHeaderCell">Range</td>
                    <td class="edHeaderCell">Duration</td>
                    <td class="edHeaderCell">Effect</td>
                </tr>
                <xsl:apply-templates select="//edc:SPELLS[@discipline=$disciplineName]/edc:SPELL"/>
            </table>
        </div>
    </xsl:if>
</xsl:template>

<xsl:template match="//edc:SPELL">
    <tr>
        <td class="edCell"><xsl:value-of select="@name" /></td>
        <td class="edCell">TODO</td>
        <td class="edCell"><xsl:value-of select="@type" /></td>
        <td class="edCell"><xsl:value-of select="@circle" /></td>
        <td class="edCell"><xsl:value-of select="@threads" /></td>
        <td class="edCell"><xsl:value-of select="@weavingdifficulty" /></td>
        <td class="edCell"><xsl:value-of select="@reattuningdifficulty" /></td>
        <td class="edCell"><xsl:value-of select="@castingdifficulty" /></td>
        <td class="edCell"><xsl:value-of select="@range" /></td>
        <td class="edCell"><xsl:value-of select="@duration" /></td>
        <td class="edCell"><xsl:value-of select="@effect" /></td>
    </tr>
</xsl:template>

<xsl:template name="disciplineBonuses">
    <xsl:if test="./edt:DISCIPLINEBONUS">
        <div class="edDisciplineBonuses">    
            <div class="edSubSubHeader">Discipline Bonuses</div>
            <table>
                <tr>
                    <td class="edHeaderCell">Circle</td>
                    <td class="edHeaderCell">Bonus/Ability</td>
                </tr>
                <xsl:apply-templates select="./edt:DISCIPLINEBONUS"/>
            </table>
        </div>
    </xsl:if>
</xsl:template>

<xsl:template match="//edt:DISCIPLINEBONUS">
    <tr>
        <td class="edCell"><xsl:value-of select="@circle"/></td>
        <td class="edCell"><xsl:value-of select="@bonus"/></td>
    </tr>
</xsl:template>

<xsl:template name="weapons">
    <div class="edWeapons">
        <div class="edSubHeader">Weapons</div>
        <table>
            <xsl:apply-templates select="//edc:WEAPON"/>
        </table>
    </div>
</xsl:template>

<xsl:template match="//edc:WEAPON">
        <tr>
            <td class="edKeyCell">Name:</td>
            <td class="edValueCell"><xsl:value-of select="@name" /></td>
            <td class="edKeyCell">Damage Step:</td>
            <td class="edValueCell"><xsl:value-of select="@damagestep" /></td>
            <td class="edKeyCell">Size:</td>
            <td class="edValueCell"><xsl:value-of select="@size" /></td>
            <xsl:if test="@timesforged">
                <td class="edKeyCell">Times forged:</td>
                <td class="edValueCell"><xsl:value-of select="@timesforged" /></td>
            </xsl:if>
            <xsl:if test="@shortrange">
                <td class="edKeyCell">Short:</td>
                <td class="edValueCell"><xsl:value-of select="@shortrange" /></td>
            </xsl:if>
            <xsl:if test="@mediumrange">
                <td class="edKeyCell">Medium:</td>
                <td class="edValueCell"><xsl:value-of select="@mediumrange" /></td>
            </xsl:if>
            <xsl:if test="@longrange">
                <td class="edKeyCell">Long:</td>
                <td class="edValueCell"><xsl:value-of select="@longrange" /></td>
            </xsl:if>
        </tr>
</xsl:template>

<xsl:template name="skills">
    <div class="edSkills">
        <div class="edSubHeader">Skills</div>
        <table>
            <tr>
                <td class="edHeaderCell">Skillname</td>
                <td class="edHeaderCell">Action</td>
                <td class="edHeaderCell">Strain</td>
                <td class="edHeaderCell">Attibute</td>
                <td class="edHeaderCell">Rank</td>
                <td class="edHeaderCell">Step</td>
                <td class="edHeaderCell">Action Dice</td>
            </tr>
            <xsl:apply-templates select="//edc:SKILL"/>
        </table>
    </div>
</xsl:template>

<xsl:template match="//edc:SKILL">
    <tr>
        <td class="edCell"><xsl:value-of select="@name"/></td>
        <td class="edCell"><xsl:value-of select="@action"/></td>
        <td class="edCell"><xsl:value-of select="@strain"/></td>
        <td class="edCell"><xsl:value-of select="@attribute"/></td>
        <td class="edCell"><xsl:value-of select="./edt:RANK/@rank"/></td>
        <td class="edCell"><xsl:value-of select="./edt:RANK/@step"/></td>
        <td class="edCell"><xsl:value-of select="./edt:RANK/@dice"/></td>   
    </tr>
</xsl:template>

<xsl:template name="raceabilities">
    <div class="edRaceabilities">
        <div class="edSubHeader">Racial Abilities</div>
        <table border="1">
            <xsl:apply-templates select="//edc:RACEABILITES"/>
        </table>
    </div>
</xsl:template>

<xsl:template match="//edc:RACEABILITES">
    <tr>
        <td><xsl:value-of select="."/></td>
    </tr>
</xsl:template>

<xsl:template name="experience">
    <div class="edExperience">
        <div class="edSubHeader">Experience</div>
        <table>
            <tr>
                <td class="edKeyCell">Reputation:</td>
                <td class="edValueCell"><xsl:value-of select="//edc:EXPERIENCE/@reputation" /></td>
            </tr>
            <tr>
                <td class="edKeyCell">Renown:</td>
                <td class="edValueCell"><xsl:value-of select="//edc:EXPERIENCE/@renown" /></td>
            </tr>
            <tr>
                <td class="edKeyCell">Currentledgend Points:</td>
                <td class="edValueCell"><xsl:value-of select="//edc:EXPERIENCE/@currentlegendpoints" /></td>
            </tr>
            <tr>
                <td class="edKeyCell">Totalledgend Points:</td>
                <td class="edValueCell"><xsl:value-of select="//edc:EXPERIENCE/@totallegendpoints" /></td>
            </tr>
        </table>        
    </div>   
</xsl:template>

<xsl:template name="equipment">
    <xsl:if test="//edc:ITEM">
        <div class="edEquipment">                    
            <div class="edSubHeader">Equipment</div>
            <xsl:apply-templates select="//edc:ITEM"/>
        </div>
    </xsl:if>
</xsl:template>

<xsl:template match="//edc:ITEM">
    <hr />
    Name=<xsl:value-of select="@name" />
    Weight=<xsl:value-of select="@weight" />
</xsl:template>

<xsl:template name="coins">
    <xsl:if test="//edc:COINS">
        <div class="edCoins">
            <div class="edSubHeader">Coins</div>
            <xsl:apply-templates select="//edc:COINS"/>
        </div>
    </xsl:if>    
</xsl:template>

<xsl:template match="//edc:COINS">
    <hr />
    TODO    
</xsl:template>

<xsl:template name="patternItems">
    <xsl:if test="//edc:PATTERNITEM">
        <div class="edPatternItems">
            <div class="edSubHeader">Pattern Items</div>
            TODO
        </div>
    </xsl:if>
</xsl:template>

<xsl:template name="magicItems">
    <xsl:if test="//edc:MAGICITEM">
        <div class="edMagicItems">
            <div class="edSubHeader">Magic Items</div>
            TODO
        </div>
    </xsl:if>
</xsl:template>

</xsl:stylesheet>
