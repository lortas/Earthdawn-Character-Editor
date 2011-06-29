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
        <!-- Heading -->
        <div class="edHeader">Earthdawn Character Sheet</div>
        <div class="edLayoutRow">
            <table width="100%">
                <tr>
                    <td class="edMiscellaneous">
                        <!-- Miscellaneous -->
                        <xsl:call-template name="miscellaneous" />
                    </td>
                    <td class="edAttributes">
                        <!-- Attributes -->
                        <xsl:call-template name="attributes" />
                    </td>
                </tr>
            </table>
        </div>
        <!-- Characteristics -->
        <xsl:call-template name="characteristics"/>
        <div class="edLayoutRow">   
        <table width="100%">
            <tr>
                <td class="edHeaderCell">
                    <!-- Experience -->
                    <xsl:call-template name="experience" />
                </td>
                <td class="edHeaderCell">
                    <!-- Calculated Legend Points -->
                    <xsl:call-template name="calculatedLegendPoints" />
                </td>
            </tr>
        </table>
        </div>
        <!-- Discipline -->
        <xsl:call-template name="discipline"/>
        <div class="edLayoutRow">
            <table width="100%">
                <tr>
                    <td class="edSkills" rowspan="7">
                        <!-- Skills -->
                        <xsl:call-template name="skills" />
                    </td>
                </tr>
                <tr>
                    <td class="edCoins">
                        <!-- Coins -->
                        <xsl:call-template name="coins" />
                    </td>
                </tr>
                <tr>
                    <td class="edWeapons">
                        <!-- Weapons -->
                        <xsl:call-template name="weapons" />
                    </td>
                </tr>
                <tr>
                    <td class="edArmor">
                        <!-- Weapons -->
                        <xsl:call-template name="armor" />
                    </td>
                </tr>
                <tr>
                    <td class="edMagicItems">
                        <!-- Magic Items -->
                        <xsl:call-template name="magicItems" />
                    </td>
                </tr>
                <tr>
                    <td class="edPatternItems">
                        <!-- Pattern Items -->
                        <xsl:call-template name="patternItems" />
                    </td>
                </tr>
                <tr>
                    <td class="edBloodItems">
                        <!-- Blood Items -->
                        <xsl:call-template name="bloodItems" />
                    </td>
                </tr>
                <tr>
                    <td class="edThreadItems" colspan="2">
                        <!-- Thread Items -->
                        <xsl:call-template name="threadItems" />
                    </td>
                </tr>
                <tr>
                    <td class="edEquipment" colspan="2">
                        <!-- Equipment -->
                        <xsl:call-template name="equipment" />
                    </td>
                </tr>
            </table>
        </div>
        <xsl:call-template name="portraits"/>
    </body> 
</html> 
</xsl:template>

<xsl:template name="miscellaneous">
    <div class="edSubHeader">General Information</div>
    <table width="100%">
        <!-- Name -->
        <tr>
            <td class="edKeyCell">Name:</td>
            <td colspan="5" class="edValueCell"><xsl:value-of select="/edc:EDCHARACTER/@name" /></td>
        </tr>
        <!-- Appearance -->
        <xsl:apply-templates select="//edc:APPEARANCE"/>
    </table>
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
        <!-- Racial Abilities -->
        <td class="edKeyCell">Racial Abilities:</td>
        <td class="edValueCell" colspan="5">
            <xsl:apply-templates select="//edc:RACEABILITES"/>
        </td>
    </tr>
    <tr>
        <td class="edKeyCell">Eyes:</td>
        <td class="edValueCell"><xsl:value-of select="@eyes" /></td>
    </tr>
</xsl:template>

<xsl:template match="//edc:RACEABILITES">
    <xsl:value-of select="."/>
</xsl:template>

