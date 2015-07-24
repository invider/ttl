package io.ttl;

import io.ttl.val.Val;

public interface Pile {
    public void push(Val val);
    public Val pop();
    public Val peek();
    public Val val(String name);
}
