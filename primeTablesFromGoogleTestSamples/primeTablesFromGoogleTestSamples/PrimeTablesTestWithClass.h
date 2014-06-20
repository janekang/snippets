/*
 * PrimeTablesTestWithClass.cpp
 *
 * Jane Kang, Agent Developer Intern (summer 2014)
 *
 * Use Google Test framework's testing::Test
 *
 * This example is adopted from GooleTest's sample code available and open at
 * https://code.google.com/p/googletest/source/browse/trunk/samples/prime_tables.h
 * Although I modified the code to adopt my coding style, this is strictly for
 * practicing the application of Google Test C++ framework. I claim no copyrights.
 */

#ifndef GTEST_SAMPLES_PRIME_TABLES_WCLASS_H_
#define GTEST_SAMPLES_PRIME_TABLES_WCLASS_H_

#include <iostream>
#include "PrimeTables.h"
#include "gtest/gtest.h"

class PrimeTablesTestWithClass : public testing::Test {
protected:
	// Variables
	int max;
	NoPrepPrimeTable* notPrepped;
	PrepPrimeTable* prepped;

	// Called before each test run
	virtual void SetUp() {
		max = 100;
		notPrepped = new NoPrepPrimeTable();
		prepped = new PrepPrimeTable(max);
	}

	// Called after each test run, for clean up
	virtual void TearDown() {
		delete[] prepped;
	}

	// Helper function
	// Change max limit on prime num
	virtual void ChangeMaxLimit(int newLimit) {
		max = newLimit;

		delete[] prepped;
		prepped = new PrepPrimeTable(max);
	}
};

#endif