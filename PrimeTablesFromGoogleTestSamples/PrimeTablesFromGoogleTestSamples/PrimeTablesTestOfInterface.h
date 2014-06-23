/*
 * PrimeTablesTestOfInterface.h
 *
 * Jane Kang, Agent Developer Intern (summer 2014)
 *
 * Show how to test common properties of mutliple implementations of same interface
 * (aka interface tests)
 * Use Google Test framework's testing::Test
 *
 * This example is adopted from GooleTest's sample code available and open at
 * https://code.google.com/p/googletest/source/browse/trunk/samples/prime_tables.h
 * Although I modified the code to adopt my coding style, this is strictly for
 * practicing the application of Google Test C++ framework. I claim no copyrights.
 */

#ifndef GTEST_SAMPLES_PRIME_TABLES_OINTERFACE_H_
#define GTEST_SAMPLES_PRIME_TABLES_OINTERFACE_H_

#include "PrimeTables.h"
#include "gtest/gtest.h"

// Factory functions for creating instances of implementations
template <class T>
PrimeTable* CreatePrimeTable();

template<>
PrimeTable* CreatePrimeTable<NoPrepPrimeTable>() {
	return new NoPrepPrimeTable;
}

template<>
PrimeTable* CreatePrimeTable<PrepPrimeTable>() {
	return new PrepPrimeTable(10000);
}

// Testing class
template <class T>
class PrimeTablesTestOfInterface : public testing::Test {
protected:
	// Variables
	PrimeTable* const table_;

	// Constructor
	// Call factory function to create a prime table implemented by T
	PrimeTablesTestOfInterface() : table_(CreatePrimeTable<T>()) {
	}

	// Deconstructor
	virtual ~PrimeTablesTestOfInterface() {
		delete table_;
	}
};

#endif