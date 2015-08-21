package io.ttl;

import io.ttl.sys.ExitFun;
import io.ttl.sys.PrintFun;
import io.ttl.sys.TimeFun;
import io.ttl.val.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Scanner;

public class REPL extends Frame {

    public boolean multiline = false;

    private String srcBuffer;

    public REPL() {
        defun(this, new ExitFun());
        defun(this, new TimeFun());
        defun(this, new PrintFun());
        loadFirmware("./firmware");
    }

    private void defun(Frame frame, SysFun sysfun) {
        frame.set(sysfun.getName(), sysfun);
    }

    private void loadFirmware(String path) {
        try {
            if (loadFile(this, path + "/boot")) {
                this.exec("boot?boot()");
            } else {
                System.out.println("no boot script located, proceeding to load firmware without boot");
            }
            File firmware = new File(path);
            if (!firmware.isDirectory()) throw new EvalException("wrong path to firmware");
            if (!firmware.canRead()) throw new EvalException("can't read firmware directory");
            for (File file: firmware.listFiles()) {
                loadFile(this, file);
            }
            this.exec("config?config()!!print('no config script found in firmware')");
        } catch (Throwable e) {
            System.out.println("can't load the firmware");
            e.printStackTrace();
        }
    }

    private String normalizeName(String name) {
       return name.replace('.', '!').replace('~', '$');
    }

    private boolean loadFile(Frame scope, String path) {
        File file = new File(path);
        if (!file.exists()) return false;
        loadFile(scope, file);
        return true;
    }


    private void loadFile(Frame scope, File file) {
        try {
            //System.out.println("loading " + file.getPath() + "/" + file.getName());
            if (!file.canRead()) throw new EvalException();
            if (file.isDirectory()) {
                Frame frame = new Frame();
                scope.set(normalizeName(file.getName()), frame);
                for (File f: file.listFiles()) {
                    loadFile(frame, file);
                }
            } else {
                String body = readFile(file);
                // TODO figure out if that is a number
                scope.set(normalizeName(file.getName()), new Str(body));
            }
        } catch (Throwable e) {
            System.out.println("can't read [" + file.getPath() + "/" + file.getName() + "]");
        }
    }

    private String readFile(File file) throws FileNotFoundException {
        StringBuilder body = new StringBuilder((int)file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");

        try {
            while(scanner.hasNextLine()) {
                body.append(scanner.nextLine() + lineSeparator);
            }
            return body.toString().trim();
        } finally {
            scanner.close();
        }
    }

    @Override
    public Val eval(String src, Frame frame) {
        try {
            if (multiline) {
                src = srcBuffer + "\n" + src;
            }
            Reader reader = new StringReader(src);
            Lex lex = new Lex(reader);
            Parser parser = new Parser(this, lex);
            Val val = parser.parse();
            multiline = false;
            //System.out.println("< " + val);
            //System.out.println("& " + val.toTree());
            return val.eval(frame);
        } catch (EolException e) {
            srcBuffer = src;
            multiline = true;
            return Nil.NIL;
        } catch (Throwable t) {
            throw new EvalException(t, src);
        }
    }
}
