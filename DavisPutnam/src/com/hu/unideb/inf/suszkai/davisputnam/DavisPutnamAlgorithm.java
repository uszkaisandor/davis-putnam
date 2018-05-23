package com.hu.unideb.inf.suszkai.davisputnam;

import java.util.*;

/**
 * Implement the original Davis-Putnam algorithm (1960)
 */

public class DavisPutnamAlgorithm {


    public static Boolean runDavisPutnam(Set<Clause> clauses) {
        Set<Clause> clauseSetBeforeIteration;
        Boolean isAlgorithmRunning = true;
        //  The algorithm stops if reached EMPTY clause, the clause set is empty.
        //  If there isn't any applicable rule
        while (isAlgorithmRunning) {
            clauseSetBeforeIteration = new HashSet<>(clauses);
            //  Single literal rule
            clauses = singleLiteralRule(clauses);
            switch (clauseSetState(clauses)) {
                case 0:
                    return false;
                case 1:
                    return true;
            }
            //  Pure literal rule
            clauses = pureLiteralRule(clauses);
            switch (clauseSetState(clauses)) {
                case 0:
                    return false;
                case 1:
                    return true;
            }
            //  Resolution
            clauses = resolution(clauses);
            switch (clauseSetState(clauses)) {
                case 0:
                    return false;
                case 1:
                    return true;
            }

            if (!hasAnyRuleWorked(clauses, clauseSetBeforeIteration)) {
                System.out.println("There are no appliable rules. Algorithm over.");
                return false;
            }
            System.out.println();
        }
        return true;
    }

    public static Boolean hasAnyRuleWorked(Set<Clause> clauses, Set<Clause> clausesBeforeRuleApply) {
        return !clauses.equals(clausesBeforeRuleApply);
    }


    public static int clauseSetState(Set<Clause> clauses) {
        //  If clause set is empty, the formula is satisfiable
        if (clauses.isEmpty()) {
            System.out.println("Clause set is empty. Algorithm is over.");
            return 1;
        }
        Iterator<Clause> clauseIterator = clauses.iterator();
        while (clauseIterator.hasNext()) {
            //  If EMPTY CLAUSE reached, the formula is unsatisfiable
            if (clauseIterator.next().getLiteralList().isEmpty()) {
                System.out.println("Reached EMPTY clause. Algorithm is over.");
                return 0;
            }
        }
        //  Else continue algorithm.
        return 2;
    }

    //  Implementation of the single literal rule
    public static Set<Clause> singleLiteralRule(Set<Clause> clauses) {
        System.out.println("\nTrying to apply single literal rule.");
        //  The reason of single literal and it's negate is in a HashSet
        //  is the compatibility with removeClauseContainsLiteral() method
        Set<Literal> singleLiteral = new HashSet<>();
        Set<Literal> negatedSingleLiteral = new HashSet<>();
        for (Clause actualClause : clauses) {
            if (actualClause.getLiteralList().size() == 1) {
                singleLiteral.add(actualClause.getLiteralList().get(0));
                negatedSingleLiteral.add(actualClause.getLiteralList().get(0).negated());
                break;
            }
        }

        if (singleLiteral.isEmpty()) {
            System.out.println("\tThere are no single literals");
            printClauseSet(clauses);
            return clauses;
        }

        System.out.println("\tSingle literal: " + singleLiteral);
        System.out.println("\tRemove clauses which contain " + negatedSingleLiteral + " literal.");
        //  Remove all clauses which contain the selected single literal.
        removeClauseContainsLiteral(clauses, singleLiteral, false);

        //  Remove all negates of single literal from clauses
        removeNegates(clauses, negatedSingleLiteral);

        printClauseSet(clauses);
        return clauses;
    }

    //  Removes instances of the chosen literal from all clauses
    public static void removeNegates(Set<Clause> clauses, Set<Literal> negatedSingleLiteral) {
        for (Clause actualClause : clauses) {
            List<Literal> actualLiteralList = actualClause.getLiteralList();
            actualLiteralList.removeAll(negatedSingleLiteral);
            actualClause.setLiteralList(actualLiteralList);
        }
    }

