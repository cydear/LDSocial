package com.ldsocial.app.ldlogin.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.ldsocial.app.ldlogin.R;
import com.ldsocial.app.ldlogin.utils.DisplayUtil;

/**
 * @ClassName AudioCircleProgressView
 * @Description: 录音进度条
 * @Author: Lary.huang
 * @CreateDate: 2020/8/19 3:55 PM
 * @Version: 1.0
 */
public class AudioCircleProgressView extends View {
    private Context context;
    /**
     * 绘制周边阴影
     */
    private Paint mShaderPaint;
    /**
     * 背景画笔
     */
    private Paint mBackgroundPaint;
    /**
     * 绘制底层圆环的画笔
     */
    private Paint mCirclePaint;
    /**
     * 进度画笔
     */
    private Paint mProgressPaint;
    /**
     * 图片绘制
     */
    private Paint mBitmapPaint;
    /**
     * 绘制区域
     */
    private RectF mRectF;
    /**
     * 阴影绘制区域
     */
    private RectF mShaderRectF;
    /**
     * 音筒
     */
    private Rect mAudioRect;
    /**
     * 进度
     */
    private float mProgress;
    /**
     * 圆环渐变色
     */
    private int[] mColorArray;
    /**
     * 进度条的宽度
     */
    private static int mStrokeWidth = 2;
    /**
     * 阴影区域宽度
     */
    private static int mShaderStrokeWidth;
    /**
     * 进度条颜色
     */
    private static int mProgressColor = R.color.color_ff565cdd;
    /**
     * 底层圆环的颜色
     */
    private static int mStrokeColor = R.color.color_ffffffff;
    /**
     * 背景颜色
     */
    private static int mBackgroundColor = R.color.color_ffe7e8fa;
    /**
     * 阴影颜色
     */
    private static int mShaderStrokeColor = R.color.color_fff4f5ff;
    /**
     * 阴影区域颜色渐变
     */
    private static int mShaderStrokeEndColor = R.color.color_fffafaff;
    /**
     * 音筒
     */
    private static Bitmap mAudioBitmap = null;
    /**
     * 总进度
     */
    private static int mTotalProgress = 60;

    public AudioCircleProgressView(Context context) {
        this(context, null);
    }

    public AudioCircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AudioCircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(context);
        initShaderPaint(context);
        initBackgroundPaint(context);
        initCirclePaint(context);
        initProgressPaint(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        //Bitmap画笔
        mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShaderStrokeWidth = DisplayUtil.dp2px(context, 2);
        mColorArray = new int[]{context.getResources().getColor(mShaderStrokeColor),
                context.getResources().getColor(mShaderStrokeEndColor)};
        mAudioBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_audio_record);
    }

    /**
     * 绘制周边阴影
     *
     * @param context
     */
    private void initShaderPaint(Context context) {
        if (mShaderPaint == null) {
            mShaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mShaderPaint.setStyle(Paint.Style.STROKE);
            //设置圆角
            mShaderPaint.setStrokeCap(Paint.Cap.ROUND);
            //设置抖动
            mShaderPaint.setDither(true);
            //设置圆形进度条宽度
            mShaderPaint.setStrokeWidth(DisplayUtil.dp2px(context, mShaderStrokeWidth));
            //设置进度条颜色
            mShaderPaint.setColor(context.getResources().getColor(mShaderStrokeColor));
        }
    }

    /**
     * 初始化背景画笔
     *
     * @param context
     */
    private void initBackgroundPaint(Context context) {
        if (mBackgroundPaint == null) {
            mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBackgroundPaint.setStyle(Paint.Style.FILL);
            //设置圆角
            mBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);
            //设置抖动
            mBackgroundPaint.setDither(true);
            //设置进度条颜色
            mBackgroundPaint.setColor(context.getResources().getColor(mBackgroundColor));
        }
    }

    /**
     * 底层圆环
     *
     * @param context
     */
    private void initCirclePaint(Context context) {
        if (mCirclePaint == null) {
            mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCirclePaint.setStyle(Paint.Style.STROKE);
            //设置圆角
            mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
            //设置抖动
            mCirclePaint.setDither(true);
            //设置圆形进度条宽度
            mCirclePaint.setStrokeWidth(DisplayUtil.dp2px(context, mStrokeWidth));
            //设置进度条颜色
            mCirclePaint.setColor(context.getResources().getColor(mStrokeColor));
        }
    }

    /**
     * 初始化进度条画笔
     *
     * @param context
     */
    private void initProgressPaint(Context context) {
        if (mProgressPaint == null) {
            mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mProgressPaint.setStyle(Paint.Style.STROKE);
            //设置圆角
            mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
            //设置抖动
            mProgressPaint.setDither(true);
            //设置圆形进度条宽度
            mProgressPaint.setStrokeWidth(DisplayUtil.dp2px(context, mStrokeWidth));
            //设置进度条颜色
            mProgressPaint.setColor(context.getResources().getColor(mProgressColor));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int viewWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int viewHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int rectWidth = (int) (Math.min(viewWidth, viewHeight) - Math.max(mShaderPaint.getStrokeWidth(), mShaderPaint.getStrokeWidth()));
        int left = getPaddingLeft() + (viewWidth - rectWidth) / 2;
        int top = getPaddingTop() + (viewHeight - rectWidth) / 2;
        mShaderRectF = new RectF(left, top, left + rectWidth, top + rectWidth);
        mRectF = new RectF(left + mShaderStrokeWidth, top + mShaderStrokeWidth, left + rectWidth - mShaderStrokeWidth, top + rectWidth - mShaderStrokeWidth);

        if (mColorArray != null && mColorArray.length > 0) {
            mShaderPaint.setShader(new LinearGradient(0, 0, 0, getMeasuredWidth(), mColorArray, null, Shader.TileMode.MIRROR));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mShaderRectF, 0, 360, false, mShaderPaint);
        //绘制背景
        canvas.drawArc(mRectF, 0, 360, false, mBackgroundPaint);
        //绘制圆环
        canvas.drawArc(mRectF, 0, 360, false, mCirclePaint);

        //绘制进度
        canvas.drawArc(mRectF, 275, 360 * mProgress / mTotalProgress, false, mProgressPaint);

        canvas.drawBitmap(mAudioBitmap, null, mAudioRect, mBitmapPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mAudioRect = getAudioRect();
    }

    public void reset() {
        setProgress(0, 0);
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();
    }

    public void setTotalProgress(int totalProgress) {
        this.mTotalProgress = totalProgress;
    }

    /**
     * 设置进度
     *
     * @param progress
     * @param animTime
     */
    public void setProgress(int progress, long animTime) {
        if (animTime <= 0) {
            setProgress(progress);
        } else {
            ValueAnimator animator = ValueAnimator.ofFloat(mProgress, progress);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mProgress = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(animTime);
            animator.start();
        }
    }

    /**
     * 音筒Rect
     *
     * @return
     */
    public Rect getAudioRect() {
        //int bitmapWidth = DisplayUtil.dp2px(context, mAudioBitmap.getWidth());
        //int bitmapHeight = DisplayUtil.dp2px(context, mAudioBitmap.getHeight());
        int bitmapWidth = mAudioBitmap.getWidth();
        int bitmapHeight = mAudioBitmap.getHeight();
        int left = getWidth() / 2 - bitmapWidth / 2;
        int top = getHeight() / 2 - bitmapHeight / 2;
        int right = getWidth() / 2 + bitmapWidth / 2;
        int bottom = getHeight() / 2 + bitmapHeight / 2;
        Rect rect = new Rect(left, top, right, bottom);
        return rect;
    }
}
