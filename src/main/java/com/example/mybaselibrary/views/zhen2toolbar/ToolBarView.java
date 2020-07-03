package com.example.mybaselibrary.views.zhen2toolbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mybaselibrary.R;


/**
 *
 */
public class ToolBarView extends FrameLayout implements View.OnClickListener {

    public static final int SKIN_COLOR = -1;
    public static final int SKIN_GREEN = 0;
    public static final int SKIN_WHITE = 1;
    public static final int SKIN_ALPHA = 2;
    public static final int SKIN_RED = 3;
    private int mBackgroundColor;
    private int skinColorRes;
    private int textColorRes;
    private int mTextColor;
    private int mTitleColor;
    private TextView mTitleView;
    private ToolBarViewListener mNavigationListener;
    private TextView mLeftTextView;
    private TextView mLeftIconView;
    private LinearLayout mLeftLayout;
    private TextView mRightIconView;
    private TextView mRightTextView;
    private LinearLayout mRightLayout;
    private View mUnreadView;
    private TextView mRightSecondTextView;
    private TextView mRightSecondIconView;
    private LinearLayout mRightSecondLayout;
    private View mSecondUnreadView;
    private boolean showBadge = false;
    private View mDivider;
    private RelativeLayout navigationLayout;
    private int iconColorRes;

    public ToolBarView(Context context) {
        this(context, null);
    }

    public ToolBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ToolBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ToolBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View.inflate(getContext(), R.layout.navigation_layout, this);
        navigationLayout = (RelativeLayout) findViewById(R.id.navigation_layout);
        mLeftLayout = (LinearLayout) findViewById(R.id.left_layout);
        mLeftLayout.setOnClickListener(this);
        mLeftTextView = (TextView) findViewById(R.id.left_text);
        mLeftIconView = (TextView) findViewById(R.id.left_icon);
        mRightLayout = (LinearLayout) findViewById(R.id.right_layout);
        mRightLayout.setOnClickListener(this);
        mRightTextView = (TextView) findViewById(R.id.right_text);
        mRightIconView = (TextView) findViewById(R.id.right_icon);
        mRightSecondLayout = (LinearLayout) findViewById(R.id.right_second_layout);
        mRightSecondLayout.setOnClickListener(this);
        mRightSecondTextView = (TextView) findViewById(R.id.right_second_text);
        mRightSecondIconView = (TextView) findViewById(R.id.right_second_icon);
        mTitleView = (TextView) findViewById(R.id.title);
        mTitleView.setOnClickListener(this);
        mUnreadView = findViewById(R.id.unread_num);
        mSecondUnreadView = findViewById(R.id.second_unread_num);
        mDivider = findViewById(R.id.nav_divider);
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.NavigationView);
            skinColorRes = typedArray.getColor(R.styleable.NavigationView_skin_color, -1);
            textColorRes = typedArray.getColor(R.styleable.NavigationView_text_color, -1);
            iconColorRes = typedArray.getColor(R.styleable.NavigationView_icon_color, -1);
            int skin = skinColorRes == -1 ? typedArray.getInt(R.styleable.NavigationView_skin, 1) : -1;
            String mLeftText = typedArray.getString(R.styleable.NavigationView_text_left);
            String mLeftIcon = typedArray.getString(R.styleable.NavigationView_icon_left);
            String mRightText = typedArray.getString(R.styleable.NavigationView_text_right);
            String mRightIcon = typedArray.getString(R.styleable.NavigationView_icon_right);
            String mRightSecondText = typedArray.getString(R.styleable.NavigationView_text_right_second);
            String mRightSecondIcon = typedArray.getString(R.styleable.NavigationView_icon_right_second);
            String mTitleText = typedArray.getString(R.styleable.NavigationView_text_title);
            float iconSize = typedArray.getDimension(R.styleable.NavigationView_icon_size, 20);
            float titleSize = typedArray.getDimension(R.styleable.NavigationView_title_size, 18);
            float textSize = typedArray.getDimension(R.styleable.NavigationView_text_size, 14);
            boolean showDivider = typedArray.getBoolean(R.styleable.NavigationView_show_divider, false);
            boolean showShadow = typedArray.getBoolean(R.styleable.NavigationView_show_shadow, false);
            boolean showIconBg = typedArray.getBoolean(R.styleable.NavigationView_show_icon_bg, true);
            setSkin(skin);
            if (showShadow) {
                mTitleView.setShadowLayer(3, 0, 5, Color.parseColor("#88000000"));
            }
            if (!showIconBg) {
                mLeftIconView.setBackgroundResource(R.color.transparent);
                mRightIconView.setBackgroundResource(R.color.transparent);
                mRightSecondIconView.setBackgroundResource(R.color.transparent);
            }
            if (showDivider) {
                setDividerVisibility(VISIBLE);
            }
