package com.example.androidwq.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidwq.R;
import com.example.androidwq.client;
import com.example.androidwq.widget.VerifyEditView;
import com.mob.MobSDK;

import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.mob.tools.utils.ResHelper.getStringRes;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout phoneNumberLayout,checkCodeLayout,passwordLayout;
    private EditText etPhoneNumber,passwordText,certainPasswordText;
    private TextView tvTip;
    private Button btnCertainPhoneNumber,btnRegisterCode,btnCertainCheckCode,btnCertainPassword;
    private VerifyEditView chechCode;

    private int time = 60;
    private boolean flag = false;
    private String iPhone;//手机号码
    private String iCord;//验证码
    private String psw,pswAgain;//获取用户名，密码
    client client1 = new client("114.55.35.90",3350);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去除顶部标题栏
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forget_password);
        //client1.start();
        initShareSDK();
        initView();
        initData();
        initListener();
        registerTime();
    }

    //对于shareSDK的初始化（必要的）
    private void initShareSDK(){
        MobSDK.init(this);
    }

    private void initView(){
        phoneNumberLayout = findViewById(R.id.ll_phone_number);//输入电话号码页面
        checkCodeLayout = findViewById(R.id.ll_check_code_layout);//验证码页面
        passwordLayout = findViewById(R.id.ll_password_layout);//修改密码页面
        etPhoneNumber = findViewById(R.id.et_phone_number);//输入手机号
        btnCertainPhoneNumber = findViewById(R.id.btn_certain_phone_number);//检查手机号
        chechCode = findViewById(R.id.check_code);
        btnRegisterCode = findViewById(R.id.register_btn_getCode);//获得验证码
        tvTip = findViewById(R.id.tip);
        btnCertainCheckCode = findViewById(R.id.btn_certain_check_code);//检查验证码;
        btnCertainPassword = findViewById(R.id.btn_certain_password);//确认密码按钮
        passwordText = findViewById(R.id.et_password);//输入密码
        certainPasswordText = findViewById(R.id.et_confirm_password);//再次输入密码
    }

    private void initData(){

    }

    private void initListener(){
        btnCertainPhoneNumber.setOnClickListener(this);
        btnRegisterCode.setOnClickListener(this);
        btnCertainCheckCode.setOnClickListener(this);
        btnCertainPassword.setOnClickListener(this);
    }

    //实现验证信息的时间限制
    private void registerTime(){

        EventHandler eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = Message.obtain();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh);
    }

    Handler handlerText = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){
                if(time>0){
                    tvTip.setText("验证码已发送"+time+"秒");
                    time--;
                    handlerText.sendEmptyMessageDelayed(1, 1000);
                }else{
                    tvTip.setText("提示信息");
                    time = 60;
                    tvTip.setVisibility(View.GONE);
                    btnRegisterCode.setVisibility(View.VISIBLE);
                }
            }else{
                chechCode.setText("");
                tvTip.setText("提示信息");
                time = 60;
                tvTip.setVisibility(View.GONE);
                btnRegisterCode.setVisibility(View.VISIBLE);
            }
        }
    };

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event="+event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回Activity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功,验证通过
                    Toast.makeText(getApplicationContext(), "验证码校验成功", Toast.LENGTH_SHORT).show();
                    handlerText.sendEmptyMessage(2);
                    flag = true;
                    judgeFlag(flag);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){//服务器验证码发送成功
                    reminderText();
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){//返回支持发送验证码的国家列表
                    Toast.makeText(getApplicationContext(), "获取国家列表成功", Toast.LENGTH_SHORT).show();
                }
            } else {
                if(flag){
                    btnRegisterCode.setVisibility(View.VISIBLE);
                    Toast.makeText(ForgetPasswordActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                    btnRegisterCode.setVisibility(View.VISIBLE);
                    tvTip.setVisibility(View.GONE);
                }else{
                    ((Throwable) data).printStackTrace();
                    int resId = getStringRes(ForgetPasswordActivity.this, "smssdk_network_error");
                    Toast.makeText(ForgetPasswordActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    chechCode.selectAll();
                    if (resId > 0) {
                        Toast.makeText(ForgetPasswordActivity.this, resId, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    //验证码送成功后提示文字
    private void reminderText() {
        tvTip.setVisibility(View.VISIBLE);
        handlerText.sendEmptyMessageDelayed(1, 1000);
    }

    private void judgeFlag(boolean result){
        //如果校验码正确就跳转到修改密码页面
        if(result){
            checkCodeLayout.setVisibility(View.GONE);
            passwordLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btn_certain_phone_number:
                iPhone += etPhoneNumber.getText();
                if(!TextUtils.isEmpty(iPhone.trim())){
                    if(etPhoneNumber.getText().toString().trim().length()==11){
                        iPhone = etPhoneNumber.getText().toString().trim();
                        //判断手机号是否注册
                        if(judgePhone(iPhone)){
                        phoneNumberLayout.setVisibility(View.GONE);
                        checkCodeLayout.setVisibility(View.VISIBLE);
                        tvTip.setVisibility(View.GONE);
                        }else {
                            Toast.makeText(ForgetPasswordActivity.this, "此手机号未注册", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(ForgetPasswordActivity.this, "请输入完整电话号码", Toast.LENGTH_LONG).show();
                        etPhoneNumber.requestFocus();
                    }
                }else{
                    Toast.makeText(ForgetPasswordActivity.this, "请输入您的电话号码", Toast.LENGTH_LONG).show();
                    etPhoneNumber.requestFocus();
                }
                break;
            case R.id.register_btn_getCode:
                SMSSDK.getVerificationCode("86",iPhone);
                chechCode.requestFocus();
                btnRegisterCode.setVisibility(View.GONE);
                tvTip.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_certain_check_code:
                if(!TextUtils.isEmpty(chechCode.getText().toString().trim())){
                    if(chechCode.getText().toString().trim().length()==4){
                        iCord = chechCode.getText().toString().trim();
                        SMSSDK.submitVerificationCode("86", iPhone, iCord);
                    }else{
                        Toast.makeText(ForgetPasswordActivity.this, "请输入完整验证码", Toast.LENGTH_LONG).show();
                        chechCode.requestFocus();
                    }
                }else{
                    Toast.makeText(ForgetPasswordActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
                    chechCode.requestFocus();
                }
                break;
            case R.id.btn_certain_password:
                changepaw();
                break;
            default:
                break;
        }
    }
/*
**判断手机号是否注册
 */
    private boolean judgePhone(String phoneNumber) {
        try {
            JSONObject forgetInfor = new JSONObject();
            forgetInfor.put("type","forgetphone");
            forgetInfor.put("phone", iPhone);
            client.sendMessThread sendMessThread1 = client1.new sendMessThread();
            sendMessThread1.run(forgetInfor);
            client.receiveMessThread receiveMessThread1 = client1.new receiveMessThread();
            receiveMessThread1.start();//接受服务器的返回值message
            /*JSONObject Authorization =new JSONObject();
            Authorization.put("Person",loginInfor);*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(client.message=="success")
            return true;
        else
            return  false;
    }
/*
**修改密码页面判断并封装修改后的密码进行上传
 */
    private void changepaw() {
        psw=passwordText.getText().toString().trim();
        pswAgain=certainPasswordText.getText().toString().trim();
        if(TextUtils.isEmpty(psw)){
            Toast.makeText(ForgetPasswordActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(pswAgain)) {
            Toast.makeText(ForgetPasswordActivity.this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
        } else if(!psw.equals(pswAgain)){
            Toast.makeText(ForgetPasswordActivity.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
        }else{
            pottingJSON(); //封装密码手机号数据
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
    }
    //将新密码传给服务器
    private void pottingJSON() {
        try {
            JSONObject forgetInfor = new JSONObject();
            forgetInfor.put("type", "forget");
            //forgetInfor.put("phone", iPhone);
            forgetInfor.put("password", psw);
            client.sendMessThread sendMessThread1 = client1.new sendMessThread();
            sendMessThread1.run(forgetInfor);
            /*JSONObject Authorization =new JSONObject();
            Authorization.put("Person",loginInfor);*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
