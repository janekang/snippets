ReverseStrExample
=================

Jane Kang, Agent Developer Intern (summer 2014)

C++
Google Test 1.7.0
Mac OS X Maverick


String reverse functions used for google test testing,
using Sublime Text 2, terminal


Set up library file, libgtest.a
0) get make, bash shell script, and c++ compiler on
	sudo apt-get install make
	sudo apt-get install bash
	sudo apt-get install g++
1) unzip {google test download}
2) cd to that directory
3) ./configure
4) makefile
	make
5) run all tests
	make check
6) build google test library, libgtest.a
	g++ -I. -I./include -c src/gtest-all.cc
	ar -rv libgtest.a gtest-all.o

Build sample test
1) cd to /make/
2) build sample test with makefile
	make
3) run sample test
	./sample1_unittest
	if got errors, open the source with
		sudo getit Makefile
	and replace '-lpthread' with '-pthread' at line 80

Run google tests
0) cpp and h files must be in the folder where google test is installed (for library issues)
1) compile everything with google test
	g++ -I. -I./include {cpp files} libgtest.a -lpthread -o {exe file name}
2) run exe
	./{exe file name}

Source
http://avneetkhasla.wordpress.com/2013/04/12/google-c-testing-frameworkgoogle-test-gtest/