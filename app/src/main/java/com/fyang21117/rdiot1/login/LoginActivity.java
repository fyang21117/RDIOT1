package com.fyang21117.rdiot1.login;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fyang21117.rdiot1.MainActivity;
import com.fyang21117.rdiot1.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FingerprintCore mFingerprintCore;
    private KeyguardLockScreenManager mKeyguardLockScreenManager;//指纹管理

    private Handler mHandler = new Handler(Looper.getMainLooper());//主线程
    private Toast mToast;
    private EditText editText;
    private String string;
    public static boolean unlocked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("应用锁");

        initFingerprintCore();

        Button psw_login = findViewById(R.id.psw_login);
        Button use_fingerprint = findViewById(R.id.use_fingerprint);
        Button test = findViewById(R.id.test);

        editText = findViewById(R.id.unlock_num);
        test.setOnClickListener(this);
        psw_login.setOnClickListener(this);
        use_fingerprint.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final int viewId = v.getId();
        switch (viewId) {
            case R.id.test:
                MainActivity.actionStart(this);
                finish();
                break;

           case R.id.use_fingerprint:
               //调用非活动布局的控件
               View view= getLayoutInflater().inflate(R.layout.fingerprint_dialog, null);
               final MyDialog mMyDialog= new MyDialog(this, 0, 0, view, R.style.DialogTheme);
               Button cancel_fingerprint = view.findViewById(R.id.cancel_unlock);
               mMyDialog.setCancelable(true);
               mMyDialog.show();
               startFingerprintRecognition();
               cancel_fingerprint.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       mMyDialog.dismiss();
                   }
               });
               break;

            case R.id.psw_login:
                string =editText.getText().toString();
                if(string.equals("123456")) {
                    unlocked = true;
                    MainActivity.actionStart(this);
                    finish();
                }else {
                    Toast.makeText(this, "Unlocking failed!Please input again!", Toast.LENGTH_SHORT).show();
                }
                 break;
           default:break;
        }
     }
    /*** 指纹识别初始化*/
    private void initFingerprintCore() {
        mFingerprintCore = new FingerprintCore(this);
        mFingerprintCore.setFingerprintManager(mResultListener);
        mKeyguardLockScreenManager = new KeyguardLockScreenManager(this);
    }

    /*** 进入系统设置指纹界面*/
    private void enterSysFingerprintSettingPage() {
        FingerprintUtil.openFingerPrintSettingPage(this);
    }
    private void startFingerprintRecognitionUnlockScreen() {
        if (mKeyguardLockScreenManager == null) {
            return;
        }
        if (!mKeyguardLockScreenManager.isOpenLockScreenPwd()) {

            FingerprintUtil.openFingerPrintSettingPage(this);
            return;
        }
        mKeyguardLockScreenManager.showAuthenticationScreen(this);
    }

    /*** 开始指纹识别*/
    private void startFingerprintRecognition() {
        if (mFingerprintCore.isSupport()) {
            if (!mFingerprintCore.isHasEnrolledFingerprints()) {
                toastTipMsg(R.string.fingerprint_recognition_not_enrolled);
                FingerprintUtil.openFingerPrintSettingPage(this);
                return;
            }

            toastTipMsg(R.string.fingerprint_recognition_tip);
            if (mFingerprintCore.isAuthenticating()) {
                toastTipMsg(R.string.fingerprint_recognition_authenticating);
            } else {
                mFingerprintCore.startAuthenticate();
            }
        } else {
            toastTipMsg(R.string.fingerprint_recognition_not_support);
        }
    }

    private void IntoActivity() {
        unlocked = true;
        MainActivity.actionStart(this);//识别成功
    }

    private FingerprintCore.IFingerprintResultListener mResultListener
            = new FingerprintCore.IFingerprintResultListener() {
        @Override
        public void onAuthenticateSuccess() {
            toastTipMsg(R.string.fingerprint_recognition_success);
            IntoActivity();
        }
        @Override
        public void onAuthenticateFailed(int helpId) {
            toastTipMsg(R.string.fingerprint_recognition_failed);
        }
        @Override
        public void onAuthenticateError(int errMsgId) {
            toastTipMsg(R.string.fingerprint_recognition_error);
        }
        @Override
        public void onStartAuthenticateResult(boolean isSuccess) {
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == KeyguardLockScreenManager.REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            // Challenge completed, proceed with using cipher
            if (resultCode == RESULT_OK) {
                toastTipMsg(R.string.sys_pwd_recognition_success);
            } else {
                toastTipMsg(R.string.sys_pwd_recognition_failed);
            }
        }
    }

    private void toastTipMsg(int messageId) {
        if (mToast == null) {
            mToast = Toast.makeText(this, messageId, Toast.LENGTH_SHORT);
        }
        mToast.setText(messageId);
        mToast.cancel();
        mHandler.removeCallbacks(mShowToastRunnable);
        mHandler.postDelayed(mShowToastRunnable, 0);
    }

    private void toastTipMsg(String message) {
        if (mToast == null) {
            mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        }
        mToast.setText(message);
        mToast.cancel();
        mHandler.removeCallbacks(mShowToastRunnable);
        mHandler.postDelayed(mShowToastRunnable, 200);
    }

    private Runnable mShowToastRunnable = new Runnable() {
        @Override
        public void run() {
            mToast.show();
        }
    };

    @Override
    protected void onDestroy() {
        if (mFingerprintCore != null) {
            mFingerprintCore.onDestroy();
            mFingerprintCore = null;
        }
        if (mKeyguardLockScreenManager != null) {
            mKeyguardLockScreenManager.onDestroy();
            mKeyguardLockScreenManager = null;
        }
        mResultListener = null;
        mShowToastRunnable = null;
        mToast = null;
        super.onDestroy();
    }
}
