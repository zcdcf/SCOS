package es.source.code.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import es.source.code.model.GlobalConst;
import es.source.code.model.User;

public class LoginOrRegister extends AppCompatActivity {

    @BindView(R.id.pbShowLogin)
    ProgressBar pbShowLogin;
    @BindView(R.id.bLogin)
    Button bLogin;
    @BindView(R.id.bReturn)
    Button bReturn;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etUserPassword)
    EditText etUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
        ButterKnife.bind(this);
    }

    CountDownTimer countDownTimer = new CountDownTimer(2000, 100) {

        @Override
        public void onTick(long millisUntilFinished) {
            progressbarNum += 5;
            pbShowLogin.setProgress(progressbarNum);
        }

        @Override
        public void onFinish() {
            pbShowLogin.setProgress(100);
            pbShowLogin.setVisibility(View.INVISIBLE);
            progressbarNum = 0;
            if(userNameIsRight && userPasswordIsRight && loginSuccess) {
                onBackPressed();
            }
        }
    };

    int progressbarNum = 0;
    boolean loginSuccess = false;
    boolean registerSuccess = false;

    @OnClick({R.id.bLogin, R.id.bReturn, R.id.bRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bLogin:
                countDownTimer.start();
                pbShowLogin.setVisibility(View.VISIBLE);
                if(userPasswordIsRight && userNameIsRight) {
                    loginSuccess = true;
                }
                break;
            case R.id.bReturn:
                onBackPressed();
                break;
            case  R.id.bRegister:
                if(userNameIsRight && userPasswordIsRight) {
                    registerSuccess = true;
                    onBackPressed();
                }
        }
    }

    @Override
    public void onBackPressed() {
        if(userPasswordIsRight && userNameIsRight && (loginSuccess || registerSuccess)) {
            if(loginSuccess) {
                User loginUser = new User(userName, password, GlobalConst.IS_OLD_USER);
                Intent intent = new Intent();
                intent.putExtra("infoFromLogin", GlobalConst.INFO_LOGIN_SUCCESS_TO_MAINSCREEN_FROM_LOGIN);
                intent.putExtra("infoLoginUser", loginUser);
                setResult(GlobalConst.LOGIN_SUCCESS_RESULT_CODE, intent);
            } else if(registerSuccess) {
                User loginUser = new User(userName, password, GlobalConst.NOT_OLD_USER);
                Intent intent = new Intent();
                intent.putExtra("infoFromLogin", GlobalConst.INFO_REGISTER_SUCCESS_TO_MAINSCREEN_FROM_LOGIN);
                intent.putExtra("infoLoginUser", loginUser);
                setResult(GlobalConst.REGISTER_SUCCESS_RESULT_CODE, intent);
            }
        } else {
            Intent intent = new Intent();
            intent.putExtra("infoFromLogin", GlobalConst.INFO_RETURN_TO_MAINSCREEN_FROM_LOGIN);
            setResult(GlobalConst.LOGIN_RETURN_RESULT_CODE,intent);
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }


    private boolean userNameIsRight = false;
    private boolean userPasswordIsRight = false;

    String userName;
    String password;

    @OnTextChanged({R.id.etUserName,R.id.etUserPassword})
    public void onTextChanged() {
        View rootView = this.getWindow().getDecorView();
        int focusId = rootView.findFocus().getId();
        switch (focusId) {
            case R.id.etUserName: {
                if (checkFormat(userName = etUserName.getText().toString())) {
                    userNameIsRight = true;
                } else {
                    etUserName.setError("格式错误：请输入字母或数字，且不含空格");
                    userNameIsRight = false;
                }
            }
            case R.id.etUserPassword: {
                if (checkFormat(password = etUserPassword.getText().toString())) {
                    userPasswordIsRight = true;
                } else {
                    etUserPassword.setError("格式错误：请输入字母或数字，且不含空格");
                    userPasswordIsRight = false;
                }
            }
        }
    }

    private boolean checkFormat(String str) {
        if (str.matches("[a-zA-z0-9]{0,20}")) {
            return true;
        } else {
            return false;
        }
    }

}
