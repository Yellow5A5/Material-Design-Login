package yellow5a5.materialdesignlogin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import yellow5a5.materialdesignlogin.View.AnimCardView;
import yellow5a5.materialdesignlogin.View.CatchScrollLayout;
import yellow5a5.materialdesignlogin.IView.IAnimCard;

public class LoginActivity extends AppCompatActivity {

    private Button mStartBtn;
    private Button mCloseBtn;
    private List<IAnimCard> mAnimList = new ArrayList<>();
    private CatchScrollLayout mCatchScrollLayout;

    private int mHeight;
    private int mWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initParameter();
        addSolidCardView(3);

    }

    private void initView() {
        mStartBtn = (Button) findViewById(R.id.begin_btn);
        mCloseBtn = (Button) findViewById(R.id.close_btn);
        mCatchScrollLayout = (CatchScrollLayout) findViewById(R.id.catch_sroll_layout);

        mCatchScrollLayout.setIScrollCallBack(new CatchScrollLayout.IScrollCallBack() {
            @Override
            public void onScrollProcess(int process, boolean isLeft) {
                for (IAnimCard view : mAnimList){
                    view.setAnimCurrentTime(process,isLeft);
//                    Log.d(LoginActivity.class.getName(), "onScrollProcess: " + process);
                }
            }
        });

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (IAnimCard view : mAnimList){
//                    view.setAnimCurrentTime();
                }
            }
        });
    }

    public void initParameter() {
        mHeight = getResources().getDisplayMetrics().heightPixels;
        mWidth = getResources().getDisplayMetrics().widthPixels;
    }

    public void addSolidCardView(int num) {
        for (int i = 0; i < num; i++) {
            View itemView = LayoutInflater.from(this).inflate(R.layout.card_view_item, mCatchScrollLayout, false);
//            itemView.setLayoutParams(new ViewGroup.LayoutParams((int) (mWidth - getResources().getDisplayMetrics().density * 10), (int) (getResources().getDisplayMetrics().density * 70)));
            AnimCardView cardView = (AnimCardView) itemView.findViewById(R.id.anim_cardview);
            EditText tv = (EditText) itemView.findViewById(R.id.input_edit_v);
            tv.setText("Just Test " + i);
//            itemView.setCardElevation(20);
//            cardView.setRadius(10);
            mCatchScrollLayout.addView(itemView);
            mAnimList.add(cardView);
        }
    }


}
