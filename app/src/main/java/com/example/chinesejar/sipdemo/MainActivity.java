package com.example.chinesejar.sipdemo;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, IMainView {

    private MainPresenter mainPresenter;


    private Spinner spinner_networks = null;
    private EditText et_dstip = null;
    private ArrayAdapter<String> adapter = null;
    private TextView tv_receive = null;

    private String socket_type = null;
    private String package_type = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mainPresenter = new MainPresenter(this);

        spinner_networks = (Spinner) findViewById(R.id.spinner_networks);
        et_dstip = (EditText) findViewById(R.id.et_dstip);
        tv_receive = (TextView) findViewById(R.id.tv_receive);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mainPresenter.getNetworkInterfaces());
        //设置下拉列表风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到spinner中去
        spinner_networks.setAdapter(adapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        RadioButton rd = (RadioButton) findViewById(checkedId);
        switch (group.getId()){
            case R.id.radio_group_socket:
                socket_type = rd.getText().toString();
                Log.d("socket checked", socket_type);
                break;
            case R.id.radio_group_package:
                package_type = rd.getText().toString();
                Log.d("package checked", socket_type);
                break;
        }
    }

    private void tvClear(){
        tv_receive.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_clear:
                tvClear();
                break;
            case R.id.btn_send:
                mainPresenter.socket_send(et_dstip.getText().toString(), spinner_networks.getSelectedItem().toString(), socket_type, package_type);
                break;
        }
    }

    @Override
    public void sendSuccess(String msg){
        Log.d("log", msg);
        tv_receive.append(msg);
    }

    @Override
    public void sendFailed(String msg){
        Log.d("log", msg);
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
