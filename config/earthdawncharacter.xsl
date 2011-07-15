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
					<td class="edSkills">
						<!-- Skills -->
						<xsl:call-template name="skills" />
					</td>
					<td>
						<div class="edLanguages"><xsl:call-template name="languages"/></div>
						<div class="edCoins"><xsl:call-template name="coins"/></div>
						<div class="edWeapons"><xsl:call-template name="weapons"/></div>
						<div class="edArmor"><xsl:call-template name="armor"/></div>
						<div class="edMagicItems"><xsl:call-template name="magicItems"/></div>
						<div class="edPatternItems"><xsl:call-template name="patternItems"/></div>
						<div class="edBloodItems"><xsl:call-template name="bloodItems"/></div>
					</td>
				</tr>
			</table>
			<div class="edThreadItems" width="100%"><xsl:call-template name="threadItems"/></div>
			<div class="edEquipment" width="100%"><xsl:call-template name="equipment"/></div>
			<table width="100%">
				<tr>
					<td>
						<xsl:call-template name="portraits"/>
					</td>
					<td>
						<xsl:call-template name="description"/>
					</td>
					<td>
						<xsl:call-template name="comment"/>
					</td>
				</tr>
			</table>
		</div>
		<!-- DisciplineSpells -->
		<xsl:call-template name="disciplinespells"/>
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
		<td class="edMidCell"><xsl:value-of select="@racevalue"/></td>
		<td class="edMidCell"><xsl:value-of select="@lpincrease"/></td>
		<td class="edMidCell"><xsl:value-of select="@currentvalue"/></td>
		<td class="edMidCell"><xsl:value-of select="@step"/></td>
		<td class="edMidCell"><xsl:value-of select="@dice"/></td>   
	</tr>
</xsl:template>

<xsl:template name="characteristics">
	<div class="edCharacteristics">
		<div class="edSubHeader">Characteristics</div>
		<table width="100%">
			<tr>
				<!-- Defense -->
				<td class="edDefense">
					<div class="edSubSubHeader">Defense/Armor Ratings</div>
					<table>
						<xsl:apply-templates select="//edc:DEFENSE"/>	
						<xsl:apply-templates select="//edc:PROTECTION"/>
					</table>
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
</xsl:template>

<xsl:template match="//edc:PROTECTION">
	<tr>
		<td class="edKeyCell">Mystic Armor:</td>
		<td class="edValueCell"><xsl:value-of select="@mysticarmor" /></td>
	</tr>
	<tr>
		<td class="edKeyCell">Physical Armor:</td>
		<td class="edValueCell"><xsl:value-of select="@physicalarmor" /></td>
	</tr>
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
				<td class="edDisciplineBonuses" colspan="2">
					<!-- Discipline Bonuses -->
					<xsl:call-template name="disciplineBonuses"/>
				</td>
			</tr>
			<tr>
				<td class="edDisciplineTalents">
					<!-- Discipline Talents -->
					<div class="edSubSubHeader">Discipline Talents</div>
					<table width="100%">
						<tr>
							<td class="edHeaderCell" style="text-align: left;">Talentname</td>
							<td class="edHeaderCell">Action</td>
							<td class="edHeaderCell">S</td>
							<td class="edHeaderCell">Attr.</td>
							<td class="edHeaderCell">Rank</td>
							<td class="edHeaderCell">Step</td>
							<td class="edHeaderCell">Dice</td>
							<td class="edHeaderCell">Book</td>
						</tr>
						<xsl:apply-templates select="./edt:DISZIPLINETALENT"/>
					</table>
				</td>
				<td class="edOptionalTalents">
					<!-- Other Talents -->
					<div class="edSubSubHeader">Optional Talents</div>
					<table width="100%">
						<tr>
							<td class="edHeaderCell" style="text-align: left;">Talent Name</td>
							<td class="edHeaderCell">K?</td>
							<td class="edHeaderCell">Action</td>
							<td class="edHeaderCell">S</td>
							<td class="edHeaderCell">Attr.</td>
							<td class="edHeaderCell">Rank</td>
							<td class="edHeaderCell">Step</td>
							<td class="edHeaderCell">Dice</td>
							<td class="edHeaderCell">Book</td>
						</tr>
						<xsl:apply-templates select="./edt:OPTIONALTALENT"/>
					</table>
				</td>
			</tr>
		</table>
	</div>
