package com.ko.wastatus.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.ko.wastatus.R;
import com.ko.wastatus.utils.TypefaceUtils;


public class FontedTextView extends AppCompatTextView {

    public FontedTextView(Context context) {
        this(context, null);
    }

    public FontedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyTypeFace(context, attrs);
    }

    private void applyTypeFace(Context context, AttributeSet attr) {
        if (isInEditMode())
            return;

        TypedArray array = context.obtainStyledAttributes(attr, R.styleable.FontTypeface);
        int typefaceId = array.getInt(R.styleable.FontTypeface_typeface, TypefaceUtils.ROBOTO_REGULAR);
        array.recycle();

        Typeface typeFace = TypefaceUtils.get(context, typefaceId);

        if (typeFace != null) {
            setTypeface(typeFace);
        }
    }

}
