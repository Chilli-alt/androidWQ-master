package com.example.androidwq.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidwq.BuildConfig;
import com.example.androidwq.R;
import com.example.androidwq.client;
import com.example.androidwq.utils.FileUtil;
import com.mob.MobSDK;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.example.androidwq.utils.FileUtil.getRealFilePathFromUri;
import static com.mob.tools.utils.ResHelper.getStringRes;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText passwordText,certainPasswordText,username;
    private CheckBox passwordEye,certainPasswordEye;
    private ImageView registerBack,btnClearPhoneNumber,btnCleanRegisterCode;
    private Button btnRegisterCode,btnSaveInfo;
    private EditText etPhoneNumber,etRegisterCode;
    private TextView tvTip;

    private int time = 60;
    private boolean flag = false;
    private String userName,psw,pswAgain;//获取用户名，密码
    private String iPhone;//手机号码
    private String iCord;//验证码
    private String imgString;//头像转化为字符串

    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    //头像
    private ImageView headImage2;
    //调用照相机返回图片文件
    private File tempFile;
    // 1: qq, 2: weixin
    private int type;
    client client1 = new client("114.55.35.90",3350);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        //client1.start();
        initShareSDK();
        initView();
        initData();
        initTextWatch();
        registerTime();
        iniLister();
    }
    /**
     * 布局的初始化
     */
    private void initView(){
        passwordEye = findViewById(R.id.password_eye);
        certainPasswordEye = findViewById(R.id.certain_password_eye);
        passwordText = findViewById(R.id.register_et_password);
        certainPasswordText = findViewById(R.id.register_et_repassword);
        username = findViewById(R.id.register_et_username);
        headImage2 = findViewById(R.id.head_image2);
        registerBack = findViewById(R.id.register_titleBar_iv_back);
        etPhoneNumber = findViewById(R.id.register_et_phoneNumber);
        btnClearPhoneNumber = findViewById(R.id.register_iv_clear_phoneNumber);
        etRegisterCode = findViewById(R.id.register_et_code);
        btnRegisterCode = findViewById(R.id.register_btn_getCode);
        btnCleanRegisterCode = findViewById(R.id.register_iv_clear_code);
        tvTip = findViewById(R.id.tip);
        btnSaveInfo = findViewById(R.id.save_info);
    }
    /**
     * 数据的初始化
     */
    private void initData(){
        registerBack.setOnClickListener(this);
        btnClearPhoneNumber.setOnClickListener(this);
        btnRegisterCode.setOnClickListener(this);
        btnCleanRegisterCode.setOnClickListener(this);
        btnSaveInfo.setOnClickListener(this);
        //切换明文密码与暗文密码
        passwordEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(passwordEye.isChecked()){
                    passwordText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else{
                    passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        certainPasswordEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(certainPasswordEye.isChecked()){
                    certainPasswordText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else{
                    certainPasswordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }
    private void iniLister(){
        headImage2.setOnClickListener(this);
    }
    /**
     * @param view
     * 点击事件
     */
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            //上传头像
            case R.id.head_image2:
                type = 2;
                uploadHeadImage();
                break;
            //返回按钮
            case R.id.register_titleBar_iv_back:
                finish();
                break;
            //清空输入的手机号码
            case R.id.register_iv_clear_phoneNumber:
                etPhoneNumber.setText("");
                btnClearPhoneNumber.setVisibility(View.GONE);
                break;
            //短信注册码的发送
            case R.id.register_btn_getCode:
                if(!TextUtils.isEmpty(etPhoneNumber.getText().toString().trim())){
                    if(etPhoneNumber.getText().toString().trim().length()==11){
                        iPhone = etPhoneNumber.getText().toString().trim();
                        SMSSDK.getVerificationCode("86",iPhone);
                        etRegisterCode.requestFocus();
                        btnRegisterCode.setVisibility(View.GONE);
                        tvTip.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(RegisterActivity.this, "请输入完整电话号码", Toast.LENGTH_LONG).show();
                        etPhoneNumber.requestFocus();
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "请输入您的电话号码", Toast.LENGTH_LONG).show();
                    etPhoneNumber.requestFocus();
                }
                break;
            //清空输入的注册码
            case R.id.register_iv_clear_code:
                etRegisterCode.setText("");
                btnCleanRegisterCode.setVisibility(View.GONE);
                break;
            //注册按钮
            case R.id.save_info:
                register();
                break;
            default:
                break;
        }
    }

    private void register() {
        /**
         * 获取控件中的字符串
         */
        userName=username.getText().toString().trim();
        psw=passwordText.getText().toString().trim();
        pswAgain=certainPasswordText.getText().toString().trim();
        if(TextUtils.isEmpty(userName)){
            Toast.makeText(RegisterActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(psw)){
            Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(pswAgain)) {
            Toast.makeText(RegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
        } else if(!psw.equals(pswAgain)){
            Toast.makeText(RegisterActivity.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();

            /**
             *从SharedPreferences中读取输入的用户名，判断SharedPreferences中是否有此用户名
             */
        }else if(!TextUtils.isEmpty(etRegisterCode.getText().toString().trim())){
            if(etRegisterCode.getText().toString().trim().length()==4){
                iCord = etRegisterCode.getText().toString().trim();
                SMSSDK.submitVerificationCode("86", iPhone, iCord);
            }else{
                Toast.makeText(RegisterActivity.this, "请输入完整验证码", Toast.LENGTH_LONG).show();
                etRegisterCode.requestFocus();
            }
        }else if(TextUtils.isEmpty(etRegisterCode.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_LONG).show();
            etRegisterCode.requestFocus();
        }
    }

    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.register_icon_pop, null);
        TextView btnCarema = view.findViewById(R.id.btn_camera);
        TextView btnPhoto = view.findViewById(R.id.btn_photo);
        TextView btnCancel = view.findViewById(R.id.btn_cancel);

        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(this);
        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到调用系统相机
                    gotoCamera();
                }
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
                if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //申请READ_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
                } else {
                    //跳转到相册
                    gotoPhoto();
                }
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    /**
     * 外部存储权限申请返回
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCamera();
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto();
            }
        }
    }


    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Log.d("evan", "*****************打开图库********************");
        //跳转到调用系统图库
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCamera() {
        Log.d("evan", "*****************打开相机********************");
        //创建拍照存储的图片文件
        tempFile = new File(FileUtil.checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"), System.currentTimeMillis() + ".jpg");

        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //设置7.0中共享文件，分享路径定义在xml/file_paths.xml
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(RegisterActivity.this, BuildConfig.APPLICATION_ID + ".fileProvider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);

                    headImage2.setImageBitmap(bitMap);

                    //此处后面可以将bitMap转为字符串上传后台网络
                    //转化格式
                     imgString = changeToString(bitMap);
                }
                break;
        }
    }
/*
**转化格式
 */
    private String changeToString(Bitmap bitMap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        if(imageString!=null){
            return imageString;
        }
        else {
            return "";
        }
    }

    /**
     * 打开截图界面
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }




    /**
     * 对于shareSDK的初始化（必要的）
     */
    private void initShareSDK(){
        MobSDK.init(this);
    }

    /**
     * 对于EditText的监听事件
     */
    private void initTextWatch(){
        //为输入手机号的EditText添加监听事件
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!etPhoneNumber.getText().equals("")){
                    btnClearPhoneNumber.setVisibility(View.VISIBLE);
                }
                else{
                    btnClearPhoneNumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(etPhoneNumber.getText().equals("")){
                    btnClearPhoneNumber.setVisibility(View.GONE);
                }
            }
        });
        //为输入的验证码EditText设置监听事件
        etRegisterCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!etRegisterCode.getText().equals("")){
                    btnCleanRegisterCode.setVisibility(View.VISIBLE);

                }
                else{
                    btnCleanRegisterCode.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(etRegisterCode.getText().equals("")){
                    btnCleanRegisterCode.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 实现验证信息的时间限制
     */
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

    /**
     * 显示验证码发送时间
     */
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
                etRegisterCode.setText("");
                tvTip.setText("提示信息");
                time = 60;
                tvTip.setVisibility(View.GONE);
                btnRegisterCode.setVisibility(View.VISIBLE);
            }
        }
    };


    /**
     * 判断验证码是否正确
     */
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
                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RegisterActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                    etPhoneNumber.requestFocus();
                    btnRegisterCode.setVisibility(View.VISIBLE);
                    tvTip.setVisibility(View.GONE);
                }else{
                    ((Throwable) data).printStackTrace();
                    int resId = getStringRes(RegisterActivity.this, "smssdk_network_error");
                    Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                    etRegisterCode.selectAll();
                    if (resId > 0) {
                        Toast.makeText(RegisterActivity.this, resId, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    /**
     * 验证码送成功后提示文字
     */
    private void reminderText() {
        tvTip.setVisibility(View.VISIBLE);
        handlerText.sendEmptyMessageDelayed(1, 1000);
    }

    /**
     *Json 封装并上传服务器
     */
    private void pottingJSON() {
        try {
            JSONObject loginInfor = new JSONObject();
            loginInfor.put("type","register");
            loginInfor.put("phone", iPhone);
            loginInfor.put("username", userName);
            loginInfor.put("password", psw);
            if(imgString != null) {
                loginInfor.put("image", imgString);
            }
            client.sendMessThread sendMessThread1 = client1.new sendMessThread();
            sendMessThread1.run(loginInfor);
            /*JSONObject Authorization =new JSONObject();
            Authorization.put("Person",loginInfor);*/
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * @param result 传入的为Flag，Flag在验证码正确后会被赋值为true
     * 在验证码输入正确后，会跳转到用户名注册界面
     */
    private void judgeFlag(boolean result){
        if(result){
            pottingJSON(); //封装用户名密码手机号头像数据
            client.receiveMessThread receiveMessThread1 = client1.new receiveMessThread();
            receiveMessThread1.start();//接受服务器的返回值message
            if(client.message=="success") {
                Intent intent = new Intent(this, LoginActivity.class);//注册成功进行跳转
                startActivity(intent);
            }else{
                Toast.makeText(RegisterActivity.this, "手机号已被注册", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