</xsl:template>

<xsl:template match="//edt:DISZIPLINETALENT">
	<tr>
		<td class="edCapabCell" style="text-align: left;">
			<xsl:value-of select="@name"/>
			<xsl:if test="@limitation!=''">: <xsl:value-of select="@limitation"/></xsl:if>
			<xsl:if test="./edt:TEACHER/@byversatility!='yes'"> (v)</xsl:if>
		</td>
		<td class="edCapabCell"><xsl:value-of select="@action"/></td>
		<td class="edCapabCell"><xsl:value-of select="@strain"/></td>
		<td class="edCapabCell"><xsl:value-of select="@attribute"/></td>
		<td class="edCapabCell">
			<xsl:value-of select="./edt:RANK/@rank"/>
			<xsl:if test="./edt:RANK/@bonus>0">+</xsl:if>
			<xsl:if test="./edt:RANK/@bonus!=0"><xsl:value-of select="./edt:RANK/@bonus"/></xsl:if>
		</td>
		<td class="edCapabCell"><xsl:value-of select="./edt:RANK/@step"/></td>
		<td class="edCapabCell"><xsl:value-of select="./edt:RANK/@dice"/></td>
		<td class="edCapabCell"><xsl:value-of select="@bookref"/></td>
	</tr>
</xsl:template>

<xsl:template match="//edt:OPTIONALTALENT">
	<tr>
		<td class="edCapabCell" style="text-align: left;">
			<xsl:value-of select="@name"/>
			<xsl:if test="@limitation!=''">: <xsl:value-of select="@limitation"/></xsl:if>
		</td>
		<td class="edCapabCell"><xsl:value-of select="@karma"/></td>
		<td class="edCapabCell"><xsl:value-of select="@action"/></td>
		<td class="edCapabCell"><xsl:value-of select="@strain"/></td>
		<td class="edCapabCell"><xsl:value-of select="@attribute"/></td>
		<td class="edCapabCell">
			<xsl:value-of select="./edt:RANK/@rank"/>
			<xsl:if test="./edt:RANK/@bonus>0">+</xsl:if>
			<xsl:if test="./edt:RANK/@bonus!=0"><xsl:value-of select="./edt:RANK/@bonus"/></xsl:if>
		</td>
		<td class="edCapabCell"><xsl:value-of select="./edt:RANK/@step"/></td>
		<td class="edCapabCell"><xsl:value-of select="./edt:RANK/@dice"/></td>
		<td class="edCapabCell"><xsl:value-of select="@bookref"/></td>
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

<xsl:template name="disciplinespells">
	<xsl:for-each select="//edc:DISCIPLINE">
		<xsl:if test="./edt:SPELL">
			<div class="edDiscipline">
				<div class="edSubHeader">Spells <xsl:value-of select="@name" /></div>
				<div class="edSpells">
					<div class="edSubSubHeader">Spells</div>
					<table width="100%">
						<tr>
							<td class="edHeaderCell">Spellname</td>
							<td class="edHeaderCell">M<sup>1)</sup></td>
							<td class="edHeaderCell">Type</td>
							<td class="edHeaderCell">Circle</td>
							<td class="edHeaderCell">T<sup>2)</sup></td>
							<td class="edHeaderCell">WD<sup>3)</sup></td>
							<td class="edHeaderCell">RD<sup>4)</sup></td>
							<td class="edHeaderCell">CD<sup>5)</sup></td>
							<td class="edHeaderCell">Range</td>
							<td class="edHeaderCell">Duration</td>
							<td class="edHeaderCell">Effect</td>
							<td class="edHeaderCell">Book</td>
						</tr>
						<xsl:apply-templates select="./edt:SPELL"/>
					</table>
					<div>
						<span class="sup">1)</span>In Matrix?;
						<span class="sup">2)</span>Threads;
						<span class="sup">3)</span>Weaving Difficulty;
						<span class="sup">4)</span>Reattuning Difficulty;
						<span class="sup">5)</span>Casting Difficulty;
					</div>
				</div>
			</div>
		</xsl:if>
	</xsl:for-each>
