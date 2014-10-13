#
# Create simple selection of actions
# User selects which action to perform
#
# Jane Kang, Agent Developer Intern, FireEye Mandiant
#
while :
	do
		clear
		echo "------------------------"
		echo " Selections "
		echo "------------------------"
		echo "1. Show today's date and time"
		echo "2. Show files in current directory"
		echo "3. Show calendar"
		echo "4. Start vim"
		echo "5. exit/stop"
		echo "------------------------"
		echo -n "Enter your choice [1-5]: "
		read decision
		case $decision in
			1) echo "Today is `date`, press a key to return" ; read ;;
			2) echo "Files in `pwd`" ; ls -l; echo "press a key to return"; read ;;
			3) cal ; echo "press a key to return" ; read ;;
			4) vim ;;
			5) exit 0 ;;
			*) echo "Please select one of the choices";
			   echo "press a key to return" ; read ;;
		esac
	done
