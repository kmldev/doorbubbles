package com.laundry.bubbles.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Varun Ji on 10/12/18.
 */
public class CustomTextViewBold extends TextView
{

    public CustomTextViewBold(Context context) {

        super(context);
        applyCustomFont(context);
    }

    public CustomTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomTextViewBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    public CustomTextViewBold(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context);
    }
    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("segoe_ui.ttf", context);
        setTypeface(customFont, Typeface.BOLD);
    }

    public void setBackground(int button_red) {
    }
}
