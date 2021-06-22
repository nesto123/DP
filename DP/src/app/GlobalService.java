package app;

import message.MsgHandler;

public interface GlobalService extends MsgHandler{
    public void initialize(String x, FuncUser prog);
    public String computeGlobal();
}
