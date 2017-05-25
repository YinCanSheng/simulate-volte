package com.example.chinesejar.sipdemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PackageActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar = null;
    private Button btn_save = null;
    private EditText tv_package = null;

    private String package_type = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);

        initView();
    }

    private void initView() {
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        tv_package = (EditText) findViewById(R.id.tv_package);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        package_type = intent.getStringExtra("msg");
        Log.d("MSG", package_type);
        tv_package.setText(intent.getStringExtra("data"));
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_save:
                Log.d("click", "SAVE");
                tvSave();
                break;
        }
    }

    private void tvSave() {
        Intent intent = new Intent();
        intent.putExtra("type", package_type);
        intent.putExtra("data", tv_package.getText().toString());
        setResult(1001, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
