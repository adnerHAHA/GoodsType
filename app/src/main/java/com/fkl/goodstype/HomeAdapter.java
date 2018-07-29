package com.fkl.goodstype;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

/**
 * 右侧主界面ListView的适配器
 *
 * @author Administrator
 */
public class HomeAdapter extends BaseAdapter {

    private Context context;
    private List<CategoryBean.DataBean> foodDatas;

    public HomeAdapter(Context context, List<CategoryBean.DataBean> foodDatas) {
        this.context = context;
        this.foodDatas = foodDatas;
    }

    @Override
    public int getCount() {
        if (foodDatas != null) {
            return foodDatas.size();
        } else {
            return 10;
        }
    }

    @Override
    public Object getItem(int position) {
        return foodDatas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryBean.DataBean dataBean = foodDatas.get(position);
        List<CategoryBean.DataBean.DataListBean> dataList = dataBean.getDataList();
        ViewHold viewHold = null;
        GridViewOnItemClick gridViewOnItemClick=null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_home, null);
            viewHold = new ViewHold();
            gridViewOnItemClick=new GridViewOnItemClick();
            viewHold.gridView = (GridViewForScrollView) convertView.findViewById(R.id.gridView);
            viewHold.blank = (TextView) convertView.findViewById(R.id.blank);
            convertView.setTag(viewHold.gridView.getId(),gridViewOnItemClick);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
            gridViewOnItemClick= (GridViewOnItemClick) convertView.getTag(viewHold.gridView.getId());
        }

        HomeItemAdapter adapter = new HomeItemAdapter(context, dataList);
        gridViewOnItemClick.setDataBean(dataList);
        viewHold.gridView.setOnItemClickListener(gridViewOnItemClick);

        viewHold.blank.setText(dataBean.getModuleTitle());
        viewHold.gridView.setAdapter(adapter);

        return convertView;
    }

    private static class ViewHold {
        private GridViewForScrollView gridView;

        private TextView blank;
    }
    private ContentOnItemClickListener onItemClickListener;

    public void setOnItemClickListener(ContentOnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

     interface ContentOnItemClickListener{
       void onItemClick(CategoryBean.DataBean.DataListBean dataListBean);
  }

    class GridViewOnItemClick implements AdapterView.OnItemClickListener {
        List<CategoryBean.DataBean.DataListBean> dataList;
        public void setDataBean(List<CategoryBean.DataBean.DataListBean> dataList) {
            this.dataList = dataList;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (onItemClickListener!=null){
                onItemClickListener.onItemClick(dataList.get(position));
            }
        }
    }
}
