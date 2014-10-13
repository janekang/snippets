ReverseStrExample
=================

Jane Kang, Agent Developer Intern (summer 2014)

C++
Google Test 1.7.0
Microsoft Visual Studio 2008 (administrator)
VMware with Windows 7


String reverse functions used for google test testing,
using Microsoft Visual Studio 2008 (Administrator)


Settings Steps
0) unzip google test download/source code, disable read-only on all /include/ files
0.25) build debug and release solutions of /msvc/gtest.sln, /msvc/gtest-md.sln
0.5) project should be of template visual c++ > win32 > win32 console application
1) right-click on the project (named solely by the name you chose)
2) go to properties
	change config to debug
3) config properties > c/c++ > general > additional include dir
	add path to /include
4) config properties > c/c++ > code generation > runtime library
 	check whether it is DLL or not
5) config properties > linker > general > additional library dir
 	if DLL, add /msvc/gtest-md/debug
 	else add /msvc/gtest/debug
6) config properties > linker > input > additional dependencies
	add gtestd.lib
 
 Files Steps
 1) add static headers to stdafx.h
 2) #include "stdafx.h" on all cpp files (else error by visual studio)
 3) #include <iostream> for console on where main() is
 4) #include "gtest/gtest.h" on where main() is
 5) start debugging
 
 Source
 http://stackoverflow.com/questions/531941/how-to-setup-google-c-testing-framework-gtest-on-visual-studio-2005
