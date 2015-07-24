package io.ttl;

public class Token {

    enum TokenType {
        number,
        operator,
        string,
        id,
        eol,
        eof
    }

    public TokenType type;

    public Object value;

    public Token(TokenType type) {
        this.type = type;
    }

    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public boolean isOperator(String op) {
        return type == TokenType.operator
            && value != null
            && value instanceof String
            && value.equals(op);
    }

    public Double getDouble() {
        if (type != TokenType.number || value == null || !(value instanceof Double)) {
            throw new EvalException("type mismatch: double is expected");
        }
        return (Double)value;
    }

    @Override
    public String toString() {
        return "<" + type.toString() + ">:" + value;
    }
}
