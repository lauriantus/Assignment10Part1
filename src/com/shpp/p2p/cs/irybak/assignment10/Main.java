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
        getsNumbersInFormula(args);
        /* FIXME: Prints operands for the test and result formula */
        for (Character operand : operands) {
            System.out.print("[" + operand + "] ");
        }
        System.out.println();
        System.out.println(totalFormula);

        for (int i = totalFormula.size() - 1; i > 0; i--) {
            if (totalFormula.get(i).equals('-')) {
                totalFormula.set(i + 1, (Double) totalFormula.get(i + 1) * -1);
                totalFormula.remove(i);
            } else if (totalFormula.get(i).equals('+')) {
                totalFormula.remove(i);
            }
            if (totalFormula.get(i).equals('^')) {
                Double value = (Double) totalFormula.get(i - 1);
                Double exponent = (Double) totalFormula.get(i + 1);
                totalFormula.set(i, math(value, exponent, '^'));
                totalFormula.remove(i + 1);
                totalFormula.remove(i - 1);
            }
        }
        System.out.println(totalFormula);

        for (Character operand : operands) {
            System.out.print("[" + operand + "] ");
        }
        System.out.println();
        for (int i = 1; i < totalFormula.size(); i++) {
            i = mathSimple(i, '*');
            i = mathSimple(i, '/');
        }
        setsPlusMinus();
        System.out.println(totalFormula);
        for (int i = 0; i < totalFormula.size(); i++) {
            i = mathSimple(i, '+');
            i = mathSimple(i, '-');
        }
    }

    private static void getsNumbersInFormula(String[] args) {
        // Gets formula from the args
        String formula = args[0];
        // Gets variables and values
        String[] variables2 = new String[args.length - 1];
        System.arraycopy(args, 1, variables2, 0, args.length - 1);
        // Gets formula with value instead of variables
        parsingFormula(deleteSpaces(formula));
        parsingVariables(variables2);

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
//                if (num.isEmpty()) {
//                    // Adds + if this is positive number
//                    if (i == 0 || formula.charAt(i - 1) != '-') {
//                        num.append('+');
//                    }
//                    // Adds - if this is negative number
//                    if (i != 0 && formula.charAt(i - 1) == '-') {
//                        num.append('-');
//                    }
//                }
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