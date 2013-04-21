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
						<xsl:call-template name="miscellaneous" />
					</td>
					<td class="edAttributes">
						<xsl:call-template name="attributes" />
					</td>
				</tr>
			</table>
		</div>
		<xsl:call-template name="defense"/>
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
		<div class="edMagicItems"><xsl:call-template name="magicItems"/></div>
		<div class="edPatternItems"><xsl:call-template name="patternItems"/></div>
		<div class="edBloodItems"><xsl:call-template name="bloodcharmItems"/></div>
		<div class="edThreadItems" width="100%"><xsl:call-template name="threadItems"/></div>
		<table width="100%">
			<tr>
				<td valign="top" rowspan="2">
					<xsl:call-template name="portraits"/>
				</td>
				<td valign="top">
					<xsl:call-template name="description"/>
				</td>
			</tr>
			<tr>
				<td valign="top">
					<xsl:call-template name="comment"/>
				</td>
			</tr>
		</table>
		<xsl:call-template name="openspells"/>
		<xsl:call-template name="disciplinespells"/>
		<xsl:call-template name="legendpointsreceived"/>
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
		<td class="edValueCell"><xsl:value-of select="format-number(@weight,'##')" />lb (<xsl:value-of select="format-number(@weight*0.453592,'##')" />kg)</td>
		<td class="edKeyCell">Height:</td>
		<td class="edValueCell"><xsl:value-of select="format-number(@height,'##')" />ft (<xsl:value-of select="format-number(@height*0.3048,'##.00')" />m)</td>
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
		<xsl:apply-templates select="//edc:ATTRIBUTE[@name='DEX']"/>
		<xsl:apply-templates select="//edc:ATTRIBUTE[@name='STR']"/>
		<xsl:apply-templates select="//edc:ATTRIBUTE[@name='TOU']"/>
		<xsl:apply-templates select="//edc:ATTRIBUTE[@name='PER']"/>
		<xsl:apply-templates select="//edc:ATTRIBUTE[@name='WIL']"/>
		<xsl:apply-templates select="//edc:ATTRIBUTE[@name='CHA']"/>
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
	<div class="edHealth">
		<div class="edSubHeader">Defense/Armor Rating</div>
		<table width="100%">
			<tr>
				<td class="edKeyCell">Physical&#160;Defense:</td>
				<td class="edValueCell"><xsl:value-of select="//edc:DEFENSE/@physical"/></td>
				<td class="edKeyCell">Physical&#160;Armor:</td>
				<td class="edValueCell">
					<xsl:value-of select="//edc:PROTECTION/@physicalarmor"/> (<xsl:for-each select="//edc:PROTECTION/edt:ARMOR[@physicalarmor!=0] | //edc:PROTECTION/edt:SHIELD[@physicalarmor!=0]">
						<xsl:value-of select="@physicalarmor"/>
						<xsl:if test="position()!=last()">+</xsl:if>
					</xsl:for-each>)
				</td>
				<td class="edKeyCell">Movement&#160;Ground:</td>
				<td class="edValueCell"><xsl:value-of select="//edc:MOVEMENT/@ground"/>hex (<xsl:value-of select="format-number(//edc:MOVEMENT/@ground*1.8288,'##.0')"/>m)</td>
			</tr>
			<tr>
				<td class="edKeyCell">Spell&#160;Defense:</td>
				<td class="edValueCell"><xsl:value-of select="//edc:DEFENSE/@spell"/></td>
				<td class="edKeyCell">Mystic&#160;Armor:</td>
				<td class="edValueCell">
					<xsl:value-of select="//edc:PROTECTION/@mysticarmor"/> (<xsl:for-each select="//edc:PROTECTION/edt:ARMOR[@mysticarmor!=0] | //edc:PROTECTION/edt:SHIELD[@mysticarmor!=0]">
						<xsl:value-of select="@mysticarmor"/>
						<xsl:if test="position()!=last()">+</xsl:if>
					</xsl:for-each>)
				</td>
				<td class="edKeyCell">Movement&#160;Flight:</td>
				<td class="edValueCell"><xsl:value-of select="//edc:MOVEMENT/@flight"/>hex (<xsl:value-of select="format-number(//edc:MOVEMENT/@flight*1.8288,'##.0')"/>m)</td>
			</tr>
			<tr>
				<td class="edKeyCell">Social&#160;Defense:</td>
				<td class="edValueCell"><xsl:value-of select="//edc:DEFENSE/@social"/></td>
				<td class="edKeyCell">Penalty:</td>
				<td class="edValueCell"><xsl:value-of select="//edc:PROTECTION/@penalty"/></td>
			</tr>
		</table>
	</div>
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
				<td class="edKeyCell">Current Damage</td>
				<td class="edValueCell"><xsl:value-of select="//edc:HEALTH/@damage" /></td>
			</tr>
			<tr>
				<td class="edKeyCell">Recovery</td>
				<td class="edValueCell"><xsl:value-of select="//edt:RECOVERY/@step" /> (<xsl:value-of select="//edt:RECOVERY/@dice" />)</td>
				<td class="edKeyCell">Recovery Tests</td>
				<td class="edValueCell"><xsl:value-of select="//edt:RECOVERY/@testsperday" /></td>
				<td class="edKeyCell">Wound Threshold</td>
				<td class="edValueCell"><xsl:value-of select="//edc:HEALTH/edt:WOUNDS/@threshold" /></td>
			</tr>
			<tr>
				<td class="edKeyCell">Normal Wounds</td>
				<td class="edValueCell"><xsl:value-of select="//edc:HEALTH/edt:WOUNDS/@normal" /></td>
				<td class="edKeyCell">Blood Wounds</td>
				<td class="edValueCell"><xsl:value-of select="//edc:HEALTH/edt:WOUNDS/@blood" /></td>
				<td class="edKeyCell">Wound Penalties</td>
				<td class="edValueCell"><xsl:value-of select="//edc:HEALTH/edt:WOUNDS/@penalties" /></td>
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
				<td class="edHeaderCell">Book</td>
			</tr></thead>
			<xsl:apply-templates select="./edt:DISZIPLINETALENT">
				<xsl:sort select="@circle" data-type="number" order="ascending"/>
				<xsl:sort select="./edt:RANK/@rank" data-type="number" order="descending"/>
				<xsl:sort select="./edt:RANK/@step" data-type="number" order="descending"/>
				<xsl:sort select="@name"/>
				<xsl:sort select="./edt:LIMITATION"/>
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
				<td class="edHeaderCell">Book</td>
			</tr></thead>
			<xsl:apply-templates select="./edt:OPTIONALTALENT">
				<xsl:sort select="@circle" data-type="number" order="ascending"/>
				<xsl:sort select="./edt:RANK/@rank" data-type="number" order="descending"/>
				<xsl:sort select="./edt:RANK/@step" data-type="number" order="descending"/>
				<xsl:sort select="@name"/>
				<xsl:sort select="./edt:LIMITATION"/>
			</xsl:apply-templates>
		</table>
	</div>
