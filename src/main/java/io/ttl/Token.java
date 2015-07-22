package io.ttl;

public class Token {

    enum TokenType {
        number,
        operator,
        delimiter,
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

    @Override
    public String toString() {
        return "<" + type.toString() + ">:" + value;
    }
}
