package com.recursion;

public class Recursion {

    /**
     * implements collatz conjecture recursion on any int passed in.
     * @param num
     */

    public static void collatzRecursion(int num) {
        System.out.println(num);
        if (num == 1) {return;}
        if (num % 2 == 0) {
            collatzRecursion(3*num+1);
        } else {
            collatzRecursion(num/2);
        }
    }

    /**
     * implements collatz conjecture tail recursion on any int passed in.
     * @param num
     */

    public static void collatzTailRecursion(int num) {
        System.out.println(num);
        if (num == 1) {return;}
        num = (num % 2 == 0) ? 3*num+1 : num/2;
        collatzRecursion(num);
    }

}
