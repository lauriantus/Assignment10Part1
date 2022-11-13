package com.shpp.p2p.cs.irybak.assignment10;

import java.util.ArrayList;

/**
 * Змінною може записуватись лише однією маленькою латинською літерою. Якщо літери буде дві - це вважається множенням.
 * якщо знак відсутній перед числом, воно вважається додатнім. Так, як наявність дужок не врахована в цій версії
 * більшість операції виконується без урахування знаку перед ними(знак наслідується результату операції). Виключенням
 * буде наступні типу виразів ->
 * [1 + - 2] -> [1 - 2] -> [-1];
 * [1 * - 2] -> [+ - 1 * 2] -> [- 1 * 2] -> [-2].
 * Найбільший
 * пріоритет має піднесення до степеня, потім множення та ділення, потім додавання та віднімання. Якщо степені
 * йдуть одна за одною, спочатку виконується та що справа, потім та що зліва.
 * Алгоритм:
 * <p>
 * - Перевірка на грубі помилки запису
 * Пошук наявності зайвих символів, відсутність значень для змінних, неправильний порядок символів тощо
 * <p>
 * - Видалення пробілів
 * <p>
 * - Парсинг на елементи
 * Розбиває все рівняння на елементи для зручності видалення кожного з них при виконанні математичних операцій
 * <p>
 * - Робота із степінями
 * Для врахування пріоритету необхідно спочатку виконати саме ці операції. До того ж з кінця рівняння.
 * Пошук першого степіня з кінця -> Виконання піднесення до степіня -> заміна трьох елементів на один результат ->
 * Пошук першого степеня з кінця -> ...  -> Степінь не знайдена -> завершення циклу
 * <p>
 * - Робота із множенням та діленням
 * Починаючи з самого початку виконуємо всі операції з множенням та діленням. Цикл буде завершено коли таких операцій
 * не буде в рівнянні
 * - Перевірка на наявність двох знаків підяд(наприклад, +-7+4) та заміна враховуючи правила заміни(-7+4)
 * - Робота із плюсами та мінусам
 * В результаті отримуємо вираз виду 4 = 5, або одразу результат.
 * - Якщо результат ще не отримано, то робимо операцію для видалення знаку =, тобто переносимо ліву частину в праву із
 * зміною знаку і отримуємо вже результат.
 */
public class Main {

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
            "0,.1",
            "0*/1",
            "d",
            "acd",
            "A"
    };
    static String[] variables = {
            "a",
            "b",
            "c"
    };
    static ArrayList<String> formula = new ArrayList<>();

    public static void main(String[] args) {
        tests();
    }

    private static void tests() {
        for (int i = 0; i < formulaForTests.length; i++) {
            checksMistakes(formulaForTests[i]);
        }
    }

    private static void checksMistakes(String formulaForTest) {
        /* for "" */
        if (formulaForTest.length() < 1) {
            System.err.println("[" + formulaForTest + "]" + "\tDon't have the text for equal!");
        }

        /* for " " */
        StringBuilder test = new StringBuilder();
        for (int i = 0; i < formulaForTest.length(); i++) {
            if (formulaForTest.charAt(i) != ' ') test.append(formulaForTest.charAt(i));
        }
        if (test.isEmpty()) {
            System.err.println("[" + formulaForTest + "]" + "\tDon't have the text for equal! Only spaces! This is incorrect");
        }

        /* for "^^"*/
        char[] symbols = {'^', '*', '/'};
        for (int i = 0; i < formulaForTest.length() - 1; i++) {
            char sym = formulaForTest.charAt(i);
            char symNext = formulaForTest.charAt(i + 1);
            if (sym == '^' || sym == '/' || sym == '*') {
                if (symNext == '^' || symNext == '/'
                        || symNext == '*' || symNext == '-'
                        || symNext == '+' || symNext == '=') {
                    System.err.println("[" + formulaForTest + "]" + "\tMistake in the " + i + " position on equal!");
                }
            }
        }

    }

    private static void parsing(String[] args) {
        for (int i = 0; i < args.length; i++) {

        }
    }

    /**
     * Отримуємо центр піднесення до степіня. Рахуємо з кінця, для того, щоб врахувати пріоритет степіня.
     *
     * @param formula
     * @return
     */
    private static Integer getCircumflexPosition(String formula) {
        for (int i = formula.length(); i > 0; i--) {
            if (formula.charAt(i) == '^') {
                return i;
            }
        }
        return null;
    }

    private static String getLeftPartEqual(String formula, Integer pos) {
        StringBuilder leftPart = new StringBuilder();
        for (int i = pos; i > 0; i--) {
        }
        return null;
    }

    /**
     * Видаляє всі пробіли з рівняння та параметрів(що б не подали на вхід, видаляє пробіли)
     *
     * @param s
     * @return
     */
    private static String deleteSpaces(String s) {
        StringBuilder sNew = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ') {
                sNew.append(s.charAt(i));
            }
        }
        return sNew.toString();
    }

    private static double getValue(String curFormula) {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < curFormula.length(); i++) {
            if (curFormula.charAt(i) == ' ' && value.isEmpty()) {
                continue;
            } else if (curFormula.charAt(i) >= '0' && curFormula.charAt(i) <= '9') {

            } else {
                System.out.println("Need value for exponent for correct equaling!");
                System.exit(0);
            }
        }
        if (value.isEmpty()) {
            System.out.println();
        }
        return 0;
    }
}