    //  Implementation of the pure literal rule
    public static Set<Clause> pureLiteralRule(Set<Clause> clauses) {
        System.out.println("\nTrying to apply pure literal rule.");
        Set<Literal> allLiteralsOfClauses = new HashSet<>();
        Set<Literal> pureLiterals = new HashSet<>();
        //  Get all of the literals from clauses
        for (Clause actualClause : clauses) {
            allLiteralsOfClauses.addAll(actualClause.getLiteralList());
        }
        //  Get pure literals
        Iterator<Literal> iterator = allLiteralsOfClauses.iterator();
        while (iterator.hasNext()) {
            Literal literal = iterator.next();
            if (!allLiteralsOfClauses.contains(literal.negated())) {
                pureLiterals.add(literal);
            }
        }
        System.out.println("\tRemove clauses which containing the following pure literals: " + pureLiterals);
        //  Finally, iterate over clauses and delete those which are including pure literals
        removeClauseContainsLiteral(clauses, pureLiterals, true);
        printClauseSet(clauses);

        return clauses;
    }

    //  Implementation of the resolution
    public static Set<Clause> resolution(Set<Clause> clauses) {
        Set<Clause> clauseSetCopy = new HashSet<>(clauses);
        Set<Clause> resolutionResult = new HashSet<>(clauses);
        //  Choose the parameter of resolution
        Literal resolutionParam = chooseResolutionParameter(clauses);
        System.out.println("\nTrying to apply resolution with parameter: " + resolutionParam);

        Iterator<Clause> clauseSet = clauses.iterator();
        List<Literal> resolvents = new ArrayList<>();
        while (clauseSet.hasNext()) {
            Clause clause = clauseSet.next();
            // If a clause contains the resolution parameter, check the clause set for clauses to resolve
            if (clause.getLiteralList().contains(resolutionParam)) {
                Iterator<Clause> negatesIterator = clauseSetCopy.iterator();
                while (negatesIterator.hasNext()) {
                    Clause nClause = negatesIterator.next();
                    if (nClause.getLiteralList().contains(resolutionParam.negated())) {
                        System.out.println("\tResolution with formula " + clause + " , " + nClause);
                        //  Add all literals from the clauses
                        resolvents.addAll(clause.getLiteralList());
                        resolvents.addAll(nClause.getLiteralList());
                        //  Then remove the resolution parameter and it's negate from resolvent set.
                        resolvents.remove(resolutionParam);
                        resolvents.remove(resolutionParam.negated());
                        System.out.println("\tResolvents: " + resolvents);
                        resolutionResult.add(new Clause(resolvents));
                        System.out.println("\tClause set after adding resolvents: " + resolutionResult);
                    }
                }

            }
        }
        //  Remove all clauses which contain p or not p (p is an alias for the resolution parameter)
        System.out.println("\tRemove all clauses which contain " + resolutionParam + " or " + resolutionParam.negated());
        Set<Literal> removeFilter = new HashSet<>();
        removeFilter.add(resolutionParam);
        removeFilter.add(resolutionParam.negated());
        removeClauseContainsLiteral(resolutionResult, removeFilter, true);
        System.out.println("\tClause set after remove " + resolutionParam
                + " and " + resolutionParam.negated() + ": " + resolutionResult);
        printClauseSet(resolutionResult);
        return resolutionResult;
    }

    //  Removes clauses from the clause set if they are containing any literal from the remove filter set.
    public static void removeClauseContainsLiteral(
            Set<Clause> removeFrom, Set<Literal> removeFilter, Boolean removeSingles) {
        Iterator<Clause> clauseIterator = removeFrom.iterator();
        while (clauseIterator.hasNext()) {
            Clause clause = clauseIterator.next();
            for (Literal literal : removeFilter) {
                if (clause.getLiteralList().contains(literal)) {
                    if (clause.getLiteralList().size() > 1) {
                        clauseIterator.remove();
                        break;
                    } else if (removeSingles && clause.getLiteralList().size() == 1) {
                        clauseIterator.remove();
                        break;
                    }
                }
            }
        }
    }

    //  Method for selecting resolution parameter. Selects the most frequent literal from clause set.
    public static Literal chooseResolutionParameter(Set<Clause> clauses) {
        Map<Literal, Integer> literalInstanceNums = new HashMap<>();
        for (Clause actualClause : clauses) {
            for (Literal actualLiteral : actualClause.getLiteralList()) {
                literalInstanceNums.putIfAbsent(actualLiteral, 0);
                literalInstanceNums.put(actualLiteral, literalInstanceNums.get(actualLiteral) + 1);

            }
        }
        return Collections
                .max(literalInstanceNums.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue())
                .getKey();
    }

    //  Prints the actual state of clause set
    public static void printClauseSet(Set<Clause> clauses) {
        System.out.println("\n---------The actual state of clause set---------\n" + clauses + "\n");
    }


}
