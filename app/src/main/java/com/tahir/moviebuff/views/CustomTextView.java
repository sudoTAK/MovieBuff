package com.tahir.moviebuff.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


import com.tahir.moviebuff.R;


public class CustomTextView extends android.support.v7.widget.AppCompatTextView {
    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.attrs_fonts,0, 0);
        String fontType = a.getString(R.styleable.attrs_fonts_font_type);
        if (fontType != null) {
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),fontType);
            setTypeface(typeface/*, -1*/);
        }else{
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),getResources().getString(R.string.helveticamedmd_fontfamily));
            setTypeface(typeface/*, -1*/);
        }


    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.attrs_fonts,0, 0);
        String fontType = a.getString(R.styleable.attrs_fonts_font_type);
        if (fontType != null) {
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), fontType);
            setTypeface(typeface/*, -1*/);
        }else{
            Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(), getResources().getString(R.string.helveticamedmd_fontfamily));
            setTypeface(normalTypeface/*, -1*/);
        }

    }

    public CustomTextView(Context context) {
        super(context);
        Typeface normalTypeface = Typeface.createFromAsset(getContext().getAssets(),getResources().getString(R.string.helveticamedmd_fontfamily));
        setTypeface(normalTypeface/*, -1*/);
    }

  /*  public void setTypeface(Typeface tf, int style) {
        super.setTypeface(normalTypeface*//*, -1*//*);

    }*/
}

