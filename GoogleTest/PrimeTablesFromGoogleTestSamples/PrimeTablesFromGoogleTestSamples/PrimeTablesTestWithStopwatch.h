/*
 * PrimeTablesTestWithStopwatch.h
 *
 * Jane Kang, Agent Developer Intern (summer 2014)
 *
 * Put timing component into the tests, all tests must be done within 10 sec
 * Use Google Test framework's testing::Test
 *
 * This example is adopted from GooleTest's sample code available and open at
 * https://code.google.com/p/googletest/source/browse/trunk/samples/prime_tables.h
 * Although I modified the code to adopt my coding style, this is strictly for
 * practicing the application of Google Test C++ framework. I claim no copyrights.
 */

#ifndef GTEST_SAMPLES_PRIME_TABLES_WSTOPWATCH_H_
#define GTEST_SAMPLES_PRIME_TABLES_WSTOPWATCH_H_

#include <limits.h>
#include <time.h>
#include "PrimeTables.h"
#include "gtest/gtest.h"

// Class for measuring elapsed time
class StopwatchTest : public testing::Test {
protected:
	// Variables
	int timelimit;
	time_t start_time_; // UTC time in sec when test starts

	// Called before each test run, record start time
	virtual void SetUp() {
		timelimit = 10;
		start_time_ = time(NULL);
	}

	// Called after each test run, record end time, check elapsed time
	virtual void TearDown() {
		const time_t end_time = time(NULL);
		EXPECT_TRUE(end_time - start_time_ <= timelimit) << "The test took longer than " << timelimit << " sec";
	}

	// Change test run timelimit
	virtual void ChangeTimeLimit(int newLimit) {
		EXPECT_TRUE(newLimit >= 0) << "Negitive time limit imposed";
		timelimit = newLimit;
	}
};

// Class for testing
class PrimeTablesTestWithStopwatch : public StopwatchTest {
protected:
	// Variables
	int max;
	NoPrepPrimeTable* notPrepped;
	PrepPrimeTable* prepped;

	// Called before each test run
	virtual void SetUp() {
		super::SetUp();

		max = 100;
		notPrepped = new NoPrepPrimeTable();
		prepped = new PrepPrimeTable(max);
	}

	// Called after each test run, for clean up
	virtual void TearDown() {
		super::TearDown();

		delete[] prepped;
	}

	// Helper function
	// Change max limit on prime num
	virtual void ChangeMaxLimit(int newLimit) {
		max = newLimit;

		delete[] prepped;
		prepped = new PrepPrimeTable(max);
	}

private:
	// No 'super' keyword in C++, so made one
	typedef StopwatchTest super;
};

#endif