package com.example.partytricks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingActivity extends AppCompatActivity {

    private Button mBtn_connect;
    private Button mBtn_game1;
    private Button mBtn_game2;
    private Button mBtn_game3;
    private Button mBtn_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mBtn_game1 = findViewById(R.id.btn01);
        mBtn_game1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SettingActivity.this,GunActivity.class);
                startActivity(intent);
            }
        });


        mBtn_game2 = findViewById(R.id.btn02);
        mBtn_game2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SettingActivity.this,SodaActivity.class);
                startActivity(intent);
            }
        });


        mBtn_game3 = findViewById(R.id.btn03);
        mBtn_game3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SettingActivity.this,MagicActivity.class);
                startActivity(intent);
            }
        });

        mBtn_return = findViewById(R.id.btn04);
        mBtn_return.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
