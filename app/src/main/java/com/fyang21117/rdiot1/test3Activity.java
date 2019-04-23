package com.fyang21117.rdiot1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fyang21117.rdiot1.iflytek.voicedemo.AsrDemo;
import com.fyang21117.rdiot1.iflytek.voicedemo.IatDemo;
import com.fyang21117.rdiot1.iflytek.voicedemo.IseDemo;
import com.fyang21117.rdiot1.iflytek.voicedemo.TtsDemo;
import com.fyang21117.rdiot1.iflytek.voicedemo.YyDemo;
import com.fyang21117.rdiot1.iflytek.voicedemo.faceonline.OnlineFaceDemo;
import com.fyang21117.rdiot1.iflytek.voicedemo.vocalverify.VocalVerifyDemo;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.sunflower.FlowerCollector;

import static com.fyang21117.rdiot1.iflytek.speech.setting.UrlSettings.PREFER_NAME;

public class test3Activity extends AppCompatActivity implements View.OnClickListener {
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, test3Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }

    private static final String   TAG              = test3Activity.class.getSimpleName();
    private              Toast    mToast;
    private final        int      URL_REQUEST_CODE = 0X001;
    private              TextView edit_text;

    @SuppressLint("ShowToast")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5c0881d0");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("讯飞语音示例");

        edit_text = findViewById(R.id.edit_text);
        StringBuffer buf = new StringBuffer();
        buf.append("当前APPID为：");
        buf.append(getString(R.string.app_id) + "\n");
        buf.append(getString(R.string.example_explain));
        edit_text.setText(buf);
        requestPermissions();
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        // 采用sdk默认url
        //mscInit(null);
        test3Activity.SimpleAdapter listitemAdapter = new test3Activity.SimpleAdapter();
        ((ListView) findViewById(R.id.listview_main)).setAdapter(listitemAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
            }
            break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int tag = Integer.parseInt(view.getTag().toString());
        Intent intent = null;
        switch (tag) {
            case 0:
                // 语音转写
                intent = new Intent(test3Activity.this, IatDemo.class);
                break;
            case 1:
                // 语法识别
                intent = new Intent(test3Activity.this, AsrDemo.class);
                break;
            case 2:
                // 语音助手
                intent = new Intent(test3Activity.this, YyDemo.class);
                break;
            case 3:
                // 语音合成
                intent = new Intent(test3Activity.this, TtsDemo.class);
                break;
            case 4:
                // 语音评测
                intent = new Intent(test3Activity.this, IseDemo.class);
                break;
            case 5:
                // 声纹
                intent = new Intent(test3Activity.this, VocalVerifyDemo.class);
                break;
            case 6:
                intent = new Intent(test3Activity.this, OnlineFaceDemo.class);
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    // Menu 列表
    String items[] = {
            "立刻体验语音听写", "立刻体验语法识别",
            "立刻体验语音助手", "立刻体验语音合成",
            "立刻体验语音评测","立刻体验声纹密码",
            "立刻体验人脸识别"};

    private class SimpleAdapter extends BaseAdapter {
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == convertView) {
                LayoutInflater factory = LayoutInflater.from(test3Activity.this);
                convertView = factory.inflate(R.layout.list_items, null);
            }
            Button btn = convertView.findViewById(R.id.btn);
            btn.setOnClickListener(test3Activity.this);
            btn.setTag(position);
            btn.setText(items[position]);
            return convertView;
        }

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    @Override
    protected void onResume() {
        FlowerCollector.onResume(test3Activity.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(test3Activity.this);
        super.onPause();
    }

    private void requestPermissions() {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                int permission = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            //voice
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.LOCATION_HARDWARE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_SETTINGS,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.READ_CONTACTS,
                            //location
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 0x0010);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void mscInit(String serverUrl) {
        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
        StringBuffer bf = new StringBuffer();
        bf.append("appid=" + getString(R.string.app_id));
        bf.append(",");
        if (!TextUtils.isEmpty(serverUrl)) {
            bf.append("server_url=" + serverUrl);
            bf.append(",");
        }
        //此处调用与SpeechDemo中重复，两处只调用其一即可
        SpeechUtility.createUtility(this.getApplicationContext(), bf.toString());
    }

    private void mscUninit() {
        if (SpeechUtility.getUtility() != null) {
            SpeechUtility.getUtility().destroy();
            try {
                new Thread().sleep(40);
            } catch (InterruptedException e) {
                Log.w(TAG, "msc uninit failed" + e.toString());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (URL_REQUEST_CODE == requestCode) {
            Log.d(TAG, "onActivityResult>>");
            try {
                SharedPreferences pref = getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
                String server_url = pref.getString("url_preference", "");
                String domain = pref.getString("url_edit", "");
                Log.d(TAG, "onActivityResult>>domain = " + domain);
                if (!TextUtils.isEmpty(domain)) {
                    server_url = "http://" + domain + "/msp.do";
                }
                Log.d(TAG, "onActivityResult>>server_url = " + server_url);
                mscUninit();
                new Thread().sleep(40);
                //mscInit(server_url);
            } catch (Exception e) {
                showTip("reset url failed");
            }

        }
    }
}
