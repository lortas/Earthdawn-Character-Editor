$Error.Clear()
$ErrorActionPreference = "SilentlyContinue"
$JAR="EarthdawnCharacterEditor.jar"
$JV=java --version

if ( $Error.Count -gt 0 ) {
    $r=[System.Windows.MessageBox]::Show("Can not find any java to run.","No Java")
    exit 1
}

$JV=(($JV[0] -replace "^[^ ]* +","") -replace " .*","") -replace "\..*",""

if( ( $Error.Count -gt 0 ) -or ( $JV -eq "" ) ) {
    $r=[System.Windows.MessageBox]::Show("Can not identifiy java version.","Unknown Java Version")
    exit 2
}

if( $JV -lt 11 ) {
    $JAR="EarthdawnCharacterEditor_java8.jar"
}

java -jar $JAR --rulesetversion ED4 --language en
