package com.xdag.wallet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,AuthDialogFragment.AuthInputListener{

    private EditText txtPool;
    private TextView tvBalance;
    private TextView tvAccount;
    private EditText txtAmount;
    private EditText txtRecvAddress;

    private Button btnConncet;
    private Button btnDisConnect;
    private Button btnXfer;
    private HandlerThread xdagProcessThread;
    private Handler xdagMessageHandler;

    private static final String TAG = "XdagWallet";
    private static final int PERMISSION_REQUESTCODE = 1;

    private static final int MSG_CONNECT_TO_POOL = 1;
    private static final int MSG_DISCONNECT_FROM_POOL = 2;
    private static final int MSG_XFER_XDAG_COIN = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initPermission();
        initXdagFiles();
    }

    private void initView() {
        txtPool = (EditText)findViewById(R.id.txt_pool);
        tvBalance = (TextView)findViewById(R.id.tv_balance);
        tvAccount = (TextView)findViewById(R.id.tv_account);
        txtAmount = (EditText)findViewById(R.id.txt_amount);
        txtRecvAddress = (EditText)findViewById(R.id.txt_recv_account);

        btnConncet = (Button)findViewById(R.id.btn_connect);
        btnDisConnect = (Button)findViewById(R.id.btn_disconnect);
        btnXfer = (Button)findViewById(R.id.btn_xfer);

        btnConncet.setOnClickListener(this);
        btnDisConnect.setOnClickListener(this);
        btnXfer.setOnClickListener(this);
    }

    private void initData(){
        EventBus.getDefault().register(this);

        xdagProcessThread = new HandlerThread("XdagProcessThread");
        xdagProcessThread.start();

        xdagMessageHandler = new Handler(xdagProcessThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.arg1){
                    case MSG_CONNECT_TO_POOL:
                    {
                        Log.i(TAG,"receive msg connect to the pool thread id " + Thread.currentThread().getId());
                        Bundle data = msg.getData();
                        String poolAddr = data.getString("pool");
                        XdagWrapper xdagWrapper = XdagWrapper.getInstance();
                        xdagWrapper.XdagConnectToPool(poolAddr);
                    }
                    break;
                    case MSG_DISCONNECT_FROM_POOL:
                    {

                    }
                    break;
                    case MSG_XFER_XDAG_COIN:
                    {

                    }
                    break;
                    default:
                    {
                        Log.e(TAG,"unkown command from ui");
                    }
                    break;
                }
            }
        };
    }

    private void initPermission() {
        List<String> permissionLists = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionLists.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionLists.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionLists.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionLists.add(Manifest.permission.ACCESS_WIFI_STATE);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            permissionLists.add(Manifest.permission.INTERNET);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            permissionLists.add(Manifest.permission.CAMERA);
        }

        if(!permissionLists.isEmpty()){//说明肯定有拒绝的权限
            ActivityCompat.requestPermissions(this, permissionLists.toArray(new String[permissionLists.size()]), PERMISSION_REQUESTCODE);
        }else{
            Toast.makeText(this, "all permission is allowed", Toast.LENGTH_SHORT).show();
        }
    }

    private void initXdagFiles() {

        String xdagFolderPath = "/sdcard/xdag";

        File file = new File(xdagFolderPath);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    @Override
    public void onClick(View view) {
        int resId = view.getId();
        switch (resId){
            case R.id.btn_connect:
                onBtnConnectClicked();
                break;
            case R.id.btn_disconnect:
                onBtnDisConnectClicked();
                break;
            case R.id.btn_xfer:
                onBtnXferConnectClicked();
                break;
        }
    }

    private void onBtnConnectClicked() {
        String poolAddr = txtPool.getText().toString();
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        data.putString("pool",poolAddr);
        msg.arg1 = MSG_CONNECT_TO_POOL;
        msg.setData(data);
        xdagMessageHandler.sendMessage(msg);
    }

    private void onBtnDisConnectClicked() {
        XdagWrapper xdagWrapper = XdagWrapper.getInstance();
        xdagWrapper.XdagDisConnectFromPool();

    }

    private void onBtnXferConnectClicked() {
        String address = tvAccount.getText().toString();
        String amount = txtAmount.getText().toString();
        XdagWrapper xdagWrapper = XdagWrapper.getInstance();
        xdagWrapper.XdagXferToAddress(address,amount);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ProcessXdagEvent(XdagEvent event) {
        Log.i(TAG,"process msg in Thread " + Thread.currentThread().getId());
        Log.i(TAG,"event event type is " + event.eventType);
        Log.i(TAG,"event account is " + event.address);
        Log.i(TAG,"event balace is " + event.balance);
        //show dialog and ask user to type in password
        switch (event.eventType){
            case XdagEvent.en_event_set_pwd:
            case XdagEvent.en_event_type_pwd:
            case XdagEvent.en_event_retype_pwd:
            case XdagEvent.en_event_set_rdm:
            {
                AuthDialogFragment authDialogFragment = new AuthDialogFragment();
                authDialogFragment.setAuthEventType(event.eventType);
                authDialogFragment.setAuthHintInfo(GetAuthHintString(event.eventType));
                authDialogFragment.show(getFragmentManager(), "Auth Dialog");
            }
            break;
        }
    }

    private String GetAuthHintString(final int eventType){
        switch (eventType){
            case XdagEvent.en_event_set_pwd:
                return "set password";
            case XdagEvent.en_event_type_pwd:
                return "input password";
            case XdagEvent.en_event_retype_pwd:
                return "retype password";
            case XdagEvent.en_event_set_rdm:
                return "set random keys";
            default:
                return "input password";
        }
    }

    @Override
    protected void onDestroy(){
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void onAuthInputComplete(XdagUiNotifyMsg msg) {
        //notify native thread
        XdagWrapper xdagWrapper = XdagWrapper.getInstance();
        xdagWrapper.XdagNotifyMsg(msg);
    }
}
