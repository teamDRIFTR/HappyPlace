package com.example.happyplace;

import android.graphics.Path;

public class Draw {
    public int color;
    public int paintSize;
    public Path path;
    public Draw(int color,int paintSize,Path path){
        this.color = color;
        this.paintSize = paintSize;
        this.path = path;
    }
}