</xsl:template>

<xsl:template match="//edt:DISZIPLINETALENT">
	<tr>
		<xsl:if test="@realigned>0">
			<xsl:attribute name="style">text-decoration:line-through;</xsl:attribute>
		</xsl:if>
		<td class="edKeyCell">
			<xsl:value-of select="@name"/>
			<xsl:if test="./edt:LIMITATION">
				<xsl:text>: </xsl:text>
				<xsl:for-each select="./edt:LIMITATION">
					<xsl:value-of select="."/>
					<xsl:if test="position() &lt; last()">, </xsl:if>
				</xsl:for-each>
			</xsl:if>
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
		<td class="edCapabCell"><xsl:value-of select="@bookref"/></td>
	</tr>
</xsl:template>

<xsl:template match="//edt:OPTIONALTALENT">
	<tr>
		<xsl:if test="@realigned>0">
			<xsl:attribute name="style">text-decoration:line-through;</xsl:attribute>
		</xsl:if>
		<td class="edKeyCell">
			<xsl:value-of select="@name"/>
			<xsl:if test="./edt:LIMITATION">
				<xsl:text>: </xsl:text>
				<xsl:for-each select="./edt:LIMITATION">
					<xsl:value-of select="."/>
					<xsl:if test="position() &lt; last()">, </xsl:if>
				</xsl:for-each>
			</xsl:if>
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

<xsl:template name="openspells">
	<xsl:if test="//edc:OPENSPELL">
		<div class="edDiscipline">
			<div class="edSubHeader">Open Spells</div>
			<div class="edSpells">
				<div class="edSubSubHeader">Spells</div>
				<table width="100%">
					<thead><tr>
						<td class="edHeaderCell">Spellname</td>
						<td class="edHeaderCell">M<sup>1)</sup></td>
						<td class="edHeaderCell">Type</td>
						<td class="edHeaderCell">Element</td>
						<td class="edHeaderCell">Circle</td>
						<td class="edHeaderCell">T<sup>2)</sup></td>
						<td class="edHeaderCell">WD<sup>3)</sup></td>
						<td class="edHeaderCell">RD<sup>4)</sup></td>
						<td class="edHeaderCell">CD<sup>5)</sup></td>
						<td class="edHeaderCell">Range</td>
						<td class="edHeaderCell">Duration</td>
						<td class="edHeaderCell">Effect</td>
						<td class="edHeaderCell">Book</td>
					</tr></thead>
					<xsl:apply-templates select="//edc:OPENSPELL"/>
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
</xsl:template>

