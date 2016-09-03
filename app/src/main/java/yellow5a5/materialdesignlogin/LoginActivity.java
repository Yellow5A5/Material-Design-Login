package yellow5a5.materialdesignlogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import yellow5a5.materialdesignlogin.View.CatchScrollLayout;
import yellow5a5.materialdesignlogin.View.SignUpContainer;

public class LoginActivity extends AppCompatActivity {

    private TextView mProcessShowTv;
    private CatchScrollLayout mCatchScrollLayout;
    private SignUpContainer mSignUpContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mProcessShowTv = (TextView) findViewById(R.id.process_show);
        mCatchScrollLayout = (CatchScrollLayout) findViewById(R.id.catch_sroll_layout);
        mSignUpContainer = (SignUpContainer) findViewById(R.id.sign_up_container);

        mCatchScrollLayout.setIScrollCallBack(new CatchScrollLayout.IScrollCallBack() {
            @Override
            public void onScrollProcess(int process, boolean isLeft) {
                if (!isLeft){
                    process = 100 - process;
                }
                mSignUpContainer.setAnimProportion(process);
                mProcessShowTv.setText("Process:" + process + "  isLeft:" + String.valueOf(isLeft));
            }
        });

        mSignUpContainer.setIConfirmCallBack(new SignUpContainer.IConfirmCallBack() {
            @Override
            public void goNext() {
                //TODO
            }
        });



    }
}
