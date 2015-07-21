package io.ttl;

import java.io.*;

public class Lex {

    private int pos = 0;

    private Reader in;

    private boolean isBuffered = false;

    private char buf;

    public Lex(Reader reader) {
        this.in = new BufferedReader(reader, 8192);
    }

    private char getc() {
        try {
            if (isBuffered) {
                isBuffered = false;
                return buf;
            }
            int c = in.read();
            if (c == -1) return 0;
            buf = (char)c;
            return buf;
        } catch (IOException e) {
            return 0;
        }
    }

    private void retc() {
        if (isBuffered) throw new EvalException("Lex Error!");
        isBuffered = true;
    }

    public Token getNext() {
        while(true) {
            char c = getc();
            if (c == 0) return new Token(Token.TokenType.eof);
            if (c == ' ');
            else if (c >= '0' && c <= '9') {
                return new Token(Token.TokenType.number, "" + c);
            } else if (Character.isLetter(c)) {
                return new Token(Token.TokenType.id, "" + c);
            }
        }
    }
}
