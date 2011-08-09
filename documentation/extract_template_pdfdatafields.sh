#!/bin/sh

if [ -z "`which pdftk`" ]
then
  echo "You have to install pdftk to run this script!"
  exit
fi

dir=`pwd`
cd ../templates

for i in *.pdf
do
 pdftk "$i" dump_data_fields >"$dir/${i%pdf}"data_fields.txt
done
