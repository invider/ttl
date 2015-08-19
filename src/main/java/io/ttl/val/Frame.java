package io.ttl.val;

import io.ttl.EvalException;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public class Frame implements Val {

    private Frame parent;

    protected Map<String, Val> map = new ConcurrentHashMap<>();

    protected Map<Long, Val> numMap = new TreeMap<>();

    protected Long numMapIndex = new Long(0);

    public Frame() {
        this.parent = null;
    }

    public Frame(Frame parent) {
        this.parent = parent;
    }

    public Frame getParent() {
        return parent;
    }

    public void setParent(Frame parent) {
        this.parent = parent;
    }

    public void set(String name, Val val) {
        if (name.startsWith("_") && map.containsKey(name)) {
            throw new EvalException("constant [" + name + "] is already defined");
        }
        if (val.getType() != Type.nil) {
            map.put(name, val);
            if (val instanceof Frame) {
                ((Frame) val).setParent(this);
            }
        } else {
            map.remove(name);
        }
    }

    public void set(Long index, Val val) {
        if (val.getType() != Type.nil) {
            numMap.put(index, val);
            if (val instanceof Frame) {
                ((Frame) val).setParent(this);
            }
        } else {
            numMap.remove(index);
        }
    }

    public void set(Val val) {
        set(numMapIndex++, val);
    }

    public Val val(String name) {
        Val res = map.get(name);
        if (res == null) {
            if (parent == null) return Nil.NIL;
            else return parent.val(name);
        }
        return res;
    }

    public Val val(Long index) {
        Val res = numMap.get(index);
        if (res != null) return res;
        else return Nil.NIL;
    }

    public Map<String, Val> getNameMap() {
        return map;
    }

    public Map<Long, Val> getNumMap() {
        return numMap;
    }

    @Override
    public Val eval(Frame frame) {
        return this;
    }

    public Val eval(String src, Frame frame) {
        if (parent == null)
            new EvalException("can't evaluate in this frame");
        return parent.eval(src, frame);
    }

    public Val eval(String src) {
        return eval(src, this);
    }

    public String exec(String src) {
        return "" + eval(src);
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.frame) {
            throw new EvalException(
                "" + t + " was expected, but a frame " + this + " found");
        }
        return this;
    }

    @Override
    public Type getType() {
        return Val.Type.frame;
    }

    @Override
    public boolean isAtom() {
        return true;
    }

    @Override
    public Double evalNum(Frame frame) {
        throw new EvalException("can't evaluate frame to number" + this);
    }

    @Override
    public String evalStr(Frame frame) {
        throw new EvalException("can't evaluate frame to string " + this);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("[");
        for (Long index : numMap.keySet()) {
            buf.append("#" + index)
                    .append(":")
                    .append(numMap.get(index))
                    .append(", ");
        }
        for (String name : map.keySet()) {
            buf.append(name)
                    .append(":")
                    .append(map.get(name)).append(", ");
        }
        String res = buf.toString();
        if (res.length() > 1) {
            res = res.substring(0, res.length() - 2);
        }
        return res + "]";
    }

    @Override
    public String toTree() {
        StringBuilder buf = new StringBuilder("[");
        for (Long index : numMap.keySet()) {
            buf.append("#" + index)
                    .append(":")
                    .append(numMap.get(index).toTree())
                    .append(", ");
        }
        for (String name : map.keySet()) {
            buf.append(name)
                    .append(":")
                    .append(map.get(name).toTree()).append(", ");
        }
        String res = buf.toString();
        if (res.length() > 1) {
            res = res.substring(0, res.length() - 2);
        }
        return res + "]";
    }
}
