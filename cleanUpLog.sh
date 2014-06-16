#!/bin/bash
# ^ Proper header for Bash script

# Cleans up log viles in /var/log
# Run as root
if [ "$UID" -ne "$ROOT_UID"]
then
	echo "User ID must be root to run this script."
	exit $E_NOTROOT
fi

# Test whether there is a command-line argument (non-empty)
if [ -n "$1"]
then
	lines=$1
else
	lines=$LINES #Default, if not specified on command-line
fi

# Also suggested for command-line argument checks
# Uses loops
#
# E_WRONGARGS=85 # Non-numerical argument (bad argument format)
#
# case "$1" in
# ""		) lines=50;;
# *[!0-9]*) echo "Usage: `basename $0` lines-to-cleanup";
# exit $E_WRONGARGS;;
# *			) lines=$1;;
# exac

cd $LOG_DIR

# Double-check if it is in right directory before messing with log files
if [`pwd` != "$LOG_DIR"] # or if ["$PWD" != "$LOG_DIR"]
then # Not in /var/log
	echo "Can't change to $LOG_DIR."
	exit $E_XCD
fi

# Also suggested, more efficient:
#
# cd /var/log || {
# 	echo "Cannot change to necessary directory." >&2
# 	exit $E_XCD;
# }

# Save last asection of message log file
tail -n $lines messages > mesg.temp
# Rename it as system log file
mv mesg.temp messages

# cat /dev/null > messages # Not needed anymore, above is safer

cat /dev/null > wtmp # :> wtmp and > wtmp are same
echo "Log files cleaned up."

exit 0