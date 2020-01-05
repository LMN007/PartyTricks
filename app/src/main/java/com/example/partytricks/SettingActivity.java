package com.example.partytricks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.partytricks.box2d.software.MyBox2dActivity;

public class SettingActivity extends AppCompatActivity {

    private Button mBtn_test_link;
    private ImageButton mBtn_game1;
    private ImageButton mBtn_game2;
    private ImageButton mBtn_game3;
    private ImageButton mBtn_return;
//    private Button mBtn_game1
//    private Button mBtn_game2;
//    private Button mBtn_game3;
//    private Button mBtn_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mBtn_test_link = findViewById(R.id.btn00);
        mBtn_test_link.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String msg="<DATA STRUCTURE>";
                ((ServiceOnBack)getApplication()).sendMessage(msg.getBytes());
            }
        });
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
