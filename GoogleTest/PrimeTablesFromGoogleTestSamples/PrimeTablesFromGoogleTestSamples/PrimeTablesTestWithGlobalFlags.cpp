#include "stdafx.h"

#include <iostream>
#include "PrimeTablesTestWithGlobalFlags.h"

using namespace std;

/*
 * PrimeTablesTestWithGlobalFlags.cpp
 *
 * Jane Kang, Agent Developer Intern (summer 2014)
 *
 * Show how to test relying on some global flag variables
 * Use Combine() to generate all possible combinations of flags,
 * each test given one combination as a parameter
 * Use Google Test framework's testing::Test
 *
 * This example is adopted from GooleTest's sample code available and open at
 * https://code.google.com/p/googletest/source/browse/trunk/samples/prime_tables.h
 * Although I modified the code to adopt my coding style, this is strictly for
 * practicing the application of Google Test C++ framework. I claim no copyrights.
 */

// Check whether Combine() is supported (depends on compilers)
/*#if GTEST_HAS_COMBINE

// Check for basic functionality
// Inside test body, can refer to test parameter (=table_) by GetParam()
// or save it in fixture's SetUp() or constructor and use saved copy in tests
TEST_P(PrimeTablesTestWithGlobalFlags, IsItPrimeWNonPrimes) {
	EXPECT_FALSE(table_->isItPrime(-5));
	EXPECT_FALSE(table_->isItPrime(0));
	EXPECT_FALSE(table_->isItPrime(1));
	EXPECT_FALSE(table_->isItPrime(4));
	EXPECT_FALSE(table_->isItPrime(6));
	EXPECT_FALSE(table_->isItPrime(9));
	EXPECT_FALSE(table_->isItPrime(1024));
}

TEST_P(PrimeTablesTestWithGlobalFlags, IsItPrimeWPrimes) {
	EXPECT_TRUE(table_->isItPrime(3));
	EXPECT_TRUE(table_->isItPrime(7));
	EXPECT_TRUE(table_->isItPrime(11));
	EXPECT_TRUE(table_->isItPrime(131));
}

TEST_P(PrimeTablesTestWithGlobalFlags, NextPrime) {
	EXPECT_EQ(3, table_->nextPrime(2));
	EXPECT_EQ(11, table_->nextPrime(7));
	EXPECT_EQ(47, table_->nextPrime(43));
	EXPECT_EQ(131, table_->nextPrime(128));
}

// Instantiate tests with list of test parameters
// All boolean flag variation combined with case-covering values,
// some within PrepPrimeTable bound and some outside
INSTANTIATE_TEST_CASE_P(
	TestParametersCombination,
	PrimeTablesTestWithGlobalFlags,
	Combine(Bool(), Values(1, 10)));

#endif // GTEST_HAS_COMBINE*/