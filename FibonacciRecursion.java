/* Recursive Fibonacci function with data structure passing */

import java.util.*;
import java.lang.*;
import java.io.*;

/* Wrapper class for Java syntax. */
class FibonacciRecursion
{
	/* 
	 * @return nth fibonacci number
	 */
	static int fib (int n) {
		int[] temp = new int[3];
		
		if (n == 0) {
			return 0;
		}
		else if (n == 1) {
			return 1;
		}
		else if (n > 1) {
			temp[0] = 0;
			temp[1] = 1;
			return fibRec(n, temp);
		}
		return -1;
	}
	
	/* 
	 * recursively find nth fibonacci number
	 * temp always size 3
	 */
	static int fibRec(int n, int[] temp) {
		if (n > 1) {
			temp[(n-1) % 3] = fibRec(n-1, temp);
			temp[n % 3] = temp[(n-1) % 3] + temp[(n-2) % 3]; 
			return temp[n % 3];
		}
		else if (n == 1) {
			return 1;
		}
		return -1; // Should not reach this
	}
	
	/*
	 * main
	 */
	public static void main (String[] args) throws java.lang.Exception
	{
		int n = 6;
		System.out.println(""+fib(n));
	}
}