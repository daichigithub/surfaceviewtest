package com.dc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;

/**
 * Created by Administrator on 2018/4/2 0002.
 *
 * 画幸运大转盘
 */

public class LuckyPan extends SurfaceViewTemplate {
    int mRadius;
    Paint mArcPaint;
    Paint mTextPaint;
    RectF mRange;
    int mSpeed = 0;
    boolean isShouldEnd;

    private float mTextSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());

    private String[] mStrs = new String[] { "单反相机", "IPAD", "恭喜发财", "IPHONE",
            "妹子一只", "恭喜发财" };

    private int [] mColors = new int[] { 0xFFFFC300, 0xFFF17E01, 0xFFFFC300,
            0xFFF17E01, 0xFFFFC300, 0xFFF17E01};

    private int [] mBitmaps = new int[]{R.drawable.danfan, R.drawable.ipad, R.drawable.f015, R.drawable.iphone, R.drawable.meizi, R.drawable.f040};

    private Bitmap [] mImgs;

    private int mItemCount = 6;
    public LuckyPan(Context context) {
        super(context);
    }

    public LuckyPan(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void run() {
        while(mIsDrawing) {
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            try {
                if ((end - start)< 50){
                    Thread.sleep( 50 - (end-start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    float mStartAngle = 0;
    public void draw() {
        try {
            mCanvas = mHolder.lockCanvas();
            if (mCanvas != null) {
                //先画背景
                drawBg();
                //画arc
//                drawArc();
                float tmpAngle = mStartAngle;
//                float itemAngle = (float)(360/mItemCount);
//                for(int i = 0 ; i < mItemCount; i ++){
//                    mArcPaint.setColor(mColors[i]);
//                    mCanvas.drawArc(mRange, tmpAngle, itemAngle, true, mArcPaint);
//                    drawText(tmpAngle, itemAngle, mStrs[i]);
//                    drawIcon(tmpAngle, i);
//                    tmpAngle += itemAngle;
//                }

                mCanvas.save();
                mCanvas.translate(mCenter, mCenter);

                RectF r = new RectF(-1 * mRadius/2, -1 * mRadius/2, mRadius/2 , mRadius/2);
                for(int i = 0; i < mItemCount; i ++){
                    mArcPaint.setColor(mColors[i]);
                    mCanvas.drawArc(r, tmpAngle,  60, true, mArcPaint);
                    drawText2(tmpAngle, 60, mStrs[i]);
                    drawIcon2(tmpAngle, i);
                    mCanvas.rotate(60);
                }
                mCanvas.restore();

                mStartAngle += mSpeed;

                if (isShouldEnd){
                    mSpeed -= 1;
                }

                if (mSpeed < 0){
                    mSpeed = 0;
                    isShouldEnd = false;
                }

//                calInExactArea(mStartAngle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null){
                mHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        mRange = new RectF(getPaddingLeft(), getPaddingLeft(), mRadius + getPaddingLeft(), mRadius + getPaddingLeft());
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(Color.WHITE);

        mImgs = new Bitmap[mItemCount];
        for(int i = 0; i < mItemCount; i ++){
            mImgs[i]= BitmapFactory.decodeResource(getResources(), mBitmaps[i]);
        }
    }

    //中心点
    int mCenter;
    //边距
    int mPadding;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Math.min(getMeasuredHeight(), getMeasuredWidth());
        //直径
        mRadius = width - getPaddingLeft() - getPaddingRight();

        mCenter = width/2;
        mPadding = getPaddingLeft();

        setMeasuredDimension(width, width);
    }


    //背景图片
    Bitmap mBgBitmap;
    //画背景
    public void drawBg(){
        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
        mCanvas.drawColor(0XFFFFFFFF);
        mCanvas.drawBitmap(mBgBitmap, null, new Rect(mPadding /2, mPadding /2 ,
               getMeasuredWidth() - mPadding/2, getMeasuredHeight() - mPadding /2), null);

    }

   public void drawText(float startAngle, float item, String title){
       Path  path = new Path();
       path.addArc(mRange, startAngle, item);
       float textWidth = mTextPaint.measureText(title);

       float h = (float) (Math.PI * mRadius /mItemCount/2 - textWidth/2);
       float v = mPadding * 3/2;
       mCanvas.drawTextOnPath(title, path, h, v, mTextPaint);
   }
   //旋转的方式得到
   public void drawText2(float startAngle, float gapAngle, String title){
       RectF r = new RectF(-1 * mRadius/2, -1 * mRadius/2, mRadius/2, mRadius/2);
       Path p = new Path();
       p.addArc(r, startAngle , gapAngle);

       float textWidth = mTextPaint.measureText(title);

       float hoffset = (float) (Math.PI * mRadius /mItemCount/2 - textWidth/2);
       float voffset = mPadding * 3/2;
       mCanvas.drawTextOnPath(title, p, hoffset, voffset, mTextPaint);

   }

   public void drawIcon(float startAngle, int i){
       int imgWidth = mRadius/2/4;
       float angle = (float)( (30 + startAngle) * (Math.PI / 180));

       int x = (int)(mCenter + mRadius /2 /2 * Math.cos(angle));
       int y = (int)( mCenter + mRadius /2/2 * Math.sin(angle));

       Rect rect = new Rect(x - imgWidth/2, y - imgWidth/2, x+ imgWidth/2, y + imgWidth/2);

       mCanvas.drawBitmap(mImgs[i], null, rect, null);

   }

   public void drawIcon2(float startAngle, int i){
       int imgWidth = mRadius/2/4;
       float angle = (float) ((mStartAngle + 30) * (Math.PI/180));
       int x = (int) (mRadius/4 * Math.cos(angle));
       int y = (int) (mRadius/4 * Math.sin(angle));
       Bitmap bitmap = BitmapFactory.decodeResource(getResources(), mBitmaps[i]);
       Rect r = new Rect(x - imgWidth/2, y - imgWidth/2, x + imgWidth/2 , y+imgWidth/2);
       mCanvas.drawBitmap(bitmap, null, r, null);
   }

   public boolean isStart(){
       return mSpeed != 0;
   }

   public boolean isShouldEnd(){
        return isShouldEnd;
    }

}
