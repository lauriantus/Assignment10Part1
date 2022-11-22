package com.shpp.p2p.cs.irybak.assignment10;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Double.*;

/**
 * <h1>Calculator Simple</h1>
 * <h3>Task 10:
 *     <a href = https://scs.p2p.programming.org.ua/material/10/assignment10-calc1>Link</a></h3>
 * <p>
 * At the input of the program (read about main and args if you haven't yet), the first parameter is a mathematical
 * expression. For example: 1 + a * 2 (it can be much more complicated and longer!) The rest of the parameters are
 * optional, and look like a name = value, for example a = 2 The result of this program will be the number 5.
 * In total, you need to support the following operators: -, +, /, *, ^(exponentiation)
 * </p>
 *
 * @author Ivan
 * @since 1.0
 */
public class Assignment10Part1 {
    /**
     * Colors for console
     */
    public static final String RED = "\u001B[31m";
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String VIOLET = "\u001B[36m";
    /**
     * The operands that occur in the equation in the desired order.
     */
    static ArrayList<Character> operands = new ArrayList<>();
    /**
     * Variables that occur in the equation in the desired order.
     */
    static ArrayList<String> variables = new ArrayList<>();
    /**
     * Values for variables set by the user.
     */
    static HashMap<String, Double> values = new HashMap<>();
    /**
     * The general formula in decomposed form.
     */
    static ArrayList<Object> totalFormula = new ArrayList<>();

    /**
     * Gets the result of solving the equation that is specified at startup.
     *
     * @param args The equation that is set at startup.
     */
    public static void main(String[] args) {
        getsResult(args);
    }

    /**
     * The final method for summing all operations and displaying intermediate results on the console.
     * It takes an equation as input in the form ["equal" "variable = value" .... ] and solves the operations of
     * exponentiation, multiplication and division, sum and subtraction in the desired order.
     * You can use two identical variable names, but for this you need to set a separate value for each (it can be
     * the same). Brackets are used to improve the visual perception of the program.
     *
     * @param args Equation and variables with values in the form -> equation variable = value
     * @return Fractional type of value. For example, 1.0, or 2.2, or -34.0, etc.
     */
    public static Double getsResult(String[] args) {
        System.out.println(args[0]);        // prints the initial formula
        getsNumbersInFormula(args);         // substitutes the value into the formula
        raiseToPower();                     // Raises the value to the power
        simpleArithmeticOperations();       // Performs multiplication, division, addition and subtraction operations
        return (Double) totalFormula.get(0);
    }

    /**
     * Performs multiplication and division, addition and subtraction operations.
     * Prints the result of its actions after each block.
     */
    private static void simpleArithmeticOperations() {
        // Performs multiplication and division operations
        for (int i = 1; i < totalFormula.size(); i++) {
            i = mathSimple(i, '*');
            i = mathSimple(i, '/');
        }
        // Performs addition and subtraction operations
        for (int i = 0; i < totalFormula.size(); i++) {
            i = mathSimple(i, '+');
            i = mathSimple(i, '-');
        }
        System.out.println(totalFormula.get(0));
    }

    /**
     * Checks the whole equation from the end. When finding a sign of exponentiation, performs the operation,
     * removing the used elements. There is a reaction to raising a negative number to a fractional power.
     * And print result of method.
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
     * Outputs the formula to the console in a convenient form. It is recommended to use it to see the order of actions.
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
     * in the formula with their values. And print result of method.
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
            variable = deleteSpaces(variable);
            values.put(String.valueOf(variable.charAt(0)), getNumber(variable));
        }

        // Sets value instead the variable
        for (int i = 0; i < totalFormula.size(); i++) {
            String currentTot = totalFormula.get(i).toString();
            if (values.containsKey(currentTot)) {
                totalFormula.set(i, values.get(currentTot));
            }
        }
        printFormula();
    }

    /**
     * It is part of the main method {@link #simpleArithmeticOperations()} Performs the algorithm of basic arithmetic
     * operations in the equation.
     * Adapted to remove elements and replace them with the result.
     *
     * @param i   element in order in the equation
     * @param obj operand( * or / or + or - )
     * @return current element in order in the equation
     */
    private static int mathSimple(int i, char obj) {
        if (totalFormula.get(i).equals(obj)) {
            // Gets the value of the first and second element for the arithmetic operation.
            Double value1 = (Double) totalFormula.get(i - 1);
            Double value2 = (Double) totalFormula.get(i + 1);

            // At the desired position, performs the selected operation by deleting the used elements and
            // inserting the result instead.
            totalFormula.set(i, math(value1, value2, obj));
            totalFormula.remove(i + 1);
            totalFormula.remove(i - 1);
            return 0;
        }
        return i;
    }

