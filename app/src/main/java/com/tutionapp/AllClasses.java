package com.tutionapp;

public class AllClasses {
    String classname;
    public AllClasses(String classname){
        this.classname = classname;
        int crossMark = R.drawable.ic_baseline_cancel_24;
    }

    public String getClassNames(){
        return classname;
    }
}