<xsl:template name="disciplinespells">
	<xsl:for-each select="//edc:DISCIPLINE">
		<xsl:if test="./edt:SPELL">
			<div class="edDiscipline">
				<div class="edSubHeader">Spells <xsl:value-of select="@name" /></div>
				<div class="edSpells">
					<div class="edSubSubHeader">Spells</div>
					<table width="100%">
						<thead><tr>
							<td class="edHeaderCell">Spellname</td>
							<td class="edHeaderCell">M<sup>1)</sup></td>
							<td class="edHeaderCell">Type</td>
							<td class="edHeaderCell">Element</td>
							<td class="edHeaderCell">Circle</td>
							<td class="edHeaderCell">T<sup>2)</sup></td>
							<td class="edHeaderCell">WD<sup>3)</sup></td>
							<td class="edHeaderCell">RD<sup>4)</sup></td>
							<td class="edHeaderCell">CD<sup>5)</sup></td>
							<td class="edHeaderCell">Range</td>
							<td class="edHeaderCell">Duration</td>
							<td class="edHeaderCell">Effect</td>
							<td class="edHeaderCell">Book</td>
						</tr></thead>
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

<xsl:template match="//edt:SPELL | //edc:OPENSPELL">
	<tr>
		<td class="edCapabCell" style="text-align: left;"><xsl:value-of select="@name" /></td>
		<td class="edCapabCell"><xsl:if test="@inmatrix='yes'">X</xsl:if></td>
		<td class="edCapabCell"><xsl:value-of select="@type" /></td>
		<td class="edCapabCell"><xsl:value-of select="@element" /></td>
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
		<td class="edCapabCell"><xsl:value-of select="@shortrange"/>hex (<xsl:value-of select="format-number(@shortrange*1.8288,'##.0')"/>m)</td>
		<td class="edCapabCell"><xsl:value-of select="@longrange"/>hex (<xsl:value-of select="format-number(@longrange*1.8288,'##.0')"/>m)</td>
		<td class="edCapabCell"><xsl:value-of select="@dexteritymin"/></td>
		<td class="edCapabCell"><xsl:value-of select="@strengthmin"/></td>
		<td class="edCapabCell"><xsl:value-of select="@timesforged"/></td>
		<td class="edCapabCell"><xsl:value-of select="@dateforged"/></td>
		<td class="edCapabCell"><xsl:value-of select="@location"/></td>
		<td class="edCapabCell"><xsl:value-of select="format-number(@weight,'##')"/>lb</td>
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
		</td>
		<td class="edCapabCell">
			<xsl:choose>
				<xsl:when test="@physicalarmor>0"><xsl:value-of select="@physicalarmor"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
			<xsl:if test="@timesforged_physical>0">
				(<xsl:value-of select="@timesforged_physical"/>)
			</xsl:if>
			/
			<xsl:choose>
				<xsl:when test="@mysticarmor>0"><xsl:value-of select="@mysticarmor"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
			<xsl:if test="@timesforged_mystic>0">
				(<xsl:value-of select="@timesforged_mystic"/>)
			</xsl:if>
			/
			<xsl:choose>
				<xsl:when test="@penalty>0"><xsl:value-of select="@penalty"/></xsl:when>
				<xsl:otherwise>0</xsl:otherwise>
			</xsl:choose>
		</td>
		<td class="edCapabCell"><xsl:value-of select="@edn"/></td>
		<td class="edCapabCell"><xsl:value-of select="@dateforged"/></td>
		<td class="edCapabCell"><xsl:value-of select="@location"/></td>
		<td class="edCapabCell"><xsl:value-of select="format-number(@weight,'##')"/></td>
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
		<td class="edCapabCell"><xsl:value-of select="format-number(@weight,'##')"/></td>
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
			<td class="edHeaderCell">Book</td>
		</tr></thead>
		<xsl:apply-templates select="//edc:SKILL">
			<xsl:sort select="./edt:RANK/@rank" data-type="number" order="descending"/>
			<xsl:sort select="./edt:RANK/@step" data-type="number" order="descending"/>
			<xsl:sort select="@name"/>
			<xsl:sort select="./edt:LIMITATION"/>
		</xsl:apply-templates>
	</table>
</xsl:template>

