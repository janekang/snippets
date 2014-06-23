/*
 * GoogleTestLeakChecker.h
 *
 * Jane Kang, Agent Developer Intern (summer 2014)
 *
 * Show how to use Google Test listener API to implement primitive leak checker
 * Use Google Test framework's testing::Test
 *
 * This example is adopted from GooleTest's sample code available and open at
 * https://code.google.com/p/googletest/source/browse/trunk/samples/prime_tables.h
 * Although I modified the code to adopt my coding style, this is strictly for
 * practicing the application of Google Test C++ framework. I claim no copyrights.
 */

#ifndef GTEST_SAMPLES_LEAK_CHECKER_H_
#define GTEST_SAMPLES_LEAK_CHECKER_H_

#include <stdio.h>
#include <stdlib.h>
#include "gtest/gtest.h"

using ::testing::EmptyTestEventListener;
using ::testing::Test;
using ::testing::TestCase;
using ::testing::TestEventListeners;
using ::testing::TestInfo;
using ::testing::TestPartResult;
using ::testing::UnitTest;

namespace {
	/// class used to track memory ////////////////////////////////////////////////////////
	class Drop {
	public:
		// help control drop allocation
		void* operator new(size_t allocation_size) {
			allocated_++;
			return malloc(allocation_size);
		}

		void operator delete(void* block, size_t /* allocation_size */) {
			allocated_--;
			free(block);
		}

		// @return allocated num
		static int allocated() {
			return allocated_;
		}

	private:
		// variables
		static int allocated_;
	};

	// variables, static because used out of class
	int Drop::allocated_ = 0;


	/// event listener ///////////////////////////////////////////////////////////

	// monitors how many objects are created and destroyed by each test
	// reports failure if test leaks objects (compare num of live objects at beginning, end)
	class LeakChecker : public EmptyTestEventListener {
	private:
		// variables
		int init_allocated_;

		// called before a test starts
		virtual void OnTestStart(const TestInfo& /* test_info */) {
			init_allocated_ = Drop::allocated();
		}

		// called after a test ends
		virtual void OnTestEnd(const TestInfo& /* test_info */) {
			int diff = Drop::allocated() - init_allocated_;
			EXPECT_LE(diff, 0) << "Leaked " << diff << "unit(s) of Drop";
		}
	};

	// test, should fail when --check_for_leaks flag is specified
	TEST(CheckForLeaksTest, LeaksDrop) {
		Drop* drop = new Drop;
		EXPECT_TRUE(drop != NULL);
	}
}

#endif