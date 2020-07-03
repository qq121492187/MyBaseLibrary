package com.example.mybaselibrary.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.ColorInt;
import android.util.AttributeSet;

import com.example.mybaselibrary.R;


public class BaseButton extends androidx.appcompat.widget.AppCompatButton {
    //形状 图
    private GradientDrawable shapeDrawable;

    public BaseButton(Context context) {
        super(context);
    }

    public BaseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.BaseButton);
        if (a != null) {
            //背景颜色
            int bgColor = a.getColor(R.styleable.BaseButton_bgColor, 0);
            setBgColor(bgColor);
            //获取形状
            int shape = a.getInteger(R.styleable.BaseButton_shape, 0);
            setShape(shape);
            //获取圆角
            float radius = a.getFloat(R.styleable.BaseButton_radius, 0);
            setRadius(radius);
            //获取左上圆角
            float topLeftRadius = a.getFloat(R.styleable.BaseButton_top_left_radius, 0);
            //获取右上圆角
            float topRightRadius = a.getFloat(R.styleable.BaseButton_top_right_radius, 0);
            //获取左下圆角
            float bottomLeftRadius = a.getFloat(R.styleable.BaseButton_bottom_left_radius, 0);
            //获取右下圆角
            float bottomRightRadius = a.getFloat(R.styleable.BaseButton_bottom_right_radius, 0);
            if (topLeftRadius != 0 || bottomLeftRadius != 0 || topRightRadius != 0 || bottomRightRadius != 0) {
                setRadius(topLeftRadius, topRightRadius, bottomLeftRadius, bottomRightRadius);
            }
            //边框宽度
            int borderWidth = (int) a.getDimension(R.styleable.BaseButton_borderWidth, 0);
            //边框颜色
            int borderColor = a.getColor(R.styleable.BaseButton_borderColor, bgColor);
            setBorder(borderWidth, borderColor);
        }
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
    public void setBgColor(@ColorInt int bgColor) {
        getGradientDrawable();
        shapeDrawable.setColor(bgColor);
        setDrawable(shapeDrawable);
    }


    /**
     * 设置圆角
     *
     * @param radius 角度
     */
    public void setRadius(float radius) {
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
