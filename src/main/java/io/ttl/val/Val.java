package io.ttl.val;

public interface Val {

    public ValType getType();

    public Double getNum();

    public String getStr();

    public Val getValue();

    public String eval();

}
