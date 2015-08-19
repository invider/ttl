package io.ttl.val;

public class Block extends Frame {

    private Val val;

    public Block(Val val) {
        this.val = val;
    }

    @Override
    public Val eval(Frame frame) {
        Frame newFrame = new Frame(frame);
        if (val.getType() == Type.op && ((Op)val).matchOperator(":")) {
            val.eval(newFrame); // name association side effect
        } else {
            newFrame.set(val.eval(newFrame)); // associate with index
        }
        return newFrame;
    }
}