</xsl:template>

<xsl:template match="//edt:SPELL">
	<tr>
		<td class="edCapabCell" style="text-align: left;"><xsl:value-of select="@name" /></td>
		<td class="edCapabCell"><xsl:if test="@inmatrix='yes'">X</xsl:if></td>
		<td class="edCapabCell"><xsl:value-of select="@type" /></td>
		<td class="edCapabCell"><xsl:value-of select="@circle" /></td>
		<td class="edCapabCell"><xsl:value-of select="@threads" /></td>
		<td class="edCapabCell"><xsl:value-of select="@weavingdifficulty" /></td>
		<td class="edCapabCell"><xsl:value-of select="@reattuningdifficulty" /></td>
		<td class="edCapabCell"><xsl:value-of select="@castingdifficulty" /></td>
		<td class="edCapabCell"><xsl:value-of select="@range" /></td>
		<td class="edCapabCell"><xsl:value-of select="@duration" /></td>
		<td class="edCapabCell" style="text-align: left;"><xsl:value-of select="@effect" /></td>
		<td class="edCapabCell"><xsl:value-of select="@bookref"/></td>
	</tr>
</xsl:template>

<xsl:template name="weapons">
	<div class="edSubHeader">Weapons</div>
	<table width="100%">
		<tr>
			<td class="edHeaderCell">Kind</td>
			<td class="edHeaderCell" style="text-align: left;">Name</td>
			<td class="edHeaderCell">Damage</td>
			<td class="edHeaderCell">Str</td>
			<td class="edHeaderCell">Sz</td>
			<td class="edHeaderCell">Short</td>
			<td class="edHeaderCell">Long</td>
			<td class="edHeaderCell">Forged</td>
			<td class="edHeaderCell">Date</td>
			<td class="edHeaderCell">Location</td>
			<td class="edHeaderCell">Weight</td>
			<td class="edHeaderCell">Used?</td>
		</tr>
		<xsl:apply-templates select="//edc:WEAPON"/>
	</table>
</xsl:template>

<xsl:template match="//edc:WEAPON">
	<tr>
		<td class="edCapabCell"><xsl:value-of select="@kind"/></td>
		<td class="edCapabCell" style="text-align: left;"><xsl:value-of select="@name"/></td>
		<td class="edCapabCell"><xsl:value-of select="@damagestep"/></td>
		<td class="edCapabCell"><xsl:value-of select="@strengthmin"/></td>
		<td class="edCapabCell"><xsl:value-of select="@size"/></td>
		<td class="edCapabCell"><xsl:value-of select="@shortrange"/></td>
		<td class="edCapabCell"><xsl:value-of select="@longrange"/></td>
		<td class="edCapabCell"><xsl:value-of select="@timesforged"/></td>
		<td class="edCapabCell"><xsl:value-of select="@dateforged"/></td>
		<td class="edCapabCell"><xsl:value-of select="@location"/></td>
		<td class="edCapabCell"><xsl:value-of select="@weight"/></td>
		<td class="edCapabCell"><xsl:value-of select="@used"/></td>
	</tr>
</xsl:template>

<xsl:template name="magicItems">
	<div class="edSubHeader">Magic Items</div>
	<table>
		<xsl:apply-templates select="//edc:MAGICITEM"/>
	</table>
</xsl:template>

