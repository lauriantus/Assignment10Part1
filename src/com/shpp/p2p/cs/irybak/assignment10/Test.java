package com.shpp.p2p.cs.irybak.assignment10;

import java.util.ArrayList;

import static com.shpp.p2p.cs.irybak.assignment10.Main.*;

public class Test {
    static int mistakes = 0;
    static boolean mist;
    static ArrayList<String> formulas = new ArrayList<>();

    public static void tests(String formula) {
        // adds formula to the total list
        formulas.add(formula);
        // checks every formula from the list
        for (int i = 0; i < formulas.size(); i++) {
            checksMistakes(formulas.get(i), i);
        }
        // gets result of tests
        if (mistakes > 0) {
            System.out.println(err + "Mistakes have mistakes for equal in " + mistakes + " tests!" + reset);
            System.exit(0);
        } else System.out.println(ok + "All OK!" + reset);

    }

    private static void checksMistakes(String formulaForTest, int part) {
        // Outputs the formula on the console with spaces and without it
        System.out.println("----------------------------");
        System.out.print("[" + sss + formulaForTest + reset + "]\t- >\t");
        formulaForTest = deleteSpaces(formulaForTest);
        System.out.println("[" + sss + formulaForTest + reset + "]");

        /* Gets the mistake if string is empty */
        getMistakeIfNeed(formulaForTest.length() < 1, "Don't have the text for equal!");
        /* Gets the mistake if double operand [+, -, *, /, ,, .] */
        for (int i = 0; i < formulaForTest.length(); i++) {
            char sym = formulaForTest.charAt(i);
            if (isOperand(sym) || sym == '=') {
                if (i == 0 || i == formulaForTest.length() - 1) {
                    System.out.println("Problem with " + i + " position");
                    mist = true;
                } else if (sym != '+') {
                    getMistakeIfNeed(isValueOrNumber(formulaForTest.charAt(i - 1)), ("Don't have value left from position " + i));
                    getMistakeIfNeed(isValueOrNumber(formulaForTest.charAt(i + 1)), "Don't have value right from position " + i);
                }
            }
        }

        /* Додає невиконання одного тесту в систему підрахунку. Після додавання обнуляє стан помилки
         * для наступного тесту.*/
        if (mist) {
            mistakes++;
            mist = false;
        }
    }

    protected static void outTestsOnScreen() {
        System.out.println();
        printFormulaOrResult();
        System.out.println();
        for (Character operand : operands) {
            System.out.print("[" + operand + "] ");
        }
        System.out.println();
        for (Character variable : variables) {
            System.out.print("[" + variable + "] ");
        }
        System.out.println();
        for (Double number : numbers) {
            System.out.print("[" + number + "] ");
        }
        System.out.println();
        for (Double value : values) {
            System.out.print("[" + value + "] ");
        }
        System.out.println("\nSize var = " + variables.size() + "\tSize val = " + values.size());
    }

    static boolean isValueOrNumber(char symbol) {
        for (int i = 'a'; i <= 'z'; i++) {
            if (symbol == i) return true;
        }
        for (int i = '1'; i < '9'; i++) {
            if (symbol == i || symbol == '0') return true;
        }
        return false;
    }

    private static void getMistakeIfNeed(boolean formulaForTest, String x) {
        if (!formulaForTest) {
            System.out.println(x);
            mist = true;
        }
    }


    public static void printFormulaOrResult() {
        for (Object o : totalFormula) {
            System.out.print(o + " ");
        }
        System.out.println();
    }


}
