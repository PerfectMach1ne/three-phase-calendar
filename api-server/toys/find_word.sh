#!/bin/bash
# find_word.sh
##############
# Find all occurrences of a specified word in a Lua/Luau file,
# then print the lines where it has been found alongside
# the file name & path.

readarray -d '' array < <(find . -name "*.java" -print0)

for i in "${array[@]}"; do
	grepres=$(cat "$i" | grep "new QueryParamsFilter")
	if [[ -n "$grepres" ]]; then
		echo "[$i]: $grepres"
	fi
done

####### Copyleft LVSA 09.2024
# Terms of Use:
# 1. Be competent, especially if you're over 32.
# 2. Don't steal 1.3k$ from your close ones.
