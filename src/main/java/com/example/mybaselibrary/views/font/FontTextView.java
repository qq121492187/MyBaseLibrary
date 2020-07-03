package com.example.mybaselibrary.views.font;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.mybaselibrary.R;


/**
 * 全局统一的自定义font的textView
 */
public class FontTextView extends AppCompatTextView {

    // Style
    public static final int NORMAL = 0;
    public static final int LIGHT = 1;
    public static final int MEDIUM = 2;
    public static final int BOLD = 3;
    //形状 图
    private GradientDrawable shapeDrawable;

    public FontTextView(Context context) {
        this(context, null);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (!isInEditMode()) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
            int textStyle = typedArray.getInt(R.styleable.FontTextView_font_style, 0);
            initFont(textStyle);
        }
    }

    private void initFont(int textStyle) {
        switch (textStyle) {
            case NORMAL:
                Typeface typeface = FontManager.getInstance().getNormalTypeface();
                if (typeface == null)
                    return;
                setTypeface(typeface);
                break;
            case LIGHT:
                Typeface lightTypeface = FontManager.getInstance().getLightTypeface();
                if (lightTypeface == null)
                    return;
                setTypeface(lightTypeface);
                break;
            case MEDIUM:
                Typeface mediumTypeface = FontManager.getInstance().getMediumTypeface();
                if (mediumTypeface == null)
                    return;
                setTypeface(mediumTypeface);
                break;
            case BOLD:
                Typeface boldTypeface = FontManager.getInstance().getBoldTypeface();
                if (boldTypeface == null)
                    return;
                setTypeface(boldTypeface);
                break;
        }
    }

    public void setFontStyle(int textStyle) {
        initFont(textStyle);
    }

    /**
     * 设置边框
     *
     * @param borderColor 边框颜色
     * @param borderWidth 边框宽度
     */
    private void setBorder(int borderWidth, @ColorInt int borderColor) {
        getGradientDrawable();
        shapeDrawable.setStroke(borderWidth, borderColor);
        setDrawable(shapeDrawable);
    }

    /**
     * 设置背景颜色
     *
     * @param bgColor
     */
    private void setBgColor(@ColorInt int bgColor) {
        if (bgColor == -1) {
            return;
        }
        getGradientDrawable();
        shapeDrawable.setColor(bgColor);
        setDrawable(shapeDrawable);
    }


    /**
     * 设置圆角
     *
     * @param radius 角度
     */
    private void setRadius(float radius) {
        getGradientDrawable();
        shapeDrawable.setCornerRadius(radius);
        setDrawable(shapeDrawable);
    }


    /**
     * 设置圆角
     *
     * @param topLeftRadius     左上
     * @param topRightRadius    右上
     * @param bottomLeftRadius  左下
     * @param bottomRightRadius 右下
     */
    private void setRadius(float topLeftRadius, float topRightRadius, float bottomLeftRadius, float bottomRightRadius) {
        getGradientDrawable();
        shapeDrawable.setCornerRadii(new float[]{
                topLeftRadius, topLeftRadius,
                topRightRadius, topRightRadius,
                bottomRightRadius, bottomRightRadius,
                bottomLeftRadius, bottomLeftRadius
        });
        setDrawable(shapeDrawable);
    }


    /**
     * 设置图形类型
     *
     * @param shape 形状
     */
    private void setShape(int shape) {
        getGradientDrawable();
        shapeDrawable.setShape(shape);
        setDrawable(shapeDrawable);
    }


    /**
     * 设置背景
     *
     * @param drawable 背景
     */
    private void setDrawable(Drawable drawable) {
        setBackgroundDrawable(drawable);
    }

    /**
     * 获取需要设置到背景的图片
     */
    private void getGradientDrawable() {
        if (shapeDrawable == null) {
            shapeDrawable = new GradientDrawable();
        }
    }
}
