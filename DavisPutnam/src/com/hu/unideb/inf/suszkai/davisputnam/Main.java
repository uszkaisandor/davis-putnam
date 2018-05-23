package com.hu.unideb.inf.suszkai.davisputnam;

import java.util.Scanner;
import java.util.Set;

/**
 * The purpose of the Main class is reading the input and call the Davis-Putnam algorithm.
 * Input format examples (CNF):
 * (x)
 * ((r)∧(¬r))
 * ((r)∧(¬r∨¬s)∧(s)∧(p∨s))
 */

public class Main {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("The formula is: ");
        //  Read the formula from standard input:
        String inputFormula = sc.nextLine();
        //  Parse the input string to build a list of clauses.
        Set<Clause> clauses = Parser.parseFormula(inputFormula);

        if (DavisPutnamAlgorithm.runDavisPutnam(clauses)) {
            System.out.println("The formula is satisfiable");
        } else {
            System.out.println("The formula is unsatisfiable");
        }

        return;
    }


}