<xsl:template match="//edc:SKILL">
	<tr>
		<xsl:if test="@realigned>0">
			<xsl:attribute name="style">text-decoration:line-through;</xsl:attribute>
		</xsl:if>
		<td class="edKeyCell">
			<xsl:value-of select="@name"/>
			<xsl:if test="./edt:LIMITATION">
				<xsl:text>: </xsl:text>
				<xsl:for-each select="./edt:LIMITATION">
					<xsl:value-of select="."/>
					<xsl:if test="position() &lt; last()">, </xsl:if>
				</xsl:for-each>
			</xsl:if>
		</td>
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
		<td class="edCapabCell"><xsl:value-of select="@bookref"/></td>
	</tr>
</xsl:template>

<xsl:template name="languages">
	<div class="edSubHeader">Languages</div>
	<b>Speak:</b>&#160;
	<xsl:for-each select="//edc:LANGUAGE">
		<xsl:if test="@speak!='no'"><xsl:value-of select="@language"/> (via <xsl:value-of select="@speak"/>); </xsl:if>
	</xsl:for-each>
	<br/>
	<b>Read/Write:</b>&#160;
	<xsl:for-each select="//edc:LANGUAGE">
		<xsl:if test="@readwrite!='no'"><xsl:value-of select="@language"/> (via <xsl:value-of select="@readwrite"/>); </xsl:if>
	</xsl:for-each>
</xsl:template>

<xsl:template name="coins">
	<table class="invisible" width="100%">
		<thead><tr>
			<td class="edHeaderCell" style="text-align: right;">Purse Name</td>
			<td class="edHeaderCell">Weight</td>
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
				<xsl:attribute name="src"><xsl:value-of select="/edc:EDCHARACTER/@editorpath"/><xsl:text>icons/</xsl:text><xsl:value-of select="translate(@kind,$smallcase,$uppercase)" /><xsl:if test="not(@kind)">UNDEFINED</xsl:if><xsl:text>.png</xsl:text></xsl:attribute>
			</img>
			<xsl:value-of select="@name"/>
		</td>
		<td class="edCapabCell"><xsl:value-of select="format-number(@weight,'##')" /></td>
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
				<td class="edHeaderCell" width="44%">Name</td>
				<td class="edHeaderCell" width="8%">Weight</td>
				<td class="edHeaderCell" width="8%">Size</td>
				<td class="edHeaderCell" width="8%">Blood Damage</td>
				<td class="edHeaderCell" width="8%">Depatterning</td>
				<td class="edHeaderCell" width="8%">Location</td>
				<td class="edHeaderCell" width="8%">Used</td>
				<td class="edHeaderCell" width="8%">Book Ref</td>
			</tr>
			<xsl:apply-templates select="//edc:ITEM"> 
				<xsl:sort select="@name"/>
			</xsl:apply-templates>
		</table>
	</xsl:if>
</xsl:template>
<xsl:template match="//edc:ITEM">
	<tr>
		<td class="edItemCell" style="text-align: left;">
			<img class="edIcon">
				<xsl:attribute name="src"><xsl:value-of select="/edc:EDCHARACTER/@editorpath"/><xsl:text>icons/</xsl:text><xsl:value-of select="translate(@kind,$smallcase,$uppercase)" /><xsl:if test="not(@kind)">UNDEFINED</xsl:if><xsl:text>.png</xsl:text></xsl:attribute>
			</img>
			<xsl:value-of select="@name" />
		</td>
		<td class="edItemCell"><xsl:value-of select="format-number(@weight,'##')" />lb</td>
		<td class="edItemCell"><xsl:value-of select="@size"/></td>
		<td class="edItemCell"><xsl:value-of select="@blooddamage"/></td>
		<td class="edItemCell"><xsl:value-of select="@depatterningrate"/></td>
		<td class="edItemCell"><xsl:value-of select="@location"/></td>
		<td class="edItemCell"><xsl:value-of select="@used"/></td>
		<td class="edItemCell"><xsl:value-of select="@bookref"/></td>
	</tr>
</xsl:template>

<xsl:template name="bloodcharmItems">
	<xsl:if test="//edc:BLOODCHARMITEM">
		<div class="edSubHeader">Bloodcharm Items</div>
		<table width="100%">
			<xsl:call-template name="magicItemsTableHeader"/>
			<xsl:apply-templates select="//edc:BLOODCHARMITEM"> 
				<xsl:sort select="@name"/>
			</xsl:apply-templates>
		</table>
	</xsl:if>
