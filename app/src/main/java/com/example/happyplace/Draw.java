package com.example.happyplace;

import android.graphics.Path;

//initializing
public class Draw {
    public int color;
    public int paintWidth;
    public Path path;

    public Draw(int color,int paintWidth,Path path){
        this.color = color;
        this.paintWidth = paintWidth;
        this.path = path;
    }
}
