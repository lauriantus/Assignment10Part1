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
 * - Parsing на елементи
 * Розбиває все рівняння на елементи для зручності видалення кожного з них при виконанні математичних операцій
 * <p>
 * - Робота зі степенями
 * Для врахування пріоритету необхідно спочатку виконати саме ці операції. До того ж з кінця рівняння.
 * Пошук першого степеня з кінця -> Виконання піднесення до степеня -> заміна трьох елементів на один результат ->
 * Пошук першого степеня з кінця -> ...  -> Степінь не знайдена -> завершення циклу
 * <p>
 * - Робота із множенням та діленням
 * Починаючи з самого початку виконуємо всі операції з множенням та діленням. Цикл буде завершено коли таких операцій
 * не буде в рівнянні
 * - Перевірка на наявність двох знаків підряд(наприклад, +-7+4) та заміна враховуючи правила заміни(-7+4)
 * - Робота із плюсами та мінусам
 * В результаті отримуємо вираз виду 4 = 5, або одразу результат.
 * - Якщо результат ще не отримано, то робимо операцію для видалення знаку =, тобто переносимо ліву частину в праву зі
 * зміною знаку й отримуємо вже результат.
 */
public class Main {
    public static String err = "\u001B[31m";
    public static String reset = "\u001B[0m";
    public static String ok = "\u001B[32m";
    public static String sss = "\u001B[36m";

    public static void main(String[] args) {
        String formula = args[0];
        Test.tests(deleteSpaces(formula)); // FIXME
        String[] variables = new String[args.length - 1];
        System.arraycopy(args, 1, variables, 0, args.length - 1);

        parsingFormula(formula);
        parsingVariables(variables);
        printFormula();
        setFormulaWithValues();
        printFormula();
        for (int i = totalFormula.size() - 1; i > 0; i--) {
            if (totalFormula.get(i).equals('^')) {
                Double value = (Double) totalFormula.get(i - 1);
                Double exponent = (Double) totalFormula.get(i + 1);
                totalFormula.set(i, math(value, exponent, '^'));
                totalFormula.remove(i + 1);
                totalFormula.remove(i - 1);
            }
        }
        printFormula();
        for (int i = 1; i < totalFormula.size(); i++) {
            i = mathSimple(i, '*');
            i = mathSimple(i, '/');
        }
        printFormula();
        setsPlusMinus();
        for (int i = 0; i < totalFormula.size(); i++) {
            i = mathSimple(i, '+');
            i = mathSimple(i, '-');
        }
        printFormula();
    }

    private static void setsPlusMinus() {
        for (int i = 0; i < totalFormula.size(); i++) {
            if (totalFormula.get(i).equals('+') && totalFormula.get(i + 1).equals('+')) {
                totalFormula.remove(i + 1);
                i = 0;
            }
            if (totalFormula.get(i).equals('+') && totalFormula.get(i + 1).equals('-')
                    || totalFormula.get(i).equals('-') && totalFormula.get(i + 1).equals('+')) {
                totalFormula.remove(i);
                i = 0;
            }
            if (totalFormula.get(i).equals('-') && totalFormula.get(i + 1).equals('-')) {
                totalFormula.remove(i + 1);
                totalFormula.set(i, '+');
                i = 0;
            }
        }
    }

    public static void printFormula() {
        for (Object o : totalFormula) {
            System.out.print(o + " ");
        }
        System.out.println();
    }

    private static int mathSimple(int i, char obj) {
        if (totalFormula.get(i).equals(obj)) {
            Double value1 = (Double) totalFormula.get(i - 1);
            Double value2 = (Double) totalFormula.get(i + 1);
            totalFormula.set(i, math(value1, value2, obj));
            totalFormula.remove(i + 1);
            totalFormula.remove(i - 1);
            return 0;
        }
        return i;
    }

    private static Double math(Double value1, Double value2, Character operand) {
        switch (operand) {
            case '^' -> {
                return Math.pow(value1, value2);
            }
            case '*' -> {
                return value1 * value2;
            }
            case '/' -> {
                return value1 / value2;
            }
            case '+' -> {
                return value1 + value2;
            }
            case '-' -> {
                return value1 - value2;
            }
            default -> {
                System.out.println("Something wrong with operands.");
                System.exit(0);
                return 0.0;
            }
        }
    }


    private static void setFormulaWithValues() {
        for (int i = 0, j = 0; i < totalFormula.size(); i++) {
            if (variables.get(j).equals(totalFormula.get(i))) {
                totalFormula.set(i, values.get(j));
                j++;
            }
        }
    }

    static ArrayList<Character> operands = new ArrayList<>();
    static ArrayList<Character> variables = new ArrayList<>();
    static ArrayList<Double> numbers = new ArrayList<>();
    static ArrayList<Double> values = new ArrayList<>();
    static ArrayList<Object> totalFormula = new ArrayList<>();


    /**
     * Першим символом можу бути лише змінна або число
     */
    private static void parsingFormula(String formula) {
        System.out.println("\u001B[35m" + formula + reset);
        System.out.println("--------------------------------------------");
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < formula.length(); i++) {
            char sym = formula.charAt(i);           // покращуємо швидкодію, завдяки зменшенню виклику метода
            /* якщо це число, то додаємо в змінну всі цифри з числа, інакше переходимо далі*/
            if (isNumber(sym)) {
                num.append(sym);            // з цифр створюємо число
                /* для випадку коли стоъть цифра, а за нею змінна(необхідно завершити вводити число) */
                if ((i + 1) < formula.length() && !isNumber(formula.charAt(i + 1))) {
                    numbers.add(Double.parseDouble(num.toString()));
                    totalFormula.add(numbers.get(numbers.size() - 1));
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
            } else if (isOperand(sym) || isOtherOperands(sym)) {
                operands.add(sym);
                totalFormula.add(sym);
            }
        }
    }

    private static void parsingVariables(String[] variables) {
        for (String variable : variables) {
            values.add(getNumber(deleteSpaces(variable)));
        }
    }

    private static Double getNumber(String variable) {
        /* get value for every variable */
        StringBuilder value = new StringBuilder();
        for (int i = variable.length(); i > 0; i--) {
            char sym = variable.charAt(i - 1);
            if (sym == '=') {
                value.reverse();
                return Double.valueOf(value.toString());
            } else if (isNumber(sym)
                    || sym == '-' || sym == '.' || sym == ',') {
                if (sym == ',') {
                    sym = '.';
                }
                value.append(sym);
            }
        }
        return null;
    }

    private static void shortForm(boolean formula) {
        if (formula) {
            operands.add('*');
            totalFormula.add('*');
        }
    }

    static boolean isOperand(char sym) {
        return sym == '^' || sym == '/' || sym == '*' || sym == '+' || sym == '-';
    }

    static boolean isOtherOperands(char sym) {
        return sym == ',' || sym == '.' || sym == '=';
    }

    private static boolean isNumber(char sym) {
        return (sym >= '1' && sym <= '9') || sym == '0';
    }

    static boolean isValue(char symbol) {
        for (int i = 'a'; i <= 'z'; i++) {
            if (symbol == i) return true;
        }
        return false;
    }

    public static String deleteSpaces(String s) {
        StringBuilder sNew = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != ' ') {
                sNew.append(s.charAt(i));
            }
        }
        return sNew.toString();
    }


}