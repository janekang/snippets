#
# Script to print user information who is currently logged in.
# Prints out current date and time.
#
# Jane Kang, Agent Developer Intern, FireEye Mandiant
#
clear
echo "Hello $USER"
echo "Today is `date`"
echo "Number of user login: " ; who | wc -l
echo "Calendar"
cal
exit 0