</xsl:template>
<xsl:template name="magicItems">
	<xsl:if test="//edc:MAGICITEM">
		<div class="edSubHeader">Magic Items</div>
		<table width="100%">
			<xsl:call-template name="magicItemsTableHeader"/>
			<xsl:apply-templates select="//edc:MAGICITEM"> 
				<xsl:sort select="@name"/>
			</xsl:apply-templates>
		</table>
	</xsl:if>
</xsl:template>
<xsl:template name="magicItemsTableHeader">
	<thead><tr>
		<td class="edHeaderCell" width="50%">Name</td>
		<td class="edHeaderCell" width="5%">Weight</td>
		<td class="edHeaderCell" width="5%">Size</td>
		<td class="edHeaderCell" width="5%">Blood Damage</td>
		<td class="edHeaderCell" width="5%">Depatt Rate</td>
		<td class="edHeaderCell" width="5%">Spell Def</td>
		<td class="edHeaderCell" width="5%">EDN</td>
		<td class="edHeaderCell" width="5%">Location</td>
		<td class="edHeaderCell" width="5%">Used</td>
		<td class="edHeaderCell" width="5%">Book Ref</td>
	</tr></thead>
</xsl:template>
<xsl:template match="//edc:MAGICITEM | //edc:BLOODCHARMITEM">
	<tr>
		<xsl:element name="td">
			<xsl:attribute name="class">edItemCell</xsl:attribute>
			<xsl:attribute name="style">text-align: left;</xsl:attribute>
			<xsl:if test="@effect and (@effect!='')"><xsl:attribute name="rowspan">2</xsl:attribute></xsl:if>
			<img class="edIcon">
				<xsl:attribute name="src"><xsl:value-of select="/edc:EDCHARACTER/@editorpath"/><xsl:text>icons/</xsl:text><xsl:value-of select="translate(@kind,$smallcase,$uppercase)" /><xsl:if test="not(@kind)">UNDEFINED</xsl:if><xsl:text>.png</xsl:text></xsl:attribute>
			</img>
			<xsl:value-of select="@name" />
		</xsl:element>
		<td class="edItemCell"><xsl:value-of select="format-number(@weight,'##')" />lb</td>
		<td class="edItemCell"><xsl:value-of select="@size"/></td>
		<td class="edItemCell"><xsl:value-of select="@blooddamage"/></td>
		<td class="edItemCell"><xsl:value-of select="@depatterningrate"/></td>
		<td class="edItemCell"><xsl:value-of select="@spelldefense"/></td>
		<td class="edItemCell"><xsl:value-of select="@enchantingdifficultynumber"/></td>
		<td class="edItemCell"><xsl:value-of select="@location"/></td>
		<td class="edItemCell"><xsl:value-of select="@used"/></td>
		<td class="edItemCell"><xsl:value-of select="@bookref"/></td>
	</tr>
	<xsl:if test="@effect and (@effect!='')">
		<tr>
			<td class="edHeaderCell" stype="text-align: right;">Effect:</td>
			<td class="edItemCell" colspan="8" style="text-align: left;"><xsl:value-of select="@effect"/></td>
		</tr>
	</xsl:if>
</xsl:template>

<xsl:template name="patternItems">
	<xsl:if test="//edc:PATTERNITEM">
		<div class="edSubHeader">Pattern Items</div>
		<table width="100%">
			<thead><tr>
				<td class="edHeaderCell" width="40%">Name</td>
				<td class="edHeaderCell" width="5%">Weavn Thrd Rk</td>
				<td class="edHeaderCell" width="5%">Pattern Kind</td>
				<td class="edHeaderCell" width="5%">Weight</td>
				<td class="edHeaderCell" width="5%">Size</td>
				<td class="edHeaderCell" width="5%">Blood Damage</td>
				<td class="edHeaderCell" width="5%">Depatt Rate</td>
				<td class="edHeaderCell" width="5%">Spell Def</td>
				<td class="edHeaderCell" width="5%">EDN</td>
				<td class="edHeaderCell" width="5%">Location</td>
				<td class="edHeaderCell" width="5%">Used</td>
				<td class="edHeaderCell" width="5%">Book Ref</td>
			</tr></thead>
			<xsl:apply-templates select="//edc:PATTERNITEM"> 
				<xsl:sort select="@name"/>
			</xsl:apply-templates>
		</table>
	</xsl:if>
