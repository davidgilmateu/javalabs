#!/bin/bash
# This script is thought to be used on Google Photos json files. Most of them are useless, so they can be removed. This script will help you delete the redundant lines and detect the ones with no sensitive information
find . -name *.json | sed -e 's/ /\\ /g' | awk '{print "cat "$0" | sed -e \047/^  \"url\": \"\047/d | sed -e \047/^  \"title\": \"\047/d >" $0"-2 && mv "$0"-2 "$0}' > purge-json-drive.sh
chmod 775 purge-json-drive.sh
## Uncomment if you do not want to execute it manually
# Deletes redundant lines
# ./purge-json-drive.sh
# Detects empty Google-generated and purgued json files
# fdupes -r .
