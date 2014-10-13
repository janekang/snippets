/* Recursive Fibonacci function with data structure passing */

import java.util.*;
import java.lang.*;
import java.io.*;

/* Wrapper class for Java syntax. */
public class FibonacciSumRec
{
	/* 
	 * @return nth fibonacci number
	 */
	static int fibSum (int n) {
		int[] temp = new int[n+1];
		
		if (n == 0) {
			return 0;
		}
		else if (n == 1) {
			return 1;
		}
		else if (n > 1) {
			temp[0] = 0;
			temp[1] = 1;
			fibRec(n, temp);

			// Sum up the fibonacci numbers
			return fibSumRec(n, temp);
		}
		return -1;
	}
	
	/* 
	 * recursively find nth fibonacci number
	 * temp always size 3
	 */
	static int[] fibRec(int n, int[] temp) {
		if (n > 1) {
			temp = fibRec(n-1, temp);
			temp[n] = temp[n-1] + temp[n-2]; 
			return temp;
		}
		else if (n == 1) {
			return temp;
		}
		System.out.println("Invalid: should not reach this");
		return temp; // Should not reach this
	}

	/*
	 * recursively add up to nth fibonacci number
	 */
	static int fibSumRec(int n, int[] temp) {
		if (n == 0) {
			return temp[0];
		}
		else if (n > 0) {
			return fibSumRec(n-1, temp) + temp[n];
		}
		return -1; // Should not reach this
	}
	
	/*
	 * main
	 */
	public static void main (String[] args) throws java.lang.Exception
	{
		int n = 6;
		System.out.println(""+fibSum(n));
	}
}