package es.source.code.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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
    @BindView(R.id.bRegister)
    Button bRegister;

    SharedPreferences userState = null;
    SharedPreferences.Editor editor = null;
    private String loginUrl = GlobalConst.BASE_URL+"/SCOSServer/LoginValidator";

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
        ButterKnife.bind(this);

        userState = getSharedPreferences("user",Context.MODE_PRIVATE);
        editor = userState.edit();

        if (userState.contains("userName")) {
            bRegister.setVisibility(View.INVISIBLE);
            etUserName.setText(userState.getString("userName",""));
            userName = userState.getString("userName","");
            userNameIsRight = true;
        } else {
            bLogin.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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
            /*if (userNameIsRight && userPasswordIsRight && loginSuccess) {
                onBackPressed();
            } else {
                Toast toast = Toast.makeText(LoginOrRegister.this, "登录失败", Toast.LENGTH_SHORT);
                toast.show();
            }*/
        }
    };

    int progressbarNum = 0;
    boolean loginSuccess = false;
    boolean registerSuccess = false;

    @OnClick({R.id.bLogin, R.id.bReturn, R.id.bRegister})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bLogin:
                if(userNameIsRight&&userPasswordIsRight) {
                    countDownTimer.start();
                    pbShowLogin.setVisibility(View.VISIBLE);
                /*if (userPasswordIsRight && userNameIsRight) {
                    loginSuccess = true;
                }*/
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> loginInfo = new HashMap<>();
                            loginInfo.put("name", userName);
                            loginInfo.put("password", password);

                            try {

                                JSONObject result = new JSONObject(connectToServer(loginInfo));
                                if (result.get("RESULTCODE").toString().equals("1")) {
                                    loginSuccess = true;
                                    Log.i("login state:", "success");
                                } else {
                                    Log.i("login state:", "failure");
                                    loginSuccess = false;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            EventBus.getDefault().post(new GlobalConst.LoginMessageEvent(loginSuccess));
                        }
                    }).start();
                } else {
                    Toast.makeText(this,"用户名或密码格式错误",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bReturn:
                onBackPressed();
                break;
            case R.id.bRegister:
                if (userNameIsRight && userPasswordIsRight) {
                    registerSuccess = true;
                    onBackPressed();
                }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(GlobalConst.LoginMessageEvent event) {
        if (event.getLoginInfo()) {
            if (userNameIsRight && userPasswordIsRight && loginSuccess) {
                onBackPressed();
            }
        } else {
                Toast toast = Toast.makeText(LoginOrRegister.this, "用户名或密码错误", Toast.LENGTH_SHORT);
                toast.show();
        }
    }

    @Override
    public void onBackPressed() {
        if (userPasswordIsRight && userNameIsRight && (loginSuccess || registerSuccess)) {
            if (loginSuccess) {
                setDataInSharedPreference();
                User loginUser = new User(userName, password, GlobalConst.IS_OLD_USER);
                Intent intent = new Intent();
                intent.putExtra("infoFromLogin", GlobalConst.INFO_LOGIN_SUCCESS_TO_MAINSCREEN_FROM_LOGIN);
                intent.putExtra("infoLoginUser", loginUser);
                setResult(GlobalConst.LOGIN_SUCCESS_RESULT_CODE, intent);
            } else if (registerSuccess) {
                setDataInSharedPreference();
                User registerUser = new User(userName, password, GlobalConst.NOT_OLD_USER);
                Intent intent = new Intent();
                intent.putExtra("infoFromLogin", GlobalConst.INFO_REGISTER_SUCCESS_TO_MAINSCREEN_FROM_LOGIN);
                intent.putExtra("infoLoginUser", registerUser);
                setResult(GlobalConst.REGISTER_SUCCESS_RESULT_CODE, intent);
            }
        } else {
            Intent intent = new Intent();
            intent.putExtra("infoFromLogin", GlobalConst.INFO_RETURN_TO_MAINSCREEN_FROM_LOGIN);
            if(userState.contains("userName")) {
                editor.putInt("loginState",GlobalConst.LOGIN_FAILED_STATE);
            }
            setResult(GlobalConst.LOGIN_RETURN_RESULT_CODE, intent);
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    private void setDataInSharedPreference() {
        editor.putString("userName", userName);
        editor.putString("password", password);
        editor.putInt("loginState", GlobalConst.LOGIN_SUCCESS_STATE);
        editor.commit();
    }


    private boolean userNameIsRight = false;
    private boolean userPasswordIsRight = false;

    String userName;
    String password;

    @OnTextChanged({R.id.etUserName, R.id.etUserPassword})
    public void onTextChanged() {
        View rootView = this.getWindow().getDecorView();
        if(rootView.findFocus()!=null) {
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
    }


    private boolean checkFormat(String str) {
        if (str.matches("[a-zA-z0-9]{0,20}")) {
            return true;
        } else {
            return false;
        }
    }

    private String connectToServer(Map<String,String> loginInfo) {
        StringBuilder sb = new StringBuilder();

        try {
            // initialize a new connection
            URL url = new URL(loginUrl);
            Log.i("Login state:","try to connect Server");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(true);

            // write username and password in the connection
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            writer.write(getStringFromOutput(loginInfo));

            writer.flush();
            writer.close();
            outputStream.close();
            Log.i("login stae","send information");

            // get the response
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String temp;
                while ((temp = reader.readLine()) != null) {
                    Log.i("login state: get response, ",temp);
                    sb.append(temp);
                }

                reader.close();
            } else {
                return "connection error:" + connection.getResponseCode();
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    // transform the username and password stored in the map into string
    private static String getStringFromOutput(Map<String,String> map) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;

        for(Map.Entry<String,String> entry:map.entrySet()){
            if(isFirst)
                isFirst = false;
            else
                sb.append("&");

            sb.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
            sb.append("=");
            sb.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
        }
        Log.i("login state:","send information is"+sb.toString());
        return sb.toString();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
