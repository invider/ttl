package io.ttl;

public class Token {

    enum Type {
        number,
        operator,
        string,
        id,
        eol,
        eof
    }

    public Type type;

    public Object value;

    public Token(Type type) {
        this.type = type;
    }

    public Token(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public boolean isOperator(String op) {
        return type == Type.operator
            && value != null
            && value instanceof String
            && value.equals(op);
    }

    public Double getDouble() {
        if (type != Type.number || value == null || !(value instanceof Double)) {
            throw new EvalException("type mismatch: double is expected");
        }
        return (Double)value;
    }

    @Override
    public String toString() {
        return "<" + type.toString() + ">:" + value;
    }
}
