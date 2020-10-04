@echo off

java -version > nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
	msg * "Can not find any java to run."
	goto :end
)

for /f tokens^=2-5^ delims^=.-_^" %%j in ('java -version 2^>^&1') do (
	SET "JV1=%%j"
	SET "JV2=%%k"
	SET "JV3=%%l"
	SET "JV4=%%m"
	goto :decide
)

:decide
IF "%JV1%" == "" (
	msg * "Can not identifiy java version."
	goto :end
)
SET /a "JV1=%JV1%"
SET JAR=EarthdawnCharacterEditor.jar
IF %JV1% LSS 11 (
	SET JAR=EarthdawnCharacterEditor_java8.jar
)
java -jar %JAR% --rulesetversion ED4 --language de

:end
