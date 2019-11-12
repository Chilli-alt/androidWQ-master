package com.example.androidwq.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidwq.R;
import com.example.androidwq.client;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mPasswordLayout;
    private TextView mForgetPsdView,mRegisterView;//注册账号和忘记密码
    private EditText mAccountView,mPasswordView;
    private ImageView mClearAccountView, mClearPasswordView;
    private CheckBox mEyeView;
    private Button mLoginView;
    private String password,iPhone,strImg;//获取用户名，密码
    client client1 = new client("114.55.35.90",3350);

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        //client1.start();
        initView();
        initData();
        initListener();
    }

    public void initView(){
        mAccountView = findViewById(R.id.et_input_account);
        mPasswordView = findViewById(R.id.et_input_password);
        mClearAccountView = findViewById(R.id.iv_clear_account);
        mClearPasswordView = findViewById(R.id.iv_clear_password);
        mEyeView = findViewById(R.id.iv_login_open_eye);
        mLoginView = findViewById(R.id.btn_login);
        mForgetPsdView = findViewById(R.id.tv_forget_password);
        mRegisterView = findViewById(R.id.tv_register_account);
        mPasswordLayout = findViewById(R.id.rl_password_layout);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initData(){
        mPasswordView.setLetterSpacing(0.2f);

        mEyeView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    mPasswordView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else
                    mPasswordView.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

    }

    public void initListener(){
        mClearAccountView.setOnClickListener(this);
        mClearPasswordView.setOnClickListener(this);
        mForgetPsdView.setOnClickListener(this);
        mRegisterView.setOnClickListener(this);
        mLoginView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.iv_clear_account:
                mAccountView.setText("");
                break;
            case R.id.iv_clear_password:
                mPasswordView.setText("");
                break;
            case R.id.tv_forget_password:
                intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_register_account:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                iPhone=mAccountView.getText().toString().trim();
                password=mPasswordView.getText().toString().trim();
                if(judgeuser(iPhone,password)) {
                    //获取userimg数据
                    client.receiveMessThread receiveMessThread1 = client1.new receiveMessThread();
                    receiveMessThread1.start();//接受服务器的返回值message
                    strImg=client.message;
                    //把strimg传到主界面。。。
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "账号或密码不正确，请重新输入", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
//判断密码和手机号是否正确
    private boolean judgeuser(String iPhone, String password) {
        try {
            JSONObject loginInfor = new JSONObject();
            loginInfor.put("type","login");
            loginInfor.put("phone", iPhone);
            loginInfor.put("password", password);
            client.sendMessThread sendMessThread1 = client1.new sendMessThread();
            sendMessThread1.run(loginInfor);
            // client.message
            /*JSONObject Authorization =new JSONObject();
            Authorization.put("Person",loginInfor);*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(client.message=="success")
        return true;
        else
            return false;
    }
}