//            else if (skin == SKIN_WHITE) {
//                setDividerVisibility(VISIBLE);
//            }
            else {
                setDividerVisibility(GONE);
            }
            mTitleView.setTextSize(titleSize);
            mLeftIconView.setTextSize(iconSize);
            mRightIconView.setTextSize(iconSize);
            mRightSecondIconView.setTextSize(iconSize);
            mLeftTextView.setTextSize(textSize);
            mRightTextView.setTextSize(textSize);
            mRightSecondTextView.setTextSize(textSize);
            setLeftText(mLeftText);
            setLeftIcon(mLeftIcon);
            setRightIcon(mRightIcon);
            setRightText(mRightText);
            setRightSecondIcon(mRightSecondIcon);
            setRightSecondText(mRightSecondText);
            setTextTitle(mTitleText);
            typedArray.recycle();
        }
    }

    public void setDividerVisibility(int visibility) {
        mDivider.setVisibility(visibility);
    }

    public void setSkin(int skin) {
        if (skin == SKIN_GREEN) {
            mBackgroundColor = getResources().getColor(R.color.theme_green);
            mTextColor = getResources().getColor(R.color.white);
            mTitleColor = getResources().getColor(R.color.white);
            mLeftIconView.setBackgroundResource(R.color.transparent);
            mRightIconView.setBackgroundResource(R.color.transparent);
            mRightSecondIconView.setBackgroundResource(R.color.transparent);
        } else if (skin == SKIN_WHITE) {
            mBackgroundColor = getResources().getColor(R.color.white);
            mTextColor = getResources().getColor(R.color.main_font);
            mTitleColor = getResources().getColor(R.color.main_font);
            mLeftIconView.setBackgroundResource(R.color.transparent);
            mRightIconView.setBackgroundResource(R.color.transparent);
            mRightSecondIconView.setBackgroundResource(R.color.transparent);
        } else if (skin == SKIN_ALPHA) {
            mBackgroundColor = getResources().getColor(R.color.transparent);
            mTextColor = getResources().getColor(R.color.white);
            mTitleColor = getResources().getColor(R.color.white);
//            mLeftIconView.setBackgroundResource(R.drawable.navigation_icon_bg);
//            mRightIconView.setBackgroundResource(R.drawable.navigation_icon_bg);
//            mRightSecondIconView.setBackgroundResource(R.drawable.navigation_icon_bg);
        } else if (skin == SKIN_RED) {
//            mBackgroundColor = getResources().getColor(R.color.theme_red);
            mTextColor = getResources().getColor(R.color.white);
            mTitleColor = getResources().getColor(R.color.white);
            mLeftIconView.setBackgroundResource(R.color.transparent);
            mRightIconView.setBackgroundResource(R.color.transparent);
            mRightSecondIconView.setBackgroundResource(R.color.transparent);
        } else if (skin == SKIN_COLOR) {
            mBackgroundColor = skinColorRes;
            mTextColor = textColorRes;
            mTitleColor = getResources().getColor(R.color.white);
            mLeftIconView.setBackgroundResource(R.color.transparent);
            mRightIconView.setBackgroundResource(R.color.transparent);
            mRightSecondIconView.setBackgroundResource(R.color.transparent);
        }

        setBackgroundColor(mBackgroundColor);
        mLeftTextView.setTextColor(mTextColor);
        mLeftIconView.setTextColor(mTextColor);
        mRightTextView.setTextColor(mTextColor);
        mRightIconView.setTextColor(mTextColor);
        mRightSecondTextView.setTextColor(mTextColor);
        mRightSecondIconView.setTextColor(mTextColor);
        if (mTextColor != 1) {
            mTitleView.setTextColor(mTextColor);
        } else {
            mTitleView.setTextColor(mTitleColor);
        }
    }


    public void setUnreadCount(long count) {
        if (!showBadge)
            return;
        if (count <= 0) {
            mUnreadView.setVisibility(View.GONE);
        } else {
            mUnreadView.setVisibility(View.VISIBLE);
        }
    }

    public void setSecondUnreadCount(long count) {
        if (!showBadge)
            return;
        if (count <= 0) {
            mSecondUnreadView.setVisibility(View.GONE);
        } else {
            mSecondUnreadView.setVisibility(View.VISIBLE);
        }
    }


    public void setLeftText(String text) {
        if (!TextUtils.isEmpty(text)) {
            mLeftTextView.setText(text);
            mLeftTextView.setVisibility(View.VISIBLE);
        } else {
            mLeftTextView.setVisibility(View.GONE);
        }
    }

    public void setLeftTextColor(int color) {
        mLeftTextView.setTextColor(color);
    }

    public void setLeftIcon(String text) {
        if (!TextUtils.isEmpty(text)) {
            mLeftIconView.setText(Html.fromHtml(text));
            mLeftIconView.setVisibility(View.VISIBLE);
        } else {
            mLeftIconView.setVisibility(View.GONE);
        }
    }

    public void setLeftIcon(int resId) {
        if (resId > 0) {
            mLeftIconView.setText(resId);
            mLeftIconView.setVisibility(View.VISIBLE);
        } else {
            mLeftIconView.setVisibility(View.GONE);
        }
    }

    public void setLeftIconColor(int color) {
        mLeftIconView.setTextColor(color);
    }

    public void setLeftIconBackground(int resId) {
        mLeftIconView.setBackgroundResource(resId);
    }

    public void setLeftLayoutBackground(int resId) {
        mLeftLayout.setBackgroundResource(resId);
    }

    public void setRightLayoutBackground(int resId) {
        mRightLayout.setBackgroundResource(resId);
    }

    public TextView getmLeftIconView() {
        return mLeftIconView;
    }

    public void setRightSecondLayoutBackground(int resId) {
        mRightSecondLayout.setBackgroundResource(resId);
    }

    public void setRightText(String text) {
        if (!TextUtils.isEmpty(text)) {
            mRightTextView.setText(text);
            mRightTextView.setVisibility(View.VISIBLE);
        } else {
            mRightTextView.setVisibility(View.GONE);
        }
    }

    public void setRightTextColor(int color) {
        mRightTextView.setTextColor(color);
    }

    public void setRightIcon(String text) {
        if (!TextUtils.isEmpty(text)) {
            mRightIconView.setText(Html.fromHtml(text));
            mRightIconView.setVisibility(View.VISIBLE);
        } else {
            mRightIconView.setVisibility(View.GONE);
        }
    }

    public void setRightIcon(int resId) {
        if (resId > 0) {
            mRightIconView.setText(resId);
            mRightIconView.setVisibility(View.VISIBLE);
        } else {
            mRightIconView.setVisibility(View.GONE);
        }
    }

    public void setRightIconColor(int color) {
        mRightIconView.setTextColor(color);
    }

    public void setRightIconBackground(int resId) {
        mRightIconView.setBackgroundResource(resId);
    }

    public void setRightSecondText(String text) {
        if (!TextUtils.isEmpty(text)) {
            mRightSecondTextView.setText(text);
            mRightSecondTextView.setVisibility(View.VISIBLE);
        } else {
            mRightSecondTextView.setVisibility(View.GONE);
        }
    }

    public void setRightSecondTextColor(int color) {
        mRightSecondTextView.setTextColor(color);
    }

    public void setRightSecondIcon(String text) {
        if (!TextUtils.isEmpty(text)) {
            mRightSecondIconView.setText(Html.fromHtml(text));
            mRightSecondIconView.setVisibility(View.VISIBLE);
        } else {
            mRightSecondIconView.setVisibility(View.GONE);
        }
    }

    public void setRightSecondIcon(int resId) {
        if (resId > 0) {
            mRightSecondIconView.setText(resId);
            mRightSecondIconView.setVisibility(View.VISIBLE);
        } else {
            mRightSecondIconView.setVisibility(View.GONE);
        }
    }

    public void setRightSecondIconColor(int color) {
        mRightSecondIconView.setTextColor(color);
    }

    public void setRightSecondIconBackground(int resId) {
        mRightSecondIconView.setBackgroundResource(resId);
    }

    public void setTextTitle(String text) {
        if (!TextUtils.isEmpty(text)) {
            mTitleView.setText(text);
            mTitleView.setVisibility(View.VISIBLE);
        } else {
            mTitleView.setVisibility(View.GONE);
        }
    }

    public void setTitleColor(int color) {
        mTitleView.setTextColor(color);
    }

    public void setOnNavigationListener(ToolBarViewListener listener) {
        mNavigationListener = listener;
    }

    public String getTitle() {
        return mTitleView.getText().toString();
    }

    public int getmBackgroundColor() {
        return this.mBackgroundColor;
    }

    @Override
    public void onClick(View v) {
        if (mNavigationListener == null) {
            return;
        }
        if (R.id.left_layout == v.getId()) {
            mNavigationListener.onLeftClick();
        } else if (R.id.right_layout == v.getId()) {
            mNavigationListener.onRightClick();
        } else if (R.id.right_second_layout == v.getId()) {
            mNavigationListener.onRightSecondClick();
        }
    }

    public void setShowBadge(boolean showBadge) {
        this.showBadge = showBadge;
    }


    private void alphaBar(float alpha) {
        if (alpha >= 0 && alpha <= 1.0) {
            Log.e("alphaBar", alpha + "");
            this.setAlpha(alpha);
        }
    }

}
