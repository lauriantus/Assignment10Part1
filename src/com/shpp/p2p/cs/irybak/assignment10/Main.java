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
    static String err = "\u001B[31m";
    static String reset = "\u001B[0m";
    static String ok = "\u001B[32m";
    static String sss = "\u001B[36m";

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
            "0*/1",
            "d",
            "acd",
            "A"
    };
    static int mistakes = 0;
    static boolean mist;


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
            checksMistakes(formulaForTests[i], i);
        }
        if (mistakes > 0) System.out.println(err + "Mistakes have mistakes for equal in " + mistakes + " tests!" + reset);
        else System.out.println(ok + "All OK!" + reset);

    }

    private static void checksMistakes(String formulaForTest, int part) {
        /* Розділення між формулами. Сама формула. Операція видалення пробілів для зручності підрахунку.*/
        System.out.println("----------------------------");
        System.out.println("[" + sss + formulaForTest + reset + "]");
        formulaForTest = deleteSpaces(formulaForTest);

        /* видає помилку, якщо пустий рядок */
        getMistakeIfNeed(formulaForTest.length() < 1, "Don't have the text for equal!");

        /* for "^^"*/
        for (int i = 0; i < formulaForTest.length(); i++) {
            char sym = formulaForTest.charAt(i);
            if (sym == '^' || sym == '/' || sym == '*' || sym == ',' || sym == '.' || sym == '=') {
                if (i == 0 || i == formulaForTest.length() - 1) {
                    System.out.println("Problem with " + i + " position");
                    mist = true;
                } else {
                    getMistakeIfNeed(!isValue(formulaForTest.charAt(i - 1)), ("Don't have value left from position " + i));
                    getMistakeIfNeed(!isValue(formulaForTest.charAt(i + 1)), "Don't have value right from position " + i);
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

    private static boolean isValue(char symbol) {
        for (int i = 'a'; i <= 'z'; i++) {
            if (symbol == i) return true;
        }
        for (int i = '1'; i <= '9'; i++) {
            if (symbol == i) return true;
        }
        return symbol == '0';
    }

    private static void getMistakeIfNeed(boolean formulaForTest, String x) {
        if (formulaForTest) {
            System.out.println(x);
            mist = true;
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

}