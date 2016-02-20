package com.ishraq.janna.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ishraq.janna.R;
import com.ishraq.janna.service.CommonService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CommonService commonService = new CommonService(this);
        Toast.makeText(this, commonService.getSettings().getId()+"", Toast.LENGTH_SHORT).show();

    }
}
