package com.example.mybaselibrary.views.font;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.mybaselibrary.R;


public class IconTextView extends AppCompatTextView {
    public IconTextView(Context context) {
        super(context);
        init(null);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        String fontPath = null;
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.IconTextView);
            fontPath = typedArray.getString(R.styleable.IconTextView_use_font);
        }
        Typeface typeface;
        if (TextUtils.isEmpty(fontPath)) {
            typeface = FontManager.getInstance().getIconTypeface();
        } else {
            typeface = Typeface.createFromAsset(getContext().getAssets(), fontPath);
        }
        if (typeface == null) {
            typeface = FontManager.getInstance().getDefaultIconTypeFace(getContext());
        }
        if (typeface == null)
            return;
        setTypeface(typeface);
    }


}
