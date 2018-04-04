package com.xdag.wallet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText txtPool;
    private TextView tvBalance;
    private TextView tvAccount;
    private EditText txtAmount;
    private EditText txtRecvAddress;

    private Button btnConncet;
    private Button btnDisConnect;
    private Button btnXfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
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

    }

    private void onBtnDisConnectClicked() {

    }

    private void onBtnXferConnectClicked() {

    }

}
