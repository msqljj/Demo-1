package com.google.ringwave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * 作 者 : 陈冠杰
 * 版 本 ： 1.0
 * 创建日期 ：2016/5/22 23:12
 * 描 述 ：
 * 修订历史 ：
 * ============================================================
 **/
public class RingWave extends View {

    private List<Wave> mWaveList = new ArrayList<>();
    private int[] colors = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.GRAY, Color.YELLOW};

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            flushData();
            invalidate();
            if (!mWaveList.isEmpty()){
                mHandler.sendEmptyMessageDelayed(0,50);
            }
        }
    };

    private void flushData() {
        ArrayList<Wave> removeList = new ArrayList<>();
        for (Wave wave : mWaveList){
            wave.radius += 3;
            wave.paint.setStrokeWidth(wave.radius/3);

            if (wave.paint.getAlpha() < 0){
                removeList.add(wave);
                continue;
            }

            int alpha = wave.paint.getAlpha();
            alpha -= 5;
            if (alpha < 0) alpha = 0;

            wave.paint.setAlpha(alpha);
        }
        mWaveList.removeAll(removeList);
    }


    public RingWave(Context context) {
        super(context);
    }

    public RingWave(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (Wave wave : mWaveList){
            canvas.drawCircle(wave.cx, wave.cy, wave.radius, wave.paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (MotionEventCompat.getActionMasked(event)){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                addPoint((int)event.getX(), (int)event.getY());
                break;
        }
        return true;
    }

    private void addPoint(int x, int y) {
        if (mWaveList.isEmpty()){
            addWave(x, y);
            mHandler.sendEmptyMessageDelayed(0,50);
        }else {
            Wave lastWave = mWaveList.get(mWaveList.size() - 1);
            if (Math.abs(x - lastWave.cx) > 10 || Math.abs(y - lastWave.cy) > 10){
                addWave(x, y);
            }
        }
    }

    private void addWave(int x, int y) {
        Wave wave = new Wave();
        wave.cx = x;
        wave.cy = y;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(wave.radius/3);
        paint.setAlpha(255);
        paint.setAntiAlias(true);

        int colorIndex = (int) (Math.random()*5);
        paint.setColor(colors[colorIndex]);

        wave.paint = paint;

        mWaveList.add(wave);
    }

    private class Wave{
        public int cx;
        public int cy;
        public int radius;
        public Paint paint;
    }
}
