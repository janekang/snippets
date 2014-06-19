// reverseStrExample.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include <iostream>
#include "gtest/gtest.h"

/*
 * String reverse functions used for google test testing,
 * using Microsoft Visual Studio 2008 (Administrator)
 *
 * Jane Kang, Agent Developer Intern 
 *
 * Settings Steps
 * 0) unzip google test download, disable read-only on all /include/ files
 * 0.25) build debug and release solutions of /msvc/gtest.sln, /msvc/gtest-md.sln
 * 0.5) project should be of template visual c++ > win32 > win32 console application
 * 1) right-click on the project (named solely by the name you chose)
 * 2) go to properties
 *		change config to debug
 * 3) config properties > c/c++ > general > additional include dir
 *		add path to /include
 * 4) config properties > c/c++ > code generation > runtime library
 *		check whether it is DLL or not
 * 5) config properties > linker > general > additional library dir
 *		if DLL, add /msvc/gtest-md/debug
 *		else add /msvc/gtest/debug
 * 6) config properties > linker > input > additional dependencies
 *		add gtestd.lib
 * 
 * Files Steps
 * 1) add static headers to stdafx.h
 * 2) #include "stdafx.h" on all cpp files (else error by visual studio)
 * 3) #include <iostream> for console on where main() is
 * 4) #include "gtest/gtest.h" on where main() is
 * 5) start debugging
 *
 * http://stackoverflow.com/questions/531941/how-to-setup-google-c-testing-framework-gtest-on-visual-studio-2005
 */
TEST (ReverseStrTest, CheckTestOperation) {
	EXPECT_EQ (1, 1);
}

TEST (ReverseStrTest, StrLengthOdd) {
	ASSERT_EQ ("a", reverseStr("a")) << "Failed to reverse length 1 string";
	EXPECT_EQ ("edcba", reverseStr("abcde"));

	ASSERT_EQ ("a", reverseInPlace("a")) << "Failed to reverse length 1 string";
	EXPECT_EQ ("987654321", reverseInPlace("123456789"));
}

TEST (ReverseStrTest, StrLengthEven) {
	ASSERT_EQ ("", reverseStr("")) << "Failed to reverse empty string";
	EXPECT_EQ ("abcabc", reverseStr("cbacba"));

	ASSERT_EQ ("", reverseInPlace("")) << "Failed to reverse empty string";
	EXPECT_EQ ("9876543210", reverseInPlace("0123456789"));
}

int _tmain(int argc, _TCHAR* argv[])
{
	::testing::InitGoogleTest(&argc, argv);
	RUN_ALL_TESTS();

	// Keep console open until return keystroke
	std::getchar();
	//return 0;
}

