package com.xdag.wallet;

/**
 * Created by Bill on 2018/4/4.
 */

public class XdagWrapper {
    static {
        System.loadLibrary("xdag");
    }

    public native int XdagConnect();
    public native int XdagDisConnect();
    public native int XdagXfer();
}
