package com.qb.exercise.biz;

import java.util.List;


public interface IBiz<T> {
    void setData(List<T> list);
    List<T> getData();
    void deleteData(int position);
    void addOne(int index,T t);
}
