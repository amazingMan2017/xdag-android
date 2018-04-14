package com.xdag.wallet;

/**
 * Created by Bill on 2018/4/4.
 */

public class XdagWrapper {
    static {
        System.loadLibrary("xdag");
    }

    private static XdagWrapper instance = null;
    private XdagWrapper(){}
    public static XdagWrapper getInstance() {
        if (instance == null) {
            synchronized (XdagWrapper.class) {
                if (instance == null) {
                    instance = new XdagWrapper();
                }
            }
        }
        return instance;
    }

    public int XdagConnectToPool(String poolAddr){
        return XdagConnect(poolAddr);
    }

    public int XdagDisConnectFromPool(){
        return XdagDisConnect();
    }

    public int XdagXferToAddress(String address,String amount){
        return XdagXfer(address,amount);
    }

    public int XdagWrapperInit(){
        return XdagInit();
    }

    public int XdagWrapperUnInit(){
        return XdagUnInit();
    }

    private native int XdagInit();
    private native int XdagUnInit();
    private native int XdagConnect(String poolAddr);
    private native int XdagDisConnect();
    private native int XdagXfer(String address,String amount);


    public void updateUi(XdagEvent event){

    }
}
