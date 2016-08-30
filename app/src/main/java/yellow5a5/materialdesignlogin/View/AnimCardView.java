package yellow5a5.materialdesignlogin.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;

import yellow5a5.materialdesignlogin.IView.IAnimCard;

/**
 * Created by Weiwu on 16/8/25.
 */
public class AnimCardView extends CardView implements IAnimCard {

    private int ANIM_DURATION_TIME = 6000;

    private ValueAnimator mScaleAnim;
    private ValueAnimator mAlphaAnim;

    public AnimCardView(Context context) {
        this(context, null);
    }

    public AnimCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAnim();
    }

    public void initAnim() {
        mAlphaAnim = ValueAnimator.ofFloat(1.0f, 0f);
        mAlphaAnim.setDuration(ANIM_DURATION_TIME);
        mAlphaAnim.setInterpolator(new LinearInterpolator());
        mAlphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float factor = (float) animation.getAnimatedValue();
                AnimCardView.this.setAlpha(factor);
            }
        });

        mScaleAnim = ValueAnimator.ofFloat(1.0f, 0.3f);
        mScaleAnim.setDuration(ANIM_DURATION_TIME);
        mScaleAnim.setInterpolator(new AnticipateOvershootInterpolator());
        mScaleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float factor = (float) animation.getAnimatedValue();
                AnimCardView.this.setScaleX(factor);
                AnimCardView.this.setScaleY(factor);
            }
        });
    }

    @Override
    public void setAnimCurrentTime(int process, boolean isLeft) {
        if(isLeft){
            process = 100 - process;
        }
        float currentTime = process / 100.0f;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mScaleAnim.setCurrentFraction(currentTime);
            mAlphaAnim.setCurrentFraction(currentTime);
            Log.d(AnimCardView.class.getName(), "setAnimCurrentTime: currentTime: " + process);
        } else {
            mScaleAnim.setCurrentPlayTime((long) (currentTime * ANIM_DURATION_TIME));
            mAlphaAnim.setCurrentPlayTime((long) (currentTime * ANIM_DURATION_TIME));
        }

    }
}
