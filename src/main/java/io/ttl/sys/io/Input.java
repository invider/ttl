package io.ttl.sys.io;

import io.ttl.EvalException;
import io.ttl.val.Frame;
import io.ttl.val.Str;
import io.ttl.val.SysFun;
import io.ttl.val.Val;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Input extends SysFun {

    private BufferedReader br =
            new BufferedReader(new InputStreamReader(System.in));

    protected Val syscall(Frame frame) {
        try {
            return new Str(br.readLine());
        } catch (IOException e) {
            throw new EvalException("can't read from input", e);
        }
    }
}
