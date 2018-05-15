package com.dc;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2018/4/2 0002.
 * 画正炫曲线
 */

public class ZXQX extends SurfaceViewTemplate {

    public static final  int TIME_INTER_FRAMES = 30;
    public Path mPath  ;
    public Paint mPaint;

    int x = 0;
    int y = 0;
    public ZXQX(Context context) {
        super(context);
    }

    public ZXQX(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initView() {
        super.initView();
        mPath = new Path();
        mPaint = new Paint();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
    }


    @Override
    public void run() {
        while (mIsDrawing){
            long start = System.currentTimeMillis();
            synchronized (mHolder) {
                draw();
                x += 1;
                y=(int)(100*Math.sin(x*2*Math.PI/180)+400);
                mPath.lineTo(x,y);
            }
            long end = System.currentTimeMillis();
            int diff = (int) (end - start);
            while (diff < TIME_INTER_FRAMES){
                diff = (int)(System.currentTimeMillis() - start);
                Thread.yield();
            }
        }
    }

    public void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);
            mCanvas.drawPath(mPath, mPaint);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null){
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }
}
