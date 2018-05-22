cat quad_src.txt | while read line
do
	echo "$line = $(curl http://ipinfo.io/{$line}/org)" >> src_org.txt
done