package io.ttl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Total {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        REPL repl = new REPL();

        while(true) {
            try {
                if (repl.multiline) System.out.print("' ");
                else System.out.print("> ");
                String line = br.readLine().trim();
                String res = repl.exec(line);
                if (!repl.multiline) {
                    System.out.println("= " + res);
                } else {
                }
            } catch (EvalException e) {
                System.out.println("! " + e.getMessage() + " @(" + e.getSrc() + ")");
            } catch (IOException e) {
                System.out.println("! " + e.getMessage());
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
