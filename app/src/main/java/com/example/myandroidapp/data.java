package com.example.myandroidapp;

import java.io.Serializable;

class Data implements Serializable {

    public String year;
    public String month;

    public Data() {}

    public Data(String year , String month) {
        this.year = year;
        this.month = month;
    }
}