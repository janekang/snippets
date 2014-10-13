#include "stdafx.h"

#include <iostream>
#include "PrimeTablesTestWithStopwatch.h"

using namespace std;

/*
 * PrimeTablesTestWithStopwatch.cpp
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

// Check for default set up
TEST_F(PrimeTablesTestWithStopwatch, PrimeLimit) {
	ASSERT_TRUE(prepped->primeMaxLimit() > 0);
}

// Check for basic functionality
TEST_F(PrimeTablesTestWithStopwatch, IsItPrime) {
	// On-the-fly testing
	EXPECT_FALSE(notPrepped->isItPrime(0));
	EXPECT_FALSE(notPrepped->isItPrime(1));
	EXPECT_TRUE(notPrepped->isItPrime(3));
	EXPECT_FALSE(notPrepped->isItPrime(4));
	EXPECT_FALSE(notPrepped->isItPrime(9));

	// Prepped testing
	EXPECT_FALSE(prepped->isItPrime(0));
	EXPECT_FALSE(prepped->isItPrime(1));
	EXPECT_TRUE(prepped->isItPrime(7));
	EXPECT_FALSE(prepped->isItPrime(6));
	EXPECT_FALSE(prepped->isItPrime(9));

	// Check whether they give same results
	EXPECT_EQ(notPrepped->isItPrime(2), prepped->isItPrime(2));
}

TEST_F(PrimeTablesTestWithStopwatch, NextPrime) {
	int base = 3;

	// bounds
	EXPECT_GT(notPrepped->nextPrime(base), base);
	EXPECT_NE(prepped->nextPrime(base), base);

	if (base >= prepped->primeMaxLimit()) { // edge case, off bounds
		EXPECT_EQ(-1, prepped->nextPrime(base));
		EXPECT_GT(notPrepped->nextPrime(base), prepped->nextPrime(base));
	}
	else {
		EXPECT_GT(prepped->nextPrime(base), base);
		EXPECT_EQ(notPrepped->nextPrime(base), prepped->nextPrime(base));
	}
}

// Check with base limit change
TEST_F(PrimeTablesTestWithStopwatch, HelperChangeMax) {
	// Change performance time limit
	ChangeTimeLimit(1);

	int base = 19;

	for (int i = 1; i < 10; ++i) {
		ChangeMaxLimit(10*i);
		EXPECT_EQ(10*i, prepped->primeMaxLimit());

		// Get next prime value
		EXPECT_NE(prepped->nextPrime(base), base);
		if (notPrepped->nextPrime(base) >= prepped->primeMaxLimit()) { // edge case, off bounds
			EXPECT_EQ(-1, prepped->nextPrime(base));
		}
		else {
			EXPECT_GT(prepped->nextPrime(base), base);
			EXPECT_EQ(notPrepped->nextPrime(base), prepped->nextPrime(base));
			
			// Found next prime value, move onto that
			base = prepped->nextPrime(base);
		}
	}
}