package com.xdag.wallet;


//typedef struct {
//        en_xdag_procedure_type  procedure_type;
//        en_xdag_event_type      event_type;
//        en_xdag_app_log_level   log_level;
//        en_xdag_program_state   xdag_program_state;
//        en_address_load_state   xdag_address_state;
//        en_balance_load_state   xdag_balance_state;
//        char error_msg[MAX_XDAG_ERR_MSG_LEN + 1];
//        char address[MAX_XDAG_ADDRESS_LEN + 1];
//        char balance[MAX_XDAG_BANLANCE_LEN + 1];
//        char state[MAX_XDAG_STATE_LEN + 1];
//        char app_log_msg[MAX_XDAG_LOG_BUF_SIZE + 1];
//} st_xdag_event;

//en_event_retype_pwd             = 0x1002,
//en_event_set_rdm                = 0x1003,
//en_event_pwd_not_same           = 0x1004,
//en_event_pwd_error              = 0x1005,
//en_event_pwd_format_error       = 0x1006,
//
////dnet wallet storage error
//en_event_open_dnetfile_error    = 0x2000,
//en_event_open_walletfile_error  = 0x2001,
//en_event_load_storage_error     = 0x2002,
//en_event_write_dnet_file_error  = 0x2003,
//en_event_add_trust_host_error   = 0x2004,
//
////xfer error
//en_event_nothing_transfer       = 0x3000,
//en_event_balance_too_small      = 0x3001,
//en_event_invalid_recv_address   = 0x3002,
//en_event_xdag_transfered        = 0x3003,
//
////miner net thread error
//en_event_connect_pool_timeout   = 0x4000,
//en_event_make_block_error       = 0x4001,
//
////invoke print log or update ui
//en_event_xdag_log_print         = 0x5000,
//en_event_update_progress        = 0x5001,
//en_event_update_state           = 0x5002,
//
////block thread error(work_thread)
//
//
//en_event_cannot_create_block    = 0x7000,
//en_event_cannot_find_block      = 0x7001,
//en_event_cannot_load_block      = 0x7002,
//en_event_cannot_create_socket   = 0x7003,
//en_event_host_is_not_given      = 0x7004,
//en_event_cannot_reslove_host    = 0x7005,
//en_event_port_is_not_given      = 0x7006,
//en_event_cannot_connect_to_pool = 0x7007,
//en_event_socket_isclosed        = 0x7008,
//en_event_socket_hangup          = 0x7009,
//en_event_socket_error           = 0x700a,
//en_event_read_socket_error      = 0x700b,
//en_event_write_socket_error     = 0x700c,
//
//
//en_event_unkown                 = 0xf000,

enum XdagProcedureType{
    en_procedure_init_wallet(0),
    en_procedure_xfer_coin(1),
    en_procedure_work_thread(2),
    en_procedure_pool_thread(3);

    private int procedureType;
    private XdagProcedureType(int type){
        this.procedureType = type;
    }
    public int getProcedureType() {
        return procedureType;
    }

    public void setProcedureType(int procedureType) {
        this.procedureType = procedureType;
    }
}

enum XdagEventType{

    en_event_type_pwd(0x1000),
    en_event_set_pwd(0x1001),
    en_event_retype_pwd(0x1002),
    en_event_set_rdm(0x1003),
    en_event_pwd_not_same(0x1004),
    en_event_pwd_error(0x1005),
    en_event_pwd_format_error(0x1006);

    private int eventType;

    private XdagEventType(int type){
        this.eventType = type;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }
}

enum XdagLogLevel {

    en_xdag_no_error(0),
    en_xdag_fatal(1),
    en_xdag_critical(2),
    en_xdag_internal(3),
    en_xdag_error(4),
    en_xdag_warning(5),
    en_xdag_message(6),
    en_xdag_info(7),
    en_xdag_debug(8),
    en_xdag_trace(9);

    private int logLevel;
    private XdagLogLevel(int level){
        this.logLevel = level;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }
}

enum XdagAddressLoadState{

    en_address_not_ready(0),
    en_address_ready(1);

    private int loadState;
    private XdagAddressLoadState(int state){
        this.loadState = state;
    }

    public int getLoadState() {
        return loadState;
    }

    public void setLoadState(int loadState) {
        this.loadState = loadState;
    }
}

enum XdagBalanceLoadState{

    en_balance_not_ready(0),
    en_balance_ready(1);

    private int loadState;
    private XdagBalanceLoadState(int state){
        this.loadState = state;
    }

    public int getLoadState() {
        return loadState;
    }

    public void setLoadState(int loadState) {
        this.loadState = loadState;
    }
}

enum XdagProgramState{
    NINT(1),
    INIT(2),
    KEYS(3),
    REST(4),
    LOAD(5),
    STOP(6),
    WTST(7),
    WAIT(8),
    TTST(9),
    TRYP(10),
    CTST(11),
    CONN(12),
    XFER(13),
    PTST(14),
    POOL(15),
    MTST(16),
    MINE(17),
    STST(18),
    SYNC(19);

    private int state;
    XdagProgramState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

public class XdagEvent {
    public XdagProcedureType procedureType;
    public XdagEventType eventType;
    public XdagLogLevel logLevel;
    public XdagProgramState programState;
    public XdagAddressLoadState addressLoadState;
    public XdagBalanceLoadState balanceLoadState;
    public String status;
    public String account;
    public String balance;
    public String errorMsg;
    public String state;

    public XdagEvent(XdagProcedureType procedureType,
                     XdagEventType eventType,
                     XdagLogLevel logLevel,
                     XdagProgramState programState,
                     XdagAddressLoadState addressLoadState,
                     XdagBalanceLoadState balanceLoadState,
                     String status,
                     String account,
                     String balance,
                     String errorMsg,
                     String state) {
        this.procedureType = procedureType;
        this.eventType = eventType;
        this.logLevel = logLevel;
        this.programState = programState;
        this.addressLoadState = addressLoadState;
        this.balanceLoadState = balanceLoadState;
        this.status = status;
        this.account = account;
        this.balance = balance;
        this.errorMsg = errorMsg;
        this.state = state;
    }
}
