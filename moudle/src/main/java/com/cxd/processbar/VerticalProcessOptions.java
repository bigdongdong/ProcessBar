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

    /*父布局的属性设置*/
    public int backColor ;
    public int backRadius ;
    
    /*bar的属性*/
    public int barColor ;
    public int barRadius ;
    public int barFillColor ;
    public int barWidth ;
    public int barMargin ; //上下的margin
    
    /*滑块属性*/
    public View slider ;
    public int sliderOffset ; //正是向右，垂直方向默认居中（与当前进度点重合）
    
    /*进度条方向*/
    public GradientDrawable.Orientation direction = GradientDrawable.Orientation.TOP_BOTTOM; //TOP_BOTTOM  or BOTTOM_TOP
    
    /*进度条初始进度值*/
    public int initialProcess; //这里设置是初始进度值

    public VerticalProcessOptions(Builder builder) {
        this.backColor = builder.backColor ;
        this.backRadius = builder.backRadius ;
        this.barMargin = builder.barMargin ;
        this.barColor = builder.barColor ;
        this.barRadius = builder.barRadius ;
        this.barFillColor = builder.barFillColor ;
        this.barWidth = builder.barWidth ;
        this.slider = builder.slider ;
        this.sliderOffset = builder.sliderOffset ;
        this.direction = builder.direction ;
        this.initialProcess = builder.initialProcess ;
    }

    public static class Builder{

        private int backColor ;
        private int backRadius ;
        private int barMargin ; //上下的margin
        private int barColor ;
        private int barRadius ;
        private int barFillColor ;
        private int barWidth ;
        private View slider ;
        private int sliderOffset ; //正是向右，垂直方向默认居中（与当前进度点重合）
        private GradientDrawable.Orientation direction = GradientDrawable.Orientation.TOP_BOTTOM; //TOP_BOTTOM  or BOTTOM_TOP
        private int initialProcess ; //这里设置是初始进度值

        public Builder backColor(int backColor){
            this.backColor = backColor ;
            return this ;
        }
        public Builder backRadius(int backRadius){
            this.backRadius = backRadius ;
            return this ;
        }
        public Builder barMargin(int barMargin){
            this.barMargin = barMargin ;
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
        public Builder slider(View slider){
            this.slider = slider ;
            return this ;
        }
        public Builder sliderOffset(int sliderOffset){
            this.sliderOffset = sliderOffset ;
            return this ;
        }
        public Builder initialProcess(int initialProcess){
            this.initialProcess = initialProcess ;
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
