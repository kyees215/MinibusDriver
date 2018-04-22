package com.hkminibus.minibusdriver;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.TextView;


public class ToolbarHelper {

    public static void addMiddleTitle(Context context, CharSequence title, Toolbar toolbar) {
            TextView textView = new TextView(context);
            textView.setText(title);

            Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            textView.setTextSize(40);

            Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/Bintar Regular.otf");
            textView.setTypeface(custom_font);

            toolbar.addView(textView, params);
    }
}

