package com.qb.exercise.biz;

import com.qb.exercise.data.DemoModel;

import java.util.ArrayList;
import java.util.List;


public class DemoBiz implements IBiz<DemoModel> {
    private List<DemoModel> modelList;

    public DemoBiz() {
        modelList = new ArrayList<>();
    }


    @Override
    public void setData(List<DemoModel> list) {
        this.modelList = list;
    }

    @Override
    public List<DemoModel> getData() {
        return modelList;
    }

    @Override
    public void deleteData(int position) {
        modelList.remove(position);
    }

    @Override
    public void addOne(int index,DemoModel demoModel) {
        modelList.add(index, demoModel);
    }
}