<xsl:template match="//edc:MAGICITEM">
	<tr>
		<td class="edCapabCell"><xsl:value-of select="@kind"/></td>
		<td class="edCapabCell" style="text-align: left;"><xsl:value-of select="@name"/></td>
		<td class="edCapabCell"><xsl:value-of select="@location"/></td>
		<td class="edCapabCell"><xsl:value-of select="@enchantingdifficultynumber"/></td>
		<td class="edCapabCell"><xsl:value-of select="@effect"/></td>
		<td class="edCapabCell"><xsl:value-of select="@weight"/></td>
		<td class="edCapabCell"><xsl:value-of select="@used"/></td>
	</tr>
</xsl:template>

<xsl:template name="patternItems">
	<div class="edSubHeader">Pattern Items</div>
	<table width="100%">
		<xsl:apply-templates select="//edc:PATTERNITEM"/>
	</table>
</xsl:template>

<xsl:template match="//edc:PATTERNITEM">
	<tr>
		<td class="edCapabCell"><xsl:value-of select="@kind"/></td>
		<td class="edCapabCell" style="text-align: left;"><xsl:value-of select="@name"/></td>
		<td class="edCapabCell"><xsl:value-of select="@type"/></td>
		<td class="edCapabCell"><xsl:value-of select="@location"/></td>
		<td class="edCapabCell"><xsl:value-of select="@enchantingdifficultynumber"/></td>
		<td class="edCapabCell"><xsl:value-of select="@effect"/></td>
		<td class="edCapabCell"><xsl:value-of select="@weight"/></td>
		<td class="edCapabCell"><xsl:value-of select="@used"/></td>
	</tr>
</xsl:template>

<xsl:template name="bloodItems">
	<div class="edSubHeader">Blood Items</div>
	<table width="100%">
		<tr>
			<td class="edHeaderCell">Kind</td>
			<td class="edHeaderCell" style="text-align: left;">Name</td>
			<td class="edHeaderCell">Type</td>
			<td class="edHeaderCell">EDN</td>
			<td class="edHeaderCell">BD</td>
			<td class="edHeaderCell">Effect</td>
			<td class="edHeaderCell">DR</td>
			<td class="edHeaderCell">Location</td>
			<td class="edHeaderCell">Weight</td>
			<td class="edHeaderCell">Used?</td>
		</tr>
		<xsl:apply-templates select="//edc:BLOODCHARMITEM"/>
	</table>
</xsl:template>

<xsl:template match="//edc:BLOODCHARMITEM">
	<tr>
		<td class="edCapabCell"><xsl:value-of select="@kind"/></td>
		<td class="edCapabCell" style="text-align: left;"><xsl:value-of select="@name"/></td>
		<td class="edCapabCell"><xsl:value-of select="@type"/></td>
		<td class="edCapabCell"><xsl:value-of select="@enchantingdifficultynumber"/></td>
		<td class="edCapabCell"><xsl:value-of select="@blooddamage"/></td>
		<td class="edCapabCell"><xsl:value-of select="@effect"/></td>
		<td class="edCapabCell"><xsl:value-of select="@depatterningrate"/></td>
		<td class="edCapabCell"><xsl:value-of select="@location"/></td>
		<td class="edCapabCell"><xsl:value-of select="@weight"/></td>
		<td class="edCapabCell"><xsl:value-of select="@used"/></td>
	</tr>
</xsl:template>

