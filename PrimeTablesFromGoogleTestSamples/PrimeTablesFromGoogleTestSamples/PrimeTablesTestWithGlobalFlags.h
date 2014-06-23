/*
 * PrimeTablesTestWithGlobalFlags.h
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
/*
#ifndef GTEST_SAMPLES_PRIME_TABLES_WGLOBALFLAGS_H_
#define GTEST_SAMPLES_PRIME_TABLES_WGLOBALFLAGS_H_

#include "PrimeTables.h"
#include "gtest/gtest.h"

using namespace std;

// Check whether Combine() is supported (depends on compilers)
#if GTEST_HAS_COMBINE

// Use flags to decide when to use NoPrepPrimeTable/PrepPrimeTable
// When memory is low, instantiate without PrepPrimeTable
class HybridPrimeTable : public PrimeTable {
public:
	// Constructor
	// @param forceNoPrep -- low memory, no PrepPrimeTable
	// @param prep_limit -- PrepPrimeTable max size
	HybridPrimeTable(bool forceNoPrep, int prep_limit)
		: noPrepPrimeTableImpl_(new NoPrepPrimeTable),
			prepPrimeTableImpl_(forceNoPrep ? NULL : new PrepPrimeTable(prep_limit)),
			prep_limit_(prep_limit) {
	}

	// Deconstructor
	virtual ~HybridPrimeTable() {
		delete noPrepPrimeTableImpl_;
		delete prepPrimeTableImpl_;
	}

	// @return true if n is prime
	// If within bound, use PrepPrimeTable
	virtual bool isItPrime(int n) const {
		if (prepPrimeTableImpl_ != NULL && n < prep_limit_) {
			return prepPrimeTableImpl_->isItPrime(n);
		}
		else {
			return noPrepPrimeTableImpl_->isItPrime(n);
		}
	}

	// @return next smallest prime num bigger than p
	// @return -1 if no prime exists within bounds
	// If within bound, use PrepPrimeTable
	virtual int nextPrime(int n) const {
		int nextPrime = -1;

		if (prepPrimeTableImpl_ != NULL && n < prep_limit_) {
			nextPrime = prepPrimeTableImpl_->nextPrime(n);
		}

		return nextPrime != -1 ? nextPrime : noPrepPrimeTableImpl_->nextPrime(n);
	}
	
private:
	// Variables
	NoPrepPrimeTable* noPrepPrimeTableImpl_;
	PrepPrimeTable* prepPrimeTableImpl_;
	int prep_limit_;
};


// Test case setup
using ::testing::TestWithParam;
using ::testing::Bool;
using ::testing::Values;
using ::testing::Combine;

// Testing class
class PrimeTablesTestWithGlobalFlags : public TestWithParam< ::testing::tuple<bool, int> > {
protected:
	// Variables
	HybridPrimeTable* table_;

	// Called before each test case
	virtual void SetUp() {
		bool forceNoPrep = ::testing::get<0>(GetParam());
		int prepLimit = ::testing::get<1>(GetParam());
		table_ = new HybridPrimeTable(forceNoPrep, prepLimit);
	}

	// Called after each test case
	virtual void TearDown() {
		delete table_;
		table_ = NULL;
	}
};

#else

// Dummy test to keep gtest_main linked
// When conditional compilation is used referring to gtest_main,
// MSVC linker will not link gtest_main and complain about
// missing entry point defined in the library
TEST(GtestMainLinkIn, CombineIsNotSupportedOnThisPlatform) {
}

#endif // GTEST_HAS_COMBINE
#endif*/