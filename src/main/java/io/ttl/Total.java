package io.ttl;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Total {

    public static boolean loop = true;

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Eval eval = new Calc();

        while(loop) {
            try {
                System.out.print("> ");
                String line = br.readLine().trim();
                if (line.toLowerCase().equals("exit")) break;
                String res = eval.exec(line);
                System.out.println("= " + res);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EvalException e) {
                System.out.println("@ " + e.getSrc());
                System.out.println("! " + e.getMessage());
            }
        }
    }
}
