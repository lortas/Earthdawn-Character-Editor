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
					<td class="edDefense">
						<!-- Defense -->
						<xsl:call-template name="defense" />
					</td>
				</tr>
			</table>
		</div>
		<!-- Health -->
		<xsl:call-template name="health"/>
		<table class="invisible" width="100%">
			<tr class="invisible">
				<td class="invisible">
					<!-- Karma -->
					<xsl:apply-templates select="//edc:KARMA"/>	
				</td>
				<td class="invisible">
					<!-- Initiative -->
					<xsl:apply-templates select="//edc:INITIATIVE"/>	
				</td>
				<td class="invisible">
					<!-- Carrying -->
					<xsl:apply-templates select="//edc:CARRYING"/>	
				</td>
			</tr>
		</table>
		<table class="invisible" width="100%">
			<tr class="invisible">
				<td class="invisible">
					<!-- Experience -->
					<xsl:call-template name="experience" />
				</td>
				<td class="invisible">
					<!-- Calculated Legend Points -->
					<xsl:call-template name="calculatedLegendPoints" />
				</td>
			</tr>
		</table>
		<!-- Disciplines -->
		<table class="invisible" width="100%">
			<tr class="invisible">
				<td style="border-style: solid; border-width: 1px;" valign="top" rowspan="2">
					<table width="100%">
						<thead><tr class="invisible">
							<td class="edHeaderCell">Step</td>
							<td class="edHeaderCell">Dice</td>
						</tr></thead>
						<tr class="invisible">
							<td class="dicetableStep">1</td>
							<td class="dicetableDice">d6-3</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">1</td>
							<td class="dicetableDice">d6-3</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">2</td>
							<td class="dicetableDice">d6-2</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">3</td>
							<td class="dicetableDice">d6-1</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">4</td>
							<td class="dicetableDice">d6</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">5</td>
							<td class="dicetableDice">d8</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">6</td>
							<td class="dicetableDice">d10</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">7</td>
							<td class="dicetableDice">d12</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">8</td>
							<td class="dicetableDice">2d6</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">9</td>
							<td class="dicetableDice">d8+d6</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">10</td>
							<td class="dicetableDice">2d8</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">11</td>
							<td class="dicetableDice">d10+d8</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">12</td>
							<td class="dicetableDice">2d10</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">13</td>
							<td class="dicetableDice">d12+d10</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">14</td>
							<td class="dicetableDice">2d12</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">15</td>
							<td class="dicetableDice">d12+2d6</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">16</td>
							<td class="dicetableDice">d12+d8+d6</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">17</td>
							<td class="dicetableDice">d12+2d8</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">18</td>
							<td class="dicetableDice">d12+d10+d8</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">19</td>
							<td class="dicetableDice">d12+2d10</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">20</td>
							<td class="dicetableDice">2d12+d10</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">21</td>
							<td class="dicetableDice">3d12</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">22</td>
							<td class="dicetableDice">2d12+2d6</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">23</td>
							<td class="dicetableDice">2d12+d8+d6</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">24</td>
							<td class="dicetableDice">2d12+2d8</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">25</td>
							<td class="dicetableDice">2d12+d10+d8</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">26</td>
							<td class="dicetableDice">2d12+2d10</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">27</td>
							<td class="dicetableDice">3d12+d10</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">28</td>
							<td class="dicetableDice">4d12</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">29</td>
							<td class="dicetableDice">3d12+2d6</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">30</td>
							<td class="dicetableDice">3d12+d8+d6</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">31</td>
							<td class="dicetableDice">3d12+2d8</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">32</td>
							<td class="dicetableDice">3d12+d10+d8</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">33</td>
							<td class="dicetableDice">3d12+2d10</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">34</td>
							<td class="dicetableDice">4d12+d10</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">35</td>
							<td class="dicetableDice">5d12</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">36</td>
							<td class="dicetableDice">4d12+2d6</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">37</td>
							<td class="dicetableDice">4d12+d8+d6</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">38</td>
							<td class="dicetableDice">4d12+2d8</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">39</td>
							<td class="dicetableDice">4d12+d10+d8</td>
						</tr>
						<tr class="invisible">
							<td class="dicetableStep">40</td>
							<td class="dicetableDice">4d12+2d10</td>
						</tr>
					</table>
				</td>
				<td class="invisible" valign="top">
					<xsl:apply-templates select="//edc:DISCIPLINE"/>
				</td>
			</tr>
			<tr>
				<td class="invisible" valign="top">
					<!-- Skills -->
					<xsl:call-template name="skills"/>
				</td>
			</tr>
		</table>
		<div class="edLanguages"><xsl:call-template name="languages"/></div>
		<div class="edCoins"><xsl:call-template name="coins"/></div>
		<div class="edWeapons"><xsl:call-template name="weapons"/></div>
		<div class="edArmor"><xsl:call-template name="armor"/></div>
		<div class="edEquipment" width="100%"><xsl:call-template name="equipment"/></div>
		<!-- <div class="edMagicItems"><xsl:call-template name="magicItems"/></div> -->
		<!-- <div class="edPatternItems"><xsl:call-template name="patternItems"/></div> -->
		<div class="edBloodItems"><xsl:call-template name="bloodItems"/></div>
		<div class="edThreadItems" width="100%"><xsl:call-template name="threadItems"/></div>
		<table width="100%">
			<tr>
				<td valign="top">
					<xsl:call-template name="portraits"/>
				</td>
				<td valign="top">
					<xsl:call-template name="description"/>
				</td>
				<td valign="top">
					<xsl:call-template name="comment"/>
				</td>
			</tr>
		</table>
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
			<td colspan="3" class="edValueCell"><xsl:value-of select="/edc:EDCHARACTER/@name" /></td>
			<td class="edKeyCell">Player:</td>
			<td colspan="3" class="edValueCell"><xsl:value-of select="/edc:EDCHARACTER/@player" /></td>
		</tr>
		<!-- Appearance -->
		<xsl:apply-templates select="//edc:APPEARANCE"/>
	</table>
