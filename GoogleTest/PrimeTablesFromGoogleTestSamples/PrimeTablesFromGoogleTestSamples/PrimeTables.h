/*
 * PrimeTables.h
 *
 * Jane Kang, Agent Developer Intern (summer 2014)
 *
 * Interface and class declarations for PrimeTables
 *
 * This example is adopted from GooleTest's sample code available and open at
 * https://code.google.com/p/googletest/source/browse/trunk/samples/prime_tables.h
 * Although I modified the code to adopt my coding style, this is strictly for
 * practicing the application of Google Test C++ framework. I claim no copyrights.
 *
 */

#ifndef GTEST_SAMPLES_PRIME_TABLES_H_
#define GTEST_SAMPLES_PRIME_TABLES_H_

#include <algorithm>

// The prime table interface
class PrimeTable {
public:
	// constructor
	PrimeTable() {}

	// deconstructor
	virtual ~PrimeTable() {}

	// @return true if n is prime
	virtual bool isItPrime(int n) const = 0;

	// @return next smallest prime num bigger than p
	// @return -1 if no prime exists within bounds
	virtual int nextPrime(int n) const = 0;
};

// No preparation, starts calculation when called
class NoPrepPrimeTable : public PrimeTable {
public:
	// calculate whether it is prime at the spot
	// @return true if n is prime
	virtual bool isItPrime(int n) const;

	// @return next smallest prime num bigger than n
	// @return -1 if no prime exists (never called)
	virtual int nextPrime(int n) const;
};

// Construct an array full of true/false depending whether the index is prime
class PrepPrimeTable : public PrimeTable {
public:
	// constructor
	// @param max bound for calculating prime num
	PrepPrimeTable(int max);

	// deconstructor
	~PrepPrimeTable();

	virtual int primeMaxLimit() const;

	// @return true if n is prime and within bounds
	virtual bool isItPrime(int n) const;

	// @return next smallest prime num bigger than n
	// @return -1 if no prime exists within bounds
	virtual int nextPrime(int n) const;

private:
	// prime calculation max value 
	int is_prime_size_;

	// true/false array for num 0~max
	bool* is_prime_;

	// disable compiler warning "assignment operator could not be generated."
	void operator=(const PrepPrimeTable& rhs);

	// prepare an array full of true/false depending whether the index is prime
	// @param max bound for calculating prime num
	void prepareTable(int max);
};

#endif // GTEST_SAMPLES_PRIME_TABLES_H_ generated