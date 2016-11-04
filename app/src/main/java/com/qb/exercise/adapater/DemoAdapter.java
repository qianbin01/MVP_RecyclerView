package com.qb.exercise.adapater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qb.exercise.R;
import com.qb.exercise.data.DemoModel;

import java.util.List;


public class DemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DemoModel> mList;
    private LayoutInflater mInflater;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;//底部footview
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //上拉加载更多状态-默认为0
    private int load_more_status = 0;

    public DemoAdapter(Context context, List<DemoModel> mList) {
        mInflater = LayoutInflater.from(context);
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.item_recycler_layout, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        } else {
            View footView = mInflater.inflate(R.layout.item_foot_layout, parent, false);
            FootViewHolder footViewHolder = new FootViewHolder(footView);
            return footViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //判断类型选择不同item布局
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).content.setText(mList.get(position).getText());
            holder.itemView.setTag(position);
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.foot_view_item_tv.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView content;
        public TextView delete;
        public LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.item_content);
            delete = (TextView) itemView.findViewById(R.id.item_delete);
            layout = (LinearLayout) itemView.findViewById(R.id.item_layout);
        }
    }

    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView foot_view_item_tv;

        public FootViewHolder(View view) {
            super(view);
            foot_view_item_tv = (TextView) view.findViewById(R.id.foot_view_item_tv);
        }
    }

    //上拉加载提示语状态修改
    public void changeMoreStatus(int status){
        load_more_status=status;
        notifyDataSetChanged();
    }
}
