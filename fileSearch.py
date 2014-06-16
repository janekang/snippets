import os, os.path
import sys

"""
Use Python script to search for a file in a directory,
reaching down to all subdirectories.
Default set to return the first file containing the given string,
-a for iterating through all files.

Jane Kang, Agent Developer Intern
"""


class InputError(Exception):
    """Exception raised for errors in the input.

    Attributes:
        expr -- input expression in which the error occurred
        msg  -- explanation of the error
    """

    def __init__(self, expr, msg):
        self.expr = expr
        self.msg = msg


# Find the file with the user's content
# @param searchTermIncluded - dict for file names and paths
# @param aFlag - flag for iterating all files (not return at first valid file)
# @return dict of file names and paths
def findFile(searchTermIncluded, aFlag):
	pathInput = raw_input("What is the full path of the directory where the Event file is at?\n")
	path = os.path.expanduser(pathInput)

	searchTerm = raw_input("What is the text you want to search for?\n")

	try:
		lineCount = int(raw_input("How many lines do you want to search for? -1 for entire file\n"))
	except Exception, e:
		print("You did not type in a number. Please restart the program.\n")

	# Iterate through all directories
	for dirName, subdirList, fileList in os.walk(path):

		# Iterate through all files
		for aFile in fileList:
			try:
				openFile = open(dirName+"/"+aFile, 'r')
				currLine = openFile.readline()
				currLineNum = 0;
				withinCount = True;

				# Iterate through file lines
				while currLine != "" and withinCount:
					index = currLine.find(searchTerm)
					currLine = openFile.readline()
					currLineNum += 1;
					withinCount = currLineNum < lineCount or lineCount == -1

					# When search term is found, save its name and path
					if index != -1:
						searchTermIncluded[aFile] = dirName
						break

				openFile.close()
				
				pass
			except Exception, e: # Unreadable files, do nothing
				pass

			# Flag checking
			if aFlag != "-a" and len(searchTermIncluded) == 1:
				break

		# Flag checking
		if aFlag != "-a" and len(searchTermIncluded) == 1:
			break

	# Printing file names and paths
	if len(searchTermIncluded) > 0:
		print "Following files contains the term '" + searchTerm + "':"

		fileNames = searchTermIncluded.keys()
		for n in fileNames:
			print " " *4 + "file: %s\n" % n + " "*8 + "path: %s" % searchTermIncluded[n]

	else:
		raise InputError


# Main function
# @param argv: sys module, provide access to command line arguments
def main(argv = None):
	if argv == None:
		argv = sys.argv
	
	# Get the flag
	aFlag = ""
	if argv == sys.argv and len(argv) == 2:
		aFlag = argv[1]
		if aFlag == "-a": print "Flag set to check all files"

	# Create dict to save file names and paths
	searchTermIncluded = dict()

	try:
		findFile(searchTermIncluded, aFlag)
	except InputError, e:
		print "No file contains the given search term\n"

if __name__ == "__main__":
	sys.exit(main())