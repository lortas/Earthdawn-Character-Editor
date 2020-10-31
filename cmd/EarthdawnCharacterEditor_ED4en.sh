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
	notify-send "Please use a Java version of at least 11."
	exit 3
fi

java -jar "${JAR}" --rulesetversion ED4 --language en
