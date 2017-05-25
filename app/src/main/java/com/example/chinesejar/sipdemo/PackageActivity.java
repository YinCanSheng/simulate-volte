package com.example.chinesejar.sipdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PackageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");
        switch (msg){
            case "REGISTER":
                break;
        }
    }
}