<xsl:template name="attributes">
    <div class="edSubHeader">Attributes</div>
    <table width="100%">
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
        <table width="100%">
            <tr>
                <!-- Defense -->
                <td class="edDefense">
                    <xsl:apply-templates select="//edc:DEFENSE"/>    
                </td>
                <!-- Pretection -->
                <td class="edProtection">
                    <xsl:apply-templates select="//edc:PROTECTION"/>    
                </td>
                <!-- Health -->
                <td class="edHealth">
                    <xsl:call-template name="health"/>
                </td> 
                <td class="edDamage">
                    <xsl:apply-templates select="//edc:HEALTH"/>
                </td>
            </tr>
        </table>
        <table width="100%">
            <tr>
                <!-- Initiative -->
                <td class="edInitiative">
                    <xsl:apply-templates select="//edc:INITIATIVE"/>    
                </td>
                <!-- Movement -->
                <td class="edMovement">
                    <xsl:apply-templates select="//edc:MOVEMENT"/>    
                </td>
                <!-- Karma -->
                <td class="edKarma">
                    <xsl:apply-templates select="//edc:KARMA"/>    
                </td>                
                <!-- Karma -->
                <td class="edCarrying">
                    <xsl:apply-templates select="//edc:CARRYING"/>    
                </td>
            </tr>
        </table>        
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
</xsl:template>

<xsl:template match="//edc:CARRYING">
    <div class="edSubSubHeader">Carrying</div>
    <table>
        <tr>
            <td class="edKeyCell">Carrying:</td>
            <td class="edValueCell"><xsl:value-of select="@carrying" /></td>
        </tr>
        <tr>
            <td class="edKeyCell">Lifting:</td>
            <td class="edValueCell"><xsl:value-of select="@lifting" /></td>
        </tr>
    </table>
</xsl:template>

<xsl:template match="//edt:SHIELD">
    Shield:<br />
    <div>
        Name=<xsl:value-of select="@name" /><br/>
        Deflection Bonus=<xsl:value-of select="@deflectionbonus" /><br/>
    </div>
</xsl:template>

<xsl:template name="health">
    <div class="edSubSubHeader">Health</div>
    <table>
        <tr>
            <td class="edKeyCell">Unconsciousness</td>
            <td class="edValueCell" colspan="2"><xsl:value-of select="//edt:UNCONSCIOUSNESS/@base" /> + <xsl:value-of select="//edt:UNCONSCIOUSNESS/@adjustment" /> =</td>
            <td class="edValueCell"><xsl:value-of select="//edt:UNCONSCIOUSNESS/@value" /></td>
        </tr>
        <tr>
            <td class="edKeyCell">Death</td>
            <td class="edValueCell" colspan="2"><xsl:value-of select="//edt:DEATH/@base" /> + <xsl:value-of select="//edt:DEATH/@adjustment" /> =</td>
            <td class="edValueCell"><xsl:value-of select="//edt:DEATH/@value" /></td>
        </tr>
        <tr>
            <td class="edKeyCell">Recovery Step</td>
            <td class="edValueCell"><xsl:value-of select="//edt:RECOVERY/@step" /></td>
            <td class="edKeyCell">Recovery Dice</td>
            <td class="edValueCell"><xsl:value-of select="//edt:RECOVERY/@dice" /></td>
        </tr>
        <tr>
            <td class="edKeyCell">Recovery Tests per Day</td>
            <td class="edValueCell"><xsl:value-of select="//edt:RECOVERY/@testsperday" /></td>
            <td class="edKeyCell">Wound Threshold</td>
            <td class="edValueCell"><xsl:value-of select="//edt:WOUNDS/@threshold" /></td>
        </tr>
    </table>
</xsl:template>
<xsl:template match="//edc:HEALTH">
    <div class="edSubSubHeader">Damage</div>
    <table>
        <tr>
            <td class="edKeyCell">Current Damage</td>
            <td class="edValueCell"><xsl:value-of select="@damage" /></td>
        </tr>
        <tr>
            <td class="edKeyCell">Current Wounds</td>
            <td class="edValueCell"><xsl:value-of select="./edt:WOUNDS/@nomal" /></td>
        </tr>
        <tr>
            <td class="edKeyCell">Bloodwounds</td>
            <td class="edValueCell"><xsl:value-of select="./edt:WOUNDS/@blood" /></td>
        </tr>
        <tr>
            <td class="edKeyCell">Wound Penalties</td>
            <td class="edValueCell"><xsl:value-of select="./edt:WOUNDS/@penalties" /></td>
        </tr>
    </table>
</xsl:template>

