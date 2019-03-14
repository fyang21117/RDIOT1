package com.fyang21117.rdiot1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class test2Activity extends AppCompatActivity implements View.OnClickListener
{
    public static void actionStart(Context context) {
        Intent intent = new Intent(context,test2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }

    private ScrollView scrollView;
    private LinearLayout ProductsContainer;
    private LinearLayout ProductNum;
    private Button btnAll;

    String cpname[]=new String[]{"空气净化器","智能风扇","智能马桶","智能电饭煲"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        setTitle("设备管理");
        initView();
        setListeners();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_all://点击添加按钮就动态添加Item
                Toast.makeText(this,"设备数据上传中，请稍后！",Toast.LENGTH_SHORT).show();
                 /* List<String> list = getDataList();
                if(list.size() <= 0)
                    Toast.makeText(this, "请选择设备 ", Toast.LENGTH_SHORT).show();
                else{
                    StringBuilder sb = new StringBuilder("");
                    for(String str : list){
                        sb.append(str).append(",");
                    }
                    Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(this,"设备数据上传中，请稍后！",Toast.LENGTH_SHORT).show();
                }
                break;*/
        }
    }
    private void initView(){
        scrollView = findViewById(R.id.scroll_view);
        ProductsContainer = findViewById(R.id.prod_container);
        ProductNum = findViewById(R.id.Add_prod_num);
        btnAll = findViewById(R.id.btn_all);

        //TextView et_vip_number =  findViewById(R.id.et_vip_number);
        //et_vip_number.setText(cpname[2]);

        for(int i = 0; i < 4; i++){
            addViewItem();
        }
    }

    private void setListeners(){
        ProductNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addViewItem();
            }
        });
        btnAll.setOnClickListener(this);
    }

    /*** 添加item*/
    private void addViewItem(){
        View viewItem = LayoutInflater.from(this).inflate(R.layout.item_add, ProductsContainer,false);
        ProductsContainer.addView(viewItem);
        sortViewItem();
        //添加并且排序之后将布局滚动到底部，方便用户继续添加
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    /*** 该方法主要用于排序（每个item中的序号），主要针对从中间删除item的情况*/
    private void sortViewItem(){
        for(int i = 0; i < ProductsContainer.getChildCount(); i++){
            final View viewItem = ProductsContainer.getChildAt(i);
            TextView prodIndex = viewItem.findViewById(R.id.prod_index);
            prodIndex.setText((i+1) + "");
            LinearLayout prodDelete =  viewItem.findViewById(R.id.prod_delete);
            prodDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductsContainer.removeView(viewItem);
                    sortViewItem();
                }
            });
        }
    }

    private List<String> getDataList() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < ProductsContainer.getChildCount(); i++) {
            View itemView = ProductsContainer.getChildAt(i);
            EditText product =  itemView.findViewById(R.id.product);
            if (product != null) {
                String productNum = product.getText().toString().trim();
                if (!TextUtils.isEmpty(productNum)) {
                    result.add(productNum);
                }
            }
        }
        return result;
    }
}