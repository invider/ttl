package io.ttl.val;

public class Num extends Nil implements Val {

    public static Num TRUE = new Num(1d);

    protected final Double value;

    public Num(Double value) {
        this.value = value;
    }

    public Num(String value) {
        this.value = Double.parseDouble(value);
    }

    @Override
    public boolean isAtom() {
        return true;
    }

    @Override
    public Type getType() {
        return Type.NUM;
    }

    @Override
    public Val eval(Env env) {
        return this;
    }

    @Override
    public Double evalNum(Env env) {
        return value;
    }

    @Override
    public String evalStr(Env env) {
        return "" + value;
    }

    @Override
    public String toString() {
        return "" + value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Num) {
            return value.doubleValue() == ((Num) obj).value.doubleValue();
        } else if (obj instanceof Str) {
            return value == Double.parseDouble(((Str)obj).value);
        }
        return super.equals(obj);
    }
}
