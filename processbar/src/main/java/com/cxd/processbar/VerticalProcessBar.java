package com.cxd.processbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 * Create by chenxiaodong on 2020/1/26 0026 21:57
 * 垂直方向的进度条
 */
public class VerticalProcessBar extends RelativeLayout {
    private final String TAG = "VerticalProcessBar_TAG";


    private VerticalProcessOptions options ;
    private Bar bar ;
    private int sliderW,sliderH ;
    private int width , height;

    @SuppressLint("ClickableViewAccessibility")
    public VerticalProcessBar(Context context , final VerticalProcessOptions options) {
        super(context);

        this.options = options ;
        this.setClipChildren(false);
        this.setWillNotDraw(false); //避免不绘制背景
        this.setLayoutParams(new LayoutParams(-1,-1));

        if(options.backColor != 0){
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(options.backColor);
            gd.setCornerRadius(options.backRadius);
            this.setBackground(gd);
        }


        /*添加bar*/
        bar = new Bar(context);
        this.addView(bar);

        /*添加slider*/
        if(options.slider != null){
            this.addView(options.slider);

            /*设置初始位位置*/
//            final int[] lastP = new int[]{0};
            options.slider.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    sliderW = options.slider.getWidth() ;
                    sliderH = options.slider.getHeight();
                    if(sliderH > 0){
//                        options.slider.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        int processH = 0 ; //进度条中的h
                        if(options.direction == GradientDrawable.Orientation.TOP_BOTTOM){
                            /*根据process值移动到对应地方*/
                            processH = (int) ((options.initialProcess * 1.0f )/ 100 * (height - options.barMargin * 2));
                        }else if(options.direction == GradientDrawable.Orientation.BOTTOM_TOP){
                            processH = (int) (((100 - options.initialProcess) * 1.0f )/ 100 * (height - options.barMargin * 2));
                        }

//                        lastP[0] = processH + options.barMargin;

                        /*slider初始位置*/
                        /*options.sliderOffset + options.slider.getWidth() */
//                        options.slider.layout(options.sliderOffset,options.barMargin + processH - sliderH / 2,
//                                options.sliderOffset + sliderW, options.barMargin + processH + sliderH / 2 );
//                        Log.i(TAG, "onGlobalLayout: options.slider.getWidth() = " +options.slider.getWidth());
                        options.slider.layout(options.sliderOffset,options.barMargin + processH - sliderH / 2,
                                width, options.barMargin + processH + sliderH / 2 );
                        Log.i(TAG, "onGlobalLayout: sliderW = " +sliderW);
                    }
                }
            });

            options.slider.setOnTouchListener(new OnTouchListener() {
                float downY = 0;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            if(processListener != null){
                                processListener.onFingerDown();
                            }

                            downY = event.getY() ;

                            break;
                        case MotionEvent.ACTION_MOVE:
                            float offsetY = event.getY() - downY ;
//                            Log.i(TAG, "onTouch: options.slider.getTop() = "+options.slider.getTop());
                            if(options.slider.getTop() + offsetY < options.barMargin - sliderH / 2 ||
                                    options.slider.getTop() + offsetY > height - options.barMargin - sliderH / 2){

                            }else{
                                options.slider.layout(options.slider.getLeft(), (int) (options.slider.getTop() + offsetY),
                                        options.slider.getRight(), (int) (options.slider.getBottom() + offsetY));
                            }

                            setProcess(getCurrentProcess());

                            if(options.slider instanceof TVDragView){
                                TVDragView view = (TVDragView) options.slider;
                                view.setText(getCurrentProcess()+"");
                            }

                            if(processListener != null){
                                processListener.onProcess(getCurrentProcess());
                            }
                            break;

                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            if(processListener != null){
                                processListener.onFingerUp(getCurrentProcess());
                            }


                            break;
                    }
                    return true;
                }
            });

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth() ;
        height = getMeasuredHeight() ;
    }

    public int getCurrentProcess () {
       int totalProcessH = height - 2 * options.barMargin ;
       int currentProcessH = 0 ;
       if(options.direction == GradientDrawable.Orientation.TOP_BOTTOM){
           currentProcessH = options.slider.getTop() + sliderH / 2 - options.barMargin  ;
       }else if(options.direction == GradientDrawable.Orientation.BOTTOM_TOP){
           currentProcessH = totalProcessH - (options.slider.getTop() + sliderH / 2 - options.barMargin) ;
       }

       float bili = (currentProcessH * 1.0f) / (totalProcessH * 1.0f) ;

       /*做边界优化*/
       int tempProcess = (int) (bili * 1000);
       if(tempProcess >= 992){
           return 100 ;
       }else if(tempProcess <= 6){
           return 0 ;
       }else{
           return (int) (bili * 100) ;
       }
    }

    public void setProcess(int process){
        if(process < 0 ){
            process = 0 ;
        }else if(process > 100){
            process = 100 ;
        }


        options.initialProcess = process ;
        bar.postInvalidate();
    }

    private IProcessListener processListener ;
    public void setOnProcessListener(IProcessListener processListener){
        this.processListener = processListener ;
    }


    class Bar extends View{
        private int height ;

        public Bar(Context context) {
            super(context);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(options.barWidth,-1) ;
            params.topMargin = params.bottomMargin = options.barMargin ;
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            this.setLayoutParams(params);

            /*绘制背景*/
            GradientDrawable gd = new GradientDrawable() ;
            gd.setCornerRadius(options.barRadius);
            gd.setColor(options.barColor);
            this.setBackground(gd);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            height = getMeasuredHeight() ;
        }

        /*获取canvas裁剪之后的path*/
        private Path path ;
        private Path getCanvasClipPath(){
            if(path == null){
                path = new Path();
            }else{
                path.reset();
            }

            RectF rectF = new RectF();
            rectF.set(0,0,options.barWidth,height);
            path.addRoundRect(rectF,options.barRadius,options.barRadius, Path.Direction.CCW);
            return path ;
        }

        private RectF rectf ;
        private Path getProcessPath(){
            if(path == null){
                path = new Path();
            }else{
                path.reset();
            }

            if(rectf == null){
                rectf = new RectF();
            }

            if(options.direction == GradientDrawable.Orientation.TOP_BOTTOM){
                /*自上而下*/
                int h = (int) ((options.initialProcess * 1.0f )/ 100 * height);

                rectf.set(0,0,options.barWidth,h);
                path.addRect(rectf, Path.Direction.CCW);
                path.close();

            }else if(options.direction == GradientDrawable.Orientation.BOTTOM_TOP){
                /*自下而上*/
                int h = (int) (((100 - options.initialProcess) * 1.0f )/ 100 * height);

                rectf.set(0,h,options.barWidth,height);
                path.addRect(rectf, Path.Direction.CCW);
                path.close();

            }

            return path ;
        }

        private Paint paint ;
        private Paint getProcessPaint(){
            if(paint == null){
                paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(options.barFillColor);
                paint.setStyle(Paint.Style.FILL);
            }
            return paint ;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            /*这里根据当前的进度值绘制*/
            canvas.clipPath(getCanvasClipPath());
            canvas.drawPath(getProcessPath(),getProcessPaint());

        }
    }

    
}