</xsl:template>

<xsl:template match="//edc:APPEARANCE"> 
	<tr>
		<td class="edKeyCell">Race:</td>
		<td class="edValueCell" colspan="2"><xsl:value-of select="@race" /></td>
		<td class="edKeyCell">Origin:</td>
		<td class="edValueCell" colspan="2"><xsl:value-of select="@origin" /></td>
		<td class="edKeyCell">Age:</td>
		<td class="edValueCell"><xsl:value-of select="@age" /></td>
	</tr>
	<tr>
		<td class="edKeyCell">Gender:</td>
		<td class="edValueCell"><xsl:value-of select="@gender" /></td>
		<td class="edKeyCell">Skin:</td>
		<td class="edValueCell"><xsl:value-of select="@skin" /></td>
		<td class="edKeyCell">Hair:</td>
		<td class="edValueCell"><xsl:value-of select="@hair" /></td>		
		<td class="edKeyCell">Eyes:</td>
		<td class="edValueCell"><xsl:value-of select="@eyes" /></td>
	</tr>
	<tr>
		<td class="edKeyCell">Weight:</td>
		<td class="edValueCell"><xsl:value-of select="@weight" /></td>
		<td class="edKeyCell">Height:</td>
		<td class="edValueCell"><xsl:value-of select="@height" /></td>
		<td class="edKeyCell">Birth:</td>
		<td class="edValueCell"><xsl:value-of select="@birth" /></td>
	</tr>
	<tr>
		<td colspan="8">Racial Abilities market with &quot;*&quot; are already calculated within the character.</td>
	</tr>
	<tr>
		<!-- Racial Abilities -->
		<td class="edKeyCell" colspan="2">Racial Abilities:</td>
		<td class="edValueCell" colspan="6">
			<xsl:apply-templates select="//edc:RACEABILITES"/>
		</td>
	</tr>
	<tr>
	</tr>
</xsl:template>

<xsl:template match="//edc:RACEABILITES">
	<xsl:value-of select="."/>
	<xsl:if test="./following::edc:RACEABILITES">; </xsl:if>
</xsl:template>

<xsl:template name="attributes">
	<div class="edSubHeader">Attributes</div>
	<table width="100%">
		<tr>
			<td class="edHeaderCell"><!-- Leer --></td>
			<td class="edHeaderCell">Race</td>
			<td class="edHeaderCell">Base</td>
			<td class="edHeaderCell">LP</td>
			<td class="edHeaderCell">Current</td>
			<td class="edHeaderCell">Step</td>
			<td class="edHeaderCell">Dice</td>
		</tr>
		<xsl:apply-templates select="//edc:ATTRIBUTE"/>
	</table>
</xsl:template>

<xsl:template match="//edc:ATTRIBUTE">
	<tr>
		<td class="edCell"><xsl:value-of select="@name"/></td>
		<td class="edMidCell"><xsl:value-of select="@racevalue"/></td>
		<td class="edMidCell"><xsl:value-of select="@basevalue"/></td>
		<td class="edMidCell"><xsl:value-of select="@lpincrease"/></td>
		<td class="edMidCell"><xsl:value-of select="@currentvalue"/></td>
		<td class="edMidCell"><xsl:value-of select="@step"/></td>
		<td class="edMidCell"><xsl:value-of select="@dice"/></td>   
	</tr>
