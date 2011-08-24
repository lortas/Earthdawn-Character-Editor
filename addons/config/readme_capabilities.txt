# creating a diff file wich can be read by ed to reconstruct new file by editing the original file
diff -ebB ../../config/capabilities.xml capabilities.xml >  capabilities.xml.ed_diff 
echo w >> capabilities.xml.ed_diff

# Using ed with the diff-input-file for reconstructing the new file by editing the original file
cp ../../config/capabilities.xml .
ed capabilities.xml < capabilities.xml.ed_diff
