package com.hadesky.cacw.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.hadesky.cacw.R;
import com.hadesky.cacw.config.MyApp;

import com.hadesky.cacw.ui.view.RegisterView;
import com.hadesky.cacw.ui.widget.AnimProgressDialog;
import com.hadesky.cacw.ui.widget.DeletableEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends BaseActivity implements RegisterView {
    private Button getCodeButton;
    private Button registerButton;

    private AnimProgressDialog progressDialog;
    private DeletableEditText emailEditText;
    private DeletableEditText pswEditText;
    private DeletableEditText pswConfirmEditText;

    //private RegisterPresenterImpl presenter;
    private DisableBtHandler handler;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initView() {
        getCodeButton = (Button) findViewById(R.id.bt_get_code);
        registerButton = (Button) findViewById(R.id.bt_register);
        pswEditText = (DeletableEditText) findViewById(R.id.det_password);
        pswConfirmEditText = (DeletableEditText) findViewById(R.id.det_password_confirm);


        progressDialog = new AnimProgressDialog(this);
        emailEditText = (DeletableEditText) findViewById(R.id.det_username);
        //presenter = new RegisterPresenterImpl(this);
    }

    @Override
    public void setupView() {
        setupGetCodeBt();
    }

    private void setupGetCodeBt() {
        getCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                if (isEmail(email)) {
                    //presenter.getAuthCode(email);
                }else showMsg("请输入正确格式的邮箱地址！");
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = emailEditText.getText().toString();
                if (!isEmail(email))
                {
                    showMsg("请输入正确格式的邮箱地址！");
                    return;
                }
                if (!pswEditText.getText().toString().equals(pswConfirmEditText.getText().toString()))
                {
                    showMsg("两次密码不一致");
                    return;
                }
                if (pswEditText.getText().length()<6)
                {
                    showMsg("密码至少6位数");
                    return;
                }
                // TODO: 2016/7/5 0005 这里没用到验证码
                //presenter.register(email,pswEditText.getText().toString(),"0");
            }
        });

    }

    /**
     * 检测邮箱地址是否合法
     * @param email 检查的email
     * @return true合法 false不合法
     */
    private boolean isEmail(String email) {
        if (!TextUtils.isEmpty(email)) {
            Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
            Matcher m = p.matcher(email);
            return m.matches();
        }
        return false;
    }

    @Override
    public void showProgress() {
        if (progressDialog != null) {
            progressDialog.setTitle("请稍后...");
            progressDialog.show();
        }
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void showMsg(String msg) {
        showToast(msg);
    }

    @Override
    public MyApp getMyApp() {
        return (MyApp) getApplication();
    }

    @Override
    public void disableGetCodeBt() {
        if (getCodeButton.isEnabled()) {
            handler = new DisableBtHandler(getMainLooper());
            Thread thread = new Thread(new DisableBtTask(30000));
            thread.start();
        }
    }

    @Override
    public void close() {
        this.finish();
    }

    /**
     * 临时禁用获取验证码的按钮，防止连续获取验证码
     */
    private class DisableBtTask implements Runnable{
        private long time;

        public DisableBtTask(long time) {
            this.time = time;
        }

        @Override
        public void run() {
            Message msg = handler.obtainMessage(DisableBtHandler.ON_START);
            handler.sendMessage(msg);
            while (time >= 0) {
                try {
                    msg = handler.obtainMessage(DisableBtHandler.ON_TIMING);
                    Bundle bundle = new Bundle();
                    bundle.putLong("lefttime", time);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                    Thread.sleep(1000);
                    time -= 1000;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            msg = handler.obtainMessage(DisableBtHandler.ON_FINISH);
            handler.sendMessage(msg);
        }
    }

    private class DisableBtHandler extends Handler {
        public static final int ON_TIMING = 0;
        public static final int ON_START = 1;
        public static final int ON_FINISH = 2;

        public DisableBtHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            switch (msg.what) {
                case ON_TIMING:
                    int second = (int) (bundle.getLong("lefttime") * 0.001);
                    getCodeButton.setText("请等待" + second + "秒");
                    break;
                case ON_START:
                    getCodeButton.setEnabled(false);
                    getCodeButton.setSelected(true);
                    break;
                case ON_FINISH:
                    getCodeButton.setEnabled(true);
                    getCodeButton.setSelected(false);
                    getCodeButton.setText("获取验证码");
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
