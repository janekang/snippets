// GoogleTestLeakChecker.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"

#include <iostream>
#include "GoogleTestLeakChecker.h"

using namespace std;
using ::testing::InitGoogleTest;

/*
 * GoogleTestLeakChecker.h
 *
 * Jane Kang, Agent Developer Intern (summer 2014)
 *
 * Show how to use Google Test listener API to implement primitive leak checker
 * Use Google Test framework's testing::Test
 *
 * Edit command line arguments:
 *		project properties > debugging > command arguments
 *
 * This example is adopted from GooleTest's sample code available and open at
 * https://code.google.com/p/googletest/source/browse/trunk/samples/prime_tables.h
 * Although I modified the code to adopt my coding style, this is strictly for
 * practicing the application of Google Test C++ framework. I claim no copyrights.
 */

int main(int argc, char **argv)
{
	InitGoogleTest(&argc, argv);

	// check flag
	bool check_for_leaks = false;
	if (argc > 1 && strcmp(argv[1], "--check_for_leaks") == 0) {
		fprintf(stdout, "--check_for_leaks flag activated\nexpect test fail\n\n");
		fflush(stdout);
		check_for_leaks = true;
	}
	else {
		printf("Run this program with --check_for_leaks to enable custom leak checking in the tests\n\n");
	}

	// if flag activated, install leak checker
	if (check_for_leaks) {
		TestEventListeners& listeners = UnitTest::GetInstance()-> listeners();

		// add leak checker to the end of listener list,
		// after default text output and default xml report generator
		// deleting done by Google Test
		// ORDER MATTERS:
		//	ensure failures gen by OnTestEnd() processed by text, xml printers
		//		attributed to right test
		//	listener receives OnXyzStart event after preceding listeners received that event,
		//		and receives OnXyzEnd event before preceding listeners receive that event
		listeners.Append(new LeakChecker);
	}
	RUN_ALL_TESTS();

	std::getchar();
	return 0;
}

