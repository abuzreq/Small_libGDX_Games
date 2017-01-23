package com.me;

import java.util.Random;

/**
 * Calculates the an approximation of the square root using method described
 * here: https://www.youtube.com/watch?v=3i94NWF39nU
 * 
 * @author Abuzreq
 * 
 */
public class SquareRoot {
	
	public static void main(String[] arg) {
		
	//	long t = System.currentTimeMillis();
	//	System.out.println(findSquareRoot3(562448656));
		//System.out.println(System.currentTimeMillis() - t);
		int tablesize = 13 ;
		long t3 = System.currentTimeMillis();
		findSquareRoot4(562448656,1,17);
		System.out.println(System.currentTimeMillis() - t3);
	//	long t2 = System.currentTimeMillis();
		//System.out.println(findSquareRoot(316348490636206336l));
//		System.out.println(System.currentTimeMillis() - t2);
	}

	public static double findSquareRoot(long n) {
		int precision = 7;
		long number = n;
		long divisor = findLeastPerfectRoot(number);
		String str = "";
		str += divisor + ".";
		while (precision > 0) {
			number = findSubtarctResult(number, divisor);
			number = extend(number, 100);

			divisor = findNext(divisor);
			divisor = extend(divisor, 10);
			long lastDigit = fillPlaceWithSuitableNum(number, divisor);
			// if the divisor before putting the last digit was already larger
			// than the number
			// then the result of the above is -1 and in this case we want to
			// extend the number by two zeros and try again
			while (lastDigit < 0) {
				number = extend(number, 100);
				lastDigit = 0;
				str += lastDigit;
				divisor = findNext(divisor);
				divisor = extend(divisor, 10);
				// System.out.println(number + " " + divisor);
				lastDigit = fillPlaceWithSuitableNum(number, divisor);
			}
			divisor += lastDigit;
			str += lastDigit;
			precision--;
			// System.out.println(number+" "+divisor);
		}
		return Double.parseDouble(str);

	}

	/**
	 * 
	 * @param current
	 * @param n
	 * @return (in the terms of long division)multiply the last digit in the
	 *         quotient by the divisor and subtract it from the dividend
	 */
	public static long findSubtarctResult(long current, long n) {
		return (current - n * (n % 10));
	}

	/**
	 * 
	 * @param threshold
	 * @param tbf
	 *            to be found , Note : tbf must have a zero in its least digit
	 * @return what is the largest number i that is if put in the least digit of
	 *         tbf and then multiplied by tbf will be less than or equal to the
	 *         threshold
	 */
	public static long fillPlaceWithSuitableNum(long threshold, long tbf) {
		long i = 0;
		for (i = 0; i < 10; i++) {
			if ((tbf + i) * i >= threshold) {
				break;
			}
		}
		i -= 1;
		return i;
	}

	/**
	 * 
	 * @param n
	 * @param tenth
	 * @return the multiplication of tenth and n , used to allow more digits to
	 *         number
	 */
	public static long extend(long n, long tenth) {
		return n * tenth;
	}

	/**
	 * 
	 * @param num
	 * @return the largest number that if multiplied by it self will be less
	 *         than or equal to @num
	 */
	public static long findLeastPerfectRoot(long num) {
		long j;
		long i = 0;
		boolean found = false;
		for (j = num; j != 0; j--) {
			for (i = num - 1; i != 0; i--) {
				if (i * i == j) {
					found = true;
					break;
				}
			}
			if (found)
				break;
		}
		return i;

	}

	/**
	 * 
	 * @param num
	 * @return returns the number after taking the least digit and putting 0 in
	 *         its place (before 5422 , after 5420), then multiplying it by 2
	 *         and adding it again to the number before : 5422 >> after : 5420 +
	 *         2*2 = 5424 . This is how it was described in the source but it's
	 *         easily to add the last digit of a number to it the whole number
	 */
	public static long findNext(long num) {
		long a = num % 10;
		num += a;
		return num;
	}
	
	public static void findSquareRoot4(double num , double initialGuess , int precision)
	{
		double guess = initialGuess;
		double avg = avg(guess , num/guess);
		int i  = precision;
		while(i > 0 )
		{
			avg = avg(avg,num/avg);
			i--;
		}
		System.out.println("Approx. Square Root "+avg);
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @param precision
	 * @return if a is equal to b in every digit and in every digit after the floating point up to a precision .
	 */
	public static boolean equalsInPrecision(double a , double b , int precision)
	{
		if(Math.floor(a)!=Math.floor(b))
			return false ;
		int i = precision  ;
		int n = 1 ;
		//after we compareed the integer part above we equate only the decimal part here
		a = a - Math.floor(a);
		b = b - Math.floor(b);
		// move the first decimal to integer part
	
		while(i>0)
		{
			i--;
			a *= 10 ;
			b *= 10;
			if(Math.floor(a)%10 != Math.floor(b)%10)
			return false ;		
			
		}
		return true ;
	}
	/**
	 * 
	 * @param num
	 * 
	 * @return the square root of num using random numbers
	 */
	public static long findSquareRoot3(int num)
	{
		Random random = new  Random();
		long y = 1 ;
		if(num >= 4)
		 y = random.nextInt(num/2) +1;
		boolean found = false ;
		while(!found)
		{
			double result = get(num,y);
			if(y==result)
			{
				found = true ;
				break;
			}
			else
			{	
				if(result>y)
				y = random.nextInt((int)result)+y;
				else
					y = random.nextInt((int)result);
			}
				
		}
		return y ;
	}

	
	 // y  ----> avg(y , x/y)
	public static long findSquareRoot2(long num)
	{
		long y = 1 ;
		boolean found = false ;
		while(!found)
		{
			double result = get(num,y);
			if(y==result)
			{
				found = true ;
				break;
			}
			else	
				y += 1 ;
				
		}
		return y ;
	}
	public static double get(long num, long root) {
		return avg(root, num / root);
	}

	public static double avg(double... nums) {
		double n = 0, sum = 0;
		for (double d : nums) {
			sum += d;
			n++;
		}
		return sum / n;
	}
}
