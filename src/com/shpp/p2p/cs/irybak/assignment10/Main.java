package com.shpp.p2p.cs.irybak.assignment10;

import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;

import static java.lang.Double.isNaN;

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
        getsNumbersInFormula(args);
        raiseToPower();
        for (int i = 1; i < totalFormula.size(); i++) {
            i = mathSimple(i, '*');
            i = mathSimple(i, '/');
        }
        System.out.println(totalFormula);
        for (int i = 0; i < totalFormula.size(); i++) {
            i = mathSimple(i, '+');
            i = mathSimple(i, '-');
        }
    }

    /**
     * Checks the whole equation from the end. When finding a sign of exponentiation, performs the operation,
     * removing the used elements. There is a reaction to raising a negative number to a fractional power.
     */
    private static void raiseToPower() {
        // Looks for the exponentiation from the end of the equation
        for (int i = totalFormula.size() - 1; i > 0; i--) {
            if (totalFormula.get(i).equals('^')) {
                // Raising to the degree
                Double value = (Double) totalFormula.get(i - 1);
                Double exponent = (Double) totalFormula.get(i + 1);
                totalFormula.set(i, Math.pow(value, exponent));

                // Checks for correctness.
                isCorrectRaise(i, value, exponent);

                // Removes those elements that were used
                totalFormula.remove(i + 1);
                totalFormula.remove(i - 1);
            }
        }
    }

    private static void isCorrectRaise(int i, Double value, Double exponent) {
        // When you try to raise a negative number to a fractional power.
        if (isNaN((Double)totalFormula.get(i))) {
            System.out.println(sss + value + " ^ " + exponent + reset);
            System.out.println(err + "The operation is undefined! You have probably tried to raise a " +
                    "negative number to a non-integer degree." + reset);
            System.exit(0);
        }
    }

    /**
     * Отримує формулу і видаляє пробіли з неї для більшої зручності. Після цього розподіляє кожен елемент, а саме -
     * операнди, числа та змінні.
     * Записує масив значень та масив для змінних. А потім заміняє змінні в формулі на їх значення.
     *
     * @param args параметри що подаються на програму у форматі [формула, змінна = значення, змінна = значення тощо]
     */
    private static void getsNumbersInFormula(String[] args) {
        // Gets formula with value instead of variables
        String formula = args[0];
        parsingFormula(deleteSpaces(formula));

        // Gets variables and values
        String[] preVariables = new String[args.length - 1];
        System.arraycopy(args, 1, preVariables, 0, args.length - 1);
        for (String variable : preVariables) {
            values.add(getNumber(deleteSpaces(variable)));
        }

        // Sets value instead the variable
        for (int i = 0, j = 0; i < totalFormula.size(); i++) {
            if (variables.get(j).equals(totalFormula.get(i))) {
                totalFormula.set(i, values.get(j));
                j++;
            }
        }
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

    private static int mathSimple(int i, char obj) {
        if (totalFormula.get(i).equals(obj)) {
            Double value1 = (Double) totalFormula.get(i - 1);
            Double value2 = getNegativeValue(i);
            totalFormula.set(i, math(value1, value2, obj));
            totalFormula.remove(i + 1);
            totalFormula.remove(i - 1);
            return 0;
        }
        return i;
    }

    private static Double getNegativeValue(int i) {
        Double value2;
        if (totalFormula.get(i + 1).equals('-')) {
            totalFormula.remove(totalFormula.get(i + 1));
            value2 = -(Double) totalFormula.get(i + 1);
        } else {
            value2 = (Double) totalFormula.get(i + 1);
        }
        return value2;
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


    static ArrayList<Character> operands = new ArrayList<>();
    static ArrayList<String> variables = new ArrayList<>();
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
            if (isNumber(sym) || sym == ',' || sym == '.') {// з цифр створюємо число
                // Adds '.' or ',' in the number if this is not integer
                if (sym == ',' || sym == '.') {
                    num.append('.');
                }
                // When symbol this is number add it to the builder(result + or - and number. Example: -0.23)
                if (isNumber(sym)) {
                    num.append(sym);
                }
                // When this is last symbol of formula or next symbol operand or variable break add symbols to the number
                if (i == (formula.length() - 1) || isValue(formula.charAt(i + 1)) || isOperand(formula.charAt(i + 1))) {
                    totalFormula.add(Double.parseDouble(num.toString()));
                    num = new StringBuilder();
                    // When after the number we have the variable(without operand)(adds * in the list with operands)
                    if (isValue(formula.charAt(i + 1))) {
                        shortForm(isValue(formula.charAt(i + 1)));
                    }
                }
                /* Якщо це змінна, тоді додаємо її до списку змінних */
            } else if (isValue(sym)) {
                // Adds + if this is positive number
                if (i == 0 || formula.charAt(i - 1) == '+') {
                    num.append('+');
                }
                // Adds - if this is negative number
                if (i != 0 && formula.charAt(i - 1) == '-') {
                    num.append('-');
                }
                num.append(sym);
                variables.add(num.toString());
                totalFormula.add(num.toString());
                num = new StringBuilder();
                /* для випадку коли стоїть цифра, а за нею літера(пропущене множення) */
                if (i < formula.length() - 1) {
                    shortForm(isNumber(formula.charAt(i + 1)));
                }
            } else if (isOperand(sym)) {
                operands.add(sym);
                totalFormula.add(sym);
                sym = formula.charAt(i + 1);
                if (isOperand(sym) && (sym == '-' || sym == '+')) {
                    num.append(sym);
                    i++;
                }
            }
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
            } else if (isNumber(sym) || sym == '-' || sym == '.' || sym == ',') {
                sym = getDot(sym);
                value.append(sym);
            }
        }
        return null;
    }

    private static char getDot(char sym) {
        if (sym == ',') {
            sym = '.';
        }
        return sym;
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

    protected static boolean isNumber(char sym) {
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