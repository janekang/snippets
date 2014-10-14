"""
Simple tutorial for testing Python's file read and write functions
Python 2.7.3

Jane Kang, Agent Developer Intern
"""

# Create a new file, read
fileNew = open('newFile.txt', 'w')
fileNew.write("hello world!\n")
fileNew.close()

fileNew = open('newFile.txt', 'r')
print fileNew.read()

fileNew = open('newFile.txt', 'a')
fileNew.write("second time writing\n")
fileNew.write("third time writing\n")
fileNew.close()

fileNew = open('newFile.txt', 'r')
print "Printing first line: " + fileNew.readline()
print "Printing second line: " + fileNew.readline()

# readlines, writelines
fileNew = open('newFile.txt', 'r+')
print "Printing list containing multiple lines: "
print fileNew.readlines()
fileNew.writelines(["fourth\n", "fifth\n"])
fileNew.close()

# Looping
fileNew = open('newFile.txt', 'r')
print "Final:"
numLines = sum(1 for line in fileNew)
fileNew = open('newFile.txt', 'r')
for i in xrange(0, numLines):
	print fileNew.readline()


# with statement, looping
with open('newFile.txt') as fileNew:
	# read in the first line
	print "Should match with the above result:"
	currentLine = fileNew.readline()

	while currentLine != "":
		print currentLine
		currentLine = fileNew.readline()
