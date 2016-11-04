package com.qb.exercise.presenter;

import com.qb.exercise.biz.DemoBiz;
import com.qb.exercise.data.DemoModel;
import com.qb.exercise.biz.IBiz;
import com.qb.exercise.view.IDemoView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class DemoPresenter {
    //view接口对象
    private IDemoView view;
    //model接口对象
    private IBiz data;
    private List<DemoModel> modelList;

    //模拟上拉可加载次数为3次
    private int index = 2;

    public DemoPresenter(IDemoView view) {
        this.view = view;
        this.data = new DemoBiz();
        modelList = new ArrayList<>();

    }

    public void setData() {
        //数据初始化
        for (int i = 0; i < 10; i++) {
            DemoModel demoModel = new DemoModel();
            demoModel.setText("item" + i);
            modelList.add(demoModel);
        }
        data.setData(modelList);
    }

    public List<DemoModel> getData() {
        return data.getData();
    }

    public void deleteData(int position) {
        data.deleteData(position);
    }

    public void addOne() {
        //模拟下拉操作，回调显示在主线程，实际可根据网络配置
        Observable
                .timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        DemoModel demoModel = new DemoModel();
                        demoModel.setText("down item");
                        data.addOne(0, demoModel);
                        //view改变
                        view.addFinish();
                    }
                });
    }

    public void addMore() {
        //模拟上拉操作，回调显示在主线程，实际可根据网络配置
        Observable
                .timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (index >= 0) {
                                DemoModel demoModel = new DemoModel();
                                demoModel.setText("up item" + index);
                                data.addOne(data.getData().size(), demoModel);
                            --index;
                            //view改变
                            view.addMoreFinish("上拉加载更多成功");
                        } else {
                            view.addMoreFinish("暂时没有新数据");
                        }
                    }
                });
    }

}
