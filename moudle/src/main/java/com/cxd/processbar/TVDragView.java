package com.cxd.processbar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 * Create by chenxiaodong on 2020/1/27 0027 18:56
 *
 * -65
 */
public class TVDragView extends LinearLayout {
    public TextView tv ;
    public TVDragView(Context context) {
        super(context);

        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setLayoutParams(new RelativeLayout.LayoutParams(-2,-2));
        this.setGravity(Gravity.CENTER_VERTICAL);

        tv = new TextView(context);
        tv.setLayoutParams(new RelativeLayout.LayoutParams(120,100));
        tv.setGravity(Gravity.CENTER|Gravity.LEFT);
        tv.setPadding(30,0,0,0);
        tv.setTextSize(14);
        tv.setTextColor(Color.WHITE);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#50111111"));
        gd.setCornerRadius(50);
        tv.setBackground(gd);
        this.addView(tv);

        DefaultDragView dragView = new DefaultDragView(context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(70,70);
        params.leftMargin = -20 ;
        dragView.setLayoutParams(params);
        this.addView(dragView);
    }


    public void setText(String s){
        if(tv != null){
            tv.setText(s);
        }
    }
}
