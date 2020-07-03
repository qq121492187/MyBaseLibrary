package com.example.mybaselibrary.views.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by Zyj on 2016/8/22.
 */
public class FontEditText extends AppCompatEditText {

    public FontEditText(Context context) {
        this(context, null);
    }

    public FontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FontEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Typeface typeface = FontManager.getInstance().getNormalTypeface();
        if (typeface == null)
            return;
        setTypeface(typeface);
    }
}
