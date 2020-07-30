package com.fungo.xiaokebang.ui.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Class:
 * Other:
 * Create by yuy on  2020/7/10.
 */
public class PreItemDecoration extends RecyclerView.ItemDecoration {

    private Paint prePaint, numPaint;

    private TextPaint textPaint; //文字
    public PreItemDecoration() {
        this.prePaint = new Paint();
        numPaint = new Paint();
        numPaint.setColor(Color.YELLOW);

        prePaint.setColor(Color.WHITE);
        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(25);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int index = parent.getChildAdapterPosition(view);
        if (index != 0) {
            outRect.left = 15;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(view);
            if (index == 0) {
                c.drawRect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom(), prePaint);
                continue;
            }
            c.drawRect(view.getLeft() - 15, view.getTop(), view.getRight(), view.getBottom(), prePaint);
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int childNum = parent.getChildCount();
        for (int i = 0; i < childNum; i++) {
            View view = parent.getChildAt(i);
            int index = parent.getChildAdapterPosition(view);
            c.drawRect(view.getLeft(), view.getTop(), view.getLeft() + 30, view.getTop() + 30, numPaint);
            c.drawText(String.valueOf(index+1), view.getLeft() + 9, view.getTop() + 22, textPaint);
        }

     }

}
