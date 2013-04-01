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
## EarthdawnCharacterEditor-0.45-20121119.zip ##
+ Bugs
	+ Karma Ritual was missing in Discipline Journeyman
	+ Aligned Skill
	+ spellability for circle 2 to 5 were missing for the discipline elementalist
	+ Knack Ghost Master had wrong value in attribute "limitation"
	+ Small bugs within random character generatore removed
	+ Blood damage was not shown within the Ajfel+Mordom PDF
+ Multiple menu entries have now key short cuts
+ Added Spell Sword
+ Multiple SpinnerEditor changed to JComboBox
+ Confirmation for exit added
+ Confirmation for create new character added
+ New menu entry switch window to fullscreen
+ New menu entry for editing DefaultOptionalTalents
+ Create for every spell an empty spelldiscription, if no spell description exists
+ PatternItem enhanced
+ If an optional rule is changed, refresh character
+ New random character template scorcher added
+ Optional Rule ALIGNINGTALENTSANDSKILLS default value is "no"
+ Optional Rule AUTOINSERTLEGENDPOINTSPENT default value is "no"
+ New Optional Rule KEEPLEGENDPOINTSYNC
## older versions ##
Change Log of older versions can be found in the changelog.txt file.
