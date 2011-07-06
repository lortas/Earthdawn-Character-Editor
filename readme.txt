Installation:
============
First of all you need a java runtime environment installed, eg. from sun/oracle.

Unzip the file to a folder on your local media. It can be something like "C:\Programme\EarthdawnCharacterEditor\". Any new release can be unzip into the same folder, but you have to allow your unzip tool to overwrite exiting files. You may have changed some files in the sub folder "config", for example the file "optionalrules.xml". You may want to backup those files before installing a new release.

You now can start the application by running one of the start scripts "EarthdawnCharacterEditor.cmd" or "EarthdawnCharacterEditor.sh". Or by starting the ".jar" file directly. In the first case, the command "java" have to be with in your PATH. In the second case, files ending with ".jar" have to be linked to the "java" command.


optionalrules.xml
=================
The whole database of the EarthdawnCharacterEditor is stored into XML files within the "config"-folder. You may want to have a look into the file "optionalrules.xml". Here you can change the behaviour of the EarthdawnCharacterEditor in specific cases.


Change Log:
===========
EarthdawnCharacterEditor-0.26-20110704.zip
 - XML Schema enhanced:
    . The element "portrait" has now the attribute "contenttype"
    . New element "language", to tag which language a character can speak or read/write
    . There are talents which must not be learned by versatility. A new talent attribute have to be defined
    . The attributes threads and strain are now strings, forced by the different values defined in the books
 - Templates for Portraits enhanced.
 - Portraits will now also displayed in the html view
 - Now, two example characters have their own portraits
 - Talents learned by versatility will now get there circle based on the character current circle
 - The attribute "bookref" for talents, skills and spells will now be displayed in the Ajfel+Mordom PDF
 - Three talents marked as initiative talents
 - Talents that are marked as initiative talents will now receive the armour penalty on there final step
 - The ranks of talents that are marked as initiative talents, are also displayed in the initiative form field off all PDFs
 - GUI: The column for startrank is now in on skills and talents the same
 - Forcing, that only talents from circle <2 can have startranks
 - New example character Kartesch
 - Bookref
    . The bookref for all spells of PG added
    . The bookref for all shaman spells above circle 7 (PC) added
 - The warnings of the Java Class ECEWorker can now be send to another PrintStream than System.err
 - All talents of Cathay Players Guide added
 - Skills of Cathay Players Guide added
 - Bugs
    . Calculating SpellLP corrected
    . The form field "brief description" in the Ajfel+Mordom PDF will no be filled
    . The form fields of the Ajfel+Mordom PDF revised
