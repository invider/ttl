package io.ttl;

import io.ttl.sys.*;
import io.ttl.val.*;

import java.io.*;
import java.nio.file.*;
import java.util.Map;

public class REPL extends Frame {

    public boolean multiline = false;

    private String srcBuffer;

    private Frame env;

    public REPL() {
        try {
            defun(new Exit());
            defun(new Time());
            defun(new TypeFunc());
            defun(new Prin());
            defun(new Print());
            defun(new Input());
            createEnv();
            execBoot("rom/boot");
            loadROM("rom");
            this.exec("config?config()!!print('no config found')");
            loadCache("cache");
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println("startup error!");
        }
    }

    private void defun(SysFun sysfun) {
        this.set(sysfun.getName(), sysfun);
    }

    private void createEnv() {
        Frame env = new Frame(this);
        this.set("env", env);
        this.env = env;
    }

    private void execBoot(String path) {
        File boot = new File(path);
        if (!boot.exists() || !boot.isFile()) {
            System.out.println("no boot script found");
        }
        if (!boot.canRead()) {
            System.out.println("can't read boot script");
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
                System.out.println("can't read: " + nextFile.getPath());
                continue;
            }
            if (nextFile.isDirectory()) {
                Frame nextFrame = new Frame();
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
            System.out.println("can't read: " + file.getPath());
            e.printStackTrace();
            return "";
        }
    }

    private void saveVal(String path, String name, Val val) {
        File parentPath = new File(path);
        parentPath.mkdirs();
        name = denormalizeId(name);

        if (val.getType() == Type.frame) {
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String normalizeId(String name) {
        return name.replace('.', '!').replace('~', '$');
    }

    private String denormalizeId(String id) {
        return id.replace('!', '.').replace('$', '~');
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
            if (env.val("showStack").getType() != Type.nil) {
                System.out.println("< " + val);
            }
            if (env.val("showTree").getType() != Type.nil) {
                System.out.println("& " + val.toTree());
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
