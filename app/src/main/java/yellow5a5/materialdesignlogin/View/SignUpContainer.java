package yellow5a5.materialdesignlogin.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import yellow5a5.materialdesignlogin.R;

/**
 * Created by Weiwu on 16/9/2.
 */
public class SignUpContainer extends RelativeLayout {

    private static final int ANIM_DURATION = 2000;
    private static final int LOGIN_STATE = 1;
    private static final int SIGN_UP_STATE = 2;

    private CardView mFirNameCv;
    private CardView mLastNameCv;
    private CardView mEmailCv;
    private CardView mPassWordCv;
    private CardView mConfirmCv;
    private EditText mFirNameEt;
    private EditText mLastNameEt;
    private EditText mEmailEt;
    private EditText mPassWordEt;
    private TextView mConfirmTv;
    private LinearLayout mLoginContainer;
    private LinearLayout mNameContainer;

    private int mState = SIGN_UP_STATE;
    private int mLoginHeaderHeight;

    private ValueAnimator mContainerAnim;
    private ValueAnimator mFirCvAnim;

    public interface IConfirmCallBack {
        //you can change it to get the parameters what you wanted.
        void goNext();
    }

    private IConfirmCallBack mIConfirmCallBack;

    public void setIConfirmCallBack(IConfirmCallBack l) {
        mIConfirmCallBack = l;
    }

    public SignUpContainer(Context context) {
        this(context, null);
    }

    public SignUpContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignUpContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.sign_up_container, this, true);
        initView();
        initAnim();
    }

    private void initView() {
        mFirNameCv = (CardView) findViewById(R.id.first_name_card_v);
        mLastNameCv = (CardView) findViewById(R.id.last_name_card_v);
        mEmailCv = (CardView) findViewById(R.id.email_card_v);
        mPassWordCv = (CardView) findViewById(R.id.password_card_v);
        mConfirmCv = (CardView) findViewById(R.id.confirm_card_v);
        mFirNameEt = (EditText) findViewById(R.id.first_name_edit_input);
        mLastNameEt = (EditText) findViewById(R.id.last_name_edit_input);
        mEmailEt = (EditText) findViewById(R.id.email_edit_input);
        mPassWordEt = (EditText) findViewById(R.id.password_edit_input);
        mConfirmTv = (TextView) findViewById(R.id.confirm_edit);
        mLoginContainer = (LinearLayout) findViewById(R.id.login_container);
        mNameContainer = (LinearLayout) findViewById(R.id.name_container);

        getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mLoginHeaderHeight = mNameContainer.getHeight();
                        initAnim();
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        mLoginContainer.setTranslationY(mLoginHeaderHeight);
                    }
                });
    }

    private void initAnim() {
        mContainerAnim = ValueAnimator.ofInt(0, (int) mLoginHeaderHeight)
                .setDuration(ANIM_DURATION);
        mContainerAnim.setInterpolator(new OvershootInterpolator());
        mContainerAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int factor = (int) animation.getAnimatedValue();
                mLoginContainer.setTranslationY(factor);
            }
        });

        mFirCvAnim = ValueAnimator.ofFloat(0, 1)
                .setDuration(ANIM_DURATION);
        mFirCvAnim.setInterpolator(new AccelerateInterpolator());
        mFirCvAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float factor = (float) animation.getAnimatedValue();
                mFirNameCv.setAlpha(factor);
                mFirNameCv.setScaleX(factor);
                mFirNameCv.setScaleY(factor);
                mLastNameCv.setAlpha(factor);
                mLastNameCv.setScaleX(factor);
                mLastNameCv.setScaleY(factor);
            }
        });
    }


    public void setAnimProportion(int timeProportion) {
        float fraction = (float) (timeProportion) / 100f;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mContainerAnim.setCurrentFraction(fraction);
            mFirCvAnim.setCurrentFraction(fraction);
        } else {
            mContainerAnim.setCurrentPlayTime((long) (fraction * ANIM_DURATION));
            mFirCvAnim.setCurrentPlayTime((long) (fraction * ANIM_DURATION));
        }
        updataConfirm(timeProportion);
    }

    private void updataConfirm(int process) {
        if (process == 100 && mState != SIGN_UP_STATE) {
            mState = SIGN_UP_STATE;
            mConfirmTv.setText("Sign Up");
        } else if (process == 0 && mState != LOGIN_STATE) {
            mState = LOGIN_STATE;
            mConfirmTv.setText("Login");
        }
    }

    public String getEmailText() {
        return String.valueOf(mEmailEt.getText());
    }

    public String getPassWord() {
        return String.valueOf(mPassWordEt.getText());
    }

    //you can add others~
}
