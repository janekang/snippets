import sys
import getopt

"""
Trying out Python to read in files using terminal.
User inputs file name through terminal.
Python 2.7.3

Jane Kang, Agent Developer Intern
"""


class Usage(Exception):
	"""Helper class for handling Exceptions.
	Type: Exception

	"""
	def __init__(self, msg):
		self.msg = msg

# Main function
def main(argv = None):
	# Set argv to default if user doesn't customize it
	if argv == None:
		argv = sys.argv

	############## Actual Functionalities #####################

	# Read in user input, open the corresponding file
	userInput = raw_input("Enter the file name: ")
	openedFile = open(userInput, 'r')
	
	print "Printing the original file content, looping line by line: "
	currentLine = openedFile.readline()
	while currentLine != "":
		print currentLine
		currentLine = openedFile.readline()

	# Replace a letter with another letter
	openedFile = open(userInput, 'r')
	currentLine = openedFile.readline()

	print "Replacing all 'i' to 'z' for printing (no edits to the file): "
	while currentLine != "":
		# Get index of char 'a'
		index = currentLine.find("i")
		while index != -1:
			currentLine = currentLine[:index] + "z" + currentLine[index+1:]
			index = currentLine.find("i")

		print currentLine
		currentLine = openedFile.readline()

	# Replace a word with another word
	openedFile = open(userInput, 'r')
	currentLine = openedFile.readline()

	print "Replacing all 'time' to 'edited' (no edits to the file): "
	while currentLine != "":
		# Index of the first char of the word
		index = currentLine.find("time")

		# Length of the word
		length = len("time")

		while index != -1:
			currentLine = currentLine[:index] + "edited" + currentLine[index+length:]
			index = currentLine.find("time")

		print currentLine
		currentLine = openedFile.readline()

	###########################################################

	# Helper try-catch to handle error messages
	try:
		try:
			opts, args = getopt.getopt(argv[1:], "h", ["help"])
		except getopt.error, msg:
			raise Usage(msg)
	except Usage, err:
		print >> sys.stderr, err.msg
		print >> sys.stderr, "for help use --help"
		return 2

# Calls to sys.exit(n) inside main => return n
if __name__ == "__main__":
	# Directly run on the terminal
	sys.exit(main())