</xsl:template>
<xsl:template match="//edc:PATTERNITEM">
	<xsl:variable name="hasEffect">
		<xsl:choose>
			<xsl:when test="@effect and (@effect!='')">1</xsl:when>
			<xsl:otherwise>0</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="hasKeyknowledge">
		<xsl:choose>
			<xsl:when test="@keyknowledge and (@keyknowledge!='')">1</xsl:when>
			<xsl:otherwise>0</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<xsl:variable name="hasTruepattern">
		<xsl:choose>
			<xsl:when test="@truepattern and (@truepattern!='')">1</xsl:when>
			<xsl:otherwise>0</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
	<tr>
		<xsl:element name="td">
			<xsl:attribute name="class">edItemCell</xsl:attribute>
			<xsl:attribute name="style">text-align: left;</xsl:attribute>
			<xsl:attribute name="rowspan"><xsl:value-of select="1+$hasEffect+$hasKeyknowledge+$hasTruepattern"/></xsl:attribute>
			<img class="edIcon">
				<xsl:attribute name="src"><xsl:value-of select="/edc:EDCHARACTER/@editorpath"/><xsl:text>icons/</xsl:text><xsl:value-of select="translate(@kind,$smallcase,$uppercase)" /><xsl:if test="not(@kind)">UNDEFINED</xsl:if><xsl:text>.png</xsl:text></xsl:attribute>
			</img>
			<xsl:value-of select="@name" />
		</xsl:element>
		<td class="edItemCell"><xsl:value-of select="@weaventhreadrank"/></td>
		<td class="edItemCell"><xsl:value-of select="@patternkind"/></td>
		<td class="edItemCell"><xsl:value-of select="format-number(@weight,'##')" />lb</td>
		<td class="edItemCell"><xsl:value-of select="@size"/></td>
		<td class="edItemCell"><xsl:value-of select="@blooddamage"/></td>
		<td class="edItemCell"><xsl:value-of select="@depatterningrate"/></td>
		<td class="edItemCell"><xsl:value-of select="@spelldefense"/></td>
		<td class="edItemCell"><xsl:value-of select="@enchantingdifficultynumber"/></td>
		<td class="edItemCell"><xsl:value-of select="@location"/></td>
		<td class="edItemCell"><xsl:value-of select="@used"/></td>
		<td class="edItemCell"><xsl:value-of select="@bookref"/></td>
	</tr>
	<xsl:if test="@effect and (@effect!='')">
		<tr>
			<td class="edHeaderCell" stype="text-align: right;">Effect:</td>
			<td class="edItemCell" colspan="10" style="text-align: left;"><xsl:value-of select="@effect"/></td>
		</tr>
	</xsl:if>
	<xsl:if test="@keyknowledge and (@keyknowledge!='')">
		<tr>
			<td class="edHeaderCell" stype="text-align: right;">Keyknowledge:</td>
			<td class="edItemCell" colspan="10" style="text-align: left;"><xsl:value-of select="@keyknowledge"/></td>
		</tr>
	</xsl:if>
	<xsl:if test="@truepattern and (@truepattern!='')">
		<tr>
			<td class="edHeaderCell" stype="text-align: right;">True Pattern:</td>
			<td class="edItemCell" colspan="8" style="text-align: left;"><xsl:value-of select="@truepattern"/></td>
		</tr>
	</xsl:if>
</xsl:template>

<xsl:template name="threadItems">
	<xsl:if test="//edc:THREADITEM">
		<div class="edSubHeader">Thread Items</div>
		<xsl:apply-templates select="//edc:THREADITEM" />
	</xsl:if>
</xsl:template>

