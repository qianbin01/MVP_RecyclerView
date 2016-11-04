package com.qb.exercise.data;

/**
 * Created by qianb-fnst on 2016/11/4.
 */

public class DemoModel {
    private String text;

    public DemoModel(String text) {
        this.text = text;
    }

    public DemoModel() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
