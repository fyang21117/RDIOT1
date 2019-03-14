package com.fyang21117.rdiot1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.fyang21117.rdiot1.second.LoginActivity.unlocked;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static void actionStart(Context context) {
            Intent intent = new Intent(context,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(intent);
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle("疯狂打码中");
            actionBar.setTitle("个人主页");
        }
        Button CP1 = findViewById(R.id.cp1);
        Button CP2 = findViewById(R.id.cp2);
        Button CP3 = findViewById(R.id.cp3);
        Button CP4 = findViewById(R.id.cp4);
        CP1.setOnClickListener(this);
        CP2.setOnClickListener(this);
        CP3.setOnClickListener(this);
        CP4.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.cp1:
                 testActivity.actionStart(this);
                  break;
            case R.id.cp2:
                test2Activity.actionStart(this);
                break;
            case R.id.cp3:
                test3Activity.actionStart(this);
                break;
            case R.id.cp4:
                test4Activity.actionStart(this);
                break;
        default :break;
         }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
           return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home: {
               // if(unlocked)
                    Toast.makeText(this,"主页完善中",Toast.LENGTH_SHORT).show();
                //finish();//返回上个活动：解锁界面
            }break;

            case R.id.add_item: {

            }break;
            default :break;
        }
        return super.onOptionsItemSelected(item);
    }
}
