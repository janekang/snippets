/*
 * PrimeTables.cpp
 *
 * Jane Kang, Agent Developer Intern (summer 2014)
 *
 * Method definitions for classes NoPrepPrimeTable and PrepPrimeTable
 *
 * This example is adopted from GooleTest's sample code available and open at
 * https://code.google.com/p/googletest/source/browse/trunk/samples/prime_tables.h
 * Although I modified the code to adopt my coding style, this is strictly for
 * practicing the application of Google Test C++ framework. I claim no copyrights.
 *
 */

#include "stdafx.h"
#include "PrimeTables.h"

// NoPrepPrimeTable/////////////////////////////////////////////////////////////////////////

	// calculate whether it is prime at the spot
	// @return true if n is prime
	bool NoPrepPrimeTable::isItPrime(int n) const {
		if (n < 2)
			return false;
		
		for (int i = 2; i*i <= n; ++i) {
			if ((n % i) == 0)
				return false;
		}
		return true;
	}

	// @return next smallest prime num bigger than n
	// @return -1 if no prime exists (never called)
	int NoPrepPrimeTable::nextPrime(int n) const {
		for (int i = n + 1; i > 0; ++i) {
			if (isItPrime(i))
				return i;
		}
		return -1;
	}


// PrepPrimeTable/////////////////////////////////////////////////////////////////////////////////

	// constructor
	// @param max bound for calculating prime num
	PrepPrimeTable::PrepPrimeTable(int max) {
		is_prime_size_ = max; // 0~max
		is_prime_ = new bool[max+1];
		prepareTable(max);
	}

	// deconstructor
	PrepPrimeTable::~PrepPrimeTable() {
		delete[] is_prime_;
	}

	// @return length of table
	int PrepPrimeTable::primeMaxLimit() const {
		return is_prime_size_;
	}

	// @return true if n is prime and within bounds
	bool PrepPrimeTable::isItPrime(int n) const {
		return 0 <= n && n <= is_prime_size_ && is_prime_[n];
	}

	// @return next smallest prime num bigger than n
	// @return -1 if no prime exists within bounds
	int PrepPrimeTable::nextPrime(int n) const {
		for (int i = n+1; i <= is_prime_size_; ++i) {
			if (is_prime_[i])
				return i;
		}
		return -1;
	}

	// prepare an array full of true/false depending whether the index is prime
	// @param max bound for calculating prime num
	void PrepPrimeTable::prepareTable(int max) {
		// Populate array with 'true'
		::std::fill(is_prime_, is_prime_ + is_prime_size_, true);
		is_prime_[0] = false;
		is_prime_[1] = false;

		for (int i = 2; i <= max; ++i) {
			if (!isItPrime(i))
				continue;

			// Found prime, set its multiples as false
			for (int j = 2*i; j <= max; j += i) {
				is_prime_[j] = false;
			}
		}
	}