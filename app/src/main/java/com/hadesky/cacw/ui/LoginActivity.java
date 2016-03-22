package com.hadesky.cacw.ui;

/**
 * 登录Activity
 * Created by Bright Van on 2015/8/26/026.
 */

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hadesky.cacw.R;
import com.hadesky.cacw.config.MyApp;
import com.hadesky.cacw.config.SessionManagement;
import com.hadesky.cacw.task.LoginTask;
import com.hadesky.cacw.ui.View.ForgetPswActivity;
import com.hadesky.cacw.widget.AnimProgressDialog;
import com.hadesky.cacw.widget.CircleImageView;

import java.util.concurrent.ExecutionException;

public class LoginActivity extends BaseActivity{

    private EditText mUsername, mPassword;
    private Button mPwButton;
    private Button mLoginButton;
    private boolean mIsPwVisitable = false;
    private SessionManagement mSession;
    private String URL;
    private View mForgetPsw;
    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        CircleImageView icon = (CircleImageView) findViewById(R.id.login_icon);
        icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
        mForgetPsw = findViewById(R.id.tv_forget_psw);
        mLoginButton = (Button) findViewById(R.id.bt_register);

        mPwButton = (Button) findViewById(R.id.password_eye);

        mUsername = (EditText) findViewById(R.id.editview_username);
        mPassword = (EditText) findViewById(R.id.editview_password);

        final MyApp app = (MyApp) getApplication();
        mSession = app.getSession();
        URL = app.getURL();
    }

    @Override
    public void setupView() {
        setupPwButton();
        setupEditText();
        setupActionBar();




    }


    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;

        actionBar.setTitle("");
    }

    private void setupEditText() {
        //设置IME输入法的监听
        mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    mLoginButton.performClick();
                }
                return true;
            }
        });
    }


    private void setupPwButton() {
        mPwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIsPwVisible(!mIsPwVisitable);
                if (mIsPwVisitable) {
                    mPwButton.setText("HIDE");
                    mIsPwVisitable = false;
                    setIsPwVisible(false);
                } else {
                    mPwButton.setText("SHOW");
                    mIsPwVisitable = true;
                    setIsPwVisible(true);
                }
            }
        });

        mForgetPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(LoginActivity.this, ForgetPswActivity.class));
            }
        });
    }

    private void setIsPwVisible(boolean visible) {
        if (visible) {
            mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            //定位到最后
            CharSequence text = mPassword.getText();
            if (text != null) {
                Spannable spanText = (Spannable) text;
                Selection.setSelection(spanText, text.length());
            }
        } else {
            mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            //定位到最后
            CharSequence text = mPassword.getText();
            if (text != null) {
                Spannable spanText = (Spannable) text;
                Selection.setSelection(spanText, text.length());
            }
        }
    }

    public void btOnClick(View view) throws ExecutionException, InterruptedException {
        //检查网络状态
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //有网络
            String username = mUsername.getText().toString();
            String password = mPassword.getText().toString();

            if (username.trim().length() > 0) {
                if (password.trim().length() > 0) {
                    login(username, password);
                } else {
                    //没输入密码
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
            } else {
                //没输入用户名
                Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请检查网络！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 条件具备时，调用该方法尝试登陆
     * @param username 用户名
     * @param password 密码
     */
    private void login(final String username, final String password) throws ExecutionException, InterruptedException {
        AnimProgressDialog progressDialog = new AnimProgressDialog(this, false, null, "登录中...");
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        LoginTask loginTask = new LoginTask(progressDialog, context, mSession);
        loginTask.execute(URL, username, password);
    }

    public void goRegisterActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }
}
