package com.feng.diningphilosophers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mFinishTimeEt;
    private Button mStartBtn;
    private Button mFinishBtn;
    private TextView mTimeTv;
    private TextView mP1Tv;
    private TextView mP2Tv;
    private TextView mP3Tv;
    private TextView mP4Tv;
    private TextView mP5Tv;

    private Philosopher mP1;
    private Philosopher mP2;
    private Philosopher mP3;
    private Philosopher mP4;
    private Philosopher mP5;

    private boolean mIsStart = false;
    private int mFinishTime;
    private int mCurrTime;

    @SuppressLint("HandlerLeak")
    private Handler mTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==100){
                mTimeTv.setText(String.valueOf(mCurrTime));
                if (mCurrTime == mFinishTime) {
                    finishTask();
                    return;
                }
                mCurrTime++;
                if (mIsStart) {
                    mTimeHandler.sendEmptyMessageDelayed(100,1000);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initThread() {
        Chopstick chopstick = new Chopstick();
        mP1 = new Philosopher(0, 0, 1,
                chopstick, new Philosopher.PhilosopherListener() {
            @Override
            public void stateChanged(int id, final String state) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (!mIsStart) {
                            return;
                        }
                        mP1Tv.setText(state);
                    }
                });
            }
        });
        mP2 = new Philosopher(1, 1, 2,
                chopstick, new Philosopher.PhilosopherListener() {
            @Override
            public void stateChanged(int id, final String state) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (!mIsStart) {
                            return;
                        }
                        mP2Tv.setText(state);
                    }
                });
            }
        });
        mP3 = new Philosopher(2, 2, 3,
                chopstick, new Philosopher.PhilosopherListener() {
            @Override
            public void stateChanged(int id, final String state) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (!mIsStart) {
                            return;
                        }
                        mP3Tv.setText(state);
                    }
                });
            }
        });
        mP4 = new Philosopher(3, 3, 4,
                chopstick, new Philosopher.PhilosopherListener() {
            @Override
            public void stateChanged(int id, final String state) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (!mIsStart) {
                            return;
                        }
                        mP4Tv.setText(state);
                    }
                });
            }
        });
        mP5 = new Philosopher(4, 4, 0,
                chopstick, new Philosopher.PhilosopherListener() {
            @Override
            public void stateChanged(int id, final String state) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (!mIsStart) {
                            return;
                        }
                        mP5Tv.setText(state);
                    }
                });
            }
        });
    }

    private void startThread() {
        mP1.start();
        mP2.start();
        mP3.start();
        mP4.start();
        mP5.start();
    }

    private void stopThread() {
        mP1.setFinish(true);
        mP2.setFinish(true);
        mP3.setFinish(true);
        mP4.setFinish(true);
        mP5.setFinish(true);
    }

    private void initView() {
        mFinishTimeEt = findViewById(R.id.et_main_finish_time);

        mStartBtn = findViewById(R.id.btn_main_start);
        mStartBtn.setOnClickListener(this);
        mFinishBtn = findViewById(R.id.btn_main_finish);
        mFinishBtn.setOnClickListener(this);

        mTimeTv = findViewById(R.id.tv_main_curr_time);
        mP1Tv = findViewById(R.id.tv_main_p_1);
        mP2Tv = findViewById(R.id.tv_main_p_2);
        mP3Tv = findViewById(R.id.tv_main_p_3);
        mP4Tv = findViewById(R.id.tv_main_p_4);
        mP5Tv = findViewById(R.id.tv_main_p_5);
    }

    private void startTask() {
        initThread();
        mIsStart = true;
        mFinishTime = Integer.parseInt(mFinishTimeEt.getText().toString());
        mTimeHandler.sendEmptyMessage(100);
        startThread();
    }

    private void finishTask() {
        mIsStart = false;
        stopThread();
        mTimeTv.setText("运行已结束，请重新开始");
        mP1Tv.setText("");
        mP2Tv.setText("");
        mP3Tv.setText("");
        mP4Tv.setText("");
        mP5Tv.setText("");
        mCurrTime = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_start:
                if (!checkInput()) {
                    break;
                }
                if (!mIsStart) {
                    hideSoftInput(MainActivity.this);
                    startTask();
                }
                break;
            case R.id.btn_main_finish:
                if (mIsStart) {
                    finishTask();
                }
                break;
            default:
                break;
        }
    }

    private boolean checkInput() {
        if (mFinishTimeEt.getText().toString().equals("")) {
            Toast.makeText(MainActivity.this, "请先输入结束时间",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