<xsl:template match="//edt:RECOVERY">
    <div>
        Recovery:<br />
        Test per day=<xsl:value-of select="@testsperday" /><br /> 
        Dice=<xsl:value-of select="@dice" /><br />
        Step=<xsl:value-of select="@step" />
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
        <!-- Discipline -->
        <div class="edSubHeader">Discipline <xsl:value-of select="@name" /> (Circle <xsl:value-of select="@circle"/>)</div>
        <table width="100%">
            <tr>
                <td class="edDisciplineTalents">
                    <!-- Discipline Talents -->
                    <xsl:call-template name="disziplinTalents">
                        <xsl:with-param name="disciplineName" select="@name" />
                    </xsl:call-template>
                </td>
                <td class="edOptionalTalents">
                    <!-- Other Talents -->
                    <xsl:call-template name="optionalTalents">
                        <xsl:with-param name="disciplineName" select="@name" />
                    </xsl:call-template>
                </td>
            </tr>
            <tr>    
                <td class="edDisciplineBonuses" colspan="2">
                    <!-- Discipline Bonuses -->
                    <xsl:call-template name="disciplineBonuses"/>
                </td>
            </tr>
        </table>
        <!-- Spells -->
        <xsl:call-template name="spells">
            <xsl:with-param name="disciplineName" select="@name" />
        </xsl:call-template>
    </div>
</xsl:template>
  
<xsl:template name="disziplinTalents">
    <xsl:param name="disciplineName" />
    <div class="edSubSubHeader">Discipline Talents</div>
    <table width="100%">
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
</xsl:template>

<xsl:template match="//edc:DISZIPLINETALENT">
    <tr>
        <td class="edCell">
        	<xsl:value-of select="@name"/>
        	<xsl:if test="@limitation!=''">: <xsl:value-of select="@limitation"/></xsl:if>
        </td>
        <td class="edCell"><xsl:value-of select="@action"/></td>
        <td class="edCell"><xsl:value-of select="@strain"/></td>
        <td class="edCell"><xsl:value-of select="@attribute"/></td>
        <td class="edCell">
        	<xsl:value-of select="./edt:RANK/@rank"/>
        	<xsl:if test="./edt:RANK/@bonus>0">+</xsl:if>
        	<xsl:if test="./edt:RANK/@bonus!=0"><xsl:value-of select="./edt:RANK/@bonus"/></xsl:if>
        </td>
        <td class="edCell"><xsl:value-of select="./edt:RANK/@step"/></td>
        <td class="edCell"><xsl:value-of select="./edt:RANK/@dice"/></td>   
    </tr>
</xsl:template>

<xsl:template name="optionalTalents">
    <xsl:param name="disciplineName" />
    <div class="edSubSubHeader">Optional Talents</div>
    <xsl:if test="//edc:TALENTS[@discipline=$disciplineName]/edc:OPTIONALTALENT">
        <table width="100%">
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
    </xsl:if>
</xsl:template>

<xsl:template match="//edc:OPTIONALTALENT">
    <tr>
        <td class="edCell">
        	<xsl:value-of select="@name"/>
        	<xsl:if test="@limitation!=''">: <xsl:value-of select="@limitation"/></xsl:if>
        </td>
        <td class="edCell"><xsl:value-of select="@karma"/></td>
        <td class="edCell"><xsl:value-of select="@action"/></td>
        <td class="edCell"><xsl:value-of select="@attribute"/></td>
        <td class="edCell"><xsl:value-of select="@attribute"/></td>
        <td class="edCell">
        	<xsl:value-of select="./edt:RANK/@rank"/>
        	<xsl:if test="./edt:RANK/@bonus>0">+</xsl:if>
        	<xsl:if test="./edt:RANK/@bonus!=0"><xsl:value-of select="./edt:RANK/@bonus"/></xsl:if>
        </td>
        <td class="edCell"><xsl:value-of select="./edt:RANK/@step"/></td>
        <td class="edCell"><xsl:value-of select="./edt:RANK/@dice"/></td>
    </tr>
</xsl:template>

<xsl:template name="disciplineBonuses">
    <div class="edSubSubHeader">Discipline Bonuses</div>
    <xsl:if test="./edt:DISCIPLINEBONUS">
        <xsl:apply-templates select="./edt:DISCIPLINEBONUS"/>
    </xsl:if>
</xsl:template>

<xsl:template match="//edt:DISCIPLINEBONUS">
        <xsl:value-of select="@bonus"/> (<xsl:value-of select="@circle"/>)
        <xsl:if test="./following::edt:DISCIPLINEBONUS">; </xsl:if>
</xsl:template>

