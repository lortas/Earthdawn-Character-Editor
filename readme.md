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
## EarthdawnCharacterEditor-0.49-20150511.zip ##
+ some small enhancements and bug fixes on Ulisses pdf export
+ new pdf export: FasaGames
+ show the 'old' ed-logo on html export
+ change method for config directory traversal to read all config files
## EarthdawnCharacterEditor-0.48-20150503.zip ##
+ enhance generic pdf export
+ add Ulisses official character PDF as export target
## EarthdawnCharacterEditor-0.47-20150404.zip ##
+ ED4 enabled.
+ ED4 example characters added.
+ a lot of bugfixes.
+ config folder organisation updated.
+ config data clean up
+ character save file format enhanced for ED4.
+ new possibility to calculate movement added.
+ programm icon created
+ new item kinds and icons
+ enhance multi language support.
## older versions ##
Change Log of older versions can be found in the changelog.txt file.
