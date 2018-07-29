package com.fkl.goodstype;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GoodSTypeActivity extends AppCompatActivity implements HomeAdapter.ContentOnItemClickListener {

    private static final String TAG = GoodSTypeActivity.class.getCanonicalName();
    private List<String> menuList = new ArrayList<>();
    private List<CategoryBean.DataBean> homeList = new ArrayList<>();
    private List<Integer> showTitle;
    private ListView lv_menu;
    private ListView lv_content;
    private MenuAdapter menuAdapter;
    private HomeAdapter homeAdapter;
    private int currentItem;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        initView();
        loadData();
    }

    /***
     * 解析json数据
     */
    private void loadData() {

        String json = getJson(this, "category.json");
        CategoryBean categoryBean = JSONObject.parseObject(json, CategoryBean.class);
        showTitle = new ArrayList<>();
        for (int i = 0; i < categoryBean.getData().size(); i++) {
            CategoryBean.DataBean dataBean = categoryBean.getData().get(i);
            menuList.add(dataBean.getModuleTitle());
            showTitle.add(i);
            homeList.add(dataBean);
        }
        tv_title.setText(categoryBean.getData().get(0).getModuleTitle());

        menuAdapter.notifyDataSetChanged();
        homeAdapter.notifyDataSetChanged();
    }

    private void initView() {
        lv_menu = (ListView) findViewById(R.id.lv_menu);
        tv_title = (TextView) findViewById(R.id.tv_titile);
        lv_content = (ListView) findViewById(R.id.lv_content);
        menuAdapter = new MenuAdapter(this, menuList);
        lv_menu.setAdapter(menuAdapter);
        homeAdapter = new HomeAdapter(this, homeList);
        lv_content.setAdapter(homeAdapter);
        homeAdapter.setOnItemClickListener(this);
        lv_menu.setOnItemClickListener(muneOnItemClickListener);
        lv_content.setOnScrollListener(contentScrollListenner);
    }
    private int scrollState;
    private AbsListView.OnScrollListener contentScrollListenner=new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            GoodSTypeActivity.this.scrollState = scrollState;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                return;
            }
            int current = showTitle.indexOf(firstVisibleItem);
            if (currentItem != current && current >= 0) {
                currentItem = current;
                tv_title.setText(menuList.get(currentItem));
                menuAdapter.setSelectItem(currentItem);
                menuAdapter.notifyDataSetInvalidated();
            }
        }
    };
    //菜单栏点击事件
private AdapterView.OnItemClickListener muneOnItemClickListener=new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        menuAdapter.setSelectItem(position);
        menuAdapter.notifyDataSetInvalidated();
        tv_title.setText(menuList.get(position));
        lv_content.setSelection(showTitle.get(position));
    }
};
    /**
     * 得到json文件中的内容
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getJson(GoodSTypeActivity context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        //获得assets资源管理器
        AssetManager assetManager = context.getAssets();
        //使用IO流读取json文件内容
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName), "utf-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    @Override
    public void onItemClick(CategoryBean.DataBean.DataListBean dataList) {
        Log.e(TAG, "onItemClick: "+dataList.toString() );
    }
}
