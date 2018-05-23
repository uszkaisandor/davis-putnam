package com.hu.unideb.inf.suszkai.davisputnam;

/**
 *  This class implements the zero-order logical literal
 */

public class Literal {


    private String symbol;

    public Literal(String symbol) {
        this.symbol = symbol;
    }

    public Literal negated(){
        return new Literal(this.symbol.startsWith("¬") ? symbol.substring(1) : "¬" + symbol);
    }

    @Override
    public String toString() {
        return symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return symbol != null ? symbol.equals(literal.symbol) : literal.symbol == null;
    }

    @Override
    public int hashCode() {
        return symbol != null ? symbol.hashCode() : 0;
    }


}
