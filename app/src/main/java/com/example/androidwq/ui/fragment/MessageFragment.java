package com.example.androidwq.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.androidwq.R;
import com.example.androidwq.ui.activity.ChatActivity;
import com.example.androidwq.ui.adapter.MessageAdapter;
import com.example.androidwq.ui.adapter.SimpleMenuAdapter;
import com.example.androidwq.ui.data.ContactShowInfo;
import com.example.androidwq.ui.component.PopupMenuWindows;
import com.example.androidwq.utils.HelpUtils;
import com.example.androidwq.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MessageFragment extends Fragment {

    public static final int TYPE_USER = 0x11;
    public static final int TYPE_SERVICE = 0X12;
    public static final int TYPE_SUBSCRIBE = 0x13;
    private RecyclerView messageRecyclerView;
    private MessageAdapter messageAdapter;
    private ImageView messageSearch,messageMore;
    private Toolbar messageToolbar;
    private Handler mHandler = new Handler();
    private List<String> list;

    private int toolbarHeight, statusBarHeight;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_message, null);

        initView(root);
        initData();

        return root;
    }

    private void initView(View root){
        messageSearch = root.findViewById(R.id.message_search);//+
        messageMore = root.findViewById(R.id.message_more);//搜索
        messageToolbar = root.findViewById(R.id.message_toolbar);
        messageRecyclerView = root.findViewById(R.id.recycler_message);

        messageToolbar.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        toolbarHeight = messageToolbar.getMeasuredHeight();
        statusBarHeight = ScreenUtils.getStatusBarHeight(getContext());
    }

    private void initData(){
        HelpUtils.transparentNav(getActivity());

        //假数据
        final int[] headImgRes = {R.drawable.hdimg_3, R.drawable.group1, R.drawable.hdimg_2, R.drawable.user_2,
                R.drawable.user_3, R.drawable.user_4, R.drawable.user_5, R.drawable.hdimg_4,
                R.drawable.hdimg_5, R.drawable.hdimg_6};

        final String[] usernames = {"Fiona", "  ...   ", "冯小", "深圳社保", "服务通知", "招商银行信用卡",
                "箫景、Fiona", "吴晓晓", "肖箫", "唐小晓"};
        //最新消息
        String[] lastMsgs = {"我看看", "吴晓晓：无人超市啊", "最近在忙些什么", "八月一号猛料，内地社保在这2...",
                "微信支付凭证", "#今日签到#你能到大的，比想象...", "箫景:准备去哪嗨", "[Video Call]", "什么东西？", "[微信红包]"};

        String[] lastMsgTimes = {"17:40", "10:56", "7月26日", "昨天", "7月27日", "09:46",
                "7月18日", "星期一", "7月26日", "4月23日"};
        int[] types = {TYPE_USER, TYPE_USER, TYPE_USER, TYPE_SUBSCRIBE, TYPE_SERVICE, TYPE_SUBSCRIBE,
                TYPE_USER, TYPE_USER, TYPE_USER, TYPE_USER};
        //静音&已读
        boolean[] isMutes = {false, true, false, false, false, false, true, false, false, false};
        boolean[] isReads = {true, true, true, true, true, true, true, true, true, true};
        final List<ContactShowInfo> infos = new LinkedList<>();

        for (int i = 0; i < headImgRes.length; i++) {
            infos.add(i, new ContactShowInfo(headImgRes[i], usernames[i], lastMsgs[i], lastMsgTimes[i], isMutes[i], isReads[i], types[i]));
        }

        //设置Adapter
        messageAdapter = new MessageAdapter(infos);
        //设置点击事件
        messageAdapter.setOnClickListener(new MessageAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getContext(), ChatActivity.class);//跳到聊天界面
                intent.putExtra("name", usernames[position]);
                intent.putExtra("profileId", headImgRes[position]);
                startActivity(intent);
            }
        });
        //设置长按事件
        messageAdapter.setOnItemLongClickListener(new MessageAdapter.OnItemLongClickListener() {
            int preX, preY;
            boolean isSlip = false, isLongClick = false;
            @Override
            public void onItemLongClick(View childView, MotionEvent event, int position) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                initPopupMenu(childView, x, y, messageAdapter, position, infos);
            }
        });
        messageRecyclerView.setAdapter(messageAdapter);
        //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext() );
        messageRecyclerView.setLayoutManager(layoutManager);
        //设置分割线
        messageRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }


    /**
     * 设置已读还是未读
     *
     * @param isRead   true已读，false未读
     * @param position item position
     * @param adapter  数据源
     * @param datas
     */
    private void setIsRead(boolean isRead, int position, MessageAdapter adapter, List<ContactShowInfo> datas) {
        ContactShowInfo info = datas.get(position);
        info.setRead(isRead);
        adapter.notifyDataSetChanged();
    }

    /**
     * 删除指定位置item
     *
     * @param position 指定删除position
     * @param adapter  数据源
     * @param datas
     */
    private void deleteMsg(int position, MessageAdapter adapter, List<ContactShowInfo> datas) {
        datas.remove(position);
        adapter.notifyDataSetChanged();
    }

    /**
     * 初始化popup菜单
     */
    private void initPopupMenu(View anchorView, int posX, int posY, final MessageAdapter adapter, final int itemPos, final List<ContactShowInfo> data) {
        list = new ArrayList<>();
        ContactShowInfo showInfo = data.get(itemPos);
        //初始化弹出菜单项
        switch (showInfo.getAccountType()) {
            case TYPE_SERVICE:
                list.clear();
                if (showInfo.isRead())
                    list.add("标为未读");
                else
                    list.add("标为已读");
                list.add("删除该聊天");
                break;

            case TYPE_SUBSCRIBE:
                list.clear();
                if (showInfo.isRead())
                    list.add("标为未读");
                else
                    list.add("标为已读");
                list.add("置顶公众号");
                list.add("取消关注");
                list.add("删除该聊天");
                break;

            case TYPE_USER:
                list.clear();
                if (showInfo.isRead())
                    list.add("标为未读");
                else
                    list.add("标为已读");
                list.add("置顶聊天");
                list.add("删除该聊天");
                break;
        }
        SimpleMenuAdapter<String> menuAdapter = new SimpleMenuAdapter<>(getContext(), R.layout.item_menu, list);
        final PopupMenuWindows ppm = new PopupMenuWindows(getContext(), R.layout.popup_menu_general_layout, menuAdapter);
        int[] posArr = ppm.reckonPopWindowShowPos(posX, posY);
        ppm.setAutoFitStyle(true);
        ppm.setOnMenuItemClickListener(new PopupMenuWindows.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClickListener(AdapterView<?> parent, View view, int position, long id) {
                switch (list.get(position)) {
                    case "标为未读":
                        setIsRead(false, itemPos, adapter, data);
                        break;

                    case "标为已读":
                        setIsRead(true, itemPos, adapter, data);
                        break;

                    case "置顶聊天":
                    case "置顶公众号":
                        stickyTop(adapter, data, itemPos);
                        break;

                    case "取消关注":
                    case "删除该聊天":
                        deleteMsg(itemPos, adapter, data);
                        break;
                }
                ppm.dismiss();
            }
        });
        ppm.showAtLocation(anchorView, Gravity.NO_GRAVITY, posArr[0], posArr[1]);
    }


    /**
     * 置顶item
     *
     * @param adapter
     * @param datas
     */
    private void stickyTop(MessageAdapter adapter, List<ContactShowInfo> datas, int position) {
        if (position > 0) {
            ContactShowInfo stickyTopItem = datas.get(position);
            datas.remove(position);
            datas.add(0, stickyTopItem);
        } else {
            return;
        }
        adapter.notifyDataSetChanged();
    }

}
