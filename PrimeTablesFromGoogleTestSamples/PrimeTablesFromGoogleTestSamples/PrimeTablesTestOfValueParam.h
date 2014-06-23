/*
 * PrimeTablesTestOfValueParam.h
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

#ifndef GTEST_SAMPLES_PRIME_TABLES_OVALUEPARAM_H_
#define GTEST_SAMPLES_PRIME_TABLES_OVALUEPARAM_H_

#include "PrimeTables.h"
#include "gtest/gtest.h"

// Check whether value-parameterized test is supported (depends on compilers)
#if GTEST_HAS_PARAM_TEST

// Factory functions for creating instances of implementations
using ::testing::TestWithParam;
using ::testing::Values;

typedef PrimeTable* CreatePrimeTableFunc();

PrimeTable* CreateNoPrepPrimeTable() {
	return new NoPrepPrimeTable();
}

template <size_t max_prepped>
PrimeTable* CreatePrepPrimeTable() {
	return new PrepPrimeTable(max_prepped);
}

// Testing class
// @param factory function, called in SetUp() to create instance to test
class PrimeTablesTestOfValueParam : public TestWithParam<CreatePrimeTableFunc*> {
public:
	// Deconstructor
	virtual ~PrimeTablesTestOfValueParam() {
		delete table_;
	}

	// Called before each test case run
	virtual void SetUp() {
		table_ = (*GetParam())();
	}

	// Called after each test case run
	// Delete used prime table, reset pointer
	virtual void TearDown() {
		delete table_;
		table_ = NULL;
	}

protected:
	// Variables
	PrimeTable* table_;
};

#else

// Dummy test to keep gtest_main linked
// When conditional compilation is used referring to gtest_main,
// MSVC linker will not link gtest_main and complain about
// missing entry point defined in the library
TEST(GtestMainLinkIn, ValueParameterizedTestIsNotSupportedOnThisPlatform) {
}

#endif
#endif