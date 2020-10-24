#!/bin/sh

JAR=EarthdawnCharacterEditor.jar

JV=`java --version`
if test $? -ne 0
then
	notify-send "Can not find any java to run."
	exit 1
fi

JV=`echo $JV | head -1 | sed -e "s/^[^ ]* \+//" -e "s/ .*//" -e "s/\..*//"`
if test -z "$JV"
then
	notify-send "Can not identifiy java version."
	exit 2
fi

if test "$JV" -lt 11
then
	JAR=EarthdawnCharacterEditor_java8.jar
fi

java -jar "${JAR}" --rulesetversion ED3 --language en
