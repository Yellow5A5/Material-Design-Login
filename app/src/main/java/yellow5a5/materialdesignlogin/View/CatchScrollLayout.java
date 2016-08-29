package yellow5a5.materialdesignlogin.View;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import yellow5a5.materialdesignlogin.IView.ICatchScroll;
import yellow5a5.materialdesignlogin.R;

/**
 * Created by Weiwu on 16/8/25.
 * <p/>
 * CatchScrollLayout负责监听外部的滑动事件。
 */
public class CatchScrollLayout extends LinearLayout implements ICatchScroll {

    private UnderlineDevider mUnderlineDevider;
    private LinearLayout mTitleLayout;

    public CatchScrollLayout(Context context) {
        this(context, null);
    }

    public CatchScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CatchScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        initTitle(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.title_scroll_layout, this, true);
        mUnderlineDevider = (UnderlineDevider) findViewById(R.id.underline_v);
        mUnderlineDevider.setDevider(2);
    }

    @Override
    public void initTitle(Context context) {
        mTitleLayout = new LinearLayout(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTitleLayout.setLayoutParams(params);
        mTitleLayout.setOrientation(HORIZONTAL);
        mTitleLayout.setGravity(Gravity.CENTER);
    }

    @Override
    public void addTitleItem(String... items) {
        for (String item : items) {
            TextView tv = new TextView(getContext());
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            tv.setLayoutParams(params);
            tv.setTextColor(Color.parseColor("#ffffff"));
            tv.setBackgroundColor(Color.parseColor("#00000000"));
            tv.setGravity(Gravity.CENTER);
            mTitleLayout.addView(tv, 0);
        }
    }

    public void setTitleHeight(int dipHeight) {
        if (mTitleLayout != null) {
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (getResources().getDisplayMetrics().density * dipHeight));
            mTitleLayout.setLayoutParams(params);
        }
    }

    @Override
    public void scrollLeft() {

    }

    @Override
    public void scrollRight() {

    }

    private IScrollCallBack mIScrollCallBack;

    public interface IScrollCallBack{
        void onScrollProcess(int process, boolean isLeft);
    };

    public void setIScrollCallBack(IScrollCallBack l){
        mIScrollCallBack = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if(mIScrollCallBack != null){
                int MAX_DISPLACEMENT = 500;
                int nowDisplacement = x - downX;
                if (nowDisplacement > MAX_DISPLACEMENT)
                    nowDisplacement = MAX_DISPLACEMENT;
                mUnderlineDevider.move(nowDisplacement, 0);
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP){
            mUnderlineDevider.fitPosition();
        }
        return super.onTouchEvent(event);
    }

    int downX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            downX = (int) event.getX();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (Math.abs(downX - event.getX()) > 50) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(event);
    }

}