</xsl:template>

<xsl:template name="defense">
	<div class="edSubHeader">Defense/Armor Rating</div>
	<table width="100%">
		<xsl:apply-templates select="//edc:DEFENSE"/>	
		<xsl:apply-templates select="//edc:PROTECTION"/>
		<xsl:apply-templates select="//edc:MOVEMENT"/>	
	</table>
</xsl:template>

<xsl:template match="//edc:DEFENSE">
	<tr>
		<td class="edKeyCell">Social&#160;Defense:</td>
		<td class="edValueCell"><xsl:value-of select="@social" /></td>
	</tr>
	<tr>
		<td class="edKeyCell">Spell&#160;Defense:</td>
		<td class="edValueCell"><xsl:value-of select="@spell" /></td>
	</tr>
	<tr>
		<td class="edKeyCell">Physical&#160;Defense:</td>
		<td class="edValueCell"><xsl:value-of select="@physical" /></td>
	</tr>
</xsl:template>

<xsl:template match="//edc:PROTECTION">
	<tr>
		<td class="edKeyCell">Mystic&#160;Armor:</td>
		<td class="edValueCell"><xsl:value-of select="@mysticarmor" /></td>
	</tr>
	<tr>
		<td class="edKeyCell">Physical&#160;Armor:</td>
		<td class="edValueCell"><xsl:value-of select="@physicalarmor" /></td>
	</tr>
</xsl:template>

<xsl:template match="//edc:MOVEMENT">
	<tr>
		<td class="edKeyCell">Movement&#160;Ground:</td>
		<td class="edValueCell"><xsl:value-of select="@ground" /></td>
	</tr>
	<tr>
		<td class="edKeyCell">Movement&#160;Flight:</td>
		<td class="edValueCell"><xsl:value-of select="@flight" /></td>
	</tr>
</xsl:template>

<xsl:template name="health">
	<div class="edHealth">
		<div class="edSubHeader">Health</div>
		<table width="100%">
			<tr>
				<td class="edKeyCell">Death</td>
				<td class="edValueCell"><xsl:value-of select="//edt:DEATH/@base" /> + <xsl:value-of select="//edt:DEATH/@adjustment" /> = <xsl:value-of select="//edt:DEATH/@value" /></td>
				<td class="edKeyCell">Unconsciousness</td>
				<td class="edValueCell"><xsl:value-of select="//edt:UNCONSCIOUSNESS/@base" /> + <xsl:value-of select="//edt:UNCONSCIOUSNESS/@adjustment" /> = <xsl:value-of select="//edt:UNCONSCIOUSNESS/@value" /></td>
				<td class="edKeyCell">Recovery Step</td>
				<td class="edValueCell"><xsl:value-of select="//edt:RECOVERY/@step" /></td>
				<td class="edKeyCell">Recovery Dice</td>
				<td class="edValueCell"><xsl:value-of select="//edt:RECOVERY/@dice" /></td>
				<td class="edKeyCell">Recovery Tests</td>
				<td class="edValueCell"><xsl:value-of select="//edt:RECOVERY/@testsperday" /></td>
			</tr>
			<tr>
				<td class="edKeyCell">Current Damage</td>
				<td class="edValueCell"><xsl:value-of select="//edc:HEALTH/@damage" /></td>
				<td class="edKeyCell">Current Wounds</td>
				<td class="edValueCell"><xsl:value-of select="//edc:HEALTH/edt:WOUNDS/@normal" /></td>
				<td class="edKeyCell">Blood Wounds</td>
				<td class="edValueCell"><xsl:value-of select="//edc:HEALTH/edt:WOUNDS/@blood" /></td>
				<td class="edKeyCell">Wound Penalties</td>
				<td class="edValueCell"><xsl:value-of select="//edc:HEALTH/edt:WOUNDS/@penalties" /></td>
				<td class="edKeyCell">Wound Threshold</td>
				<td class="edValueCell"><xsl:value-of select="//edc:HEALTH/edt:WOUNDS/@threshold" /></td>
			</tr>
		</table>
	</div>
</xsl:template>

