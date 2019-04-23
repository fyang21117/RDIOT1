package com.fyang21117.rdiot1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fyang21117.rdiot1.test2.DataArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class test2Activity extends AppCompatActivity implements View.OnClickListener {
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, test2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }

    private EditText           mDeviceNameEdit;
    private EditText           mDeviceNumEdit;
    private EditText           mDeviceStatusEdit;
    private ProgressBar        mProgressBar;
    private ListView           mDataListView;
    private DataArrayAdapter   mAdapter;
    private List<List<String>> mList = new ArrayList<>();

    String Name[]   = new String[]{"空气净化器", "智能风扇", "智能马桶", "智能电饭煲"};
    String Status[] = new String[]{"关", "开", "开", "关"};
    int    Num[]    = new int[]{1, 3, 5, 7};
    int    id       = 0, num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("设备管理");

        mProgressBar = findViewById(R.id.progress_bar);
        mDeviceNameEdit = findViewById(R.id.device_name_edit);
        mDeviceNumEdit = findViewById(R.id.device_num_edit);
        mDeviceStatusEdit = findViewById(R.id.device_status_edit);
        Button mSaveBtn = findViewById(R.id.add_btn);
        mDataListView = findViewById(R.id.data_list_view);

        mSaveBtn.setOnClickListener(this);
        mAdapter = new DataArrayAdapter(this, 0, mList);
        mDataListView.setAdapter(mAdapter);
        populateDataFromDB();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_btn: {
                id++;
                refreshListView(id, Name[num++], Num[id % 4], Status[id % 4]);
                if (num > 3) {
                    num = 0;
                }
            }
            break;
            default:
                break;
        }
    }

    private void populateDataFromDB() {
        mProgressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mList.clear();
                List<String> columnList = new ArrayList<String>();
                columnList.add("Id");
                columnList.add("Name");
                columnList.add("Num");
                columnList.add("Status");
                mList.add(columnList);
                try {
                    long Id = 0;
                    String Name = "zero";
                    int Num = 0;
                    String Status = "关";
                    List<String> stringList = new ArrayList<String>();
                    stringList.add(String.valueOf(Id));
                    stringList.add(Name);
                    stringList.add(String.valueOf(Num));
                    stringList.add(Status);
                    mList.add(stringList);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            mAdapter.notifyDataSetChanged();//重绘当前可见区域
                        }
                    });
                }
            }
        }).start();
    }

    private void refreshListView(long Id, String Name, int Num, String Status) {
        List<String> stringList = new ArrayList<String>();
        stringList.add(String.valueOf(Id));
        stringList.add(Name);
        stringList.add(String.valueOf(Num));
        stringList.add(Status);
        mList.add(stringList);
        mAdapter.notifyDataSetChanged();
        mDataListView.setSelection(mList.size());
    }
}