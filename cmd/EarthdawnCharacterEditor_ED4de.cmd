SET JAR=EarthdawnCharacterEditor.jar

FOR /f tokens^=2-5^ delims^=.-_^" %%j IN ('java -version 2^>^&1') DO SET "JV=%%j%%k%%l%%m"
IF %ERRORLEVEL% NEQ 0 (
	echo "no java found"
	EXIT 1
)
IF %JV% LSS 110000 (
	SET JAR=EarthdawnCharacterEditor_java8.jar
)

java -jar %JAR% --rulesetversion ED4 --language de
