package yellow5a5.materialdesignlogin.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import yellow5a5.materialdesignlogin.R;

/**
 * Created by Weiwu on 16/8/25.
 * <p/>
 * CatchScrollLayout
 * It is responsible for monitoring external sliding events.
 */
public class CatchScrollLayout extends LinearLayout{

    private UnderlineDevider mUnderlineDevider;


    private IScrollCallBack mIScrollCallBack;

    public interface IScrollCallBack {
        void onScrollProcess(int process, boolean isLeft);
    }

    public void setIScrollCallBack(IScrollCallBack l) {
        mIScrollCallBack = l;
    }

    public CatchScrollLayout(Context context) {
        this(context, null);
    }

    public CatchScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CatchScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.title_scroll_layout, this, true);
        mUnderlineDevider = (UnderlineDevider) findViewById(R.id.underline_v);
        mUnderlineDevider.setIMoveCallBack(new UnderlineDevider.IMoveCallBack() {
            @Override
            public void onMove(int process, boolean isLeft) {
                if (mIScrollCallBack != null) {
                    mIScrollCallBack.onScrollProcess(process, isLeft);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int x = (int) event.getX();
        if (mUnderlineDevider.isProcessFinish()) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mIScrollCallBack != null) {
                    int nowDisplacement = x - downX;
                    mUnderlineDevider.move(nowDisplacement);
                }
                break;
            case MotionEvent.ACTION_UP:
                mUnderlineDevider.updateAnimFinally();
                break;
            default:
                break;
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
            if (Math.abs(downX - event.getX()) > 20) {
                return true;
            }
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    public int getCurrentItem() {
        return mUnderlineDevider.getCurrentTabNum();
    }

    public int getItemCount() {
        return mUnderlineDevider.getDevideItemCount();
    }
}
