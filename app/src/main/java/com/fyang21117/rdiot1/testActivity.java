package com.fyang21117.rdiot1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.fyang21117.rdiot1.test1.ChartsActivity;

public class testActivity extends AppCompatActivity {
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, testActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        /* 图表选择*/
        ListView mListView = new ListView(this);

        SimpleAdapter adapter = new SimpleAdapter(this, getData(),
                android.R.layout.simple_list_item_2,
                new String[]{"title", "description"},
                new int[]{android.R.id.text1, android.R.id.text2});

        mListView.setAdapter(adapter);

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(mListView);

        setContentView(layout);
        setTitle("选择显示图表类型");
        //设置页面横屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        OnItemClickListener listener = new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                String chartsTitleCurr[] = getResources().getStringArray(R.array.chartsTitle);
                if (position > chartsTitleCurr.length - 1)
                    return;

                Bundle bundleSimple = new Bundle();
                Intent intent = new Intent();
                bundleSimple.putString("title", chartsTitleCurr[position]);
                intent.setClass(testActivity.this, ChartsActivity.class);
                bundleSimple.putInt("selected", position);
                intent.putExtras(bundleSimple);
                startActivity(intent);
            }
        };
        mListView.setOnItemClickListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, Menu.FIRST + 1, 0, "帮助");
        menu.add(Menu.NONE, Menu.FIRST + 2, 0, "返回");
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
            }
            break;

            case Menu.FIRST + 1:
                finish();
                break;

            case Menu.FIRST + 2:
                Intent intent = new Intent();
                intent.setClass(testActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
        }
        return true;
    }

    private List<Map<String, String>> getData() {
        List<Map<String, String>> listData = new ArrayList<>();

        String chartsTitle[] = getResources().getStringArray(R.array.chartsTitle);
        String chartsDesc[] = getResources().getStringArray(R.array.chartsDesc);

        int count = chartsDesc.length < chartsTitle.length ?
                chartsDesc.length : chartsTitle.length;

        for (int i = 0; i < count; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("title", chartsTitle[i]);
            map.put("description", chartsDesc[i]);
            listData.add(map);
        }
        return listData;
    }
}