<xsl:template name="spells">
    <xsl:param name="disciplineName" />
    <xsl:if test="//edc:SPELLS[@discipline=$disciplineName]/edc:SPELL">
        <div class="edSpells">
            <div class="edSubSubHeader">Spells</div>
            <table width="100%">
                <tr>
                    <td class="edHeaderCell">Spellname</td>
                    <td class="edHeaderCell">In Matrix?</td>
                    <td class="edHeaderCell">Type</td>
                    <td class="edHeaderCell">Circle</td>
                    <td class="edHeaderCell">Threads</td>
                    <td class="edHeaderCell">WD<sup>1)</sup></td>
                    <td class="edHeaderCell">RD<sup>2)</sup></td>
                    <td class="edHeaderCell">CD<sup>3)</sup></td>
                    <td class="edHeaderCell">Range</td>
                    <td class="edHeaderCell">Duration</td>
                    <td class="edHeaderCell">Effect</td>
                </tr>
                <xsl:apply-templates select="//edc:SPELLS[@discipline=$disciplineName]/edc:SPELL"/>
            </table>
            <div><span class="sup">1)</span> Weaving Difficulty; <span class="sup">2)</span> Reattuning Difficulty;<span class="sup">3)</span> Casting Difficulty;</div>
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

<xsl:template name="weapons">
    <div class="edSubHeader">Weapons</div>
    <xsl:apply-templates select="//edc:WEAPON"/>
</xsl:template>

<xsl:template match="//edc:WEAPON">
    <table class="edSubSection">
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
    </table>
</xsl:template>

<xsl:template name="bloodItems">
    <div class="edSubHeader">Blood Items</div>
    <xsl:apply-templates select="//edc:BLOODCHARMITEM"/>
</xsl:template>

<xsl:template match="//edc:BLOODCHARMITEM">
    <div class="edSubSection">
        <table>
            <tr>
                <td class="edKeyCell">Name:</td>
                <td class="edValueCell"><xsl:value-of select="@name" /></td>
                <td class="edKeyCell">Weight:</td>
                <td class="edValueCell"><xsl:value-of select="@weight" /></td>
                <td class="edKeyCell">Location:</td>
                <td class="edValueCell"><xsl:value-of select="@location" /></td>
            </tr>
        </table>
    </div>
</xsl:template>

<xsl:template name="armor">
    <div class="edSubHeader">Armor</div>
    <xsl:apply-templates select="//edc:PROTECTION/edt:ARMOR[position()  > 1]"/>
</xsl:template>

<xsl:template match="//edc:PROTECTION/edt:ARMOR">
    <div class="edSubSection">
        <table>
            <tr>
                <td class="edKeyCell">Name:</td>
                <td class="edValueCell"><xsl:value-of select="@name" /></td>
                <td class="edKeyCell">Weight:</td>
                <td class="edValueCell"><xsl:value-of select="@weight" /></td>
                <td class="edKeyCell">Location:</td>
                <td class="edValueCell"><xsl:value-of select="@location" /></td>
            </tr>
        </table>
        <table>
            <tr>
                <xsl:if test="@mysticarmor">
                    <td class="edKeyCell">Mystic Armor:</td>
                    <td class="edValueCell"><xsl:value-of select="@mysticarmor" /></td>
                </xsl:if>
                <xsl:if test="@physicalarmor">
                    <td class="edKeyCell">Physical Armor:</td>
                    <td class="edValueCell"><xsl:value-of select="@physicalarmor" /></td>
                </xsl:if>
                <xsl:if test="@penalty">
                    <td class="edKeyCell">Penalty:</td>
                    <td class="edValueCell"><xsl:value-of select="@penalty" /></td>
                </xsl:if>
                <xsl:if test="@used">
                    <td class="edKeyCell">Used:</td>
                    <td class="edValueCell"><xsl:value-of select="@used" /></td>
                </xsl:if>
                <xsl:if test="@edn">
                    <td class="edKeyCell">EDN:</td>
                    <td class="edValueCell"><xsl:value-of select="@edn" /></td>
                </xsl:if>
            </tr>
        </table>
    </div>
</xsl:template>

<xsl:template name="skills">
    <div class="edSubHeader">Skills</div>
    <table width="100%">
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
</xsl:template>

