package com.cxd.processbar_demo;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxd.processbar.DefaultDragView;
import com.cxd.processbar.DensityUtil;
import com.cxd.processbar.IProcessListener;
import com.cxd.processbar.TVDragView;
import com.cxd.processbar.VerticalProcessBar;
import com.cxd.processbar.VerticalProcessOptions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int[] process = {25};

        DefaultDragView slider = new DefaultDragView(this);
        int w = DensityUtil.dip2px(this,20);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w,w);
//        params.leftMargin = DensityUtil.dip2px(this,30 - 20) / 2  ;
        slider.setLayoutParams(params);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50,50);

//        View slider = LayoutInflater.from(this).inflate(R.layout.drag_view_layout,null,false);
//        slider.setLayoutParams(new RelativeLayout.LayoutParams(220,100));
//        final TextView tv = slider.findViewById(R.id.tv) ;

        final VerticalProcessOptions options = new VerticalProcessOptions.Builder()
                .backColor(Color.parseColor("#80FFFFFF"))
                .backRadius(DensityUtil.dip2px(this,12))
                .barMargin(DensityUtil.dip2px(this,20))
                .barColor(Color.parseColor("#DDDDDD"))
                .barFillColor(Color.BLUE)
                .barWidth(DensityUtil.dip2px(this,3))
                .barRadius(DensityUtil.dip2px(this,1.5f))
                .direction(GradientDrawable.Orientation.TOP_BOTTOM)
                .initialProcess(process[0])
                .slider(slider)
                .sliderOffset(DensityUtil.dip2px(this,30 - 20) / 2)
                .build();

        final VerticalProcessBar vpb = new VerticalProcessBar(this,options);
        RelativeLayout fl = findViewById(R.id.processBarRL);
        fl.addView(vpb);
        vpb.setAlpha(0.5f);


        /*初始 tv不显示*/
//        tv.setVisibility(View.INVISIBLE);
        vpb.setOnProcessListener(new IProcessListener() {
            @Override
            public void onProcess(int process) {
//                tv.setText(process+"");

            }

            @Override
            public void onFingerDown() {
                vpb.setAlpha(1.0f);
//                tv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFingerUp(int process) {
                vpb.setAlpha(0.5f);
//                tv.setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process[0] += 1 ;
                vpb.setProcess(process[0]);
            }
        });

    }
}
