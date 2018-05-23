package com.hu.unideb.inf.suszkai.davisputnam;

import java.util.List;

/**
 *  This class implements a clause, which is needed for CNF
 */

public class Clause {


    private List<Literal> literalList;

    public Clause(List<Literal> literalList) {
        this.literalList = literalList;
    }

    public List<Literal> getLiteralList() {
        return this.literalList;
    }

    public void setLiteralList(List<Literal> literalList) {
        this.literalList = literalList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append("(");
        Boolean firstLiteral = true;
        for (Literal literal: literalList) {
            if( firstLiteral ){
                sb.append(literal);
                firstLiteral = false;
            }
            else {
                sb.append(" ∨ ").append(literal);
            }
        }
        sb.append(")");
        return sb.toString();
    }


}
