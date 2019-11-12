package com.example.androidwq.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.androidwq.R;
import com.example.androidwq.ui.adapter.ChatAdapter;
import com.example.androidwq.ui.data.MsgData;
import com.example.androidwq.utils.HelpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author rfa
 * 聊天界面
 */

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{

    public final static int TYPE_RECEIVER_MSG = 0x21;
    public final static int TYPE_SENDER_MSG = 0x22;
    public final static int TYPE_TIME_STAMP = 0x23;

    private int profileId = R.drawable.hdimg_3;
    private List<MsgData> data;
    private ChatAdapter adapter;
    private RecyclerView chatRecycler;
    private Toolbar bar;
    private TextView chatFriendName;
    private ImageView btnBack,chatVoice,btnChatEmotion,chatAdd;
    private Button btnChatSend;
    private EditText chatMessage;

    Handler smartReplyMsgHandler = new Handler();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wechat_chat);

        initView();

        initListener();

        initData();

    }

    private void initView(){
        bar = findViewById(R.id.chat_toolbar);
        chatFriendName = findViewById(R.id.chat_friend_name);
        btnBack = findViewById(R.id.chat_back);
        chatVoice = findViewById(R.id.btn_chat_voice);
        chatMessage = findViewById(R.id.chat_message);
        btnChatEmotion = findViewById(R.id.btn_chat_emotion);
        btnChatSend = findViewById(R.id.chat_send);
        chatAdd = findViewById(R.id.chat_add);
        chatRecycler = findViewById(R.id.chat_recycler);
    }

    private void initListener(){
        btnBack.setOnClickListener(this);
        btnChatSend.setOnClickListener(this);


        chatMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("tag", "onTextChanged --- start -> " + start + " , count ->" + count + "，before ->" + before);
                if (start == 0 && count > 0) {
                    btnChatSend.startAnimation(getVisibleAnim(true, btnChatSend));
                    btnChatSend.setVisibility(View.VISIBLE);
                    chatAdd.setVisibility(View.GONE);
                }

                if (start == 0 && count == 0) {
                    chatAdd.startAnimation(getVisibleAnim(true, chatAdd));
                    btnChatSend.setVisibility(View.GONE);
                    chatAdd.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.chat_back:
                finish();
                break;
            case R.id.chat_send:
                String sendMsg = chatMessage.getText().toString();
                MsgData msgData = new MsgData(sendMsg, HelpUtils.getCurrentMillisTime(), R.drawable.hdimg_1, TYPE_SENDER_MSG);
                data.add(data.size(), msgData);
                adapter.notifyDataSetChanged();
                chatRecycler.scrollToPosition(data.size() - 1);
                chatMessage.setText("");

                smartReplyMsgHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String receiveMsg = getRandomMessage();
                        MsgData msgData1 = new MsgData(receiveMsg, HelpUtils.getCurrentMillisTime(), profileId, TYPE_RECEIVER_MSG);
                        data.add(data.size(), msgData1);
                        adapter.notifyDataSetChanged();
                        chatRecycler.scrollToPosition(data.size() - 1);
                        smartReplyMsgHandler.removeCallbacksAndMessages(null);
                    }
                },500);
                break;
            default:
                break;
        }
    }

    private void initData(){

        HelpUtils.transparentNav(ChatActivity.this);
        String name = getIntent().getStringExtra("name");
        profileId = getIntent().getIntExtra("profileId", R.drawable.hdimg_3);
        if (name != null) {
            chatFriendName.setText(name);
        }

        String[] msgs = {"在吗", "不在", "不在怎么回消息的", "我是天才机器人", "机器人这么智能啊", "是啊，时代在发展", "那你唱歌看看"
                , "你叫唱就唱啊，多没面子", "那你能干嘛", "啥都不能", "那你有啥用？", "没啥用你和我聊啥", "我想看下有到底有啥用",
                "你这智商看不出来的", "你智商能看出来啥？", "你智商不足", "不带你这么聊天的...", "那你说应该怎么聊天才对？",
                "没啥对不对就是瞎聊", "看到一条新闻\"43岁男友不回家带饭 27岁女友放火点房子涉刑罪\" ， 好逗！！！", "哈哈哈哈~~~"};
        data = new ArrayList<>();
        for (int i = 0; i < msgs.length; i++) {
            MsgData msgData = new MsgData(msgs[i], HelpUtils.getCurrentMillisTime(), i % 2 == 0 ? profileId : R.drawable.hdimg_1
                    , i % 2 == 0 ? TYPE_RECEIVER_MSG : TYPE_SENDER_MSG);
            data.add(i, msgData);
        }

//        btnChatSend.startAnimation(getVisibleAnim(false, chatAdd));
//        chatAdd.setVisibility(View.GONE);

        chatRecycler.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        adapter = new ChatAdapter(this, data);
        chatRecycler.setAdapter(adapter);
    }

    private String getRandomMessage() {
        String[] randomMsgs = {"我是机器人", "你发再多我主人也看不到", "不要回了", "你好无聊啊", "其实你发的没点用，我会全部把它过滤掉"
                , "因为我是机器人", "不管你信不信，反正我是不信", "再发我就神经错乱了", "我无法正常跟你沟通", "你说的我懂，我就是不做", "哈哈哈~~~"
                , "嘻嘻", "呵呵", "你可以走了", "我没逻辑的不要奢求多了", "我就呵呵了", "什么鬼", "我不懂", "啥意思？", "好了，你可以退下了", "本机器宝宝累了"};
        Random random = new Random();
        int pos = random.nextInt(randomMsgs.length);
        if (pos >= 0 && pos < randomMsgs.length - 1)
            return randomMsgs[pos];
        else
            return randomMsgs[0];
    }

    /**
     * @param show
     * @param view
     * @return
     *一个用于让控件显示的动画
     */
    private Animation getVisibleAnim(boolean show, View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int y = view.getMeasuredHeight() / 4;
        int x = view.getMeasuredWidth() / 4;
        if (show) {
            ScaleAnimation showAnim = new ScaleAnimation(0.01f, 1f, 0.01f, 1f, x, y);
            showAnim.setDuration(200);
            return showAnim;

        } else {

            ScaleAnimation hiddenAnim = new ScaleAnimation(1f, 0.01f, 1f, 0.01f, x, y);
            hiddenAnim.setDuration(200);
            return hiddenAnim;
        }
    }


}
