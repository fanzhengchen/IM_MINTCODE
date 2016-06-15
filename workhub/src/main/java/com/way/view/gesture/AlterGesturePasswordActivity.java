package com.way.view.gesture;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mintcode.chat.util.BitmapUtil;
import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.pojo.body.HeaderParam;
import com.mintcode.launchr.util.AppUtil;
import com.mintcode.launchr.util.GlideRoundTransform;
import com.mintcode.launchr.util.HeadImageUtil;
import com.way.view.LockPatternUtils;
import com.way.view.LockPatternView;
import com.way.view.LockPatternView.Cell;

import java.io.File;
import java.util.List;

/**
 * @author ChristLu
 */
public class AlterGesturePasswordActivity extends BaseActivity implements
        OnClickListener {
    private LockPatternView mLockPatternView;
    private int mFailedPatternAttemptsSinceLastTimeout = 0;
    private CountDownTimer mCountdownTimer = null;
    private Handler mHandler = new Handler();
    private TextView mHeadTextView;
    private Animation mShakeAnim;
    private TextView mTvGesturePwdUnlockForget;// 忘记手势密码
    private TextView mTvGesturePwdUnlockOtherLogin;// 用其他方式登录

    private Toast mToast;

    private ImageView mIvHeadView;

    /**
     * 用户名
     */
    private TextView mTvUsername;

    /**
     * 返回
     */
    private ImageView mIvBack;

    private void showToast(CharSequence message) {
        if (null == mToast) {
            mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(message);
        }

        mToast.show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gesturepassword_unlock);
        mLockPatternView = (LockPatternView) this
                .findViewById(R.id.gesturepwd_unlock_lockview);
        mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
        mLockPatternView.setTactileFeedbackEnabled(true);
        mHeadTextView = (TextView) findViewById(R.id.gesturepwd_unlock_text);
        mTvUsername = (TextView) findViewById(R.id.tv_login_user_name);
        mIvBack = (ImageView) findViewById(R.id.iv_back);

        mTvGesturePwdUnlockForget = (TextView) findViewById(R.id.gesturepwd_unlock_forget);
        mTvGesturePwdUnlockOtherLogin = (TextView) findViewById(R.id.gesturepwd_unlock_other_login);
        mTvGesturePwdUnlockForget.setOnClickListener(this);
        mTvGesturePwdUnlockOtherLogin.setOnClickListener(this);
        mIvBack.setVisibility(View.VISIBLE);
        mIvBack.setOnClickListener(this);

        mIvHeadView = (ImageView) findViewById(R.id.iv_head_image);

        initData();
        setHeadImage();
    }


    private void setHeadImage() {
        HeaderParam p = LauchrConst.getHeader(this);
        String mUserId = p.getLoginName();
        HeadImageUtil.getInstance(this).restartSetUserAvatarAppendUrl(mIvHeadView, mUserId, 0, 150, 150);

    }

    public void setImageResuces(ImageView iv, File f) {
        Glide.get(this).clearMemory();
        RequestManager man = Glide.with(this);
        GlideRoundTransform g = new GlideRoundTransform(this, 50);
        man.load(f).diskCacheStrategy(DiskCacheStrategy.NONE).transform(g).into(iv);
    }

    private void initData() {
        mHeadTextView.setText("");
        String userName = AppUtil.getInstance(this).getUserName();
        mTvUsername.setText(userName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LockPatternUtils l = new LockPatternUtils(getApplicationContext());
        if (!l.savedPatternExists()) {
            startActivity(new Intent(this, CreateAlterGesturePasswordActivity.class));
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIvBack.getVisibility() == View.VISIBLE) {
                finish();
            } else {
                return moveTaskToBack(true);
            }
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountdownTimer != null)
            mCountdownTimer.cancel();
    }

    private Runnable mClearPatternRunnable = new Runnable() {
        @Override
        public void run() {
            mLockPatternView.clearPattern();
        }
    };

    protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            mLockPatternView.removeCallbacks(mClearPatternRunnable);
            patternInProgress();
        }

        @Override
        public void onPatternCleared() {
            mLockPatternView.removeCallbacks(mClearPatternRunnable);
        }

        @Override
        public void onPatternDetected(List<Cell> pattern) {
            if (pattern == null)
                return;
            LockPatternUtils l = new LockPatternUtils(getApplicationContext());
            if (l.checkPattern(pattern)) {
                mLockPatternView
                        .setDisplayMode(LockPatternView.DisplayMode.Correct);
                setResult(RESULT_OK);
                showToast("解锁成功");
//				}
                Intent intent1 = new Intent(AlterGesturePasswordActivity.this, CreateAlterGesturePasswordActivity.class);
                startActivity(intent1);

                finish();
            } else {
                mLockPatternView
                        .setDisplayMode(LockPatternView.DisplayMode.Wrong);
                if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
                    mFailedPatternAttemptsSinceLastTimeout++;
                    int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT
                            - mFailedPatternAttemptsSinceLastTimeout;
                    if (retry >= 0) {
//						if (retry == 0) showToast("您已5次输错密码，请30秒后再试");
                        mHeadTextView.setText("密码错误，还可以再输入" + retry + "次");
                        mHeadTextView.setTextColor(Color.RED);
//						mHeadTextView.startAnimation(mShakeAnim);
                    }

                } else {
                    showToast("输入长度不够，请重试");
                }

                if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {
                    finish();
                } else {
                    mLockPatternView.postDelayed(mClearPatternRunnable, 1000);
                }
            }
        }

        @Override
        public void onPatternCellAdded(List<Cell> pattern) {

        }

        private void patternInProgress() {
        }
    };
    Runnable attemptLockout = new Runnable() {

        @Override
        public void run() {
            mLockPatternView.clearPattern();
            mLockPatternView.setEnabled(false);
            mCountdownTimer = new CountDownTimer(
                    LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
                    if (secondsRemaining > 0) {
                        mHeadTextView.setText(secondsRemaining + " 秒后重试");
                    } else {
                        mHeadTextView.setText("请绘制手势密码");
                        mHeadTextView.setTextColor(Color.WHITE);
                    }

                }

                @Override
                public void onFinish() {
                    mLockPatternView.setEnabled(true);
                    mFailedPatternAttemptsSinceLastTimeout = 0;
                }
            }.start();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gesturepwd_unlock_forget:
                clearInfo();
            case R.id.gesturepwd_unlock_other_login:
                clearInfo();
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void clearInfo() {
//		String anothrerName = TTConst.getAnotherName(this);
//		if (anothrerName != null && !anothrerName.equals("")) {
//			MessageDBService.getInstance(this, anothrerName).delTable();
//		}
    }

    private void openLargeImage(final ImageView iv, String url, final String path) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        Response.Listener<Bitmap> listener = new Response.Listener<Bitmap>() {

            @Override
            public void onResponse(Bitmap response) {
                boolean success = BitmapUtil.saveBitmap(response, getApplicationContext(), path, 100);
                if (success) {
                    File file = new File(path);
                    setImageResuces(iv, file);
//					iv.setImageBitmap(response);
                } else {
                    toast("error save bitmap");
                }
            }
        };

        Response.ErrorListener error = new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        ImageRequest request = new ImageRequest(url, listener, 0, 0, Bitmap.Config.RGB_565, error);

        mQueue.add(request);
        mQueue.start();
    }

}
