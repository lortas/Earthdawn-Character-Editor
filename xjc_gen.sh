#! /bin/sh
xjc -p de.earthdawn.data -b config/bindings.xml -d src config/earthdawn*.xsd  
sed -i -e "s,// Generated on: .*,// Generated on: see version control commit date," src/de/earthdawn/data/*
