#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void reverseStrInPlace(int strLen, char* inputStr) {
	int countForReverse = strLen / 2;
	char swap;	

	for (int i = 0; i < countForReverse; ++i) {
		swap = inputStr[i];
		inputStr[i] = inputStr[strLen - i];
		inputStr[strLen - i] = swap;
	}
}

void reverseStr(int strLen, char* inputStr, char* reversedStr) {
	for (int i = 0; i < strLen; ++i) {
		reversedStr[i] = inputStr[strLen - i];
	}
}

int main() {
	setbuf(stdout, NULL);

	char* inputStr = (char*)  malloc(100000);
	int strlen = -1;

	scanf("%d\n%s\n", &strlen, inputStr);
	
	if (strlen < 0) {
		printf("Wrong input\n");
		return 1;
	}

	char* reversedStr = (char*) malloc(100000);
	reverseStr(strlen, inputStr, reversedStr);
	printf("%s\n", reversedStr);
	
	reverseStrInPlace(strlen, inputStr);
	printf("%s\n", inputStr);

	free(inputStr);
	free(reversedStr);
	return 0;
}
