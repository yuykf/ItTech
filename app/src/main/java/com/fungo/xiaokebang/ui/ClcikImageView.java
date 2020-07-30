package com.fungo.xiaokebang.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.fungo.xiaokebang.R;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/6.
 */
public class ClcikImageView extends androidx.appcompat.widget.AppCompatImageView {


    private Paint backPaint;

    public ClcikImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        obatin(attrs);
        initPaint();
    }

    private void initPaint() {

        backPaint = new Paint();
        backPaint.setColor(Color.GREEN);
        backPaint.setAntiAlias(true);

    }

    private void obatin(AttributeSet attrs) {
//        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleab);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



    }
}
