package io.ttl;

import java.io.*;

public class Lex {

    private int pos = 0;

    private Reader in;

    private boolean isBuffered = false;

    private char buf;

    private StringBuilder curLine = new StringBuilder("");

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
            if (c == -1) c = 0;
            else curLine.append((char)c);
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

    private boolean match(char c) {
        char nc = getc();
        if (nc == c) return true;
        retc();
        return false;
    }

    private char lookupNext() {
        char nc = getc();
        retc();
        return nc;
    }


    private void verifyNoRepeat(char c) {
        if (tokenBuffer.indexOf("" + c) != -1) {
            tokenBuffer.append(c);
            throw new EvalException(
                    "lexical error: wrong number format @["
                            + tokenBuffer.toString() + "]");
        }
    }

    private enum State {
        base, id, string, number
    }
    private State state = State.base;

    private StringBuilder tokenBuffer;

    private Token parseNext() {
        while(true) {
            char c = getc();

            switch(state) {
                case base:
                    if (c == 0) return new Token(Token.TokenType.eof);
                    if (c == 0x0A) {
                        curLine = new StringBuilder("");
                        return new Token(Token.TokenType.eol);
                    }
                    if (c == 0x0D) {
                        match((char)0x0A);
                        curLine = new StringBuilder("");
                        return new Token(Token.TokenType.eol);
                    }
                    if (c == ' ') ;
                    else if (c >= '0' && c <= '9') {
                        state = State.number;
                        tokenBuffer = new StringBuilder("");
                        tokenBuffer.append(c);
                    } else if ((c >= 'a' && c <= 'z')
                            || (c >= 'A' && c <= 'Z')
                            || (c == '_')) {
                        state = State.id;
                        tokenBuffer = new StringBuilder("");
                        tokenBuffer.append(c);
                    } else if (c == '\'') {
                        state = State.string;
                        tokenBuffer = new StringBuilder("");
                    } else {
                        char nc;
                        switch(c) {
                            case '+':case '-':case '*':case '/':case '%':
                            case '(':case ')':case '[':case ']':
                            case ',':case '.':case '=':case '#':
                                return new Token(
                                        Token.TokenType.operator, "" + c);
                            case ':':
                                if (match(':')) {
                                    return new Token(Token.TokenType.operator, "::");
                                }
                                return new Token(Token.TokenType.operator, ":");
                            case '<':
                                if (match('=')) {
                                    return new Token(Token.TokenType.operator, "<=");
                                } else if (match('>')) {
                                    return new Token(Token.TokenType.operator, "<>");
                                }
                                return new Token(Token.TokenType.operator, "<");
                            case '>':
                                if (match('=')) {
                                    return new Token(Token.TokenType.operator, ">=");
                                }
                                return new Token(Token.TokenType.operator, ">");
                            case '?':
                                if (match('~')) {
                                    return new Token(Token.TokenType.operator, "?~");
                                }
                                return new Token(Token.TokenType.operator, "?");
                            case '!':
                                if (match('!')) {
                                    return new Token(Token.TokenType.operator, "!!");
                                }
                                return new Token(Token.TokenType.operator, "!");
                            default:
                                throw new EvalException(
                                        "lexical error: unexpected symbol ["
                                            + c + "]");
                        }
                    }
                    break;
                case id:
                    if ((c >= 'a' && c <= 'z')
                            || (c >= 'A' && c <= 'Z')
                            || (c >= '0' && c <= '9')
                            || (c == '_')) {
                        tokenBuffer.append(c);
                    } else {
                        retc();
                        state = State.base;
                        return new Token(Token.TokenType.id,
                                tokenBuffer.toString());
                    }
                    break;
                case number:
                    if (c >= '0' && c <= '9') {
                        tokenBuffer.append(c);
                    } else if (c == '.') {
                        verifyNoRepeat(c);
                        verifyNoRepeat('e');
                        verifyNoRepeat('E');
                        tokenBuffer.append(c);
                    } else if (c == 'e' || c == 'E') {
                        verifyNoRepeat('e');
                        verifyNoRepeat('E');
                        tokenBuffer.append(c);
                        // verify and append following +/-
                        char nc = lookupNext();
                        if (nc == '+' || nc == '-') {
                            getc();
                            tokenBuffer.append(nc);
                        }
                    } else {
                        retc();
                        state = State.base;
                        return new Token(Token.TokenType.number,
                                Double.parseDouble(tokenBuffer.toString()));
                    }
                    break;
                case string:
                    if (c == 0 || c == 0x0A || c == 0x0D) {
                        throw new EvalException(
                                "lexical error: unexpected end of string");
                    }
                    if (c == '\'') {
                        if (match('\'')) {
                            tokenBuffer.append(c);
                        } else {
                            state = State.base;
                            return new Token(Token.TokenType.string,
                                    tokenBuffer.toString());
                        }
                    } else {
                        tokenBuffer.append(c);
                    }
                    break;
            }
        }
    }

    private boolean isTokenBuffered = false;

    private Token lastToken;

    public synchronized Token nextToken() {
        if (isTokenBuffered) {
            isTokenBuffered = false;
            return lastToken;
        }
        lastToken = parseNext();
        return lastToken;
    }

    public synchronized void retToken() {
        if (isTokenBuffered) {
            throw new EvalException("lex error: trying to return second token to stream");
        }
        isTokenBuffered = true;
    }

    public synchronized boolean matchOperator(String op) {
        if (nextToken().isOperator(op)) {
            return true;
        } else {
            retToken();
            return false;
        }
    }

    public synchronized String getCurrentLine() {
        return curLine.toString();
    }
}
