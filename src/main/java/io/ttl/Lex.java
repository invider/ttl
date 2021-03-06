package io.ttl;

import java.io.*;

public class Lex {

    private StringBuilder lineBuf = new StringBuilder();

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
            if (c == -1) c = 0;
            else lineBuf.append((char)c);
            buf = (char)c;
            return buf;
        } catch (IOException e) {
            return 0;
        }
    }

    private void retc() {
        if (isBuffered) throw new EvalException(
                "lex error: can't return more than one character");
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
        base, id, string, dstring, number, comment, linecomment
    }
    private State state = State.base;

    private StringBuilder tokenBuffer;

    private boolean isReturned = false;

    private Token returnedToken;

    private Token parseToken() {
        while(true) {
            char c = getc();

            switch(state) {
                case base:
                    if (c == 0) return new Token(Token.Type.eof);
                    if (c == ' ' || c == '\t' || c == 0x0D || c == 0x0A) {
                    } else if (c == '#') {
                        state = State.linecomment;
                    } else if (c >= '0' && c <= '9') {
                        state = State.number;
                        tokenBuffer = new StringBuilder("");
                        tokenBuffer.append(c);
                    } else if ((c >= 'a' && c <= 'z')
                            || (c >= 'A' && c <= 'Z')
                            || (c == '_')
                            || (c == '$')) {
                        state = State.id;
                        tokenBuffer = new StringBuilder("");
                        tokenBuffer.append(c);
                    } else if (c == '\'') {
                        state = State.string;
                        tokenBuffer = new StringBuilder();
                    } else if (c == '"') {
                        state = State.dstring;
                        tokenBuffer = new StringBuilder();
                    } else {
                        switch(c) {
                            case '+':case '%':
                            case '=':case '(':case ')':case '[':case ']':
                            case ',':case '@':case '!':case '&':
                                return new Token(
                                        Token.Type.operator, "" + c);
                            case '.':
                                if (match('.')) {
                                    return new Token(Token.Type.operator, "..");
                                }
                                return new Token(Token.Type.operator, ".");
                            case ':':
                                if (match(':')) {
                                    return new Token(Token.Type.operator, "::");
                                } else if (match('^')) {
                                    return new Token(Token.Type.operator, ":^");
                                } else if (match('~')) {
                                    return new Token(Token.Type.operator, ":~");
                                }
                                return new Token(Token.Type.operator, ":");
                            case '/':
                                if (match('*')) {
                                    state = State.comment;
                                    break;
                                } else {
                                    return new Token(Token.Type.operator, "/");
                                }
                            case '-':
                                if (match('-')) {
                                    state = State.linecomment;
                                    break;
                                } else {
                                    return new Token(Token.Type.operator, "-");
                                }
                            case '<':
                                if (match('=')) {
                                    return new Token(Token.Type.operator, "<=");
                                } else if (match('>')) {
                                    return new Token(Token.Type.operator, "<>");
                                } else{
                                    return new Token(Token.Type.operator, "<");
                                }
                            case '>':
                                if (match('=')) {
                                    return new Token(Token.Type.operator, ">=");
                                } else{
                                    return new Token(Token.Type.operator, ">");
                                }
                            case '*':
                                if (match('~')) {
                                    return new Token(Token.Type.operator, "*~");
                                }
                                return new Token(Token.Type.operator, "*");
                            case '?':
                                if (match('~')) {
                                    return new Token(Token.Type.operator, "?~");
                                }
                                return new Token(Token.Type.operator, "?");
                            case '|':
                                if (match('|')) {
                                    return new Token(Token.Type.operator, "||");
                                }
                                return new Token(Token.Type.operator, "|");
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
                            || (c == '_')
                            || (c == '$')
                            || (c == '!')) {
                        tokenBuffer.append(c);
                    } else {
                        retc();
                        state = State.base;
                        return new Token(Token.Type.id,
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
                        return new Token(Token.Type.number,
                                Double.parseDouble(tokenBuffer.toString()));
                    }
                    break;
                case string: case dstring:
                    if (c == 0) {
                        throw new EolException();
                    }
                    if (c == '\\') {
                        if (match('\'')) {
                            tokenBuffer.append('\'');
                        } else if (match('"')) {
                            tokenBuffer.append('"');
                        } else if (match('\\')) {
                            tokenBuffer.append('\\');
                        } else if (match('n')) {
                            tokenBuffer.append('\n');
                        } else if (match('t')) {
                            tokenBuffer.append("\t");
                        } else {
                            StringBuilder hex = new StringBuilder();
                            for (int i = 0; i < 4; i++) {
                                c = getc();
                                if ((c >= '0' && c <= '9')
                                        || (c >= 'a' && c <= 'f')
                                        || (c >= 'A' && c <= 'F')) {
                                    hex.append(c);
                                } else {
                                    throw new EvalException(
                                        "lexical error: hex number or correct escape symbol is expected after \\");
                                }
                            }
                            tokenBuffer.append((char)Integer.parseInt(hex.toString(), 16));
                        }
                    } else if (state == State.string && c == '\'') {
                        if (match('\'')) {
                            tokenBuffer.append(c);
                        } else {
                            state = State.base;
                            return new Token(Token.Type.string,
                                tokenBuffer.toString());
                        }
                    } else if (state == State.dstring && c == '"') {
                        if (match('"')) {
                            tokenBuffer.append('"');
                        } else {
                            state = State.base;
                            return new Token(Token.Type.string,
                                tokenBuffer.toString());
                        }
                    } else {
                        tokenBuffer.append(c);
                    }
                    break;
                case comment:
                    if (c == 0) {
                        throw new EvalException("lexical error: multiline comment is supposed to be closed");
                    }
                    if (c == '*' && match('/')) {
                        state = State.base;
                    }
                    break;
                case linecomment:
                    if (c == 0 || c == 0x0A || c == 0x0D) {
                        retc();
                        state = State.base;
                    }
                    break;
            }
        }
    }

    public Token nextToken() {
        if (isReturned) {
            isReturned = false;
            return returnedToken;
        }
        returnedToken = parseToken();
        return returnedToken;
    }

    public void returnToken() {
        isReturned = true;
    }

    public String getLastLine() {
        if (isBuffered) return lineBuf.substring(0, lineBuf.length()-1);
        return lineBuf.toString();
    }
}
