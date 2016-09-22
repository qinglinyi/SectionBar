package com.qinglinyi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * SectionBar
 * <p/>
 * 文字大小是根据高度自动调节的
 *
 * @author qinglinyi
 * @since 1.0.0
 */
public class SectionBar extends View {

    private String[] list;//
    private Paint textPaint;
    private int textColor = 0xff000000;//
    private int hightlightBgColor = 0xff999999;//
    private float textSize;
    private int spacing;//

    private OnActionListener mOnActionListener;
    private float letterHeight;

    // 高亮
    private int curPosition = -1;
    private Paint hightlightBgPaint;
    private Bitmap hightlightBgBitmap;//
    private Matrix bitmapMatrix;
    private RectF rect;

    private String[] DEFAULT_LIST = new String[]{
            "↑", "☆", "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z",
            "#"};

    public SectionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SectionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SectionBar(Context context) {
        super(context);
        init();
    }

    private void init() {

        list = DEFAULT_LIST;
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

        hightlightBgPaint = new Paint();
        hightlightBgPaint.setAntiAlias(true);
        hightlightBgPaint.setStyle(Paint.Style.FILL);

        bitmapMatrix = new Matrix();
        rect = new RectF();
    }

    public void setList(String[] list) {
        this.list = list;
        invalidate();
    }

    public void setList(List<String> list) {
        setList((String[]) list.toArray());
    }

    /**
     * 设置文字颜色
     *
     * @param color 文字颜色
     */
    public void setTextColor(int color) {
        textColor = color;
    }

    public void setHightlightBgColor(int color) {
        hightlightBgColor = color;
    }

    public void setHightlightBgBitmap(int res) {
        hightlightBgBitmap = BitmapFactory.decodeResource(getResources(), res);
    }

    public void setHightlightBgBitmap(Bitmap bitmap) {
        hightlightBgBitmap = bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculate();

        // hightlight
        hightlightBgPaint.setColor(hightlightBgColor);

        if (curPosition >= 0 && curPosition < list.length) {
            final float top = curPosition * letterHeight + textSize;

            if (hightlightBgBitmap != null) {
                final float sx = getWidth() * 1.0f
                        / hightlightBgBitmap.getWidth();
                final float sy = letterHeight / hightlightBgBitmap.getHeight();
                bitmapMatrix.setTranslate(0, top);
                bitmapMatrix.postScale(sx, sy);
                canvas.drawBitmap(hightlightBgBitmap, bitmapMatrix,
                        hightlightBgPaint);
            } else {
                rect.set(0, top, getWidth(), top + letterHeight);
                canvas.drawRoundRect(rect, 6, 6, hightlightBgPaint);
            }

        }

        textPaint.setColor(textColor);

        textPaint.setTextSize(textSize);

        for (int i = 0; i < list.length; i++) {
            canvas.drawText(list[i], getWidth() >> 1, (i + 1) * letterHeight + textSize / 2, textPaint);
        }

    }

    private void calculate() {
        letterHeight = getHeight() / (1.0f * list.length + 1);
        if (spacing <= 0) {
            textSize = letterHeight * 0.8f;
        } else {
            textSize = letterHeight - spacing;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        calculate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                curPosition = getPositionAtY(y);
                if (mOnActionListener != null) {
                    mOnActionListener.show(list[curPosition]);
                }
                setPressed(true);// 设置状态，对应背景selector的状态
                break;
            default:
                if (mOnActionListener != null) {
                    mOnActionListener.hidden();
                }
                curPosition = -1;
                setPressed(false);
                break;
        }
        postInvalidate();
        return true;
    }

    private int getPositionAtY(int y) {
        return Math.min(Math.max(0, (int) ((y - textSize / 2) / letterHeight)), list.length - 1);
    }

    /**
     * 手指接触监听
     */
    public interface OnActionListener {
        /**
         * 当前选择的符号
         *
         * @param item 符号
         */
        void show(String item);

        /**
         * 手指离开
         */
        void hidden();
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        mOnActionListener = onActionListener;
    }

}

