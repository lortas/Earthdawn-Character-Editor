@echo off

java -version > nul 2>&1
IF %ERRORLEVEL% NEQ 0 (
	echo "no java found"
	goto :end
)

for /f tokens^=2-5^ delims^=.-_^" %%j in ('java -version 2^>^&1') do (
	SET /a "JV1=%%j"
	SET /a "JV2=%%k"
	SET /a "JV3=%%l"
	SET /a "JV4=%%m"
	goto :decide
)

:decide
set JAR=EarthdawnCharacterEditor.jar
IF %JV2% LSS 11 (
	set JAR=EarthdawnCharacterEditor_java8.jar
)
java -jar %JAR% --rulesetversion ED3 --language en

:end
