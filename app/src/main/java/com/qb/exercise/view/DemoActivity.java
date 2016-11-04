package com.qb.exercise.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.qb.exercise.R;
import com.qb.exercise.adapater.DemoAdapter;
import com.qb.exercise.data.DemoModel;
import com.qb.exercise.presenter.DemoPresenter;
import com.qb.exercise.view.ui.ItemRemoveRecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DemoActivity extends AppCompatActivity implements IDemoView {
    @Bind(R.id.recycler)
    ItemRemoveRecyclerView recycler;

    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    //MVP-P
    private DemoPresenter demoPresenter;

    //数据源
    private List<DemoModel> mList;
    //recycler布局管理器
    private LinearLayoutManager linearLayoutManager;
    //适配器
    private DemoAdapter mAdapter;

    //当前可见最后项
    private int lastVisibleItem;

    //判断上滑还是下拉
    private static int ADD_FLAG = 0;
    private static int ADD_HEAD = 0;
    private static int ADD_BOTTOM = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_layout);
        ButterKnife.bind(this);
        initConfig();
        initListener();
    }

    //配置相关
    private void initConfig(){
        demoPresenter = new DemoPresenter(this);
        setData();
        getData();
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mAdapter = new DemoAdapter(this, mList);
        recycler.setLayoutManager(linearLayoutManager);
        recycler.setAdapter(mAdapter);
        recycler.setItemAnimator(new DefaultItemAnimator());//设置默认动画
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
    }
    //listener相关
    private void initListener(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                addOne();//下拉刷新
            }
        });
        recycler.setOnItemListener(new ItemRemoveRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                System.out.println("click item" + position);//item点击事件
            }

            @Override
            public void onDeleteClick(int position) {//item左滑删除按钮事件
                deletaData(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(0, mList.size());
            }
        });
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isSignificantDelta = Math.abs(dy) > 10;//滑动幅度限制
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (isSignificantDelta) {
                    if (dy > 0) {//判断上下滑
                        ADD_FLAG = ADD_BOTTOM;
                    } else {
                        ADD_FLAG = ADD_HEAD;
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == mAdapter.getItemCount() && ADD_FLAG == ADD_BOTTOM) {
                    addMore();//上拉加载更多
                }
            }
        });
    }
    @Override
    public void getData() {
        mList = demoPresenter.getData();
    }

    @Override
    public void setData() {
        demoPresenter.setData();
    }

    @Override
    public void addOne() {
        demoPresenter.addOne();
    }

    @Override
    public void deletaData(int position) {
        demoPresenter.deleteData(position);
    }

    //下拉刷新回调
    @Override
    public void addFinish() {
        mAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, "下拉刷新成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addMore() {
        demoPresenter.addMore();
        mAdapter.changeMoreStatus(DemoAdapter.LOADING_MORE);
    }

    //上拉加载回调
    @Override
    public void addMoreFinish(String notice) {
        mAdapter.notifyDataSetChanged();
        mAdapter.changeMoreStatus(DemoAdapter.PULLUP_LOAD_MORE);
        Toast.makeText(this, notice, Toast.LENGTH_SHORT).show();
    }
}