<xsl:template match="//edc:KARMA">
	<div class="edKarma">
		<div class="edSubHeader">Karma</div>
		<table class="invisible" width="100%"><tbody class="invisible">
			<tr class="invisible">
				<td class="edKeyCell" width="90em">Karma Points:</td>
				<td class="edValueCell"><xsl:value-of select="@current" /></td>
				<td class="edKeyCell">Max:</td>
				<td class="edValueCell"><xsl:value-of select="@max" /></td>
				<td class="edKeyCell">Step:</td>
				<td class="edValueCell"><xsl:value-of select="@step" /></td>
				<td class="edKeyCell">Dice:</td>
				<td class="edValueCell"><xsl:value-of select="@dice" /></td>
				<td width="5%">&#160;</td>
			</tr>
		</tbody></table>
	</div>
</xsl:template>

<xsl:template match="//edc:INITIATIVE">
	<div class="edInitiative">
		<div class="edSubHeader">Initiative</div>
		<table width="100%">
			<tr>
				<td class="edKeyCell">Step:</td>
				<td class="edValueCell"><xsl:value-of select="@step" /></td>
				<td class="edKeyCell">Dice:</td>
				<td class="edValueCell"><xsl:value-of select="@dice" /></td>
			</tr>
		</table>
	</div>
</xsl:template>

<xsl:template match="//edc:CARRYING">
	<div class="edCarrying">
		<div class="edSubHeader">Carrying</div>
		<table width="100%">
			<tr>
				<td class="edKeyCell">Carrying:</td>
				<td class="edValueCell"><xsl:value-of select="@carrying" /></td>
				<td class="edKeyCell">Lifting:</td>
				<td class="edValueCell"><xsl:value-of select="@lifting" /></td>
			</tr>
		</table>
	</div>
</xsl:template>

<xsl:template match="//edt:SHIELD">
	Shield:<br />
	<div>
		Name=<xsl:value-of select="@name" /><br/>
		Deflection Bonus=<xsl:value-of select="@deflectionbonus" /><br/>
	</div>
</xsl:template>

<xsl:template match="//edc:DISCIPLINE">
	<div class="edDiscipline">
		<!-- Discipline -->
		<div class="edSubHeader">Discipline <xsl:value-of select="@name" /> (Circle <xsl:value-of select="@circle"/>)</div>
		<div class="edDisciplineBonuses">
			<!-- Discipline Bonuses -->
			<xsl:call-template name="disciplineBonuses"/>
		</div>
		<div class="edSubHeader" style="text-align:left">Discipline Talents :</div>
		<table width="100%">
			<thead><tr>
				<td class="edHeaderCell" style="text-align: right;">Talentname</td>
				<td class="edHeaderCell">Action</td>
				<td class="edHeaderCell">Strain</td>
				<td class="edHeaderCell">Attr.</td>
				<td class="edHeaderCell">Rank</td>
				<td class="edHeaderCell">Step</td>
				<td class="edHeaderCell">Dice</td>
				<td class="edHeaderCell">Ini.</td>
				<td class="edHeaderCell">Realign</td>
				<td class="edHeaderCell">Book</td>
			</tr></thead>
			<xsl:apply-templates select="./edt:DISZIPLINETALENT">
				<xsl:sort select="@circle" data-type="number" order="ascending"/>
				<xsl:sort select="./edt:RANK/@rank" data-type="number" order="descending"/>
				<xsl:sort select="./edt:RANK/@step" data-type="number" order="descending"/>
				<xsl:sort select="@name"/>
				<xsl:sort select="@limitation"/>
			</xsl:apply-templates>
		</table>
		<br/>
		<div class="edSubHeader" style="text-align:left">Optional Talents :</div>
		<table width="100%">
			<thead><tr>
				<td class="edHeaderCell" style="text-align: right;">Talent Name</td>
				<td class="edHeaderCell">Karma</td>
				<td class="edHeaderCell">Action</td>
				<td class="edHeaderCell">Strain</td>
				<td class="edHeaderCell">Attr.</td>
				<td class="edHeaderCell">Rank</td>
				<td class="edHeaderCell">Step</td>
				<td class="edHeaderCell">Dice</td>
				<td class="edHeaderCell">Ini.</td>
				<td class="edHeaderCell">Realign</td>
				<td class="edHeaderCell">Book</td>
			</tr></thead>
			<xsl:apply-templates select="./edt:OPTIONALTALENT">
				<xsl:sort select="@circle" data-type="number" order="ascending"/>
				<xsl:sort select="./edt:RANK/@rank" data-type="number" order="descending"/>
				<xsl:sort select="./edt:RANK/@step" data-type="number" order="descending"/>
				<xsl:sort select="@name"/>
				<xsl:sort select="@limitation"/>
			</xsl:apply-templates>
		</table>
	</div>
</xsl:template>

