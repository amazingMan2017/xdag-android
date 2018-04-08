package com.xdag.wallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText txtPool;
    private TextView tvBalance;
    private TextView tvAccount;
    private EditText txtAmount;
    private EditText txtRecvAddress;

    private Button btnConncet;
    private Button btnDisConnect;
    private Button btnXfer;
    private Thread xdagProcessThread;
    private static final String TAG = "XdagWallet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
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

//        xdagProcessThread = new Thread(new Runnable() {
//            int runTimes = 0;
//            @Override
//            public void run() {
//                while(runTimes < 10){
//                    Log.d(TAG," send Msg to UI in Thread " + Thread.currentThread().getId());
//                    MessageEvent event = new MessageEvent();
//                    event.account = "xxxx";
//                    event.msgNo = runTimes ++;
//                    EventBus.getDefault().post(event);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        xdagProcessThread.start();
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
        XdagWrapper xdagWrapper = XdagWrapper.getInstance();
        xdagWrapper.XdagConnectToPool(poolAddr);
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
    public void ProcessXdagEvent(MessageEvent event) {
        Log.d(TAG,"process msg in Thread " + Thread.currentThread().getId());
        Log.d(TAG,"event msgNo is " + event.msgNo);
        Log.d(TAG,"event account is " + event.account);
    }

    @Override
    protected void onDestroy(){
        if(EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }
}