<xsl:template match="//edc:THREADITEM">
	<table class="invisible" width="100%">
		<tr>
			<td class="edThreadItemTop">
				<table class="invisible" width="100%" style="page-break-inside:avoid">
					<tr>
						<td class="edKeyCell">Name:</td>
						<td class="edValueCell" colspan="7" style="text-align: left;">
							<img class="edIcon">
								<xsl:attribute name="src"><xsl:value-of select="/edc:EDCHARACTER/@editorpath"/><xsl:text>icons/</xsl:text><xsl:value-of select="translate(@kind,$smallcase,$uppercase)" /><xsl:if test="not(@kind)">UNDEFINED</xsl:if><xsl:text>.png</xsl:text></xsl:attribute>
							</img>
							<xsl:value-of select="@name" />
						</td>
					</tr>
					<tr>
						<td class="edKeyCell">Max Threads:</td>
						<td class="edValueCell"><xsl:value-of select="@maxthreads" /></td>
						<td class="edKeyCell">Weaven Thread Rank:</td>
						<td class="edValueCell"><xsl:value-of select="@weaventhreadrank"/></td>
						<td class="edKeyCell">Max Thread Rank:</td>
						<td class="edValueCell"><xsl:value-of select="count(./edt:THREADRANK)"/></td>
						<td class="edKeyCell">Spell Defence:</td>
						<td class="edValueCell"><xsl:value-of select="@spelldefense" /></td>
					</tr>
					<tr>
						<td class="edKeyCell">Blood Damage:</td>
						<td class="edValueCell"><xsl:value-of select="@blooddamage" /></td>
						<td class="edKeyCell">Depatterning Rate:</td>
						<td class="edValueCell"><xsl:value-of select="depatterningrate" /></td>
						<td class="edKeyCell">Weight:</td>
						<td class="edValueCell"><xsl:value-of select="format-number(@weight,'##')" />lb</td>
						<td class="edKeyCell">Size:</td>
						<td class="edValueCell"><xsl:value-of select="@size" /></td>
					</tr>
					<tr>
						<td class="edKeyCell">EDN:</td>
						<td class="edValueCell"><xsl:if test="not(@enchantingdifficultynumber) or (@enchantingdifficultynumber='')">NA</xsl:if><xsl:value-of select="@enchantingdifficultynumber" /></td>
						<td class="edKeyCell">Location:</td>
						<td class="edValueCell"><xsl:value-of select="@location" /></td>
						<td class="edKeyCell">Used:</td>
						<td class="edValueCell"><xsl:value-of select="@used" /></td>
						<td class="edKeyCell">Book Ref:</td>
						<td class="edValueCell"><xsl:value-of select="bookref" /></td>
					</tr>
					<tr>
						<td class="edKeyCell">Effect:</td>
						<td class="edThreaditemdescription" colspan="7">
							<xsl:if test="@effect=''">--</xsl:if><xsl:value-of select="@effect"/>
							<xsl:if test="./edt:ARMOR">(Armor details can be found in armor section)</xsl:if>
							<xsl:if test="./edt:SHIELD">(Shield details can be found in shield section)</xsl:if>
							<xsl:if test="./edt:WEAPON">(weapon details can be found in weapon section)</xsl:if>
						</td>
					</tr>
					<tr>
						<td class="edKeyCell">Description:</td>
						<td class="edThreaditemdescription" colspan="7">
							<xsl:value-of select="./edt:DESCRIPTION"/>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td class="edThreadItemBottom">
				<table class="invisible" width="100%">
					<tr>
						<td style="text-align: center">
							Thread Ranks
						</td>
					</tr>
					<tr>
						<td>
							<table class="invisible" width="100%">
								<xsl:apply-templates select="./edt:THREADRANK"/>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</xsl:template>

