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
import com.cxd.processbar.IProcessListener;
import com.cxd.processbar.VerticalProcessBar;
import com.cxd.processbar.VerticalProcessOptions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int[] process = {25};

//        DefalutDragView dragView = new DefalutDragView(this);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50,50);
//        dragView.setLayoutParams(params);

        View dragView = LayoutInflater.from(this).inflate(R.layout.drag_view_layout,null,false);
//        dragView.setLayoutParams(new RelativeLayout.LayoutParams(220,100));
        final TextView tv = dragView.findViewById(R.id.tv) ;

        final VerticalProcessOptions options = new VerticalProcessOptions.Builder()
                .backColor(Color.parseColor("#80FFFFFF"))
                .backRadius(40)
                .topAndBottomMargin(50)
                .barColor(Color.parseColor("#DDDDDD"))
                .barFillColor(Color.BLUE)
                .barWidth(8)
                .barRadius(20)
                .direction(GradientDrawable.Orientation.BOTTOM_TOP)
                .process(process[0])
                .dragView(dragView)
                .dragViewOffset(-95)
                .build();

        final VerticalProcessBar vpb = new VerticalProcessBar(this,options);
        RelativeLayout fl = findViewById(R.id.processBarRL);
        fl.addView(vpb);
        vpb.setAlpha(0.5f);

        
        final String TAG = "MainActivity_TAG" ;
        vpb.setOnProcessListener(new IProcessListener() {
            @Override
            public void onProcess(int process) {
                Log.i(TAG, "onProcess: " + process);

                tv.setText(process+"");

            }

            @Override
            public void onFingerDown() {
                Log.i(TAG, "onFingerDown: ");
                vpb.setAlpha(1.0f);
                tv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFingerUp() {
                Log.i(TAG, "onFingerUp: ");
                vpb.setAlpha(0.5f);
                tv.setVisibility(View.INVISIBLE);
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