    /**
     * It is part of the main method {@link #mathSimple} Performs the algorithm of basic arithmetic operations
     * in the equation.
     *
     * @param value1  The first element of the arithmetic operation.
     * @param value2  The second element of the arithmetic operation.
     * @param operand Type of logical operation.
     * @return The result of performing a logical operation with the first and second values.
     */
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
     * Parses the formula into an array of variables and operands. Also creates a general formula with numbers and all
     * variables, which can be further edited. Creates a hashmap of values for variables. Thus, a system has been
     * created for convenient management of all elements of the equation
     *
     * @param formula Raw formula, but with spaces removed.
     */
    private static void parsingFormula(String formula) {
        StringBuilder num = new StringBuilder();
        for (int i = 0; i < formula.length(); i++) {
            char sym = formula.charAt(i);
            /* if it is a number, then add all digits from the number to the variable, otherwise move on */
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
                if (i == (formula.length() - 1) || isVariable(formula.charAt(i + 1))
                        || isOperand(formula.charAt(i + 1))) {
                    totalFormula.add(Double.parseDouble(num.toString()));
                    num = new StringBuilder();
                    // When after the number we have the variable(without operand)(adds * in the list with operands)
                    if (i != (formula.length() - 1) && isVariable(formula.charAt(i + 1))) {
                        shortForm(isVariable(formula.charAt(i + 1)));
                    }
                }
                /* If it is a variable, then add it to the list of variables. */
            } else if (isVariable(sym)) {
                num.append(sym);                    // adds a digit to a number.
                variables.add(num.toString());      // adds a variable to the list.
                totalFormula.add(num.toString());   // adds a variable to the general formula.
                num = new StringBuilder();          // zeroes the value of the constructor.

                /* for the case when there is a digit followed by a letter(missing multiplication) */
                if (i < formula.length() - 1) {
                    shortForm(isNumber(formula.charAt(i + 1)));
                    shortForm(isVariable(formula.charAt(i + 1)));
                }
            } else if (isOperand(sym)) {
                // Executed when the first character is minus and the next is a variable.
                if (i == 0) {
                    totalFormula.add(Double.parseDouble("-1"));
                    operands.add('*');
                    continue;
                }
                operands.add(sym);
                totalFormula.add(sym);
            }
        }
    }

    /**
     * Searches for a number to the right of the "equal" sign.
     * If it is found, it adds it, otherwise it generates an error.
     *
     * @param variable The record that the user provides(Variable = value)
     * @return The value of the variable
     */
    private static Double getNumber(String variable) {
        /* get value for every variable */
        StringBuilder value = new StringBuilder();
        for (int i = variable.length(); i > 0; i--) {
            char sym = variable.charAt(i - 1);
            // Searches for a number to the right of the "equal" sign
            if (sym == '=') {
                value.reverse();
                return Double.valueOf(value.toString());
                // Triggered when there is a fractional number
            } else if (isNumber(sym) || sym == '-' || sym == '.' || sym == ',') {
                sym = getDot(sym);
                value.append(sym);
            }
        }
        System.out.println(RED + "Something is wrong with the type of writing variable = value" + RESET);
        System.exit(0);
        return null;
    }

    /**
     * If a comma is written, the methods will correct it to a period.
     *
     * @param sym The symbol to be checked.
     * @return Any character except comma can be used.
     */
    private static char getDot(char sym) {
        if (sym == ',') {
            sym = '.';
        }
        return sym;
    }

    /**
     * Used to write NumberVariable(Number * Variable). In the future, it can be used for Variable * Variable.
     *
     * @param place The presence of such an unusual compound.
     */
    private static void shortForm(boolean place) {
        if (place) {
            operands.add('*');
            totalFormula.add('*');
        }
    }

    /**
     * Checks if the character is equal to one of the signs of simple arithmetic.
     *
     * @param sym A symbol that needs to be checked.
     * @return If everything is true - it is true, otherwise - it is a false.
     */
    static boolean isOperand(char sym) {
        return sym == '^' || sym == '/' || sym == '*' || sym == '+' || sym == '-';
    }

    /**
     * Checks if it is a number.
     *
     * @param sym A symbol that needs to be checked.
     * @return If everything is true - it is true, otherwise - it is a false.
     */
    protected static boolean isNumber(char sym) {
        return (sym >= '1' && sym <= '9') || sym == '0';
    }

    /**
     * Checks if it is a letter.
     *
     * @param symbol A symbol that needs to be checked.
     * @return If everything is true - it is true, otherwise - it is a false.
     */
    static boolean isVariable(char symbol) {
        for (int i = 'a'; i <= 'z'; i++) {
            if (symbol == i) return true;
        }
        return false;
    }

    /**
     * It searches for all spaces and removes them.
     *
     * @param line any line
     * @return line without spaces
     */
    public static String deleteSpaces(String line) {
        StringBuilder sNew = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) != ' ') {
                sNew.append(line.charAt(i));
            }
        }
        return sNew.toString();
    }

}