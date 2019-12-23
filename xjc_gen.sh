#! /bin/sh
export LANG=C
xjc -p de.earthdawn.data -b config/bindings.xml -d src config/earthdawn*.xsd config/ece*xsd  config/charsheettemplate.xsd
sed -i -e "s,// Generated on: .*,// Generated on: see version control commit date," -e "s/\s*$//" src/de/earthdawn/data/*
