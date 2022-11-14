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
            "0*+1",
            "d",
            "acd",
            "A",
            "a"
    };
    static int mistakes = 0;
    static boolean mist;

    public static void main(String[] args) {
        String formula = args[0];
        String[] variables = new String[args.length];
        System.arraycopy(args, 1, variables, 1, args.length - 1);

        parsingFormula(formula);
        parsingVariables(variables);
    }

    static ArrayList<Character> operands = new ArrayList<>();
    static ArrayList<Character> variables = new ArrayList<>();
    static ArrayList<Integer> numbers = new ArrayList<>();
    static ArrayList<Integer> values = new ArrayList<>();
    static ArrayList<Object> totalFormula = new ArrayList<>();

    /**
     * Першим символом можу бути лише змінна або число
     *
     * @param formula
     */
    private static void parsingFormula(String formula) {
        System.out.println("\u001B[35m" + formula + reset);
        System.out.println("--------------------------------------------");
        StringBuilder num = new StringBuilder();
        char flag = 0;
        for (int i = 0; i < formula.length(); i++) {
            char sym = formula.charAt(i);           // покращуємо швидкодію, за рахунок зменшення виклику метода
            /* якщо це число, то дадаємо в змінну всі цифри з числа, інакше переходимо далі*/
            if (isNumber(sym)) {
                num.append(sym);            // з цифр створюємо число
                /* для випадку коли стоъть цифра, а за нею змінна(необхідно завершити вводити число) */
                if ((i + 1) < formula.length() && !isNumber(formula.charAt(i + 1))) {
                    numbers.add(Integer.parseInt(num.toString()));
                    totalFormula.add(num);
                    num = new StringBuilder();
                }
                /* для випадку коли стоїть цифра, а за нею літера(пропущене множення) */
                if ((i + 1) < formula.length()) {
                    shortForm(isValue(formula.charAt(i + 1)));
                }
                /* Якщо це змінна, тоді додаємо її до списку змінних */
            } else if (isValue(sym)) {
                variables.add(sym);
                totalFormula.add(sym);
                /* для випадку коли стоїть цифра, а за нею літера(пропущене множення) */
                if ((i + 1) < formula.length()) {
                    shortForm(isNumber(formula.charAt(i + 1)));
                }
            } else if (isOperand(sym)) {
                operands.add(sym);
                totalFormula.add(sym);
            }
        }
        outTestsOnScreen();
    }

    private static void parsingVariables(String[] variables) {
        for (String variable : variables) {
            values.add(getNumber(deleteSpaces(variable)));
        }
    }

    private static Integer getNumber(String variable) {
        /* get value for every variables */
        StringBuilder value = new StringBuilder();
        for (int i = variable.length(); i > 0; i--) {
            char sym = variable.charAt(i - 1);
            if (sym == '=') {
                value.reverse();
                return Integer.valueOf(value.toString());
            } else if (isNumber(sym)) {
                value.append(sym);
            }
        }
        return null;
    }

    private static void outTestsOnScreen() {
        for (Object o : totalFormula) {
            System.out.print(o + " ");
        }
        System.out.println();
        for (Character operand : operands) {
            System.out.print("[" + operand + "] ");
        }
        System.out.println();
        for (Character variable : variables) {
            System.out.print("[" + variable + "] ");
        }
        System.out.println();
        for (Integer number : numbers) {
            System.out.print("[" + number + "] ");
        }
    }

    private static void shortForm(boolean formula) {
        if (formula) {
            operands.add('*');
            totalFormula.add('*');
        }
    }

    private static boolean isOperand(char sym) {
        return sym == '^' || sym == '/' || sym == '*' || sym == ','
                || sym == '.' || sym == '=' || sym == '+' || sym == '-';
    }

    private static boolean isNumber(char sym) {
        return (sym >= '1' && sym <= '9') || sym == '0';
    }

    private static boolean isValue(char symbol) {
        for (int i = 'a'; i <= 'z'; i++) {
            if (symbol == i) return true;
        }
        return false;
    }

    private static String deleteSpaces(String s) {
        StringBuilder sNew = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ') {
                sNew.append(s.charAt(i));
            }
        }
        return sNew.toString();
    }

    private static void tests() {
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
            if (isOperand(sym)) {
                if (i == 0 || i == formulaForTest.length() - 1) {
                    System.out.println("Problem with " + i + " position");
                    mist = true;
                } else if (sym != '+' && sym != '-') {
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


}