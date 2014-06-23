/*
 * PrimeTablesTestWithAlternativeOutput.h
 *
 * Jane Kang, Agent Developer Intern (summer 2014)
 *
 * Show how to use Google Test Listener API for alternative console output
 * Show how to use UnitTest reflection API for enumeration, inspect results
 * Use Google Test framework's testing::Test
 *
 * This example is adopted from GooleTest's sample code available and open at
 * https://code.google.com/p/googletest/source/browse/trunk/samples/prime_tables.h
 * Although I modified the code to adopt my coding style, this is strictly for
 * practicing the application of Google Test C++ framework. I claim no copyrights.
 */

#ifndef GTEST_SAMPLES_PRIME_TABLES_WALTERNATIVEOUTPUT_H_
#define GTEST_SAMPLES_PRIME_TABLES_WALTERNATIVEOUTPUT_H_

#include <stdio.h>
#include "gtest/gtest.h"

using ::testing::EmptyTestEventListener;
using ::testing::Test;
using ::testing::TestCase;
using ::testing::TestEventListeners;
using ::testing::TestInfo;
using ::testing::TestPartResult;
using ::testing::UnitTest;

// Namespace for custom messages
namespace {
	// Provide alternate output mode with min information about tests
	class AlternatePrinter : public EmptyTestEventListener {
	private:
		// Called before any of the test has started
		virtual void OnTestProgramStart(const UnitTest& /* unit_test */);

		// Called after all tests ended
		virtual void OnTestProgramEnd(const UnitTest& unit_test);

		// Called before a test starts
		virtual void OnTestStart(const TestInfo& test_info);

		// Called after a failed assertion or SUCCEED() invocation
		virtual void OnTestPartResult(const TestPartResult& test_part_result);

		// Called after a test ends
		virtual void OnTestEnd(const TestInfo& test_info);
	};

	// Alternate message testings
	TEST(CustomOutputTest, PrintsMessage) {
		printf("Printing from test body...\n\n");
	}

	TEST(CustomOutputTest, Succeeds) {
		SUCCEED() << "Succeed invoked for testing";
	}

	TEST(CustomOutputTest, Fails) {
		EXPECT_EQ(0, 1) << "Fails on purpose to display alternate failure message";
	}
}

#endif