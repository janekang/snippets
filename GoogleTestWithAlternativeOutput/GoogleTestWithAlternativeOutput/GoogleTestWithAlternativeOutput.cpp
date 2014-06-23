#include "stdafx.h"

#include <iostream>
#include "GoogleTestWithAlternativeOutput.h"

using namespace std;
using ::testing::InitGoogleTest;

/*
 * PrimeTablesTestWithAlternativeOutput.h
 *
 * Jane Kang, Agent Developer Intern (summer 2014)
 *
 * Show how to use Google Test Listener API for alternative console output
 * Show how to use UnitTest reflection API for enumeration, inspect results
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

/// Class AlternatePrinter method implementations for customized printing ////////

// Called before any of the test has started
void AlternatePrinter::OnTestProgramStart(const UnitTest& /* unit_test */) {
	fprintf(stdout, "TEST Program Starting\n\n");
	fflush(stdout);
}

// Called after all tests ended
void AlternatePrinter::OnTestProgramEnd(const UnitTest& unit_test) {
	fprintf(stdout, "TEST %s\n\n", unit_test.Passed() ? "PASSED" : "FAILED");
	fflush(stdout);
}

// Called before a test starts
void AlternatePrinter::OnTestStart(const TestInfo& test_info) {
	fprintf(stdout, "*** TEST %s.%s starting.\n", test_info.test_case_name(), test_info.name());
	fflush(stdout);
}

// Called after a failed assertion or SUCCEED() invocation
void AlternatePrinter::OnTestPartResult(const TestPartResult& test_part_result) {
	fprintf(stdout, "%s in %s:%d\n%s\n\n",
		test_part_result.failed() ? "*** Failure" : "Success", test_part_result.file_name(),
		test_part_result.line_number(), test_part_result.summary());
	fflush(stdout);
}

// Called after a test ends
void AlternatePrinter::OnTestEnd(const TestInfo& test_info) {
	fprintf(stdout, "*** TEST %s.%s ending.\n", test_info.test_case_name(), test_info.name());
	fflush(stdout);
}


/// main ///////////////////////////////////////////////////////////////////////////////
int main(int argc, char **argv) {
	InitGoogleTest(&argc, argv);

	// check output type
	bool altOutput = false;
	if (argc > 1 && strcmp(argv[1], "--alt_output") == 0) {
		fprintf(stdout, "--alt_output flag activated\nresult printing customized\n\n");
		fflush(stdout);
		altOutput = true;
	}
	else {
		printf("Run this program with --alt_output to change the way it prints its output\n\n");
	}


	UnitTest& unit_test = *UnitTest::GetInstance();

	// if given --alt_output flag, suppress standard output, attach own result printer
	if (altOutput) {
		TestEventListeners& listeners = unit_test.listeners();

		// delete default console output listener from the list
		// operation transfers ownership of listener, must delete it as well
		delete listeners.Release(listeners.default_result_printer());

		// add custom output listener
		// Google Test assumes ownership after adding to list
		listeners.Append(new AlternatePrinter);
	}
	int ret_val = RUN_ALL_TESTS();

	// use UnitTest reflection API to inspect test results
	// discount failures from the tests expected to fail
	int unexpectedFailedTests = 0;
	for (int i = 0; i < unit_test.total_test_case_count(); ++i) {
		const TestCase& test_case = *unit_test.GetTestCase(i);

		for (int j = 0; j < test_case.total_test_count(); ++j) {
			const TestInfo& test_info = *test_case.GetTestInfo(j);

			// count failed test that did not meant to fail (no 'Fails' in name)
			if (test_info.result()->Failed() && strcmp(test_info.name(), "Fails") != 0) {
				unexpectedFailedTests++;
			}
		}
	}

	// tests meant to fail should not affect test program outcome
	if (unexpectedFailedTests == 0) {
		ret_val = 0;
	}

	std::getchar();

	return ret_val;
}