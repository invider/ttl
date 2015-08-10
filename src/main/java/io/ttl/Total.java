package io.ttl;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Total {
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Env calc = new Calc();

        while(true) {
            try {
                System.out.print("> ");
                String line = br.readLine().trim();
                String res = calc.exec(line);
                System.out.println("= " + res);
            } catch (EvalException e) {
                e.printStackTrace();
                System.out.println("! " + e.getMessage() + " @(" + e.getSrc() + ")");
            } catch (IOException e) {
                e.printStackTrace();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
