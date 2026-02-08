#!/usr/bin/bash
# find_word.sh
##############

readarray -d '' array < <(find . -name "*.java" -print0)

for i in "${array[@]}"; do
	grepres=$(cat "$i" | grep "new QueryParamsFilter")
	if [[ -n "$grepres" ]]; then
		echo "[$i]: $grepres"
	fi
done

####### Copyleft LVSA 09.2024
