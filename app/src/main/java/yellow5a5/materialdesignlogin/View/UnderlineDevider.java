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
    private static final int ELASTIC_FACTOR = 10;

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private int mDevederWidth;

    private int mCount;
    private ArrayList<Point> mTopPosition;
    private ArrayList<Point> mBottomPosition;

    private Point mCurrentPoint;
    private Point mLastPoint;

    private boolean isDrawFlag = false;
    private boolean isFinishCount = false;

    interface IMoveCallBack{
        void onMove(int process);
    };

    private IMoveCallBack mIMoveCallBack;

    public void setIMoveCallBack(IMoveCallBack l){
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

    public void move(int x, int y) {
        //TODO 这里回调一个process给外面的Layout～
        if(mIMoveCallBack != null){
            mIMoveCallBack.onMove();
        }

        mCurrentPoint.x = mLastPoint.x + x;
        invalidate();
    }

    private ValueAnimator mMoveAnimator;

    /**
     * 根据当前位置找合适位置移动
     */
    public void fitPosition() {
        int tabPosition = mCurrentPoint.x / mDevederWidth;
        int correctPosition = tabPosition;
        tabPosition = tabPosition > mCount ? mCount : tabPosition;
        if (mCurrentPoint.x % mDevederWidth > mDevederWidth / 2) {
            correctPosition = tabPosition + 1;
        }
        if (correctPosition > mCount - 1) {
            correctPosition = mCount - 1;
        }
        if (correctPosition < 0) {
            correctPosition = 0;
        }
        mMoveAnimator = ValueAnimator.ofFloat(mCurrentPoint.x, mDevederWidth * correctPosition)
                .setDuration(300);
        mMoveAnimator.setInterpolator(new OvershootInterpolator());
        mMoveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float factor = (float) animation.getAnimatedValue();
                mCurrentPoint.x = (int) factor;
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
            result = defaultSize;   //UNSPECIFIED
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
}
