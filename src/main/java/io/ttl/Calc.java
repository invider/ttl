package io.ttl;

import java.util.StringTokenizer;

public class Calc implements Eval, Pile {

    @Override
    public String exec(String src) {
        try {
            StringTokenizer token = new StringTokenizer(src);

            String op = token.nextToken();
            double opd;
            try {
                opd = Double.parseDouble(op);
            } catch (NumberFormatException e) {
                throw new EvalException("Number is expected, found [" + op + "] instead", src);
            }

            while(token.hasMoreTokens()) {
                String action = token.nextToken();
                if (!token.hasMoreTokens()) {
                    throw new EvalException("Unexpected end of expression", src);
                }

                String op2 = token.nextToken();
                double opd2;
                try {
                    opd2 = Double.parseDouble(op2);
                } catch (NumberFormatException e) {
                    throw new EvalException("Number is expected, found [" + op2 + "] instead", src);
                }

                switch(action) {
                    case "+":
                        opd += opd2;
                        break;
                    case "-":
                        opd -= opd2;
                        break;
                    case "*":
                        opd *= opd2;
                        break;
                    case "/":
                        opd /= opd2;
                        break;
                    case "%":
                        opd %= opd2;
                        break;
                    default:
                        throw new EvalException("Wrong operator [" + action + "]", src);
                }
            }

            return "" + opd;

        } catch (EvalException e) {
            throw e;
        } catch (Throwable t) {
            throw new EvalException(t, src);
        }
    }

    @Override
    public void push(Object val) {

    }

    @Override
    public Object pop() {
        return null;
    }

    @Override
    public Object peek() {
        return null;
    }

}
