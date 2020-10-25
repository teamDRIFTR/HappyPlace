package com.example.happyplace;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PaintView extends View {
    //constants
    public static int Brush_size = 5;
    public static final int Default_color = Color.BLUE;
    public static final int Default_bg_color = Color.WHITE;
    private static final float touchTol = 4;

    //variables
    private float mX, mY;
    private Paint mPaint;
    private Path mPath;
    private int currentcolor;
    private int BackgroundColor = Default_bg_color;
    private int BrushWidth;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmpPaint = new Paint(Paint.DITHER_FLAG);

    private ArrayList<Draw> paths = new ArrayList<>();
    private ArrayList<Draw> undo = new ArrayList<>();

    public PaintView(Context context) {
        super(context , null);
    }

    public PaintView(Context context , AttributeSet attrs) {
        super(context , attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Default_color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);
    }

    public void initialize(DisplayMetrics displayMetrics) {
        int Height = displayMetrics.heightPixels;
        int Width = displayMetrics.widthPixels;
        mBitmap = Bitmap.createBitmap(Width , Height , Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        currentcolor = Default_color;
        BrushWidth = Brush_size;
    }

    //touch events
    protected void onDraw(Canvas canvas) {
        //drawing background
        canvas.save();
        mCanvas.drawColor(BackgroundColor);

        for (Draw draw : paths) {
            mPaint.setColor(draw.color);
            mPaint.setStrokeWidth(BrushWidth);
            mPaint.setMaskFilter(null);
            mCanvas.drawPath(draw.path , mPaint);
        }
        canvas.drawBitmap(mBitmap , 0 , 0 , mBitmpPaint);
        canvas.restore();
    }

    public void start(float x , float y) {
        mPath = new Path();
        Draw draw = new Draw(currentcolor , BrushWidth , mPath);
        paths.add(draw);
        mPath.reset();
        mPath.moveTo(x , y);
        mX = x;
        mY = y;

    }

    private void TouchUp() {
        mPath.lineTo(mX , mY);
    }

    private void touchMove(float x , float y) {
        //difference in x
        float diffx = Math.abs(x - mX);
        //difference in y
        float diffy = Math.abs(y - mY);

        if (diffx >= touchTol || diffy >= touchTol) {
            mPath.quadTo(mX , mY , (x + mX) / 2 , (y + mY) / 2);

            mY = y;
            mX = x;
        }
    }

    public boolean onTouchEvent(MotionEvent mEvent) {
        float x = mEvent.getX();
        float y = mEvent.getY();

        switch (mEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start(x , y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                TouchUp();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x , y);
                invalidate();
                break;

        }
        return true;
    }

    public void WipeScreen() {
        BackgroundColor = Default_bg_color;
        paths.clear();
        invalidate();
    }

    //allow the user to undo their stroke
    public void undoStroke() {
        if (paths.size() > 0) {
            undo.add(paths.remove(paths.size() - 1));
            invalidate();
        } else {
            Toast.makeText(getContext() , "error" , Toast.LENGTH_LONG).show();
        }
    }

    //allow the user to redo their stroke
    public void RedoStroke() {
        if (undo.size() > 0) {
            paths.add(undo.remove(undo.size() - 1));
            invalidate();
        } else {
            Toast.makeText(getContext() , "error" , Toast.LENGTH_LONG).show();
        }
    }

    //set width and color
    public void SetBrushWidth(int width) {
        BrushWidth = width;
    }

    public void SetBrushColor(int color) {
        currentcolor = color;
    }

    //allows user to save image
    @SuppressLint("WrongThread")
    public void saveImage() {
        int count = 0;

        File Direct = Environment.getExternalStorageDirectory();
        File subDirect = new File(Direct.toString() + "/Picture/Paint");

        if (subDirect.exists()) {
            File[] existing = subDirect.listFiles();
            for (File file : existing) {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                    count++;
                }
            }
        }
        else {
            subDirect.mkdir();
        }

        if (subDirect.exists()) {

            File image = new File(subDirect , "/drawing_" + (count + 1) + ".png");
            FileOutputStream fileOutputStream;

            try {

                fileOutputStream = new FileOutputStream(image);

               mBitmap.compress(Bitmap.CompressFormat.PNG , 100 , fileOutputStream);


                fileOutputStream.flush();
                fileOutputStream.close();

                Toast.makeText(getContext() , "saved" , Toast.LENGTH_LONG).show();

            } catch (FileNotFoundException e) {


            } catch (IOException e) {


            }
        }
    }








}
