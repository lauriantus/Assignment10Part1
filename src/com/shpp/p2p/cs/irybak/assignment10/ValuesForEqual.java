package com.shpp.p2p.cs.irybak.assignment10;

import java.util.ArrayList;
import java.util.HashMap;

public class ValuesForEqual {
    static String formula;
    static HashMap<String, Integer> arguments = new HashMap<>();
    static ArrayList<Integer> unknownsValues = new ArrayList<>();
    static String currentKey;

    public static void main(String[] args) {
        formula = args[0];
        System.out.println(formula);
        /* gets arguments and values of arguments*/
        for (int i = 1; i < args.length; i++) {
            getArgumentsAndValues(args[i]);
            System.out.print(currentKey + " = ");
            System.out.println(arguments.get(currentKey));
        }
        for (int i = 0; i < unknownsValues.size(); i++) {
            System.out.println(unknownsValues.get(i));
        }
    }
//         "a^2 = 1"
//         "a - 2 = 1"
//         "a + 2 = 1"
//         "a / 2 = 1"
//         "a * 2 = 1"

//         "a = 1"
//         "c = 12"
//         "d = 13"
//         "a c = 999"
    private static void getArgumentsAndValues(String arg) {
        StringBuilder argument = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean flag = true;        // selected true - argument, false - value of argument
        
        // form: [argument = value]
        for (int i = 0; i < arg.length(); i++) {
            char argumentAndValue = arg.charAt(i);
            // add argument
            // TODO потрібна перевірка на вже наявний аргумент
            if (flag) {
                /* gets argument */
                if (argumentAndValue >= 'a' && argumentAndValue <= 'z') {
                    argument.append(argumentAndValue);
                    currentKey = "" + arg.charAt(i);
                }
                if (argumentAndValue == '=') {
                    flag = false;

                    /* checks on the correct form(equal doesn't have the argument*/
                    if (argument.length() < 1) {
                        System.err.println("Wrong wrote the equal. Correctly format: argument = value!");
                        unknownsValues.add(i);      // save the number of argument in the list
                    }
                }
                if (argument.length() > 0 && argumentAndValue == ' ') {
                    while (i < arg.length()) {
                        if (arg.charAt(i) == '=') {
                            flag = false;
                            break;
                        }
                        i++;
                    }
                }
            }

            if (!flag) {
                if (arg.charAt(i) >= '0' && arg.charAt(i) <= '9') {
                    value.append(arg.charAt(i));
                }
                if (arg.charAt(i) == ' ' && value.length() > 0) {
                    break;
                }
            }
        }
        arguments.put(argument.toString(), Integer.parseInt(value.toString()));
    }

    private static void calc() {
        findStepin();       // FIXME name
    }

    private static void findStepin() {
    }
}
