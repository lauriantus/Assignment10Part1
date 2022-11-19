package com.shpp.p2p.cs.irybak.assignment10;

import java.util.ArrayList;

import static java.lang.Double.isNaN;

public class Main {
    public static final String RED = "\u001B[31m";
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String VIOLET = "\u001B[36m";
    private static final String BLUE = "\u001B[34m";

    static ArrayList<Character> operands = new ArrayList<>();
    static ArrayList<String> variables = new ArrayList<>();
    static ArrayList<Double> numbers = new ArrayList<>();
    static ArrayList<Double> values = new ArrayList<>();
    static ArrayList<Object> totalFormula = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println(getsResult(args));
    }

    public static Double getsResult(String[] args) {
        getsNumbersInFormula(args);
        raiseToPower();
        for (int i = 1; i < totalFormula.size(); i++) {
            i = mathSimple(i, '*');
            i = mathSimple(i, '/');
        }
        for (int i = 0; i < totalFormula.size(); i++) {
            i = mathSimple(i, '+');
            i = mathSimple(i, '-');
        }
        if (totalFormula.get(0).equals('(')) {
            totalFormula.remove(0);
            totalFormula.remove(totalFormula.size() - 1);
        }
        return (Double) totalFormula.get(0);
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

    /**
     * Prints the formula
     */
    private static void printFormula() {
        for (Object element : totalFormula) {
            if (element.equals('-') || element.equals('+') || element.equals('*') || element.equals('/')
                    || element.equals('^') || (Double) element >= 0) {
                System.out.print(element + " ");
            } else {
                System.out.print("(" + element + ") ");
            }
        }
        System.out.println();
    }

    /**
     * Checks for the presence of raising a negative number to a fractional power.
     * Outputs a text warning and exits the program.
     *
     * @param i        element in order in the equation
     * @param value    A number that is raised to a power.
     * @param exponent The degree to which a number is raised
     */
    private static void isCorrectRaise(int i, Double value, Double exponent) {
        // When you try to raise a negative number to a fractional power.
        if (isNaN((Double) totalFormula.get(i))) {
            System.out.println(VIOLET + value + " ^ " + exponent + RESET);
            System.out.println(RED + "The operation is undefined! You have probably tried to raise a " +
                    "negative number to a non-integer degree." + RESET);
            System.exit(0);
        }
    }

    /**
     * Gets the formula and removes spaces from it for more convenience. Then allocates each element, namely operands,
     * numbers and variables. Writes an array of values and an array for variables. And then replaces the variables
     * in the formula with their values.
     *
     * @param args parameters that are submitted to the program in the format [formula, variable = value,
     *             variable = value, etc.]
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
            case '*' -> {
                return value1 * value2;
            }
            case '/' -> {
                return value1 / value2;
            }
            case '+' -> {
                return value1 + value2;
            }
            default -> {
                return value1 - value2;
            }
        }
    }

    /**
     * Першим символом можу бути лише змінна або число
     */
    private static void parsingFormula(String formula) {
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
                if (i == (formula.length() - 1) || isVariable(formula.charAt(i + 1)) || isOperand(formula.charAt(i + 1))) {
                    totalFormula.add(Double.parseDouble(num.toString()));
                    num = new StringBuilder();
                    // When after the number we have the variable(without operand)(adds * in the list with operands)
                    if (isVariable(formula.charAt(i + 1))) {
                        shortForm(isVariable(formula.charAt(i + 1)));
                    }
                }
                /* Якщо це змінна, тоді додаємо її до списку змінних */
            } else if (isVariable(sym)) {
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
                    shortForm(isVariable(formula.charAt(i + 1)));
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

    static boolean isVariable(char symbol) {
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