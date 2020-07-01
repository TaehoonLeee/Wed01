package com.example.wed01;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;


public class mySeekBar extends androidx.appcompat.widget.AppCompatSeekBar {
    public mySeekBar(Context context) {
        super(context);
    }

    public mySeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public mySeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float thumb_x = this.getThumb().getBounds().exactCenterX();
        float middle = (float) (this.getHeight());

        int progress = this.getProgress();

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        canvas.drawText("" + (progress+16), thumb_x-30, middle-40, paint);
    }
}
