package yellow5a5.materialdesignlogin.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import java.util.ArrayList;

/**
 * Created by Weiwu on 16/8/27.
 */
public class UnderlineDevider extends View {

    private static final int HEIGHT_DEFAULT = 1;
    private static final int ELASTIC_FACTOR = 30;

    private static final int PROCESS_FINISH = 100;
    private static final int ANIM_TIME = 300;

    private Paint mPaint;
    private boolean isLeft;
    private int mWidth;
    private int mHeight;
    private int mDevederWidth;

    private int mCount;
    private ArrayList<Point> mTopPosition;
    private ArrayList<Point> mBottomPosition;

    private int mCurrentTabNum;
    private Point mCurrentPoint;
    private Point mLastPoint;

    private ValueAnimator mMoveAnimator;

    private boolean isDrawFlag = false;
    private boolean isFinishCount = false;

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
        init();
    }

    private void init() {
        mCurrentPoint = new Point();
        mLastPoint = new Point();
        mTopPosition = new ArrayList<>();
        mBottomPosition = new ArrayList<>();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
    }

    public void setDevider(int count) {
        mCount = count;
        isFinishCount = false;
        mTopPosition.clear();
        mBottomPosition.clear();
        invalidate();
    }

    public void move(int x) {
        if (mIMoveCallBack != null && Math.abs(x) > 1) {
            int process = Math.abs(x) > mDevederWidth ? PROCESS_FINISH : (int) ((float) (x) / mDevederWidth * PROCESS_FINISH);
            isLeft = x < 0;
            mIMoveCallBack.onMove(Math.abs(process), isLeft);
        }
        mCurrentPoint.x = Math.abs(x) > mDevederWidth + ELASTIC_FACTOR ? mLastPoint.x + (int) (x / Math.abs(x)) * (mDevederWidth + ELASTIC_FACTOR) : mLastPoint.x + x;
        fixTheCorrectNum();
        invalidate();
    }

    /**
     * 根据当前位置找合适位置移动
     */
    public void updateAnimFinally() {
        mMoveAnimator = ValueAnimator.ofFloat(mCurrentPoint.x, mDevederWidth * mCurrentTabNum)
                .setDuration(ANIM_TIME);
        mMoveAnimator.setInterpolator(new OvershootInterpolator());
        mMoveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            boolean isProcessFinish = false;
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float factor = (float) animation.getAnimatedValue();
                mCurrentPoint.x = (int) factor;
                if (mIMoveCallBack != null && !isProcessFinish) {
                    int process = (mCurrentPoint.x - mLastPoint.x) * PROCESS_FINISH / mDevederWidth;
                    if(Math.abs(process) > PROCESS_FINISH){
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
            }
        });
        mMoveAnimator.start();
    }

    private void fixTheCorrectNum() {
        int tabPosition = mCurrentPoint.x / mDevederWidth;
        mCurrentTabNum = tabPosition;
        tabPosition = tabPosition > mCount ? mCount : tabPosition;
        if (mCurrentPoint.x % mDevederWidth > mDevederWidth / 2) {
            mCurrentTabNum = tabPosition + 1;
        }
        if (mCurrentTabNum > mCount - 1) {
            mCurrentTabNum = mCount - 1;
        }
        if (mCurrentTabNum < 0) {
            mCurrentTabNum = 0;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCount == 0) {
            return;
        }
        if (!isDrawFlag) {
            mWidth = getWidth();
            mHeight = getHeight();
            isDrawFlag = true;
            mDevederWidth = mWidth / mCount;
        }
        if (!isFinishCount) {
            int left_top_x = (int) getX();
            int left_top_y = (int) getY();
            int left_bottom_x = (int) getX();
            int left_bottom_y = (int) getY() + mHeight;

            for (int i = 0; i < mCount; i++) {
                mTopPosition.add(new Point(left_top_x + mCount * mDevederWidth, left_top_y));
                mBottomPosition.add(new Point(left_bottom_x + mCount * mDevederWidth, left_bottom_y));
            }
            isFinishCount = true;
        }
        canvas.drawRect(mCurrentPoint.x, mCurrentPoint.y, mCurrentPoint.x + mDevederWidth, mCurrentPoint.y + mHeight, mPaint);
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

    public int getCurrentTabNum() {
        return mCurrentTabNum;
    }


}