<xsl:template match="//edc:SKILL">
    <tr>
        <td class="edCell"><xsl:value-of select="@name"/></td>
        <td class="edCell"><xsl:value-of select="@action"/></td>
        <td class="edCell"><xsl:value-of select="@strain"/></td>
        <td class="edCell"><xsl:value-of select="@attribute"/></td>
        <td class="edCell">
        	<xsl:value-of select="./edt:RANK/@rank"/>
        	<xsl:if test="./edt:RANK/@bonus>0">+</xsl:if>
        	<xsl:if test="./edt:RANK/@bonus!=0"><xsl:value-of select="./edt:RANK/@bonus"/></xsl:if>
        </td>
        <td class="edCell"><xsl:value-of select="./edt:RANK/@step"/></td>
        <td class="edCell"><xsl:value-of select="./edt:RANK/@dice"/></td>   
    </tr>
</xsl:template>

<xsl:template name="coins">
    <xsl:if test="//edc:COINS">
        <div class="edSubHeader">Coins</div>
            <table>
                <tr>
                    <td class="edKeyCell">Gold:</td>
                    <td class="edValueCell"><xsl:value-of select="sum(//edc:COINS/@gold)"/></td>
                    <td class="edKeyCell">Silver:</td>
                    <td class="edValueCell"><xsl:value-of select="sum(//edc:COINS/@silver)"/></td>
                    <td class="edKeyCell">Copper:</td>
                    <td class="edValueCell"><xsl:value-of select="sum(//edc:COINS/@copper)"/></td>
                </tr>
                <tr>
                    <td class="edKeyCell">Earth:</td>
                    <td class="edValueCell"><xsl:value-of select="sum(//edc:COINS/@earth)"/></td>
                    <td class="edKeyCell">Water:</td>
                    <td class="edValueCell"><xsl:value-of select="sum(//edc:COINS/@water)"/></td>
                    <td class="edKeyCell">Fire:</td>
                    <td class="edValueCell"><xsl:value-of select="sum(//edc:COINS/@fire)"/></td>
                    <td class="edKeyCell">Air:</td>
                    <td class="edValueCell"><xsl:value-of select="sum(//edc:COINS/@air)"/></td>
                    <td class="edKeyCell">Orichalcum:</td>
                    <td class="edValueCell"><xsl:value-of select="sum(//edc:COINS/@orichalcum)"/></td>
                </tr>
            </table>
    </xsl:if>    
</xsl:template>

<xsl:template name="patternItems">
    <div class="edSubHeader">Pattern Items</div>
    <xsl:if test="//edc:PATTERNITEM">
        <div>
            TODO
        </div>
    </xsl:if>
</xsl:template>

<xsl:template name="magicItems">
    <div class="edSubHeader">Magic Items</div>
    <xsl:if test="//edc:MAGICITEM">
        <div>
            TODO
        </div>
    </xsl:if>
</xsl:template>

<xsl:template name="equipment">
    <div class="edSubHeader">Equipment</div>
    <xsl:if test="//edc:ITEM">
        <table width="100%">
            <tr>
                <td class="edHeaderCell" width="40%">Name</td>
                <td class="edHeaderCell" width="5%">Weight</td>
                <td class="edHeaderCell" width="5%">Location</td>
                <td class="edHeaderCell" width="40%">Name</td>
                <td class="edHeaderCell" width="5%">Weight</td>
                <td class="edHeaderCell" width="5%">Location</td>
            </tr>
            <xsl:apply-templates select="//edc:ITEM[(position() mod 2) = 1]" /> 
        </table>
    </xsl:if>
</xsl:template>

<xsl:template match="//edc:ITEM">
    <tr>
        <xsl:for-each select=". | following-sibling::edc:ITEM[position() &lt; 2]">
            <td class="edCell"><xsl:value-of select="@name" /></td>
            <td class="edCell"><xsl:value-of select="@weight" /></td>
            <td class="edCell"><xsl:value-of select="@location" /></td>
        </xsl:for-each>
     </tr>
</xsl:template>

<xsl:template name="threadItems">
    <div class="edSubHeader">Thread Items</div>
    <xsl:if test="//edc:THREADITEM">
        <table width="100%">
            <xsl:apply-templates select="//edc:THREADITEM[(position() mod 2) = 1]" /> 
        </table>
    </xsl:if>
</xsl:template>

