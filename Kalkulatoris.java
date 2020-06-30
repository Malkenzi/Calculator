package Calculator;

import java.util.Scanner;

public class Kalkulatoris {

    public static void main(String[] args) {
        String line = "";
        int result = 0;
        System.out.println("___ JAVA CASIO ___");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            line = scanner.nextLine();
            if (!line.equals("q")) {
                try {
                    result = compute(line);
                    System.out.println(result);
                } catch (ChybaDruhaZatvorka chybaDruhaZatvorka) {
                    System.out.println("Chyba druha zatvorka");
                }
            } else break;
        }
    }

    private static int compute(String formula) throws ChybaDruhaZatvorka {
        String result = formula.replaceAll(" ","");
        String[] operators = new String[]{"/","^","*","+","-"};
        while (hasBraces(result)) {
            result = computeBraces(result);
        }
        while (hasOperator(result)) {

            for (String operator: operators) {
                int index = result.indexOf(operator);

                if (!(index == -1)) {
                    result = computeOperator(result, index); //
                }
            }
        }
        return Integer.parseInt(result);
    }

    private static boolean hasOperator(String formula) {
        return  formula.contains("+") ||
                (formula.contains("-") && formula.indexOf("-") != 0) ||
                formula.contains("*") ||
                formula.contains("^") ||
                formula.contains("/");
    }

    private static String computeOperator(String formula, int index) {
        int left = findLeft(formula, index);
        int right = findRight(formula, index);
        int leftIndex = indexOfLeft(formula, index);
        int rightIndex = indexOfRight(formula, index);
        int result = 0;
        switch (formula.charAt(index)) {
            case '+': result = left + right; break;
            case '-': result = left - right; break;
            case '*': result = left * right; break;
            case '/': result = left / right; break;
            case '^': result = power(left,right); break;
        }
        return formula.substring(0,leftIndex) + result + formula.substring(rightIndex+1);
    }

    private static int power(int left, int right) {
        int result = 1;
        for (int i = 0; i < right; i++) {
            result = result * left;
        }
        return result;
    }

    private static int indexOfLeft(String formula, int index) {
        int before = index-1;
        int result = before;
        if (before < 0) return 0;
        while (Character.isDigit(formula.charAt(before))) {
            result = before;
            if (result == 0) break;
            before = before - 1;
        }
        return result;
    }

    private static int indexOfRight(String formula, int index) {
        int after = index+1;
        int result = after;
        if (after >= formula.length()) return 0;
        while (Character.isDigit(formula.charAt(after))) {
            result = after;
            if (result == formula.length()-1) break;
            after = after + 1;
        }
        return result;
    }

    private static int findRight(String formula, int index) {
        return Integer.parseInt(formula.substring(index+1,indexOfRight(formula,index)+1));
    }

    private static int findLeft(String formula, int index) {
        if (indexOfLeft(formula,index) == index) return 0;
        else return Integer.parseInt(formula.substring(indexOfLeft(formula,index), index));
    }

    private static boolean hasBraces(String formula) {
        return formula.contains("(");
    }

    private static String computeBraces(String formula) throws ChybaDruhaZatvorka {
        if (!formula.contains("(")) return formula;
        else {
            int start = formula.indexOf("(");
            int end = formula.indexOf(")");
            if (end == -1) throw new ChybaDruhaZatvorka();
            int innerValue = compute(formula.substring(start+1, end));
            String result = formula.substring(0,start) + innerValue + formula.substring(end+1);
            return result;
        }
    }
}
