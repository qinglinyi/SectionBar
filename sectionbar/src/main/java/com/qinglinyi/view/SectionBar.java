package com.qinglinyi.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
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

    private String[] list;// 字符列表
    private Paint textPaint;
    private int textColor = 0xFF333333;// 文本颜色
    private int textSelectedColor;// 选中的文本颜色
    private float textSize; // 字符大小,根据高度和间距计算
    private float spacing = 0;// 字符间距
    private boolean retainSelected;// 是否保留选择的状态

    private OnActionListener mOnActionListener;
    /**
     * item 高度,包括字符以及间距
     */
    private float itemHeight;

    // 高亮
    private int curPosition = -1;
    private Drawable mSelectedDrawable;
    private Drawable mHightlightDrawable;

    private Rect textBound = new Rect();

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
            textSelectedColor = a.getColor(R.styleable.SectionBar_textSelectedColor, textColor);
            mSelectedDrawable = a.getDrawable(R.styleable.SectionBar_selectedDrawable);
            mHightlightDrawable = a.getDrawable(R.styleable.SectionBar_hightlightDrawable);
            retainSelected = a.getBoolean(R.styleable.SectionBar_retainSelected, false);
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
     * <p>
     * 也可以在布局中设置textColor
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

    public void setSpacing(float spacing) {
        this.spacing = spacing;
    }

    public void setTextSelectedColor(int textSelectedColor) {
        this.textSelectedColor = textSelectedColor;
    }

    public void setHightlightDrawable(Drawable hightlightDrawable) {
        mHightlightDrawable = hightlightDrawable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 选中时候的背景
        if (isPressed() && mHightlightDrawable != null) {
            mHightlightDrawable.setBounds(0, 0, getWidth(), getHeight());
            mHightlightDrawable.draw(canvas);
        }

        // item 高亮
        if (curPosition >= 0 && curPosition < list.length) {
            float top = curPosition * itemHeight + getPaddingTop();
            if (mSelectedDrawable != null) {
                mSelectedDrawable.setBounds(
                        getPaddingLeft(),
                        (int) top,
                        getWidth() - getPaddingRight(),
                        (int) (top + itemHeight));
                mSelectedDrawable.draw(canvas);
            }
        }

        textPaint.setTextSize(textSize);

        // 绘制文本
        for (int i = 0; i < list.length; i++) {
            if (curPosition == i) {
                textPaint.setColor(textSelectedColor);
            } else {
                textPaint.setColor(textColor);
            }

            textPaint.getTextBounds(list[i], 0, list[i].length(), textBound);
            Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
            int baseline = (int) ((itemHeight - fontMetrics.bottom + fontMetrics.top) / 2
                    - fontMetrics.top + (i * itemHeight + getPaddingTop()));
            canvas.drawText(list[i], getWidth() / 2, baseline, textPaint);
        }

    }

    /**
     * 根据高度和列表size计算每个字符的高度
     *
     * @param height 控件的高度
     */
    private void calculateItemHeight(int height) {
        itemHeight = (height - getPaddingTop() - getPaddingBottom()) / (1.0f * list.length);
        if (spacing <= 0) {
            textSize = itemHeight * 0.8f;
        } else {
            textSize = itemHeight - spacing;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 使用默认的高度
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        calculateItemHeight(height);
        setMeasuredDimension(measureWidth(widthMeasureSpec), height);
    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) (itemHeight + getPaddingLeft() + getPaddingRight());
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

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
                if (!retainSelected) {
                    curPosition = -1;
                }
                setPressed(false);
                break;
        }
        postInvalidate();
        return true;
    }

    private int getPositionAtY(int y) {
        return Math.min(Math.max(0, (int) ((y - textSize / 2) / itemHeight)), list.length - 1);
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

