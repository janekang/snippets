// primeTablesFromGoogleTestSamples.cpp : Defines the entry point for the console application.
//
#include "stdafx.h"
#include <iostream>
#include "gtest/gtest.h"
//#include "PrimeTablesTestWithClass.h"

using namespace std;

TEST (PrimeTablesTest, forChecking) {
	EXPECT_EQ(1,1);
}

int _tmain(int argc, _TCHAR* argv[])
{
	::testing::InitGoogleTest(&argc, argv);
	RUN_ALL_TESTS();
	std::getchar();

	return 0;
}

