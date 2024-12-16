#!/usr/bin/bash

pwd=$(pwd)
if [[ "${pwd:(-5):5}" != "/toys" ]]; then
  echo "DO NOT RUN THIS FROM /api-server !!!";
  echo "THIS IS FOR BEING RAN DIRECTLY FROM /api-server/toys !!!";
  exit 1;
fi;

# Save .properties contents to props.
props=$(cat ../app/src/main/resources/tpc_testing.properties);

# Extract the last IP from the config.
old_ip=$(awk '/^ip=[0-9.]+$/ {print $0;}' ../app/src/main/resources/tpc_testing.properties);
old_ip=${old_ip:3:${#old_ip}};

# Fetch the current local IP from current network.

new_ip=("$new_ip");
new_ip=$(ifconfig | grep inet | grep -v inet6 | grep -v 'inet 127' | grep -v 'inet 172' | awk '{$1=""; print $2}');
# new_ip=$(echo $new_ip | grep -v '172.17.0.1' | grep -v '172.18.0.1');
printf "::old_props:\n$props\n";
echo "::old_ip=$old_ip";
echo "::new_ip=$new_ip";

# Find old_ip in props and substitute it with new_ip.
echo "${props//$old_ip/$new_ip}" > ../app/src/main/resources/tpc_testing.properties;
printf "::new_props:\n$(cat ../app/src/main/resources/tpc_testing.properties)\n";
