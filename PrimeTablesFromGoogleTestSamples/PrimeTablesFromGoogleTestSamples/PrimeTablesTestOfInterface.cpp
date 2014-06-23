#include "stdafx.h"

#include <iostream>
#include "PrimeTablesTestOfInterface.h"

using namespace std;

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

/// Typed tests, used when you know all the types to cover when you write the tests
#if GTEST_HAS_TYPED_TEST

// List of types to test
using testing::Types;
typedef Types<NoPrepPrimeTable, PrepPrimeTable> PrimeTablesTypedTestImplementations;

// Test fixture class template
template <class T>
class PrimeTablesTypedTest : public PrimeTablesTestOfInterface<T> {
};

TYPED_TEST_CASE(PrimeTablesTypedTest, PrimeTablesTypedTestImplementations);

// Check for basic functionality
// Each TYPED_TEST repeated for each type in the type list
TYPED_TEST(PrimeTablesTypedTest, IsItPrimeWNonPrimes) {
	EXPECT_FALSE(this->table_->isItPrime(-5));
	EXPECT_FALSE(this->table_->isItPrime(0));
	EXPECT_FALSE(this->table_->isItPrime(1));
	EXPECT_FALSE(this->table_->isItPrime(4));
	EXPECT_FALSE(this->table_->isItPrime(6));
	EXPECT_FALSE(this->table_->isItPrime(9));
	EXPECT_FALSE(this->table_->isItPrime(1024));
}

TYPED_TEST(PrimeTablesTypedTest, IsItPrimeWPrimes) {
	EXPECT_TRUE(this->table_->isItPrime(3));
	EXPECT_TRUE(this->table_->isItPrime(7));
	EXPECT_TRUE(this->table_->isItPrime(11));
	EXPECT_TRUE(this->table_->isItPrime(131));
}

TYPED_TEST(PrimeTablesTypedTest, NextPrime) {
	EXPECT_EQ(3, this->table_->nextPrime(2));
	EXPECT_EQ(11, this->table_->nextPrime(7));
	EXPECT_EQ(47, this->table_->nextPrime(43));
	EXPECT_EQ(131, this->table_->nextPrime(128));
}

#endif // GTEST_HAS_TYPED_TEST


/// Type-parameterized tests, used when you do not know all the types to cover when you write the tests
#if GTEST_HAS_TYPED_TEST_P

// Test fixture class template
using testing::Types;

template <class T>
class PrimeTablesTypeParameterizedTest : public PrimeTablesTestOfInterface<T> {
};

TYPED_TEST_CASE_P(PrimeTablesTypeParameterizedTest);

// Check for basic functionality
// Each test pattern repeated for each type in the type list, which is defined later
TYPED_TEST_P(PrimeTablesTypeParameterizedTest, IsItPrimeWNonPrimes) {
	EXPECT_FALSE(this->table_->isItPrime(-5));
	EXPECT_FALSE(this->table_->isItPrime(0));
	EXPECT_FALSE(this->table_->isItPrime(1));
	EXPECT_FALSE(this->table_->isItPrime(4));
	EXPECT_FALSE(this->table_->isItPrime(6));
	EXPECT_FALSE(this->table_->isItPrime(9));
	EXPECT_FALSE(this->table_->isItPrime(1024));
}

TYPED_TEST_P(PrimeTablesTypeParameterizedTest, IsItPrimeWPrimes) {
	EXPECT_TRUE(this->table_->isItPrime(3));
	EXPECT_TRUE(this->table_->isItPrime(7));
	EXPECT_TRUE(this->table_->isItPrime(11));
	EXPECT_TRUE(this->table_->isItPrime(131));
}

TYPED_TEST_P(PrimeTablesTypeParameterizedTest, NextPrime) {
	EXPECT_EQ(3, this->table_->nextPrime(2));
	EXPECT_EQ(11, this->table_->nextPrime(7));
	EXPECT_EQ(47, this->table_->nextPrime(43));
	EXPECT_EQ(131, this->table_->nextPrime(128));
}

// Enumerate the tests defined above
REGISTER_TYPED_TEST_CASE_P(
	PrimeTablesTypeParameterizedTest,
	IsItPrimeWNonPrimes, IsItPrimeWPrimes, NextPrime);


// List of types to test (can be declared later than test patterns)
typedef Types<NoPrepPrimeTable, PrepPrimeTable> PrimeTablesTypeParameterizedTestImplementations;
INSTANTIATE_TYPED_TEST_CASE_P(
	PrimeTablesOfInterfaceInstance,
	PrimeTablesTypeParameterizedTest,
	PrimeTablesTypeParameterizedTestImplementations);

#endif // GTEST_HAS_TYPED_TESTP
