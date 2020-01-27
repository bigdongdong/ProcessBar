package com.cxd.processbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.LinearLayout;

/*
 * Create by chenxiaodong on 2020/1/26 0026 22:04
 */
public class VerticalProcessOptions {

    public int backColor ;
    public int backRadius ;
    public int topAndBottomMargin ; //上下的margin
//    public boolean isHorizontalCenter = true; //是否水平居中 ，默认居中
    public int barColor ;
    public int barRadius ;
    public int barFillColor ;
    public int barWidth ;
    public View dragView ;
    public int dragViewOffset ; //正是向右，垂直方向默认居中（与当前进度点重合）
    public IProcessListener iProcessListener ;
    public GradientDrawable.Orientation direction = GradientDrawable.Orientation.TOP_BOTTOM; //TOP_BOTTOM  or BOTTOM_TOP
    public int process ; //这里设置是初始进度值

    public VerticalProcessOptions(Builder builder) {
        this.backColor = builder.backColor ;
        this.backRadius = builder.backRadius ;
        this.topAndBottomMargin = builder.topAndBottomMargin ;
        this.barColor = builder.barColor ;
        this.barRadius = builder.barRadius ;
        this.barFillColor = builder.barFillColor ;
        this.barWidth = builder.barWidth ;
        this.dragView = builder.dragView ;
        this.dragViewOffset = builder.dragViewOffset ;
        this.direction = builder.direction ;
        this.process = builder.process ;
    }

    public static class Builder{

        private int backColor ;
        private int backRadius ;
        private int topAndBottomMargin ; //上下的margin
        //    private boolean isHorizontalCenter = true; //是否水平居中 ，默认居中
        private int barColor ;
        private int barRadius ;
        private int barFillColor ;
        private int barWidth ;
        private View dragView ;
        private int dragViewOffset ; //正是向右，垂直方向默认居中（与当前进度点重合）
        private GradientDrawable.Orientation direction = GradientDrawable.Orientation.TOP_BOTTOM; //TOP_BOTTOM  or BOTTOM_TOP
        private int process ; //这里设置是初始进度值

        public Builder backColor(int backColor){
            this.backColor = backColor ;
            return this ;
        }
        public Builder backRadius(int backRadius){
            this.backRadius = backRadius ;
            return this ;
        }
        public Builder topAndBottomMargin(int topAndBottomMargin){
            this.topAndBottomMargin = topAndBottomMargin ;
            return this ;
        }
        public Builder barColor(int barColor){
            this.barColor = barColor ;
            return this ;
        }
        public Builder barRadius(int barRadius){
            this.barRadius = barRadius ;
            return this ;
        }
        public Builder barFillColor(int barFillColor){
            this.barFillColor = barFillColor ;
            return this ;
        }
        public Builder barWidth(int barWidth){
            this.barWidth = barWidth ;
            return this ;
        }
        public Builder dragView(View dragView){
            this.dragView = dragView ;
            return this ;
        }
        public Builder dragViewOffset(int dragViewOffset){
            this.dragViewOffset = dragViewOffset ;
            return this ;
        }
        public Builder process(int process){
            this.process = process ;
            return this ;
        }
        /**
         * @param direction  TOP_BOTTOM  or BOTTOM_TOP
         * @return
         */
        public Builder direction(GradientDrawable.Orientation direction){
            this.direction = direction ;
            return this ;
        }

        public VerticalProcessOptions build(){
            return new VerticalProcessOptions(this);
        }


    }

}