<xsl:template match="//edt:DISZIPLINETALENT">
	<tr>
		<td class="edKeyCell">
			<xsl:value-of select="@name"/>
			<xsl:if test="@limitation!=''">: <xsl:value-of select="@limitation"/></xsl:if>
			<xsl:if test="./edt:TEACHER/@byversatility!='yes'"> (v)</xsl:if>
		</td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@action='na'">&#8211;</xsl:when>
				<xsl:otherwise><xsl:value-of select="@action"/></xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell"><xsl:value-of select="@strain"/></td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@attribute='na'">&#8211;</xsl:when>
				<xsl:otherwise><xsl:value-of select="@attribute"/></xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell">
			<xsl:value-of select="./edt:RANK/@rank"/>
			<xsl:if test="./edt:RANK/@bonus>0">+</xsl:if>
			<xsl:if test="./edt:RANK/@bonus!=0"><xsl:value-of select="./edt:RANK/@bonus"/></xsl:if>
		</td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@attribute='na'">&#8211;</xsl:when>
				<xsl:otherwise><xsl:value-of select="./edt:RANK/@step"/></xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@attribute='na'">&#8211;</xsl:when>
				<xsl:otherwise><xsl:value-of select="./edt:RANK/@dice"/></xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell"><xsl:value-of select="@isinitiative"/></td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@realigned>0">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell"><xsl:value-of select="@bookref"/></td>
	</tr>
</xsl:template>

<xsl:template match="//edt:OPTIONALTALENT">
	<tr>
		<td class="edKeyCell">
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
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@attribute='na'">-</xsl:when>
				<xsl:otherwise><xsl:value-of select="./edt:RANK/@step"/></xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@attribute='na'">-</xsl:when>
				<xsl:otherwise><xsl:value-of select="./edt:RANK/@dice"/></xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell"><xsl:value-of select="@isinitiative"/></td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@realigned>0">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell"><xsl:value-of select="@bookref"/></td>
	</tr>
</xsl:template>

<xsl:template name="disciplineBonuses">
	Discipline Bonuses:
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
			<td class="edHeaderCell" style="text-align: left;">Name</td>
			<td class="edHeaderCell">Dmg</td>
			<td class="edHeaderCell">Sz</td>
			<td class="edHeaderCell">Short</td>
			<td class="edHeaderCell">Long</td>
			<td class="edHeaderCell">STR min</td>
			<td class="edHeaderCell">DEX min</td>
			<td class="edHeaderCell">Forged</td>
			<td class="edHeaderCell">Date</td>
			<td class="edHeaderCell">Location</td>
			<td class="edHeaderCell">Weight</td>
			<td class="edHeaderCell">Used</td>
		</tr>
		<xsl:apply-templates select="//edc:WEAPON">
				<xsl:sort select="@used"/>
				<xsl:sort select="@damagestep"/>
				<xsl:sort select="@name"/>
		</xsl:apply-templates>
	</table>
</xsl:template>

<xsl:template match="//edc:WEAPON">
	<tr>
		<td class="edKeyCell" style="text-align: right;"><xsl:value-of select="@name"/></td>
		<td class="edCapabCell"><xsl:value-of select="@damagestep"/></td>
		<td class="edCapabCell"><xsl:value-of select="@size"/></td>
		<td class="edCapabCell"><xsl:value-of select="@shortrange"/></td>
		<td class="edCapabCell"><xsl:value-of select="@longrange"/></td>
		<td class="edCapabCell"><xsl:value-of select="@dexteritymin"/></td>
		<td class="edCapabCell"><xsl:value-of select="@strengthmin"/></td>
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
		<td class="edCapabCell" style="text-align: left;">
			<img class="edIcon">
				<xsl:attribute name="src">
					<xsl:value-of select="/edc:EDCHARACTER/@editorpath"/>icons/<xsl:value-of select="translate(@kind,$smallcase,$uppercase)" /><xsl:if test="not(@kind)">UNDEFINED</xsl:if>.png
				</xsl:attribute>
			</img>
			<xsl:value-of select="@name"/>
		</td>
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
	<table width="100%"><tr>
		<td valign="top">
			<div class="edSubHeader">Armor</div>
			<table width="100%">
				<thead><tr>
					<td class="edHeaderCell" style="text-align: left;">Name</td>
					<td class="edHeaderCell">protect</td>
					<td class="edHeaderCell">edn</td>
					<td class="edHeaderCell">date</td>
					<td class="edHeaderCell">location</td>
					<td class="edHeaderCell">weight</td>
					<td class="edHeaderCell">Used</td>
				</tr></thead>
				<xsl:apply-templates select="//edc:PROTECTION/edt:ARMOR[position()  > 1]"/>
			</table>
		</td>
		<td valign="top">
			<div class="edSubHeader">Shield</div>
			<table width="100%">
				<thead><tr>
					<td class="edHeaderCell" style="text-align: left;">Name</td>
					<td class="edHeaderCell">protect</td>
					<td class="edHeaderCell">edn</td>
					<td class="edHeaderCell">shatter</td>
					<td class="edHeaderCell">pdb</td>
					<td class="edHeaderCell">mdb</td>
					<td class="edHeaderCell">date</td>
					<td class="edHeaderCell">location</td>
					<td class="edHeaderCell">weight</td>
					<td class="edHeaderCell">Used</td>
				</tr></thead>
				<xsl:apply-templates select="//edc:PROTECTION/edt:SHIELD"/>
			</table>
		</td>
	</tr></table>