<xsl:template match="//edc:THREADITEM">
    <tr>
        <td>
            <table width="100%">
                <tr>
                    <xsl:for-each select=". | following-sibling::edc:THREADITEM[position() &lt; 2]">
                        <td class="edThreadItemTop">
                            <table width="100%">
                                <tr>
                                    <td class="edKeyCell">Name:</td>
                                    <td class="edValueCell" colspan="3"><xsl:value-of select="@name" /></td>
                                </tr>
                                <tr>
                                    <td class="edKeyCell">Max Threads:</td>
                                    <td class="edValueCell"><xsl:value-of select="@maxthreads" /></td>
                                    <td class="edKeyCell">Spell Defence:</td>
                                    <td class="edValueCell"><xsl:value-of select="@spelldefense" /></td>
                                </tr>
                                <tr>
                                    <td class="edKeyCell">Description:</td>
                                    <td class="edValueCell" colspan="3"><xsl:value-of select="./edt:description" /></td>
                                </tr>
                            </table>
                        </td>
                    </xsl:for-each>
                </tr>
                <tr>
                    <xsl:for-each select=". | following-sibling::edc:THREADITEM[position() &lt; 2]">
                        <td class="edThreadItemBottom">
                            <table width="100%">
                                <tr>
                                    <td style="text-align: center">
                                        Thread Ranks
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <table width="100%">
                                            <tr>
                                                <td class="edHeaderCell">Rank</td>
                                                <td class="edHeaderCell">Knowledge/Deed</td>
                                                <td class="edHeaderCell">LPCost</td>
                                                <td class="edHeaderCell">Effect</td>
                                            </tr>
                                            <xsl:apply-templates select="./edt:THREADRANK"/>
                                        </table>
                                    </td>
                               </tr>
                            </table>
                        </td>
                    </xsl:for-each>
                </tr>
            </table>
        </td>
    </tr>
</xsl:template>

<xsl:template match="//edt:THREADRANK">
    <tr>
        <td class="edCell"><xsl:value-of select="position()" /></td>
        <td class="edCell"><xsl:value-of select="@keyknowledge" /></td>
        <td class="edCell"><xsl:value-of select="@lpcost" /></td>
        <td class="edCell"><xsl:value-of select="@effect" /></td>
    </tr>
</xsl:template>

<xsl:template name="experience">
    <div class="edExperience">
        <div class="edSubHeader">Legend Points</div>
            <table width="100%">
            <tr>
                <td class="edHeaderCell" width="25%">Reputation</td>
                <td class="edHeaderCell" width="25%">Renown</td>
                <td class="edHeaderCell" width="25%">Current</td>
                <td class="edHeaderCell" width="25%">Total</td>
            </tr>
            <tr>
                <td class="edCell"><xsl:value-of select="//edc:EXPERIENCE/@reputation" /></td>
                <td class="edCell"><xsl:value-of select="//edc:EXPERIENCE/@renown" /></td>
                <td class="edCell"><xsl:value-of select="//edc:EXPERIENCE/@currentlegendpoints" /></td>
                <td class="edCell"><xsl:value-of select="//edc:EXPERIENCE/@totallegendpoints" /></td>
            </tr>
        </table>
    </div>
</xsl:template>

<xsl:template name="calculatedLegendPoints">
    <div class="edCalculatedLegendPoints">
        <div class="edSubHeader">Calculated Legend Points</div>
        <table width="100%">
            <tr>
                <td class="edHeaderCell">Attributes</td>
                <td class="edHeaderCell">Discipline Talents</td>
                <td class="edHeaderCell">Optional Talents</td>
                <td class="edHeaderCell">Knacks</td>
                <td class="edHeaderCell">Spells</td>
                <td class="edHeaderCell">Skills</td>
                <td class="edHeaderCell">Karma</td>
                <td class="edHeaderCell">Magic Items</td>
                <td class="edHeaderCell">Total</td>
            </tr>
            <tr>
                <td class="edCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@attributes" /></td>
                <td class="edCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@disciplinetalents" /></td>
                <td class="edCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@optionaltalents" /></td>
                <td class="edCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@knacks" /></td>
                <td class="edCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@spells" /></td>
                <td class="edCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@skills" /></td>
                <td class="edCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@karma" /></td>
                <td class="edCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@magicitems" /></td>
                <td class="edCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@total" /></td>
            </tr>
        </table>
    </div>
</xsl:template>

<xsl:template name="portraits">
    <div class="edPortraits">
        <div class="edSubHeader">Portraits</div>
        <div class="edPortraitImages">
            <img>
                <xsl:attribute name="src">
                    data:<xsl:value-of select="//edc:PORTRAIT/@contenttype"/>;base64,<xsl:value-of select="//edc:PORTRAIT"/>
                </xsl:attribute>
            </img>
        </div>
    </div>
</xsl:template>

</xsl:stylesheet>
