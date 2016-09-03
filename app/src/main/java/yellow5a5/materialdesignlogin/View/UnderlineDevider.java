package yellow5a5.materialdesignlogin.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;

import yellow5a5.materialdesignlogin.R;

/**
 * Created by Weiwu on 16/8/27.
 */
public class UnderlineDevider extends View {

    private static final int HEIGHT_DEFAULT = 1;
    private static final int PROCESS_FINISH = 100;
    private static final int ANIM_DURATION = 400;
    /* Leave some space on both sides*/
    private static final int ELASTIC_FACTOR = 30;

    private Paint mPaint;
    private boolean isLeft;
    private int mWidth;
    private int mHeight;
    private int mDevederWidth;

    private int mDevideCount;
    private int mDevideColor;
    private ArrayList<Point> mTopPosition;
    private ArrayList<Point> mBottomPosition;

    private int mCurrentTabNum;
    private Point mCurrentPoint;
    private Point mLastPoint;

    private ValueAnimator mMoveAnimator;

    private boolean isDrawFlag = false;
    private boolean isFinishCount = false;
    private boolean isProcessFinish = false;

    interface IMoveCallBack {
        void onMove(int process, boolean isLeft);
    }

    private IMoveCallBack mIMoveCallBack;

    public void setIMoveCallBack(IMoveCallBack l) {
        mIMoveCallBack = l;
    }

    public UnderlineDevider(Context context) {
        this(context, null);
    }

    public UnderlineDevider(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnderlineDevider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.UnderlineDevider, 0, 0);
        mDevideCount = typeArray.getInteger(R.styleable.UnderlineDevider_devide_count, 1);
        mDevideColor = typeArray.getColor(R.styleable.UnderlineDevider_devide_color, Color.parseColor("#ffffffff"));
        init();
    }

    private void init() {
        mCurrentPoint = new Point();
        mLastPoint = new Point();
        mTopPosition = new ArrayList<>();
        mBottomPosition = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setColor(mDevideColor);
        mPaint.setStyle(Paint.Style.FILL);
        setDevider(mDevideCount);
    }

    /**
     * Set the number of Segments
     * @param count
     */
    public void setDevider(int count) {
        mDevideCount = count;
        isFinishCount = false;
        mTopPosition.clear();
        mBottomPosition.clear();
        invalidate();
    }


    public void move(int x) {
        if(isOutOfBorder(x))
            return;
        if (mIMoveCallBack != null && Math.abs(x) > 1) {
            int process = Math.abs(x) > mDevederWidth ? PROCESS_FINISH : (int) ((float) (x) / mDevederWidth * PROCESS_FINISH);
            isLeft = x < 0;
            mIMoveCallBack.onMove(Math.abs(process), isLeft);
        }
        mCurrentPoint.x = Math.abs(x) > mDevederWidth + ELASTIC_FACTOR ? mLastPoint.x + (int) (x / Math.abs(x)) * (mDevederWidth + ELASTIC_FACTOR) : mLastPoint.x + x;
        invalidate();
    }

    /**
     * To find the right position according to the current position
     */
    public void updateAnimFinally() {
        fixTheCorrectNum();
        if (mCurrentPoint.x == mDevederWidth * mCurrentTabNum)
            return;
        mMoveAnimator = ValueAnimator.ofFloat(mCurrentPoint.x, mDevederWidth * mCurrentTabNum)
                .setDuration(ANIM_DURATION);
        mMoveAnimator.setInterpolator(new OvershootInterpolator());
        mMoveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float factor = (float) animation.getAnimatedValue();
                mCurrentPoint.x = (int) factor;
                if (mIMoveCallBack != null && !isProcessFinish) {
                    int process = (mCurrentPoint.x - mLastPoint.x) * PROCESS_FINISH / mDevederWidth;
                    if (Math.abs(process) > PROCESS_FINISH) {
                        process = PROCESS_FINISH;
                        isProcessFinish = true;
                    }
                    mIMoveCallBack.onMove(Math.abs(process), isLeft);
                }
                invalidate();
            }
        });
        mMoveAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLastPoint.set(mCurrentPoint.x, mCurrentPoint.y);
                isProcessFinish = false;
            }
        });
        mMoveAnimator.start();
    }

    /**
     * Fix Current-tab's number.
     */
    private void fixTheCorrectNum() {
        int tabPosition = mCurrentPoint.x / mDevederWidth;
        mCurrentTabNum = tabPosition;
        tabPosition = tabPosition > mDevideCount ? mDevideCount : tabPosition;
        if (mCurrentPoint.x % mDevederWidth > mDevederWidth / 2) {
            mCurrentTabNum = tabPosition + 1;
        }
        if (mCurrentTabNum > mDevideCount - 1) {
            mCurrentTabNum = mDevideCount - 1;
        }
        if (mCurrentTabNum < 0) {
            mCurrentTabNum = 0;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDevideCount == 0) {
            return;
        }
        if (!isDrawFlag) {
            mWidth = getWidth();
            mHeight = getHeight();
            isDrawFlag = true;
            mDevederWidth = mWidth / mDevideCount;
        }
        if (!isFinishCount) {
            int left_top_x = (int) getX();
            int left_top_y = (int) getY();
            int left_bottom_x = (int) getX();
            int left_bottom_y = (int) getY() + mHeight;

            for (int i = 0; i < mDevideCount; i++) {
                mTopPosition.add(new Point(left_top_x + mDevideCount * mDevederWidth, left_top_y));
                mBottomPosition.add(new Point(left_bottom_x + mDevideCount * mDevederWidth, left_bottom_y));
            }
            isFinishCount = true;
        }
        canvas.drawRect(mCurrentPoint.x + ELASTIC_FACTOR, mCurrentPoint.y, mCurrentPoint.x + mDevederWidth - ELASTIC_FACTOR, mCurrentPoint.y + mHeight, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureDimension(getResources().getDisplayMetrics().widthPixels, widthMeasureSpec);
        int height = measureDimension(HEIGHT_DEFAULT, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    public int measureDimension(int defaultSize, int measureSpec) {
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * if moving out of border, return false.
     * @param x
     * @return true or false
     */
    public boolean isOutOfBorder(int x) {
        return (mCurrentTabNum == 0 && x < 0)
                || (mCurrentTabNum == mDevideCount - 1 && x > 0);
    }

    public boolean isProcessFinish() {
        return isProcessFinish;
    }

    public int getCurrentTabNum() {
        return mCurrentTabNum;
    }

    public int getDevideItemCount() {
        return mDevideCount;
    }


}