</xsl:template>

<xsl:template match="//edc:PROTECTION/edt:ARMOR">
	<tr>
		<td class="edCapabCell" style="text-align: left;">
			<xsl:value-of select="@name"/>
			<xsl:if test="(@timesforged_physical>0) or (@timesforged_mystic>0)">(<xsl:choose>
				<xsl:when test="@timesforged_physical>0"><xsl:value-of select="@timesforged_physical"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>/<xsl:choose>
				<xsl:when test="@timesforged_mystic>0"><xsl:value-of select="@timesforged_mystic"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>)</xsl:if>
		</td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@physicalarmor>0"><xsl:value-of select="@physicalarmor"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>/<xsl:choose>
				<xsl:when test="@mysticarmor>0"><xsl:value-of select="@mysticarmor"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>/<xsl:choose>
				<xsl:when test="@penalty>0"><xsl:value-of select="@penalty"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell"><xsl:value-of select="@edn"/></td>
		<td class="edCapabCell"><xsl:value-of select="@dateforged"/></td>
		<td class="edCapabCell"><xsl:value-of select="@location"/></td>
		<td class="edCapabCell"><xsl:value-of select="@weight"/></td>
		<td class="edCapabCell"><xsl:value-of select="@used"/></td>
	</tr>
</xsl:template>

<xsl:template match="//edc:PROTECTION/edt:SHIELD">
	<tr>
		<td class="edCapabCell" style="text-align: left;">
			<xsl:value-of select="@name"/>
			<xsl:if test="(@timesforged_physical>0) or (@timesforged_mystic>0)">(<xsl:choose>
				<xsl:when test="@timesforged_physical>0"><xsl:value-of select="@timesforged_physical"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>/<xsl:choose>
				<xsl:when test="@timesforged_mystic>0"><xsl:value-of select="@timesforged_mystic"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>)</xsl:if>
		</td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@physicalarmor>0"><xsl:value-of select="@physicalarmor"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>/<xsl:choose>
				<xsl:when test="@mysticarmor>0"><xsl:value-of select="@mysticarmor"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>/<xsl:choose>
				<xsl:when test="@penalty>0"><xsl:value-of select="@penalty"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell"><xsl:value-of select="@edn"/></td>
		<td class="edCapabCell"><xsl:value-of select="@shatterthreshold"/></td>
		<td class="edCapabCell"><xsl:value-of select="@physicaldeflectionbonus"/></td>
		<td class="edCapabCell"><xsl:value-of select="@mysticdeflectionbonus"/></td>
		<td class="edCapabCell"><xsl:value-of select="@dateforged"/></td>
		<td class="edCapabCell"><xsl:value-of select="@location"/></td>
		<td class="edCapabCell"><xsl:value-of select="@weight"/></td>
		<td class="edCapabCell"><xsl:value-of select="@used"/></td>
	</tr>
</xsl:template>

<xsl:template name="skills">
	<div class="edSubHeader" style="text-align:left">Skills :</div>
	<table width="100%">
		<thead><tr>
			<td class="edHeaderCell" style="text-align: right;">Skill Name</td>
			<td class="edHeaderCell">Action</td>
			<td class="edHeaderCell">Strain</td>
			<td class="edHeaderCell">Attr</td>
			<td class="edHeaderCell">Rank</td>
			<td class="edHeaderCell">Step</td>
			<td class="edHeaderCell">Dice</td>
			<td class="edHeaderCell">Ini.</td>
			<td class="edHeaderCell">Realign</td>
			<td class="edHeaderCell">Book</td>
		</tr></thead>
		<xsl:apply-templates select="//edc:SKILL">
			<xsl:sort select="./edt:RANK/@rank" data-type="number" order="descending"/>
			<xsl:sort select="./edt:RANK/@step" data-type="number" order="descending"/>
			<xsl:sort select="@name"/>
			<xsl:sort select="@limitation"/>
		</xsl:apply-templates>
	</table>