<xsl:template match="//edt:THREADRANK">
	<xsl:element name="tr">
		<xsl:choose>
			<xsl:when test="position() &gt; ../@weaventhreadrank">
				<xsl:attribute name="style">color:#999999;</xsl:attribute>
			</xsl:when>
			<xsl:when test="position() &lt; ../@weaventhreadrank">
				<xsl:attribute name="style">color:#444444;</xsl:attribute>
			</xsl:when>
		</xsl:choose>
		<td class="edThreadrank" valign="middle">Rank <xsl:value-of select="position()"/> (<xsl:value-of select="@lpcost"/>LP)</td>
		<td><table width="100%">
			<xsl:if test="@keyknowledge"><tr>
				<td class="edCell">Knowledge: <xsl:value-of select="@keyknowledge"/></td>
			</tr></xsl:if>
			<xsl:if test="@deed"><tr>
				<td class="edCell">Deed: <xsl:value-of select="@deed"/></td>
			</tr></xsl:if>
			<xsl:if test="@effect"><tr>
				<td class="edCell">Effect: <xsl:value-of select="@effect"/></td>
			</tr></xsl:if>
			<xsl:if test="./edt:ARMOR"><td class="edCell">Armor: 
				phy:<xsl:value-of select="./edt:ARMOR/@physicalarmor"/>, 
				mys:<xsl:value-of select="./edt:ARMOR/@mysticarmor"/>, 
				ini:<xsl:value-of select="./edt:ARMOR/@penalty"/>
			</td></xsl:if>
			<xsl:if test="./edt:SHIELD"><td class="edCell">Shield: 
				phy:<xsl:value-of select="./edt:SHIELD/@physicalarmor"/>, 
				mys:<xsl:value-of select="./edt:SHIELD/@mysticarmor"/>, 
				ini:<xsl:value-of select="./edt:SHIELD/@penalty"/>
			</td></xsl:if>
			<xsl:if test="./edt:WEAPON"><td class="edCell">Weapon: damage step:<xsl:value-of select="./edt:WEAPON/@damagestep"/></td></xsl:if>
			<xsl:if test="./edt:WOUND"><td class="edCell">Cause Wound: 
				threshold:<xsl:value-of select="./edt:WOUND/@threshold"/>, 
				normal:<xsl:value-of select="./edt:WOUND/@normal"/>, 
				blood:<xsl:value-of select="./edt:WOUND/@blood"/>, 
				penalties:<xsl:value-of select="./edt:WOUND/@penalties"/>
			</td></xsl:if>
			<xsl:for-each select="./edt:SPELL"><tr>
				<td class="edCell">Spell: <xsl:value-of select="."/></td>
			</tr></xsl:for-each>
			<xsl:for-each select="./edt:ABILITY"><tr>
				<td class="edCell">Ability: <xsl:value-of select="."/></td>
			</tr></xsl:for-each>
			<tr><td class="edCell">
				<xsl:for-each select="./edt:DEFENSE"> Defense: 
					<xsl:choose>
						<xsl:when test="@kind=''"> physical</xsl:when>
						<xsl:when test="not(@kind)"> physical</xsl:when>
						<xsl:otherwise> <xsl:value-of select="@kind"/></xsl:otherwise>
					</xsl:choose>
					<xsl:choose>
						<xsl:when test="@bonus=''"> +1</xsl:when>
						<xsl:when test="@bonus='0'"> 0</xsl:when>
						<xsl:when test="not(@bonus)"> +1</xsl:when>
						<xsl:otherwise> +<xsl:value-of select="@bonus"/></xsl:otherwise>
					</xsl:choose>
				; </xsl:for-each>
				<xsl:for-each select="./edt:TALENT"> Talent: <xsl:value-of select="@name"/> <xsl:if test="@limitation">(<xsl:value-of select="@limitation"/>) </xsl:if>
					<xsl:choose>
						<xsl:when test="@bonus=''"> +1</xsl:when>
						<xsl:when test="@bonus='0'"> 0</xsl:when>
						<xsl:when test="not(@bonus)"> +1</xsl:when>
						<xsl:otherwise> +<xsl:value-of select="@bonus"/></xsl:otherwise>
					</xsl:choose>
				; </xsl:for-each>
				<xsl:for-each select="./edt:SPELLABILITY"> Spell Ability: <xsl:call-template name="diszipinability_type"/> </xsl:for-each>
				<xsl:for-each select="./edt:INITIATIVE"> Initiative Step: <xsl:call-template name="diszipinability_type"/>; </xsl:for-each>
				<xsl:for-each select="./edt:RECOVERYTEST"> Recovery Test: <xsl:call-template name="diszipinability_type"/>; </xsl:for-each>
				<xsl:for-each select="./edt:KARMASTEP"> Karma Step: <xsl:call-template name="diszipinability_type"/>; </xsl:for-each>
				<xsl:for-each select="./edt:MAXKARMA"> Max Karma: <xsl:call-template name="diszipinability_type"/>; </xsl:for-each>
			</td></tr>
		</table></td>
	</xsl:element>
</xsl:template>

<xsl:template name="diszipinability_type">
	<xsl:choose>
		<xsl:when test="@count=''"> +1</xsl:when>
		<xsl:when test="@count='0'"> 0</xsl:when>
		<xsl:when test="not(@count)"> +1</xsl:when>
		<xsl:otherwise> +<xsl:value-of select="@count"/></xsl:otherwise>
	</xsl:choose>
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

<xsl:template name="legendpointsreceived">
	<div class="edSubHeader">total legendpoints received history</div>
	<table class="invisible" width="100%">
		<thead>
			<tr>
				<td class="edHeaderCell">when</td>
				<td class="edHeaderCell">value</td>
				<td class="edHeaderCell">comment</td>
			</tr>
		</thead>
		<xsl:for-each select="//edc:EXPERIENCE/edt:LEGENDPOINTS">
			<xsl:if test="@type='+'"><tr>
				<td class="edCell"><xsl:value-of select="@when"/></td>
				<td class="edCell"><xsl:value-of select="@value"/></td>
				<td class="edCell"><xsl:value-of select="@comment"/></td>
			</tr></xsl:if>
		</xsl:for-each>
	</table>
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
			<xsl:apply-templates select="//edc:PORTRAIT"/>
		</div>
	</xsl:if>
</xsl:template>

<xsl:template match="//edc:PORTRAIT">
	<xsl:element name="img">
		<xsl:attribute name="class">edPortraitImages</xsl:attribute>
		<xsl:attribute name="src">
			<xsl:text>data:</xsl:text>
			<xsl:value-of select="@contenttype"/>
			;base64,
			<xsl:value-of select="."/>
		</xsl:attribute>
	</xsl:element>
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

<xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz '" />
<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ_'" />
</xsl:stylesheet>
