# Installation #
First of all you need a java runtime environment installed, eg. from sun/oracle.

Unzip the release file to a folder on your local media. It can be something like `C:\Program Files\EarthdawnCharacterEditor\`. Any new release can be unziped into the same folder, but you have to allow your unzip tool to overwrite exiting files. You may have changed some files in the sub folder "config", for example the file `optionalrules.xml`. You may want to backup those files before installing a new release.

You now can start the application by running one of the start scripts `EarthdawnCharacterEditor.cmd` or `EarthdawnCharacterEditor.sh`. Or by starting the `.jar` file directly. In the first case, the command `java` have to be with in your `PATH`. In the second case, files ending with `.jar` have to be linked to the `java` command.

If your operating system ask you, if you would like to start the EarthdawnCharacterEditor within an terminal window or not, you can chose both. If you start it within an terminal you can also see some status and debug output. But this output is not needed for just using the EarthdawnCharacterEditor.


# optionalrules.xml #
The whole database of the EarthdawnCharacterEditor is stored into XML files within the folder "config". You may want to have a look into the file `optionalrules.xml`. Here you can change the behaviour of the EarthdawnCharacterEditor in specific cases.


# Change Log #
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
