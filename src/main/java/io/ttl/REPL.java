package io.ttl;

import io.ttl.sys.*;
import io.ttl.sys.io.Input;
import io.ttl.sys.io.Prin;
import io.ttl.sys.io.Print;
import io.ttl.sys.math.*;
import io.ttl.sys.util.DateFunc;
import io.ttl.val.*;

import java.io.*;
import java.nio.file.*;
import java.util.Map;

public class REPL extends Frame {

    public static final String SHOW_TREE = "showTree";
    public static final String SHOW_STACK = "showStack";
    public static final String SHOW_NIL = "showNil";

    public boolean multiline = false;

    private String srcBuffer;

    private Frame env, sys;

    public REPL(boolean diagnostics) {
        try {
            createEnv();
            createSys();
            defineConstants();
            defineSysFunctions();
            execBoot("rom/sys/boot");
            loadROM("rom");
            this.exec("sys.config?sys.config()||print('no sys.config found!')");
            if (diagnostics) {
                this.exec("sys.test?score:sys.test()||print('no sys.test found!')");
                this.exec("print('Test Score: ' + score)");
            }
            this.exec("sys.startup?sys.startup()||print('no sys.startup found!')");
            this.exec("sys.welcome?sys.welcome()||print('Welcome!')");
            loadCache("cache");
        } catch (Throwable t) {
            Util.error("startup error!");
            throw new EvalException("startup failed!", t);
        }
    }

    private void defun(SysFun sysfun) {
        this.set(sysfun.getName(), sysfun);
    }

    private void defineSysFunctions() {
        defun(new Exit());
        defun(new Help());
        defun(new Assert());
        defun(new True());
        defun(new DateFunc());
        defun(new io.ttl.sys.Type());
        // math
        defun(new Abs());
        defun(new Acos());
        defun(new Asin());
        defun(new Atan());
        defun(new Ceil());
        defun(new Cos());
        defun(new Exp());
        defun(new Floor());
        defun(new Log());
        defun(new Max());
        defun(new Min());
        defun(new Pow());
        defun(new Random());
        defun(new Sin());
        defun(new Sqrt());
        defun(new Tan());
        // io
        defun(new Prin());
        defun(new Print());
        defun(new Input());
        // sys
        sys.set("test", new Test());
    }

    private void defineConstants() {
        this.set("_pi", new Num(Math.PI));
        this.set("_e", new Num(Math.E));
    }

    private void createEnv() {
        this.env = new Frame(this);
        this.set("env", env);
    }

    private void createSys() {
        this.sys = new Frame(this);
        this.set("sys", sys);
    }

    private void execBoot(String path) {
        File boot = new File(path);
        if (!boot.exists() || !boot.isFile()) {
            Util.warn("no boot script found");
        }
        if (!boot.canRead()) {
            Util.warn("can't read the boot script");
        }
        String bootScript = loadFile(boot);
        this.exec(bootScript);
    }

    private void loadROM(String path) {
        File rom = new File(path);
        if (!rom.exists() || !rom.isDirectory()) {
            throw new EvalException("can't find firmware @" + path);
        }
        if (!rom.canRead()) {
            throw new EvalException("can't read firmware @" + path);
        }
        loadFrame(this, rom);
    }

    private void loadCache(String path) {
        File cache = new File(path);
        if (!cache.exists() || !cache.isDirectory()) {
            throw new EvalException("can't find cache @" + path);
        }
        if (!cache.canRead()) {
            throw new EvalException("can't read cache @" + path);
        }
        loadFrame(this, cache);
    }


    private void loadFrame(Frame frame, File file) {
        File[] dir = file.listFiles();
        for(File nextFile: dir) {
            if (!nextFile.canRead()) {
                Util.warn("can't read: " + nextFile.getPath());
                continue;
            }
            if (nextFile.isDirectory()) {
                String name = normalizeId(nextFile.getName());
                Val existingFrame = frame.val(name);
                Frame nextFrame = null;
                if (existingFrame.getType() == Type.frame) {
                    nextFrame = (Frame)existingFrame;
                } else {
                    nextFrame = new Frame();
                }
                frame.set(normalizeId(nextFile.getName()), nextFrame);
                loadFrame(nextFrame, nextFile);
            } else {
                frame.set(normalizeId(nextFile.getName()), loadVal(nextFile));
            }
        }
    }

    private Val loadVal(File file) {
        String content = loadFile(file).trim();
        try {
            return new Num(Double.parseDouble(content));
        } catch (NumberFormatException e) {
            return new Str(content);
        }
    }

    private String loadFile(File file) {
        try {
            Path path = FileSystems.getDefault().getPath(file.getParent(), file.getName());
            return new String(Files.readAllBytes(path), "utf-8");
        } catch (IOException e) {
            Util.warn("can't read: " + file.getPath());
            return "";
        }
    }

    private void saveVal(String path, String name, Val val) {
        File parentPath = new File(path);
        parentPath.mkdirs();
        name = denormalizeId(name);

        if (val.getType() == Val.Type.frame) {
            Frame frame = (Frame)val;
            String framePath = path + "/" + name;
            for (Map.Entry<String, Val> e: frame.getNameMap().entrySet()) {
                saveVal(framePath, e.getKey(), e.getValue());
            }
        } else {
            saveFile(path, name, val.toString());
        }
    }

    private void saveFile(String parent, String name, String body) {
        try {
            Path path = FileSystems.getDefault().getPath(parent, name);
            Files.write(path, body.getBytes("utf-8"),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (UnsupportedEncodingException e) {
            Util.error("can't save " + name);
        } catch (IOException e) {
            Util.error("can't save " + name);
        }
    }

    private String normalizeId(String name) {
        return name.replace('.', '!').replace('~', '$');
    }

    private String denormalizeId(String id) {
        return id.replace('!', '.').replace('$', '~');
    }

    protected boolean isEnvDefined(String key) {
        return env.val(key).getType() != Val.Type.nil;
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
            if (isEnvDefined(SHOW_TREE)) {
                Util.debug("< " + val);
            }
            if (isEnvDefined(SHOW_STACK)) {
                Util.debug("& " + val.toTree());
            }
            return val.eval(frame);
        } catch (EolException e) {
            srcBuffer = src;
            multiline = true;
            return Nil.NIL;
        } catch (Throwable t) {
            throw new EvalException(t, src);
        }
    }

    @Override
    public void set(String name, Val val) {
        super.set(name, val);
        if (name.startsWith("$")) {
            saveVal("cache", name, val);
        }
    }
}
