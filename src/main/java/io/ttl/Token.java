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

    public boolean matchOperator(String op) {
        if (type != Type.operator) return false;
        if (op.equals(value)) return true;
        return false;
    }

    @Override
    public String toString() {
        return "<" + type.toString() + ">:" + value;
    }
}
