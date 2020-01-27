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
    private Context context ;
    private Bar bar ;
    private int dragViewW,dragViewH ;
    private int width , height;

    @SuppressLint("ClickableViewAccessibility")
    public VerticalProcessBar(Context context , final VerticalProcessOptions options) {
        super(context);

        this.context = context ;
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

        /*添加dragView*/
        if(options.dragView != null){
            this.addView(options.dragView);

            /*设置初始位位置*/
//            final int[] lastP = new int[]{0};
            options.dragView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    dragViewW = options.dragView.getWidth() ;
                    dragViewH = options.dragView.getHeight();
                    if(dragViewH > 0){
//                        options.dragView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        int processH = 0 ; //进度条中的h
                        if(options.direction == GradientDrawable.Orientation.TOP_BOTTOM){
                            /*根据process值移动到对应地方*/
                            processH = (int) ((options.process * 1.0f )/ 100 * (height - options.topAndBottomMargin * 2));
                        }else if(options.direction == GradientDrawable.Orientation.BOTTOM_TOP){
                            processH = (int) (((100 - options.process) * 1.0f )/ 100 * (height - options.topAndBottomMargin * 2));
                        }

//                        lastP[0] = processH + options.topAndBottomMargin;

                        /*dragView初始位置*/
                        /*options.dragViewOffset + options.dragView.getWidth() */
//                        options.dragView.layout(options.dragViewOffset,options.topAndBottomMargin + processH - dragViewH / 2,
//                                options.dragViewOffset + dragViewW, options.topAndBottomMargin + processH + dragViewH / 2 );
//                        Log.i(TAG, "onGlobalLayout: options.dragView.getWidth() = " +options.dragView.getWidth());
                        options.dragView.layout(options.dragViewOffset,options.topAndBottomMargin + processH - dragViewH / 2,
                                width, options.topAndBottomMargin + processH + dragViewH / 2 );
                        Log.i(TAG, "onGlobalLayout: dragViewW = " +dragViewW);
                    }
                }
            });

            options.dragView.setOnTouchListener(new OnTouchListener() {
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
//                            Log.i(TAG, "onTouch: options.dragView.getTop() = "+options.dragView.getTop());
                            if(options.dragView.getTop() + offsetY < options.topAndBottomMargin - dragViewH / 2 ||
                                    options.dragView.getTop() + offsetY > height - options.topAndBottomMargin - dragViewH / 2){

                            }else{
                                options.dragView.layout(options.dragView.getLeft(), (int) (options.dragView.getTop() + offsetY),
                                        options.dragView.getRight(), (int) (options.dragView.getBottom() + offsetY));
                            }

                            setProcess(getCurrentProcess());

                            if(options.dragView instanceof TVDragView){
                                TVDragView view = (TVDragView) options.dragView;
                                view.setText(getCurrentProcess()+"");
                            }

                            if(processListener != null){
                                processListener.onProcess(getCurrentProcess());
                            }
                            break;

                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            if(processListener != null){
                                processListener.onFingerUp();
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

    private int getCurrentProcess () {
       int totalProcessH = height - 2 * options.topAndBottomMargin ;
       int currentProcessH = 0 ;
       if(options.direction == GradientDrawable.Orientation.TOP_BOTTOM){
           currentProcessH = options.dragView.getTop() + dragViewH / 2 - options.topAndBottomMargin  ;
       }else if(options.direction == GradientDrawable.Orientation.BOTTOM_TOP){
           currentProcessH = totalProcessH - (options.dragView.getTop() + dragViewH / 2 - options.topAndBottomMargin) ;
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


        options.process = process ;
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
            params.topMargin = params.bottomMargin = options.topAndBottomMargin ;
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
                int h = (int) ((options.process * 1.0f )/ 100 * height);

                //没有圆角直接添加矩形
                rectf.set(0,0,options.barWidth,h);
                path.addRect(rectf, Path.Direction.CCW);
                path.close();

//                if(h < options.barRadius || h > height - options.barRadius){
//                    return path ;
//                }
//
//                if(options.barRadius != 0){
//                    //有圆角
//                    path.moveTo(0,h);
//                    path.lineTo(0,options.barRadius);
//                    rectF.set(0,0,options.barRadius + options.barRadius,options.barRadius + options.barRadius);
//                    path.arcTo(rectF,180,90);
//                    path.lineTo(options.barWidth - options.barRadius , 0);
//                    rectF.set(options.barWidth - options.barRadius  - options.barRadius,0,options.barWidth , options.barRadius + options.barRadius);
//                    path.arcTo(rectF,270,90);
//                    path.lineTo(options.barWidth,h);
//                    path.close();
//                }else{
//                    //没有圆角直接添加矩形
//                    rectF.set(0,0,options.barWidth,h);
//                    path.addRect(rectF, Path.Direction.CCW);
//                    path.close();
//                }

            }else if(options.direction == GradientDrawable.Orientation.BOTTOM_TOP){
                /*自下而上*/
                int h = (int) (((100 - options.process) * 1.0f )/ 100 * height);

                //没有圆角直接添加矩形
                rectf.set(0,h,options.barWidth,height);
                path.addRect(rectf, Path.Direction.CCW);
                path.close();

//                if(height - h < options.barRadius || height - h > height - options.barRadius){
//                    return path ;
//                }
//
//                if(options.barRadius != 0){
//                    //有圆角
//                    path.moveTo(0,h);
//                    path.lineTo(0,height - options.barRadius );
//                    rectF.set(0,height - options.barRadius - options.barRadius,options.barRadius + options.barRadius, height);
//                    path.arcTo(rectF,180,-90);
//                    path.lineTo(options.barWidth - options.barRadius , height);
//                    rectF.set(options.barWidth - options.barRadius - options.barRadius,height - options.barRadius - options.barRadius,options.barWidth , height);
//                    path.arcTo(rectF,90,-90);
//                    path.lineTo(options.barWidth , h);
//                    path.close();
//                }else{
//                    //没有圆角直接添加矩形
//                    rectF.set(0,h,options.barWidth,height);
//                    path.addRect(rectF, Path.Direction.CCW);
//                    path.close();
//                }
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
