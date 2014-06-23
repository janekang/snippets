#include "stdafx.h"

/*
 * String reverse functions used for google test testing
 *
 * Jane Kang, Agent Developer Intern 
 */

string reverseStr(string input) {
	string result = "";

	for (int i = 0; i < input.length(); i++) {
		result += input[input.length()-1-i];
	}

	return result;
}

string reverseInPlace(string input) {
	char temp;

	for (int i = 0; i < input.length()/2; i++) {
		temp = input[i];
		input[i] = input[input.length()-1-i];
		input[input.length()-1-i] = temp;
	}

	return input;
}