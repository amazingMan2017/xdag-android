package com.xdag.wallet;

public class XdagUiNotifyMsg {

    public static final int ui_msg_set_password = 0x1001;
    public static final int ui_msg_type_password = 0x1002;
    public static final int ui_msg_retype_password = 0x1003;
    public static final int ui_msg_set_random_keys = 0x1004;
    public static final int ui_msg_xfer_coin = 0x1005;

    public int msgType;
    public String pwd;
    public String retypePwd;
    public String rdmKeys;
    public String recvAccount;
    public double sendAmount;
}
