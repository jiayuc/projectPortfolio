svn log --verbose --xml https://subversion.ews.illinois.edu/svn/sp17-cs242/jchen267  >  data/svn_log.xml
svn list --xml --recursive https://subversion.ews.illinois.edu/svn/sp17-cs242/jchen267 > data/svn_list.xml

# svn log --verbose --xml https://subversion.ews.illinois.edu/svn/sp17-cs242/jchen267/Assignment0/src > ./data/svn_log_test.xml
# svn list --xml --recursive https://subversion.ews.illinois.edu/svn/sp17-cs242/jchen267/Assignment0/src > ./data/svn_list_test.xml
