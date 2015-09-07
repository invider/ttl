package io.ttl.val;

public class Block extends Frame {

    private Val val;

    public Block(Val val) {
        this.val = val;
    }

    @Override
    public Val eval(Frame frame) {
        Frame newFrame = new Frame(frame);
        Val res = val.eval(newFrame);
        if (res.getType() != Type.success) {
            newFrame.set(res); // associate with index
        }
        return newFrame;
    }
}
