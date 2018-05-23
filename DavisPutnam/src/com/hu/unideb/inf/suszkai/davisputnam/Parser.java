package com.hu.unideb.inf.suszkai.davisputnam;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  This class is the parser for the input file
 */
public class Parser {


    //  Parse the parameter and return with a clause set
    public static Set<Clause> parseFormula(String input){

        String[] clauseStrings = input.substring(1,input.length()-1).split("∧");
        Set<Clause> clauses = new HashSet<>();
        for (String clauseString:clauseStrings) {
            List<Literal> literals = new ArrayList<>();
            String[] literalStrings = clauseString.substring(1,clauseString.length()-1).split("∨");
            for (String literalString:literalStrings) {
                literals.add(new Literal(literalString));
            }
            clauses.add(new Clause(literals));
        }
        return clauses;

    }


}