</xsl:template>

<xsl:template match="//edc:SKILL">
	<tr>
		<td class="edKeyCell"><xsl:value-of select="@name"/></td>
		<td class="edCapabCell"><xsl:value-of select="@action"/></td>
		<td class="edCapabCell"><xsl:value-of select="@strain"/></td>
		<td class="edCapabCell"><xsl:value-of select="@attribute"/></td>
		<td class="edCapabCell">
			<xsl:value-of select="./edt:RANK/@rank"/>
			<xsl:if test="./edt:RANK/@bonus>0">+</xsl:if>
			<xsl:if test="./edt:RANK/@bonus!=0"><xsl:value-of select="./edt:RANK/@bonus"/></xsl:if>
		</td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@attribute='na'">&#8211;</xsl:when>
				<xsl:otherwise><xsl:value-of select="./edt:RANK/@step"/></xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@attribute='na'">&#8211;</xsl:when>
				<xsl:otherwise><xsl:value-of select="./edt:RANK/@dice"/></xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell"><xsl:value-of select="@isinitiative"/></td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@realigned>0">yes</xsl:when>
				<xsl:otherwise>no</xsl:otherwise>
			</xsl:choose>
		</td>
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
	<table class="invisible" width="100%">
		<thead><tr>
			<td class="edHeaderCell" style="text-align: right;">Purse Name</td>
			<td class="edHeaderCell">Location</td>
			<td class="edHeaderCell">Copper (1/10)</td>
			<td class="edHeaderCell">Silver (1)</td>
			<td class="edHeaderCell">Gold (10)</td>
			<xsl:if test="sum(//edc:COINS/@gem50)>0"><td class="edHeaderCell">Gem (50)</td></xsl:if>
			<xsl:if test="sum(//edc:COINS/@earth)>0"><td class="edHeaderCell">Earth (100)</td></xsl:if>
			<xsl:if test="sum(//edc:COINS/@water)>0"><td class="edHeaderCell">Water (100)</td></xsl:if>
			<xsl:if test="sum(//edc:COINS/@gem100)>0"><td class="edHeaderCell">Gem (100)</td></xsl:if>
			<xsl:if test="sum(//edc:COINS/@gem200)>0"><td class="edHeaderCell">Gem (200)</td></xsl:if>
			<xsl:if test="sum(//edc:COINS/@gem500)>0"><td class="edHeaderCell">Gem (500)</td></xsl:if>
			<xsl:if test="sum(//edc:COINS/@air)>0"><td class="edHeaderCell">Air (1000)</td></xsl:if>
			<xsl:if test="sum(//edc:COINS/@fire)>0"><td class="edHeaderCell">Fire (1000)</td></xsl:if>
			<xsl:if test="sum(//edc:COINS/@gem1000)>0"><td class="edHeaderCell">Gem (1000)</td></xsl:if>
			<xsl:if test="sum(//edc:COINS/@orichalcum)>0"><td class="edHeaderCell">Oiricgalcum (10000)</td></xsl:if>
		</tr></thead>
		<xsl:apply-templates select="//edc:COINS">
			<xsl:sort select="@name"/>
		</xsl:apply-templates>
		<!--
		<tr>
			<td class="edKeyCell" style="text-align: right">Sum</td>
			<td class="edCapabCell"></td>
			<td class="edCapabCell"><xsl:value-of select="sum(//edc:COINS/@copper)"/></td>
			<td class="edCapabCell"><xsl:value-of select="sum(//edc:COINS/@silver)"/></td>
			<td class="edCapabCell"><xsl:value-of select="sum(//edc:COINS/@gold)"/></td>
			<td class="edCapabCell"><xsl:value-of select="sum(//edc:COINS/@gem50)"/></td>
			<td class="edCapabCell"><xsl:value-of select="sum(//edc:COINS/@earth)"/></td>
			<td class="edCapabCell"><xsl:value-of select="sum(//edc:COINS/@water)"/></td>
			<td class="edCapabCell"><xsl:value-of select="sum(//edc:COINS/@gem100)"/></td>
			<td class="edCapabCell"><xsl:value-of select="sum(//edc:COINS/@gem200)"/></td>
			<td class="edCapabCell"><xsl:value-of select="sum(//edc:COINS/@gem500)"/></td>
			<td class="edCapabCell"><xsl:value-of select="sum(//edc:COINS/@air)"/></td>
			<td class="edCapabCell"><xsl:value-of select="sum(//edc:COINS/@fire)"/></td>
			<td class="edCapabCell"><xsl:value-of select="sum(//edc:COINS/@gem1000)"/></td>
			<td class="edCapabCell"><xsl:value-of select="sum(//edc:COINS/@orichalcum)"/></td>
		</tr>
		-->
	</table>
