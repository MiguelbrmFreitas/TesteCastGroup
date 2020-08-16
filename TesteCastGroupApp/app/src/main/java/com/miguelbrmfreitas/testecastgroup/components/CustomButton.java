package com.miguelbrmfreitas.testecastgroup.components;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miguelbrmfreitas.testecastgroup.R;

public class CustomButton extends RelativeLayout {

    LayoutInflater mInflater;
    private ProgressBar mLoadingIcon;
    private RelativeLayout mButton;
    private TextView mButtonText;
    private ImageView mButtonIcon;

    private ScaleAnimation scaleButton;

    private Drawable mBackground;

    private String mText;

    private boolean mHasIcon;

    private Drawable mIcon;

    private boolean mMatchParent;

    private int mTextColor;

    private int mIconColor;

    private ColorStateList mLoadingColor;

    public CustomButton(Context context) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init();
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomButton, defStyle, 0);

        mText = a.getString(R.styleable.CustomButton_button_text);

        mTextColor = a.getColor(R.styleable.CustomButton_button_text_color, getResources().getColor(R.color.colorPrimary));

        mLoadingColor = a.getColorStateList(R.styleable.CustomButton_loading_color);

        mHasIcon = a.getBoolean(R.styleable.CustomButton_has_icon, false);

        mIcon = a.getDrawable(R.styleable.CustomButton_button_icon);

        mIconColor = a.getColor(R.styleable.CustomButton_button_icon_color, getResources().getColor(R.color.colorPrimary));

        mBackground = a.getDrawable(R.styleable.CustomButton_button_background);

        mMatchParent = a.getBoolean(R.styleable.CustomButton_match_parent, false);

        a.recycle();

        init();
    }
    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);

        mText = a.getString(R.styleable.CustomButton_button_text);

        mTextColor = a.getColor(R.styleable.CustomButton_button_text_color, getResources().getColor(R.color.colorPrimary));

        mLoadingColor = a.getColorStateList(R.styleable.CustomButton_loading_color);

        mHasIcon = a.getBoolean(R.styleable.CustomButton_has_icon, false);

        mIcon = a.getDrawable(R.styleable.CustomButton_button_icon);

        mIconColor = a.getColor(R.styleable.CustomButton_button_icon_color, getResources().getColor(R.color.colorPrimary));

        mBackground = a.getDrawable(R.styleable.CustomButton_button_background);

        mMatchParent = a.getBoolean(R.styleable.CustomButton_match_parent, false);

        a.recycle();

        init();
    }

    public void init() {
        View view = mInflater.inflate(R.layout.layout_custom_floating_button, this, true);

        scaleButton = new ScaleAnimation(1f, 0f,1f,1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleButton.setFillAfter(true);
        scaleButton.setDuration(250);

        mButton = view.findViewById(R.id.custom_floating_button);
        mButtonText = view.findViewById(R.id.button_text);
        mButtonIcon = view.findViewById(R.id.button_icon);
        mLoadingIcon = view.findViewById(R.id.loading_icon);

        if(mText != null){
            mButtonText.setText(mText);
            mButtonText.setTextColor(mTextColor);
        }

        if(!mHasIcon){
            mButtonIcon.setVisibility(View.GONE);
        } else {
            mButtonIcon.setColorFilter(mIconColor);
        }

        if(mIcon != null){
            mButtonIcon.setImageDrawable(mIcon);
        }

        if(mBackground != null){
            mButton.setBackground(mBackground);
        }

        if (mLoadingColor != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mLoadingIcon.setIndeterminateTintMode(PorterDuff.Mode.SRC_IN);
                mLoadingIcon.setIndeterminateTintList(mLoadingColor);
            }
        }
        if(mMatchParent){
            mButton.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.custom_button_height)));
        }
    }

    public void setText(String text) {
        mText = text;
        mButtonText.setText(text);
        invalidate();
    }

    public void setTextColor(int color) {
        mButtonText.setTextColor(getResources().getColor(color));
    }

    public void setIcon(Drawable icon) {
        mHasIcon = true;
        mButtonIcon.setImageDrawable(icon);
        invalidate();
    }

}
