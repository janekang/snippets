#include "googleTestExample-functionToTest.h"
#include "gtest/gtest.h"

/*
 * String reverse functions used for google test testing
 *
 * Jane Kang, Agent Developer Intern 
 *
 * note (for mac):
 * cpp and h files must be in the folder where google test is installed (for library issues)
 * steps are 1) compile everything with google test 2) run exe
 * g++ -I. -I./include {cpp files} libgtest.a -lpthread -o {exe file name}
 * ./{exe file name}
 *
 * For libgtest.a,
 * 0) get make, bash shell script, and c++ compiler on
 *		sudo apt-get install make
 *		sudo apt-get install bash
 * 		sudo apt-get install g++
 * 1) unzip {google test download}
 * 2) cd to that directory
 * 3) ./configure
 * 4) make -- makefile
 * 5) make check -- runs all tests
 * 6) build google test library, libgtest.a
 *		g++ -I. -I./include -c src/gtest-all.cc
 *		ar -rv libgtest.a gtest-all.o
 *
 * Build sample test
 * 1) cd make
 * 2) make
 * 3) ./sample1_unittest -- build sample test
 * 		if got errors, open the source with
 *		sudo getit Makefile
 *		and replace '-lpthread' with '-pthread' at line 80
 *
 * http://avneetkhasla.wordpress.com/2013/04/12/google-c-testing-frameworkgoogle-test-gtest/
 */

TEST (ReverseStrTest, StrLengthOdd) {
	ASSERT_EQ ("a", reverseStr("a"));
	EXPECT_EQ ("edcba", reverseStr("abcde"));

	EXPECT_EQ ("a", reverseInPlace("a"));
	EXPECT_EQ ("987654321", reverseInPlace("123456789"));
}

TEST (ReverseStrTest, StrLengthEven) {
	ASSERT_EQ ("", reverseStr(""));
	EXPECT_EQ ("abcabc", reverseStr("cbacba"));

	EXPECT_EQ ("", reverseInPlace(""));
	EXPECT_EQ ("9876543210", reverseInPlace("0123456789"));
}

int main(int argc, char **argv) {
	::testing::InitGoogleTest(&argc, argv);
	return RUN_ALL_TESTS();
}
