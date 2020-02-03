package com.cxd.processbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.view.View;

/*
 * Create by chenxiaodong on 2020/1/27 0027 0:01
 */
public class DefaultDragView extends View {
    private int w ;
    public DefaultDragView(Context context) {
        super(context);


        setWillNotDraw(false);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        w = getMeasuredWidth() ;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#70FFFFFF"));
        paint.setShadowLayer(5,0,0, Color.parseColor("#30111111"));

        canvas.drawCircle(w/2,w/2,w/2,paint);
    }
}
