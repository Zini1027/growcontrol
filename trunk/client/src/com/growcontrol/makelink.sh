#!/bin/sh

echo

if [ -L "gcCommon" ]; then
	echo "gcCommon symlink already exists"
	echo
	exit 1
fi

if [ -d "gcCommon" ]; then
	echo "gcCommon dir exists!"
	echo
	exit 1
fi

ln -s ../../../../gcCommon gcCommon

ls -lah
echo

