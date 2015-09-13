package io.ttl;

import io.ttl.val.Nil;
import io.ttl.val.Val;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Total {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        REPL repl = new REPL(true);

        while(true) {
            try {
                if (repl.multiline) System.out.print("' ");
                else System.out.print("> ");
                String line = br.readLine().trim();
                Val res = repl.eval(line);
                if (!repl.multiline) {
                    if (res.getType() != Val.Type.nil || repl.isEnvDefined(REPL.SHOW_NIL)) {
                        Util.res("" + res);
                    }
                } else {
                    // eval nothing and accept the rest of the input
                }
            } catch (EvalException e) {
                if (repl.eval("env.showTrace").getType() != Val.Type.nil) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                if (repl.eval("env.showTrace").getType() != Val.Type.nil) {
                    e.printStackTrace();
                }
                Util.error(e.getMessage());
            } catch(Exception e) {
                if (repl.eval("env.showTrace").getType() != Val.Type.nil) {
                    e.printStackTrace();
                }
                Util.error(e.getMessage());
            }
        }
    }
}
