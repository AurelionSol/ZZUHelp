package com.teacher.zzuhelper.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teacher.zzuhelper.R;

/**
 * Created by Administrator on 2016/9/12.
 */
public class MyImageButton extends LinearLayout implements View.OnTouchListener {
    public ImageView imageView;
    public TextView textView;
    private Paint paint;
    private MotionEvent motionEvent;
    private static final int INVALIDATE_DURATION = 20; //每次刷新的时间间隔
    private static int DIFFUSE_GAP = 20;                  //扩散半径增量

    private float maxRadio = 0;//最大半径
    private float radio = 0;//绘制半径
    private float pointX = 0;//被点击的坐标点x
    private float pointY = 0;//被点击的坐标点y
    private int viewHeight;//View高度
    private int viewWidth;//View宽度

    private boolean actionUpFlag = false;//actionUp标志位，标识是否可以相应actionUp事件
    private boolean actionDownFlag = false;//手指依然点击本view的标志位
    private boolean actionCancelFlag = false;//点击取消标志位


    public MyImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyImageButton);
        /*
        * 在attrs.xml添加属性：
        *   <declare-styleable name="ImageButtonWithText">
             <attr name="picture" format="reference"/>
            </declare-styleable>
        * */
        int picture_id = a.getResourceId(R.styleable.MyImageButton_picture, -1);
        /**
         * Recycle the TypedArray, to be re-used by a later caller. After calling
         * this function you must not ever touch the typed array again.
         */
        a.recycle();
        imageView = new ImageView(context, attrs);
        imageView.setPadding(0, 0, 0,0);
        /**
         * Sets a drawable as the content of this ImageView.
         * This does Bitmap reading and decoding on the UI
         * thread, which can cause a latency hiccup.  If that's a concern,
         * consider using setImageDrawable(android.graphics.drawable.Drawable) or
         * setImageBitmap(android.graphics.Bitmap) instead.
         * 直接在UI线程读取和解码Bitmap，可能会存在潜在的性能问题
         * 可以考虑使用 setImageDrawable(android.graphics.drawable.Drawable)
         * 或者setImageBitmap(android.graphics.Bitmap) 代替
         */
        imageView.setImageResource(picture_id);
        textView = new TextView(context, attrs);
        /**
         * Sets the horizontal alignment of the text and the
         * vertical gravity that will be used when there is extra space
         * in the TextView beyond what is required for the text itself.
         */
        //水平居中
        textView.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
        textView.setPadding(0, 0, 0, 0);
        setClickable(true);
        setFocusable(true);
        setOrientation(LinearLayout.VERTICAL);
        addView(imageView);
        addView(textView);
    }

    public void setText(int resId) {
        textView.setText(resId);
    }

    public void setText(CharSequence buttonText) {
        textView.setText(buttonText);
    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public void initPaint() {
        paint = new Paint();
        paint.setColor(Color.argb(56,0,188,212));
        paint.setAntiAlias(true);
    }

    /**
     * 设置水波纹画笔
     * setColor控制颜色
     * setAlpha控制透明度
     * **/
    public void setPaint(Paint p){
        paint = p;
    }

    //布局发生变化时回调
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.viewWidth = w;
        this.viewHeight = h;
    }

    //计算波纹最大半径
    private void countMaxRadio() {
        if (viewWidth >= viewHeight) {
            if (pointX <= viewWidth / 2) {
                maxRadio = viewWidth - pointX;
            } else {
                maxRadio = pointX;
            }
        } else {
            if (pointY <= viewHeight / 2) {
                maxRadio = viewHeight - pointY;
            } else {
                maxRadio = pointY;
            }
        }
        maxRadio = maxRadio + 20;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointX = event.getX();
                pointY = event.getY();
                actionDownFlag = true;
                countMaxRadio();
                postInvalidateDelayed(INVALIDATE_DURATION);
                break;
            case MotionEvent.ACTION_UP:
                //flag用做判断是否适合触发点击事件，false则不适合，true则适合
                if (!actionUpFlag && !actionCancelFlag) {
                    actionUpFlag = true;
                    actionCancelFlag = false;
                    motionEvent = event;
                    return true;
                }
                actionUpFlag = false;
                clearData();
                postInvalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                actionCancelFlag = true;
                clearData();
                postInvalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (!actionDownFlag) {
            return;
        }

        canvas.save();
        canvas.clipRect(0, 0, viewWidth, viewHeight);
        canvas.drawCircle(pointX, pointY, radio, paint);
        canvas.restore();

        if (radio <= maxRadio) {
            //播放动画
            radio = radio + DIFFUSE_GAP;
            postInvalidateDelayed(INVALIDATE_DURATION);
        } else {
            //动画播放完成
            if (actionUpFlag) {
                //如果为true，则说明此时actionUp已经被触发过，则重新触发
                if(motionEvent!=null){
                    MyImageButton.this.dispatchTouchEvent(motionEvent);
                }

            } else {
                //为false则说明actionUp未被触发，手指仍保留在点击状态，设flag为true，当actionUp被触发时，直接清空动画，响应点击事件
                actionUpFlag = true;
            }
//            clearData();
        }
    }

    private void clearData() {
        actionDownFlag = false;
        maxRadio = 0;//最大半径
        radio = 0;//绘制半径
        pointX = 0;//被点击的坐标点x
        pointY = 0;//被点击的坐标点y
    }



}
