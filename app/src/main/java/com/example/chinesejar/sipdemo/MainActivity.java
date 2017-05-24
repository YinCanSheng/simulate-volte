package com.example.chinesejar.sipdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener, RadioGroup.OnCheckedChangeListener, IMainView {

    private MainPresenter mainPresenter;

    private Toolbar toolbar = null;

    private Spinner spinner_networks = null;
    private EditText et_dstip = null;
    private ArrayAdapter<String> adapter = null;
    private TextView tv_receive = null;
    private Button btn_send = null;
    private Button btn_clear = null;

    private RadioGroup socketRadioGroup = null;
    private RadioGroup packageRadioGroup = null;
    private String socket_type = "TCP";
    private String package_type = "REGISTER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mainPresenter = new MainPresenter(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);

        spinner_networks = (Spinner) findViewById(R.id.spinner_networks);
        et_dstip = (EditText) findViewById(R.id.et_dstip);
        tv_receive = (TextView) findViewById(R.id.tv_receive);

        socketRadioGroup = (RadioGroup) findViewById(R.id.radio_group_socket);
        socketRadioGroup.setOnCheckedChangeListener(this);
        packageRadioGroup = (RadioGroup) findViewById(R.id.radio_group_package);
        packageRadioGroup.setOnCheckedChangeListener(this);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(this);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mainPresenter.getNetworkInterfaces());
        //设置下拉列表风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到spinner中去
        spinner_networks.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
                Log.d("package checked", package_type);
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
                Log.d("click", "CLEAR");
                tvClear();
                break;
            case R.id.btn_send:
                Log.d("click", "CLICK");
                mainPresenter.socket_send(et_dstip.getText().toString(), spinner_networks.getSelectedItem().toString(), socket_type, package_type);
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_modify:
                new AlertDialog.Builder(MainActivity.this).setTitle("选择修改的报文").setItems(
                        new String[]{getString(R.string.action_register), getString(R.string.action_invite), getString(R.string.action_option), getString(R.string.action_refer)}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, PackageActivity.class);
                                switch (which){
                                    case 0:
                                        Log.d("package", "REGISTER");
                                        intent.putExtra("msg", "REGISTER");
                                        break;
                                    case 1:
                                        Log.d("package", "INVITE");
                                        intent.putExtra("msg", "INVITE");
                                        break;
                                    case 2:
                                        Log.d("package", "OPTION");
                                        intent.putExtra("msg", "OPTION");
                                        break;
                                    case 3:
                                        Log.d("package", "REFER");
                                        intent.putExtra("msg", "REFER");
                                        break;
                                }
                                startActivity(intent);
                            }
                        }).show();
                break;
        }
        return true;
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
