package com.example.app;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lsh.XXRecyclerview.CommonRecyclerAdapter;
import com.lsh.XXRecyclerview.CommonViewHolder;
import com.lsh.XXRecyclerview.MultiTypeSupport;
import com.lsh.XXRecyclerview.PullRefreshRecycleView;
import com.lsh.XXRecyclerview.XXRecycleView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private XXRecycleView rv;

    private ArrayList<String> datas = new ArrayList<>();
    Handler mHandler = new Handler();
    int i = 0;
    private View mHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = (XXRecycleView) findViewById(R.id.rv);
//        View emptyView = findViewById(R.id.main_empty);
//        ViewGroup rootView = (ViewGroup) rv.getRootView();
//        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_view, rootView, false);
        datas.add("1");
        datas.add("2");
        datas.add("3");
        datas.add("4");
        datas.add("5");
        datas.add("6");
        datas.add("7");
        datas.add("8");
        datas.add("9");
        datas.add("10");
        datas.add("11");
        datas.add("12");
        datas.add("13");
        datas.add("14");
        datas.add("aa");
        datas.add("bb");
        datas.add("cc");
        datas.add("dd");
        datas.add("aa");
        datas.add("bb");
        datas.add("cc");
        datas.add("dd");
        datas.add("aa");
        datas.add("bb");
        datas.add("cc");
        datas.add("dd");
        datas.add("aa");
        datas.add("bb");
        datas.add("cc");
        datas.add("dd");
        datas.add("aa");
        datas.add("bb");
        datas.add("cc");
        datas.add("dd");
        datas.add("aa");
        datas.add("bb");
        datas.add("cc");
        datas.add("dd");
        datas.add("aa");
        datas.add("bb");
        datas.add("cc");
        datas.add("dd");
//        rv.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
//        final CommonRecyclerAdapter<String> recyclerAdapter = new CommonRecyclerAdapter<String>(this, datas, android.R.layout.simple_list_item_1) {
//
//            @Override
//            public void convert(CommonViewHolder helper, String item, int position, boolean itemChanged) {
//                helper.setText(android.R.id.text1, item);
//            }
//
//        };
//        rv.setAdapter(recyclerAdapter);
//        Toast.makeText(this, "设置适配器", Toast.LENGTH_SHORT).show();
        CommonRecyclerAdapter<String> adapter = new CommonRecyclerAdapter<String>(this, datas, new MultiTypeSupport<String>() {
            @Override
            public int getLayoutId(String item, int position) {
                if (position % 3 == 0) return android.R.layout.simple_list_item_1;
                return android.R.layout.simple_list_item_2;
            }
        }) {
            @Override
            public void convert(CommonViewHolder holder, String s, int position, boolean isChanged) {
//                if (position % 2 == 0) {


                if (position % 3 == 0) {
                    holder.setText(android.R.id.text1, s);
                    holder.getView(android.R.id.text1).setBackgroundColor(Color.RED);
                } else {
                    holder.setText(android.R.id.text1, s);
                    holder.setText(android.R.id.text2, s);
                    holder.getView(android.R.id.text1).setBackgroundColor(Color.GRAY);
                    holder.getView(android.R.id.text2).setBackgroundColor(Color.GRAY);

                }
//                } else {
//                    holder.setText(android.R.id.text1, s);
//                    holder.setText(android.R.id.text2, s);
//                }

            }

        };
//        rv.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        rv.setLayoutManager(new GridLayoutManager(this,4));
        rv.setAdapter(adapter);
//        WrapRecyclerAdapter wrapRecyclerAdapter = new WrapRecyclerAdapter(adapter);
        rv.setPullRefreshEnabled(true);
        rv.setLoadMoreEnabled(true);
        mHeader = LayoutInflater.from(this).inflate(R.layout.kehu_detail_header, null);
        rv.addHeaderView(mHeader);

        TextView headerView1 = new TextView(this);
        headerView1.setText("这是头部1");
//        TextView headerView2 = new TextView(this);
//        headerView2.setText("这是头部2");
//        wrapRecyclerAdapter.addHeaderView(headerView);
        TextView footerView1 = new TextView(this);
        footerView1.setText("这是脚部1");
//        TextView footerView2 = new TextView(this);
//        footerView2.setText("这是脚部2");
//        wrapRecyclerAdapter.addFooterView(footerView);
//        rv.addHeaderView(headerView2);
        rv.addFooterView(footerView1);
//        rv.addFooterView(footerView2);
//        rv.setPullRefreshEnabled(true);
//        rv.addRefreshViewCreator(new DefaultRefreshCreator());
//        rv.addHeaderView(headerView1);
//        rv.addFooterView(footerView1);
//        rv.addLoadViewCreator(new DefaultLoadCreator());
        rv.setOnLoadMoreListener(new XXRecycleView.OnLoadMoreListener() {
            @Override
            public void onLoad() {
//                i++;
//                if (i < 4) {
//                    recyclerAdapter.addAll(datas);
//                    rv.setLoadMoreEnabled(true);
//                } else {
//                    rv.setLoadMoreEnabled(false);
//                }
//                rv.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        rv.stopLoad();
//                    }
//                }, 2000);
            }

            @Override
            public void loadEnd() {
            }
        });
        rv.setOnRefreshListener(new PullRefreshRecycleView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rv.stopRefresh();
                    }
                }, 2000);
                Toast.makeText(MainActivity.this, "正在刷新", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void refreshEnd() {
//                rv.stopLoad();
//                rv.setLoadMoreEnabled(false);
            }
        });
//        adapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClickListener(CommonViewHolder commonViewHolder, int position) {
//                Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
        int footerCount = rv.getFooterCount();
        int headerCount = rv.getHeaderCount();
//        Toast.makeText(this, "footerCount:" + footerCount, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "headerCount:" + headerCount, Toast.LENGTH_SHORT).show();

    }

    int index = 0;
    public void c(View view) {
        if (index % 2 == 0) {
            rv.setCanRefresh(true);
        } else {
           rv.setCanRefresh(false);
        }
        index++;
    }
}
