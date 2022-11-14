package com.shpp.p2p.cs.irybak.assignment10;

import static com.shpp.p2p.cs.irybak.assignment10.Main.*;

public class Test {
    static String[] formulaForTests = {
            "",
            " ",
            "^^",
            "^",
            "1^",
            "1*",
            "*1",
            "=",
            "+",
            "0.1",
            "0*+1",
            "d",
            "acd",
            "A",
            "a"
    };
    static int mistakes = 0;
    static boolean mist;
    public static void main(String[] args) {

    }

    public static void tests() {
        for (int i = 0; i < formulaForTests.length; i++) {
            checksMistakes(formulaForTests[i], i);
        }
        if (mistakes > 0)
            System.out.println(err + "Mistakes have mistakes for equal in " + mistakes + " tests!" + reset);
        else System.out.println(ok + "All OK!" + reset);

    }
    private static void checksMistakes(String formulaForTest, int part) {
        /* Розділення між формулами. Сама формула. Операція видалення пробілів для зручності підрахунку.*/
        System.out.println("----------------------------");
        System.out.println("[" + sss + formulaForTest + reset + "]");
        formulaForTest = deleteSpaces(formulaForTest);

        /* видає помилку, якщо пустий рядок */
        getMistakeIfNeed(formulaForTest.length() < 1, "Don't have the text for equal!");

        /* Видає помилку якщо відбувається подвоєння знаку множення, ділення, піднесення до степеня, коми, крапки
         * або їх комбінування. Також при комбінуванні цих знаків з плюсом чи мінусом */
        for (int i = 0; i < formulaForTest.length(); i++) {
            char sym = formulaForTest.charAt(i);
            if (isOperand(sym) || isOtherOperands(sym)) {
                if (i == 0 || i == formulaForTest.length() - 1) {
                    System.out.println("Problem with " + i + " position");
                    mist = true;
                } else if (sym != '+') {
                    getMistakeIfNeed(isValue(formulaForTest.charAt(i - 1)), ("Don't have value left from position " + i));
                    getMistakeIfNeed(isValue(formulaForTest.charAt(i + 1)), "Don't have value right from position " + i);
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
    private static void getMistakeIfNeed(boolean formulaForTest, String x) {
        if (formulaForTest) {
            System.out.println(x);
            mist = true;
        }
    }


}
