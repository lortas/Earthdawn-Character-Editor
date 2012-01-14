# Installation #
First of all you need a java runtime environment installed, eg. from sun/oracle.

Unzip the release archive file to a folder on your local media. It can be something like `C:\Program Files\EarthdawnCharacterEditor\`. Any new release can be unziped into the same folder, but you have to allow your unzip tool to overwrite exiting files. You may have changed some files in the sub folder "config", for example the file `optionalrules.xml`. You may want to backup those files before installing a new release.

Now you can start the application by running one of the start scripts `EarthdawnCharacterEditor.cmd` or `EarthdawnCharacterEditor.sh`. Or by starting the `.jar` file directly. In the first case, the command `java` have to be with in your `PATH`. In the second case, files ending with `.jar` have to be linked to the `java` command.

*For Linux only*, be aware that a zip-archive do not preserve the execute-attribute of files. So you have to execute `chmod +x EarthdawnCharacterEditor.sh`, if you want to run `EarthdawnCharacterEditor.sh` without a shell as prefix.

If your operating system ask you, if you would like to start the Earthdawn-Character-Editor within an terminal window or not, you can chose both. If you start it within an terminal you can also see some status and debug output. But this output is not needed for just using the Earthdawn-Character-Editor.


# Installation of the AddOn-Distribution #

Unzip the addon distribution archive file to the same folder on your local media, where you have already installed the release archive file before. Unzip will ask you to overwrite some files. *You need to overwrite the existing files*, because the new files contains additional information needed from the addons. Remember to reinstall the addons when you update the main distribution.

You can safely install the addon distribution while the Earthdawn-Character-Editor is running. But the changes will first become effectively after restarting the Earthdawn-Character-Editor.


# optionalrules.xml #
The whole database of the EarthdawnCharacterEditor is stored into XML files within the folder "config". You may want to have a look into the file `optionalrules.xml`. Here you can change the behaviour of the Earthdawn-Character-Editor in specific cases.


# Change Log #
## EarthdawnCharacterEditor-0.37-20120114.zip ##
+ Bugs
	+ Now, player name will also be loaded
	+ Remove function now delete the selected item in the inventor no longer the first
	+ Realign of talents should now work
+ New disciplines
	+ Sword Dancer
	+ War Rider
	+ Sorcerer
+ New spell type sorcery
+ General-Tab enhanced: Portrait is displayed and can now be changed
+ LP calculation will be detailed in the XML and can used view a Reset Spent LP button
+ For each discipline the half magic and karmaritual definition can be edit, default comes from the discipline definition
+ Halfmagic text will be print into the PDFs if there is a place for it
+ More dependencies between startrank and rank
+ LANGUAGE has the new attribute "notlearnedbyskill"
+ New tab "Languages", for editing languages and languages skills
+ Layout of almost all item types with in the inventory tab updated.
+ Default languages can now be defined for each namegiver
+ Race selection order by origin and race kind
+ Portrait and Language can be reset by changing race or gender
+ Config Syntax for GUI layout updated
+ Adding for almost all discipline half magic text templates
## EarthdawnCharacterEditor-0.36-20111220.zip ##
+ Bugs
	+ if creating a new character the file handle will also be reset. So "Save" will no longer automatically overwrite the previous saved character
	+ BloodDamage will no correct calculated
	+ Ajfel+Mordom PDF form fields updated
+ FAQ file is now formatted as HTML
+ The Tabs Attributes and Disciplines have swapped their places
+ Additional column 'Dice' within the talent tabular and the attribute tabular
+ New column width for the columns of the talent tablle
+ If a armor item have an undefined type, it will now be set to 'ARMOR'
+ Inventory Tab
	+ GUI layout of armor updated and enhanced
	+ Items flagged as 'virtual' are now longer editable
	+ GUI layout of blood charms updated and enhanced
	+ GUI layout of items updated and enhanced
	+ GUI layout of weapons updated and enhanced
	+ MAGICITEMType, THREADITEMType, TALENTABILITYType, DEFENSEABILITYType will now be displayed if part of a thread item. Editing will follow later
+ The GUI Layout of the General Tab substantial updated
+ New edit fields with in the General Tab
	+ Player name
	+ Birth (day)
	+ Description
	+ Comment
	+ Blood wound
	+ No gender
+ Race abilities will now be shown on the General Tab, including those which have already take account by the editor
+ XML Schema enhanced
	+ No extra BloodCharm Type any more. All Items can have blood damage
	+ Blood charms are now from type MagicItem
	+ The attributes of DEATHType are now longer requiered. The have now an default value
	+ HEALTH enhanced with blooddamage and depatterningrate
+ New icon for item type 'AMMUNITION'
+ Example Character Eloxis updated.
+ Some more item kinds
+ New icons for ARTISAN, POT, LIGHT, TOOL, AMMUNITION, RATION, UNDEFINED, INSTRUMENT, GRIMOIRE
## EarthdawnCharacterEditor-0.35-20111201.zip ##
+ Bugs
	+ Fixating column order of the tables skill, spell and talent
	+ optional talent spell matrix now works again for circle 1 if circle 2 is in use
	+ Now Redbrick PDF export works even if no discipline was chosen
	+ Parsing problems with weight and size field solved
	+ TalentAbility of thread items now with correct bonus
	+ Defence calculation of thread items corrected
	+ Background picture of inventory tab will no longer scale with inventory
	+ Row mismatch off the optional talents area within the Ajfel+Mordom PDF export solved
	+ DamageStep of weapons will now be recognise
+ New disciplines:
	+ Monk
	+ Samurai
	+ Scholar
+ XML Schema enhanced
	+ Attribute 'birth' added to element APPEARANCE 
+ For all columns of the tables within the GUI, the width can be configured: eceguilayout.xml
+ Main window size and sub window size can now be configured: eceguilayout.xml
+ Add more comments within the source code for optional talents procedures
+ Add RemoveLastDiscipline button within the discipline tab
+ Add Show/Hide Default Skills button within the skill tab
+ Buttons within the discipline tab are no longer transparent
+ Now all tabs have background pictures
+ New background pictures
+ New default window and columns sizes
+ All yard entries removed and replaced by hex entries
+ SpellEffekt font size within the Ajfel+Mordom PDF export re adjusted
+ Example characters updated
## EarthdawnCharacterEditor-0.34-20111026.zip ##
+ New disciplines:
	+ TraveledScholar-Spy
	+ Taildancer-Kstulaami
	+ Woodsman-Assassin
	+ Windmaster-Slasher
	+ Merchant
	+ Pugilist
+ New races from Namegivers and Thera Empire
+ More basic names for random name generator
+ XML Schema enhanced:
	+ All items have now also the attribute bookref
	+ New item-type AMMUNITION
	+ spelldef extended by the attribute element
	+ element-type extended by illusion and fear
	+ earthdawndiscipline.xsd extended by the element FOREIGNTALENTS
	+ spells can now be flagged by "learned by spell ability"
+ items.xml extended with more examples from the core books
+ spelldescriptions.xml will now be read if exists, but will not be ditributed (no copyright)
+ New pdf-export: spellcards
+ some horror powers inserted into the capabilities.xml
+ first spells are now flagged which element it uses
+ filter within the spell-tab removed (depreciated)
+ characteristic statistic layout updated
+ MainWindow will no longer start at position (100,100), it now will be placed by the window manager
+ Tuned the min/max value for column width of the spell table
## EarthdawnCharacterEditor-0.33-20110907.zip ##
+ Bugs
	+ Extra rank cost for new rank 1 talents of multi disciplines works now
+ Name database for random names enhanced
+ Capabilities do now allow a collection of limitations
+ Skills like Artisan+Craftsman+Knowledge do now have a defined list of limitations
+ Do not allow a start rank for second and later disciplines
+ AddOn distribution do now contain more talents from The Earthdawn Compendium
+ New Tool "JoinCapabilities" to merge multi capabilities XML files.
## EarthdawnCharacterEditor-0.32-20110828.zip ##
+ Random name data base increased and hyphenation added.
+ Random name generator replaced by a syllable-based random name generator.
+ Comments for rules how specific namegivers do name giving added into the random name data base.
+ More default portraits for different namegivers added.
## EarthdawnCharacterEditor-0.31-20110821.zip ##
+ Bugs
	+ Talents not allowed for versatility are no more missing on the optional talent list.
	+ To save space on the Redbrick PDF export, only woven thread ranks of thread items are shown on the overview.
+ CPG:
	+ New discipline Beastlord added.
	+ New discipline Guardian of Cathay added
	+ New discipline Daughter of Heaven added
+ bookref for disciplines from NG added.
+ Button for selecting optional talents by random added.
+ Added a second spell page for the Ajfel+Mordom PDF export.
+ On the Ajfel+Mordom PDF export use unused talent rows to show talents from additional disciplines.
+ Documentation folder added.
	+ A HOWTO for inserting an own character pdf into the ECE.
	+ A FAQ added.
+ A basic RandomNameGenerator inserted. But still under construction and need more name input.
+ A RandomCharacterGenerator inserted. But only two test-templates are available. Need more templates.
+ The maximum for attribute LP increase can now be configured by optional rules.
+ Comments for name giving (from NG) added.
## EarthdawnCharacterEditor-0.30-20110805.zip ##
+ Bugs
	+ Misspelled “Dicipline” is corrected to “Discipline” (everywhere).
	+ The RB extended pdf export works again.
	+ The Durability can again only be chosen once per discipline.
	+ The save and export dialogue will now only write a file if Yes/Ok button was clicked.
+ All knacks from the CPG inclusive bookref were inserted.
+ The optional talent chooser is now a pop-up window with scrollbar.
## EarthdawnCharacterEditor-0.29-20110729.zip ##
+ Bugs
	+ Now every character starts with karma points equal to is base karma modifier.
	+ The penalty of the natural armor was wrongly set to the physical armor. Was only relevant for obsidiman.
+ XML Schema enhanced
	+ The knacks element also need the attribute limitation, while some knacks only work for specific talents.
+ Knacks (including book references) of the book namegivers added.
+ All example characters of the Players Guide added.
+ New export plausibility added: GSON; This also creates a Json file, but for human it is much better to read.
+ Some missing icons for character items added.
+ Enhanced item library.
+ Minor changes of the namegivers configuration file.
## EarthdawnCharacterEditor-0.28-20110716.zip ##
+ Bugs
	+ Header of the CSV Export are now also comma separated.
	+ The attribute "default" will now also be inherit from the capability database.
+ XML Schema enhanced
	+ The elements "disciplinetalent", "optionaltalent" and "spell" has moved from the root-element to the "discipline"-element. Sadly, this result in incompatible save files to older versions. But there is a easy way for converting old character files, please take a look into the help forum (https://sourceforge.net/projects/ed-char-editor/forums/forum/1265052)
	+ XSD-version defined and set to "1.0"
+ CSV export for skills added
+ All CSV will now UTF-8 encoded
+ The large changes of the XML-schema need to readjust the XSL and CSS files.
+ XSL and CSS was additionally modified and enhanced to include more details (e.g. showing languages) and to better fit the print-view.
+ New export plausibility added: JSON (http://www.json.org/)
## EarthdawnCharacterEditor-0.27-20110711.zip ##
+ Bugs
	+ XML encoding for storing character should now be set correct
	+ Typing error within different discipline config files corrected
	+ The maximal circle for disciplines is now reduced to 15.
+ XML Schema enhanced:
	+ select element type for the list of available talent knacks from xs:choise to xs:sequence
+ If the language list of the character is empty default language will be inserted
+ The languages of the character are listed at the Ajfel+Mordom PDF
+ This readme in markdown-syntax added
+ All spells of Cathay Players Guide added
+ New optional rule KARMALEGENDPOINTCOST added
+ The column for LP based raising of attributes is renamed from "circle" to "LP increase"
+ The knack list of the character will now be evaluated and the knack LPs will now be calculated
+ The list for versatility talents will now only show talents until current circle and also list talents multiple time with different limitations
+ Negative values of (calculated) legend points and negative values of starti ranks will now changed to red within the characteristic overview
## EarthdawnCharacterEditor-0.26-20110704.zip ##
+ XML Schema enhanced:
	- The element "portrait" has now the attribute "contenttype"
	- New element "language", to tag which language a character can speak or read/write
	- There are talents which must not be learned by versatility. A new talent attribute have to be defined
	- The attributes threads and strain are now strings, forced by the different values defined in the books
+ Templates for Portraits enhanced.
+ Portraits will now also displayed in the html view
+ Now, two example characters have their own portraits
+ Talents learned by versatility will now get there circle based on the character current circle
+ The attribute "bookref" for talents, skills and spells will now be displayed in the Ajfel+Mordom PDF
+ Three talents marked as initiative talents
+ Talents that are marked as initiative talents will now receive the armour penalty on there final step
+ The ranks of talents that are marked as initiative talents, are also displayed in the initiative form field off all PDFs
+ GUI: The column for startrank is now in on skills and talents the same
+ Forcing, that only talents from circle <2 can have startranks
+ New example character Kartesch
+ Bookref
	- The bookref for all spells of PG added
	- The bookref for all shaman spells above circle 7 (PC) added
+ The warnings of the Java Class ECEWorker can now be send to another PrintStream than System.err
+ All talents of Cathay Players Guide added
+ Skills of Cathay Players Guide added
+ Bugs
	- Calculating SpellLP corrected
	- The form field "brief description" in the Ajfel+Mordom PDF will no be filled
	- The form fields of the Ajfel+Mordom PDF revised
