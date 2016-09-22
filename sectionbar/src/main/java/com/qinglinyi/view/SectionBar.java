package com.qinglinyi.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
    private int textColor = 0xFF333333;// 默认文本颜色
    private float textSize;
    private float spacing = 0;//

    private OnActionListener mOnActionListener;
    private float letterHeight;

    // 高亮
    private int curPosition = -1;
    private Drawable mSelectedDrawable;
    private Drawable mHightlightDrawable;

    private String[] DEFAULT_LIST = new String[]{
            "↑", "☆", "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z",
            "#"};

    public SectionBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public SectionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SectionBar(Context context) {
        super(context);
        init(context, null);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SectionBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.SectionBar);
            spacing = a.getDimension(R.styleable.SectionBar_spacing, 0);
            textColor = a.getColor(R.styleable.SectionBar_textColor, textColor);
            mSelectedDrawable = a.getDrawable(R.styleable.SectionBar_selectedDrawable);
            mHightlightDrawable = a.getDrawable(R.styleable.SectionBar_hightlightDrawable);
            a.recycle();
        }

        list = DEFAULT_LIST;
        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

    }

    public void setSelectedDrawable(Drawable selectedDrawable) {
        mSelectedDrawable = selectedDrawable;
    }

    public void setSelectedDrawable(int res) {
        mSelectedDrawable = getResources().getDrawable(res);
    }

    public void setList(String[] list) {
        this.list = list;
        postInvalidate();
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


    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public String[] getList() {
        return list;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculate();

        // 选中时候的背景
        if (isPressed() && mHightlightDrawable != null) {
            mHightlightDrawable.setBounds(0, 0, getWidth(), getHeight());
            mHightlightDrawable.draw(canvas);
        }

        // item 高亮
        if (curPosition >= 0 && curPosition < list.length) {
            float top = curPosition * letterHeight + textSize / 2;
            if (spacing <= 0) {
                top += letterHeight * 0.2f;
            } else {
                top += spacing / 2;
            }
            if (mSelectedDrawable != null) {
                mSelectedDrawable.setBounds(
                        getPaddingLeft(),
                        (int) top,
                        getWidth() - getPaddingRight(),
                        (int) (top + letterHeight));
                mSelectedDrawable.draw(canvas);
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

    /**
     * Determines the width of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
//            // Measure the text
//            result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
//                    + getPaddingRight();
//            if (specMode == MeasureSpec.AT_MOST) {
//                // Respect AT_MOST value if that was what is called for by measureSpec
//                result = Math.min(result, specSize);
//            }
        }

        return result;
    }

    /**
     * Determines the height of this view
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

//        mAscent = (int) mTextPaint.ascent();
//        if (specMode == MeasureSpec.EXACTLY) {
//            // We were told how big to be
//            result = specSize;
//        } else {
//            // Measure the text (beware: ascent is a negative number)
//            result = (int) (-mAscent + mTextPaint.descent()) + getPaddingTop()
//                    + getPaddingBottom();
//            if (specMode == MeasureSpec.AT_MOST) {
//                // Respect AT_MOST value if that was what is called for by measureSpec
//                result = Math.min(result, specSize);
//            }
//        }
        return result;
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

