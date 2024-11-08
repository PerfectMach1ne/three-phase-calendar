#!/bin/bash
# WIP toy

echo "DO NOT RUN THIS FROM /api-server !!!";
echo "THIS IS FOR BEING RAN DIRECTLY FROM /api-server/toys !!!"
props=$(cat ../app/src/main/resources/tpc_testing.properties);
old_ip=$(awk '/^ip=[0-9.]+$/ {print $0;}' ../app/src/main/resources/tpc_testing.properties);

new_ip=$(ifconfig | grep inet | grep -v inet6 | grep -v 'inet 127' | awk '{$1=""; print $2}');
new_ip=("ip=$new_ip");
echo $old_ip
echo $new_ip
echo $props