<xsl:template name="armor">
	<div class="edSubHeader">Armor</div>
	<table width="100%">
		<tr>
			<td class="edHeaderCell" style="text-align: left;">Name</td>
			<td class="edHeaderCell">protec</td>
			<td class="edHeaderCell">edn</td>
			<td class="edHeaderCell">forged</td>
			<td class="edHeaderCell">date</td>
			<td class="edHeaderCell">location</td>
			<td class="edHeaderCell">weight</td>
			<td class="edHeaderCell">used?</td>
		</tr>
		<xsl:apply-templates select="//edc:PROTECTION/edt:ARMOR[position()  > 1]"/>
	</table>
	<div class="edSubHeader">Shield</div>
	<table width="100%">
		<tr>
			<td class="edHeaderCell" style="text-align: left;">Name</td>
			<td class="edHeaderCell">protec</td>
			<td class="edHeaderCell">edn</td>
			<td class="edHeaderCell">shatter</td>
			<td class="edHeaderCell">pdb</td>
			<td class="edHeaderCell">mdb</td>
			<td class="edHeaderCell">forged</td>
			<td class="edHeaderCell">date</td>
			<td class="edHeaderCell">location</td>
			<td class="edHeaderCell">weight</td>
			<td class="edHeaderCell">used?</td>
		</tr>
		<xsl:apply-templates select="//edc:PROTECTION/edt:SHIELD"/>
	</table>
</xsl:template>

<xsl:template match="//edc:PROTECTION/edt:ARMOR">
	<tr>
		<td class="edCapabCell" style="text-align: left;"><xsl:value-of select="@name"/></td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@physicalarmor>0"><xsl:value-of select="@physicalarmor"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>/
			<xsl:choose>
				<xsl:when test="@mysticarmor>0"><xsl:value-of select="@mysticarmor"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>/
			<xsl:choose>
				<xsl:when test="@penalty>0"><xsl:value-of select="@penalty"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell"><xsl:value-of select="@edn"/></td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@timesforged_physical>0"><xsl:value-of select="@timesforged_physical"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>/
			<xsl:choose>
				<xsl:when test="@timesforged_mystic>0"><xsl:value-of select="@timesforged_mystic"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell"><xsl:value-of select="@dateforged"/></td>
		<td class="edCapabCell"><xsl:value-of select="@location"/></td>
		<td class="edCapabCell"><xsl:value-of select="@weight"/></td>
		<td class="edCapabCell"><xsl:value-of select="@used"/></td>
	</tr>
</xsl:template>

<xsl:template match="//edc:PROTECTION/edt:SHIELD">
	<tr>
		<td class="edCapabCell" style="text-align: left;"><xsl:value-of select="@name"/></td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@physicalarmor>0"><xsl:value-of select="@physicalarmor"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>/
			<xsl:choose>
				<xsl:when test="@mysticarmor>0"><xsl:value-of select="@mysticarmor"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>/
			<xsl:choose>
				<xsl:when test="@penalty>0"><xsl:value-of select="@penalty"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell"><xsl:value-of select="@edn"/></td>
		<td class="edCapabCell"><xsl:value-of select="@shatterthreshold"/></td>
		<td class="edCapabCell"><xsl:value-of select="@physicaldeflectionbonus"/></td>
		<td class="edCapabCell"><xsl:value-of select="@mysticdeflectionbonus"/></td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@timesforged_physical>0"><xsl:value-of select="@timesforged_physical"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>/
			<xsl:choose>
				<xsl:when test="@timesforged_mystic>0"><xsl:value-of select="@timesforged_mystic"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell"><xsl:value-of select="@dateforged"/></td>
		<td class="edCapabCell"><xsl:value-of select="@location"/></td>
		<td class="edCapabCell"><xsl:value-of select="@weight"/></td>
		<td class="edCapabCell"><xsl:value-of select="@used"/></td>
	</tr>
</xsl:template>

<xsl:template name="skills">
	<div class="edSubHeader">Skills</div>
	<table width="100%">
		<tr>
			<td class="edHeaderCell" style="text-align: left;">Skillname</td>
			<td class="edHeaderCell">Action</td>
			<td class="edHeaderCell">S</td>
			<td class="edHeaderCell">Attr</td>
			<td class="edHeaderCell">Rank</td>
			<td class="edHeaderCell">Step</td>
			<td class="edHeaderCell">Dice</td>
			<td class="edHeaderCell">Book</td>
		</tr>
		<xsl:apply-templates select="//edc:SKILL"/>
	</table>
