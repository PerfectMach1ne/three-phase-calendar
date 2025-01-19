#!/bin/sh

# Save .properties contents to props.
props=$(cat ./app/src/main/resources/tpc_testing.properties);

# Extract the last IP from the config.
old_ip=$(awk '/^ip=[0-9.]+$/ {print $0;}' ./app/src/main/resources/tpc_testing.properties);
old_ip=${old_ip:3:${#old_ip}};
old_pgurl_ip=$(awk -F'[://]' '/^url=/ {print $5;}' ./app/src/main/resources/tpc_testing.properties);

# Fetch the current local IP from current network and identify db container IP
# (by looking up its Docker Compose service's NS name).
new_ip=$(ifconfig | grep inet | grep -v inet6 | grep -v 'inet 127' | grep -v "addr:127" | grep addr: | awk '{gsub("addr:", ""); $1=""; print $2}');
new_pgurl_ip="$(getent hosts db | awk '{ print $1 }')";

printf "::old_props:\n$props\n";
echo "::old_ip=$old_ip";
echo "::new_ip=$new_ip";
echo "::old_pgurl_ip=$old_pgurl_ip";
echo "::new_pgurl_ip=$new_pgurl_ip";

# Find old IPs in props and substitute them with new IPs, while preventing conflicts.
echo "${props//$old_ip/$new_ip}" > ./app/src/main/resources/tpc_testing.properties;
props=$(cat ./app/src/main/resources/tpc_testing.properties); # Refresh props variable.
echo "${props//$old_pgurl_ip:5432/$new_pgurl_ip:5432}" > ./app/src/main/resources/tpc_testing.properties;
printf "::new_props:\n$(cat ./app/src/main/resources/tpc_testing.properties)\n";

./gradlew run