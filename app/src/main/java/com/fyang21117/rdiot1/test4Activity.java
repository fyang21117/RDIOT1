package com.fyang21117.rdiot1;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class test4Activity extends AppCompatActivity implements View.OnClickListener{
    public static void actionStart(Context context) {
        Intent intent = new Intent(context,test4Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }

    static TextView test4Text;
    static int buffer[]=new int[]{
            R.string.example_explain,
            R.string.text_tts_source,
            R.string.text_tts_source_en,
            R.string.text_understand_hint,
            R.string.text_RDIOT_hint};
    public  static final  int UPDATE = 1;
    public static  int i=1;

    //将 Handler 声明为静态内部类。并持有外部类的弱引用
    private static class MyHandler extends Handler{
        private final WeakReference<test4Activity> mActivity;
        public MyHandler(test4Activity activity){
            mActivity= new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg){
            test4Activity activity = mActivity.get();
            if (activity!= null)
                switch (msg.what){
                case UPDATE:
                        //test4Text.setText(int resId);
                        test4Text.setText(buffer[i++]);
                        if(i>=5)
                            i=0;
                    break;
                default:break;
            }
        }
    }
    private final  MyHandler mHandler = new MyHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test4);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("handler");

        test4Text= findViewById(R.id.test4view);
        test4Text.setText(buffer[0]);
        Button change =findViewById(R.id.ChangeText);
        change.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ChangeText:
                new Thread(new Runnable() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = UPDATE;
                    message.obj=buffer;
                    mHandler.sendMessage(message);
                }
            }).start();
            break;
            default:break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home: {
                finish();
            }break;
            default :break;
        }
        return super.onOptionsItemSelected(item);
    }
}
