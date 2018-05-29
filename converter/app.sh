cat inflow_src_quad.txt | while read line
do
	echo "$line = $(curl http://ipinfo.io/{$line}/org)" >> inflow_src_org.txt
done
