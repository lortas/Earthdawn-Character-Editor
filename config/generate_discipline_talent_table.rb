#!/usr/bin/ruby

require 'rexml/document'
require 'csv'

alltalents={}
alldisciplines=[]
Dir["capabilities/ED4de/*.xml"].each do |filename|
  file = File.new(filename)
  doc = REXML::Document.new(file)
  doc.root.elements.each("./TALENT") do |talent|
    alltalents[talent["name"]]={properties: {attribute: talent["attribute"], action: talent["action"], strain: talent["strain"]||0, skilluse: talent["skilluse"]||0}}
  end
end

Dir["disciplines/ED4de/*.xml"].each do |filename|
  file = File.new(filename)
  doc = REXML::Document.new(file)
  disciplinename=doc.root.attributes["name"]
  alldisciplines << disciplinename
  circlenr=0
  doc.root.elements.each("./CIRCLE") do |circle|
    circlenr += 1
    circle.elements.each("./DISCIPLINETALENT|./OPTIONALTALENT|./FREETALENT") do |talent|
      talentkind=talent.name.downcase
      talentname=talent["name"]
      entry=alltalents[talentname]
      if entry == nil
        entry={}
        alltalents[talentname]=entry
      end
      entry[disciplinename]={ kind: talentkind , circle: circlenr.to_s }
    end
  end
end

alldisciplines=alldisciplines.sort.uniq

puts <<EOH
<!doctype html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
\t<head>
\t\t<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
\t\t<title>Talent Discipline Table</title>
\t\t<style type="text/css">@CHARSET "UTF-8";
.HeaderCell {
\t\ttext-align: center;
\t\tvertical-align:bottom;
\t\tborder-bottom-style: solid;
\t\tborder-bottom-width: thin;
}
.Cell {
\t\ttext-align: left;
\t\tvertical-align:bottom;
\t\tborder-style: hidden;
\t\tborder-bottom-style: solid;
\t\tborder-bottom-width: 1px;
\t\tborder-bottom-width: thin;
}
.MidCell {
text-align: center;
\t\tvertical-align: bottom;
\t\tborder-style: hidden;
\t\tborder-bottom-style: solid;
\t\tborder-bottom-width: 1px;
\t\tborder-bottom-width: thin;
\t\t</style>
\t</head>
<body>
<table width="100%">
\t<thead>
\t<tr>
\t\t<td class="HeaderCell">Talent</td>
\t\t<td class="HeaderCell">Properties<br/>attribute,action,strain,skilluse</td>
EOH

alldisciplines.each do |discipline|
  puts "\t\t<td class=\"HeaderCell\">"+discipline+"</td>"
end

puts "\t</tr>"
puts "\t</thead>"

alltalents.keys.sort.each do |talent|
  puts "\t<tr>"
  puts "\t\t<td class=\"HeaderCell\">"+talent+"</td>"
  if alltalents[talent].has_key? :properties
    puts "\t\t<td class=\"MidCell\">"+([:attribute,:action,:strain,:skilluse].map{|x| alltalents[talent][:properties][x]}.to_csv(row_sep: nil))+"</td>"
  else
    puts "\t\t<td class=\"MidCell\">-</td>"
  end
  alldisciplines.each do |discipline|
    e=alltalents[talent][discipline]
    if e == nil
      e={ kind: "-", circle: "" }
    end
    kind=e[:kind]
    if kind == nil
      kind="-"
    elsif kind == "disciplinetalent"
      kind="D"
    elsif kind == "optionaltalent"
      kind="O"
    elsif kind == "freetalent"
      kind="F"
    end
    puts "\t\t<td class=\"MidCell\">"+kind+e[:circle]+"</td>"
  end
  puts "\t</tr>"
end
puts "</table>"
puts "</body>"
puts "</html>"
