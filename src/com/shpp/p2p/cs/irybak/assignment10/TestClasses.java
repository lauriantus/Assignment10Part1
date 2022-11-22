package com.shpp.p2p.cs.irybak.assignment10;

import java.util.Objects;

import static com.shpp.p2p.cs.irybak.assignment10.Assignment10Part1.*;

public class TestClasses {

    public static void main(String[] args) {
        test(new String[]{"x + y", "x = 1", "y = 2"}, 3.0);
        test(new String[]{"x - y", "x = 1", "y = 2"}, 3.0);
        test(new String[]{"x / y", "x = 1", "y = 2"}, 3.0);
        test(new String[]{"x * y", "x = 1", "y = 2"}, 3.0);

    }

    private static void test(String[] args, Double res) {
        String formula = Assignment10Part1.deleteSpaces(args[0]);
        Double result = Assignment10Part1.getsResult(args);
        for (int i = 0; i < formula.length(); i++) {
            System.out.print(formula.charAt(i) + " ");
        }
        System.out.print(" = " + result + "\t");
        for (int i = 1; i < args.length; i++) {
            String var = deleteSpaces(args[i]);
            for (int j = 0; j < var.length(); j++) {
                System.out.print(var.charAt(j) + " ");
            }
            System.out.print("\t");
        }
        if (Objects.equals(result, res)) {
            System.out.println(GREEN + "All right!" + RESET);
        } else {
            System.out.println(RED + "We have mistake in this test" + RESET);
        }
    }
}
