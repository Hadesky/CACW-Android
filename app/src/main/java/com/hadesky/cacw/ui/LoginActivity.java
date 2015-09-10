package com.hadesky.cacw.ui;

/**
 * Created by Bright Van on 2015/8/26/026.
 */

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hadesky.cacw.R;
import com.hadesky.cacw.util.LoginTask;
import com.hadesky.cacw.widget.CircleImageView;

public class LoginActivity extends BaseActivity {

    private EditText mUsername, mPassword;
    private ImageButton mPwButton;
    private Button mLoginButton;
    private boolean mIsPwVisitable = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        CircleImageView icon = (CircleImageView) findViewById(R.id.login_icon);
        icon.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));

        mLoginButton = (Button) findViewById(R.id.login_login_button);

        mPwButton = (ImageButton) findViewById(R.id.login_password_eye);

        mUsername = (EditText) findViewById(R.id.login_username);
        mPassword = (EditText) findViewById(R.id.login_password);

    }

    @Override
    public void setupView() {
        setupPwButton();
        setupEditText();
        setupActionBar();
    }

    private void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
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
                    mPwButton.setSelected(false);
                    mIsPwVisitable = false;
                    setIsPwVisible(false);
                } else {
                    mPwButton.setSelected(true);
                    mIsPwVisitable = true;
                    setIsPwVisible(true);
                }
            }
        });
    }

    private void setIsPwVisible(boolean visible) {
        if (visible) {
            mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
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

    public void btOnClick(View view) {
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
    private void login(final String username, final String password) {
        ProgressDialog progressDialog = new ProgressDialog(this);

        LoginTask loginTask = new LoginTask(progressDialog, context);
        loginTask.execute(URL, username, password);
    }
}
