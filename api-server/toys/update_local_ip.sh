#!/bin/bash

echo "DO NOT RUN THIS FROM /api-server !!!";
echo "THIS IS FOR BEING RAN DIRECTLY FROM /api-server/toys !!!"

# Save .properties contents to props.
props=$(cat ../app/src/main/resources/tpc_testing.properties);

# Extract the last IP from the config.
old_ip=$(awk '/^ip=[0-9.]+$/ {print $0;}' ../app/src/main/resources/tpc_testing.properties);
old_ip=${old_ip:3:${#old_ip}}

# Fetch the current local IP from current network.
new_ip=$(ifconfig | grep inet | grep -v inet6 | grep -v 'inet 127' | awk '{$1=""; print $2}');
new_ip=("$new_ip");

# TODO: find old_ip in props and substitute it with new_ip.

printf "::props:\n$props\n"
echo "::old_ip=$old_ip"
echo "::new_ip=$new_ip"
# param expansion version (works)
printf "::new_props:\n${props//$old_ip/$new_ip}\n"
# sed version (works but without newlines??)
# printf "::new_props:\n$(echo $props | sed "s/$old_ip/$new_ip/g")\n"
echo "${props//$old_ip/$new_ip}" > ../app/src/main/resources/tpc_testing.properties
printf "::new_props_in_file:\n$(cat ../app/src/main/resources/tpc_testing.properties)\n"