</xsl:template>

<xsl:template match="//edc:COINS">
	<tr>
		<td class="edKeyCell" style="text-align: right;">
			<img class="edIcon">
				<xsl:attribute name="src">
					<xsl:value-of select="/edc:EDCHARACTER/@editorpath"/>icons/<xsl:value-of select="translate(@kind,$smallcase,$uppercase)" /><xsl:if test="not(@kind)">UNDEFINED</xsl:if>.png
				</xsl:attribute>
			</img>
			<xsl:value-of select="@name"/>
		</td>
		<td class="edCapabCell"><xsl:value-of select="@location"/></td>
		<td class="edCapabCell"><xsl:value-of select="@copper"/></td>
		<td class="edCapabCell"><xsl:value-of select="@silver"/></td>
		<td class="edCapabCell"><xsl:value-of select="@gold"/></td>
		<xsl:if test="sum(//edc:COINS/@gem50)>0"><td class="edCapabCell"><xsl:value-of select="@gem50"/></td></xsl:if>
		<xsl:if test="sum(//edc:COINS/@earth)>0"><td class="edCapabCell"><xsl:value-of select="@earth"/></td></xsl:if>
		<xsl:if test="sum(//edc:COINS/@water)>0"><td class="edCapabCell"><xsl:value-of select="@water"/></td></xsl:if>
		<xsl:if test="sum(//edc:COINS/@gem100)>0"><td class="edCapabCell"><xsl:value-of select="@gem100"/></td></xsl:if>
		<xsl:if test="sum(//edc:COINS/@gem200)>0"><td class="edCapabCell"><xsl:value-of select="@gem200"/></td></xsl:if>
		<xsl:if test="sum(//edc:COINS/@gem500)>0"><td class="edCapabCell"><xsl:value-of select="@gem500"/></td></xsl:if>
		<xsl:if test="sum(//edc:COINS/@air)>0"><td class="edCapabCell"><xsl:value-of select="@air"/></td></xsl:if>
		<xsl:if test="sum(//edc:COINS/@fire)>0"><td class="edCapabCell"><xsl:value-of select="@fire"/></td></xsl:if>
		<xsl:if test="sum(//edc:COINS/@gem1000)>0"><td class="edCapabCell"><xsl:value-of select="@gem1000"/></td></xsl:if>
		<xsl:if test="sum(//edc:COINS/@orichalcum)>0"><td class="edCapabCell"><xsl:value-of select="@orichalcum"/></td></xsl:if>
	</tr>
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
			<xsl:apply-templates select="//edc:ITEM[(position() mod 2) = 1]"> 
				<xsl:sort select="@name"/>
			</xsl:apply-templates>
		</table>
	</xsl:if>
</xsl:template>

<xsl:template match="//edc:ITEM">
	<tr>
		<xsl:for-each select=". | following-sibling::edc:ITEM[position() &lt; 2]">
			<td class="edItemCell" style="text-align: left;">
				<img class="edIcon">
					<xsl:attribute name="src">
						<xsl:value-of select="/edc:EDCHARACTER/@editorpath"/>icons/<xsl:value-of select="translate(@kind,$smallcase,$uppercase)" /><xsl:if test="not(@kind)">UNDEFINED</xsl:if>.png
					</xsl:attribute>
				</img>
				<xsl:value-of select="@name" />
			</td>
			<td class="edItemCell"><xsl:value-of select="@weight" /></td>
			<td class="edItemCell"><xsl:value-of select="@location" /></td>
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
							<td class="edValueCell" colspan="3">
								<img class="edIcon">
									<xsl:attribute name="src">
										<xsl:value-of select="/edc:EDCHARACTER/@editorpath"/>icons/<xsl:value-of select="translate(@kind,$smallcase,$uppercase)" /><xsl:if test="not(@kind)">UNDEFINED</xsl:if>.png
									</xsl:attribute>
								</img>
								<xsl:value-of select="@name" />
							</td>
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
	<div class="edDescription">
		<div class="edSubHeader">Description</div>
		<div class="edDescriptionText"><xsl:value-of select="//edc:DESCRIPTION"/></div>
	</div>
</xsl:template>

<xsl:template name="comment">
	<div class="edComment">
		<div class="edSubHeader">Comment</div>
		<div class="edCommentText"><xsl:value-of select="//edc:COMMENT"/></div>
	</div>
</xsl:template>

<xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'" />
<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'" />
</xsl:stylesheet>
