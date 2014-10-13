#include "stdafx.h"

#include <iostream>
#include "PrimeTablesTestOfValueParam.h"

using namespace std;

/*
 * PrimeTablesTestOfValueParam.cpp
 *
 * Jane Kang, Agent Developer Intern (summer 2014)
 *
 * Show how to test common properties of mutliple implementations of same interface
 * (aka interface tests) using value-parameterized tests
 * Each test in the test case has a parameter that is an interface pointer to the implementation tested
 * Use Google Test framework's testing::Test
 *
 * This example is adopted from GooleTest's sample code available and open at
 * https://code.google.com/p/googletest/source/browse/trunk/samples/prime_tables.h
 * Although I modified the code to adopt my coding style, this is strictly for
 * practicing the application of Google Test C++ framework. I claim no copyrights.
 */

// Check whether value-parameterized test is supported (depends on compilers)
#if GTEST_HAS_PARAM_TEST

using ::testing::TestWithParam;
using ::testing::Values;

// Check for basic functionality
// Each test repeated for each instanced value used as test parameters
TEST_P(PrimeTablesTestOfValueParam, IsItPrimeWNonPrimes) {
	EXPECT_FALSE(table_->isItPrime(-5));
	EXPECT_FALSE(table_->isItPrime(0));
	EXPECT_FALSE(table_->isItPrime(1));
	EXPECT_FALSE(table_->isItPrime(4));
	EXPECT_FALSE(table_->isItPrime(6));
	EXPECT_FALSE(table_->isItPrime(9));
	EXPECT_FALSE(table_->isItPrime(1024));
}

TEST_P(PrimeTablesTestOfValueParam, IsItPrimeWPrimes) {
	EXPECT_TRUE(table_->isItPrime(3));
	EXPECT_TRUE(table_->isItPrime(7));
	EXPECT_TRUE(table_->isItPrime(11));
	EXPECT_TRUE(table_->isItPrime(131));
}

TEST_P(PrimeTablesTestOfValueParam, NextPrime) {
	EXPECT_EQ(3, table_->nextPrime(2));
	EXPECT_EQ(11, table_->nextPrime(7));
	EXPECT_EQ(47, table_->nextPrime(43));
	EXPECT_EQ(131, table_->nextPrime(128));
}

// Instance test parameters
INSTANTIATE_TEST_CASE_P(
	PrimeTablesTestOfValueParamInstance,
	PrimeTablesTestOfValueParam,
	Values(&CreateNoPrepPrimeTable, &CreatePrepPrimeTable<1000>));

#endif // GTEST_HAS_PARAM_TEST