</xsl:template>

<xsl:template match="//edc:SKILL">
	<tr>
		<td class="edCapabCell" style="text-align: left;"><xsl:value-of select="@name"/></td>
		<td class="edCapabCell"><xsl:value-of select="@action"/></td>
		<td class="edCapabCell"><xsl:value-of select="@strain"/></td>
		<td class="edCapabCell"><xsl:value-of select="@attribute"/></td>
		<td class="edCapabCell">
			<xsl:value-of select="./edt:RANK/@rank"/>
			<xsl:if test="./edt:RANK/@bonus>0">+</xsl:if>
			<xsl:if test="./edt:RANK/@bonus!=0"><xsl:value-of select="./edt:RANK/@bonus"/></xsl:if>
		</td>
		<td class="edCapabCell"><xsl:value-of select="./edt:RANK/@step"/></td>
		<td class="edCapabCell"><xsl:value-of select="./edt:RANK/@dice"/></td>   
		<td class="edCapabCell"><xsl:value-of select="@bookref"/></td>   
	</tr>
</xsl:template>

<xsl:template name="languages">
	<div class="edSubHeader">Languages</div>
	<b>Speak:</b>&#160;
	<xsl:for-each select="//edc:LANGUAGE">
		<xsl:if test="@speak='yes'"><xsl:value-of select="@language"/>; </xsl:if>
	</xsl:for-each>
	<br/>
	<b>Read/Write:</b>&#160;
	<xsl:for-each select="//edc:LANGUAGE">
		<xsl:if test="@readwrite='yes'"><xsl:value-of select="@language"/>; </xsl:if>
	</xsl:for-each>
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
				<td class="edLPCell"><xsl:value-of select="//edc:EXPERIENCE/@reputation" /></td>
				<td class="edLPCell"><xsl:value-of select="//edc:EXPERIENCE/@renown" /></td>
				<td class="edLPCell"><xsl:value-of select="//edc:EXPERIENCE/@currentlegendpoints" /></td>
				<td class="edLPCell"><xsl:value-of select="//edc:EXPERIENCE/@totallegendpoints" /></td>
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
				<td class="edLPCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@attributes" /></td>
				<td class="edLPCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@disciplinetalents" /></td>
				<td class="edLPCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@optionaltalents" /></td>
				<td class="edLPCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@knacks" /></td>
				<td class="edLPCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@spells" /></td>
				<td class="edLPCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@skills" /></td>
				<td class="edLPCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@karma" /></td>
				<td class="edLPCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@magicitems" /></td>
				<td class="edLPCell"><xsl:value-of select="//edc:CALCULATEDLEGENDPOINTS/@total" /></td>
			</tr>
		</table>
	</div>
</xsl:template>

<xsl:template name="portraits">
	<xsl:if test="//edc:PORTRAIT">
		<div class="edPortraits">
			<div class="edSubHeader">Portraits</div>
			<img class="edPortraitImages">
				<xsl:attribute name="src">
					data:<xsl:value-of select="//edc:PORTRAIT/@contenttype"/>;base64,<xsl:value-of select="//edc:PORTRAIT"/>
				</xsl:attribute>
			</img>
		</div>
	</xsl:if>
</xsl:template>

<xsl:template name="description">
	<xsl:if test="//edc:DESCRIPTION">
	<div class="edDescription">
		<div class="edSubHeader">Description</div>
		<div class="edDescriptionText"><xsl:value-of select="//edc:DESCRIPTION"/></div>
	</div>
	</xsl:if>
</xsl:template>

<xsl:template name="comment">
	<xsl:if test="//edc:COMMENT">
	<div class="edComment">
		<div class="edSubHeader">Comment</div>
		<div class="edCommentText"><xsl:value-of select="//edc:COMMENT"/></div>
	</div>
	</xsl:if>
</xsl:template>

</xsl:stylesheet>
