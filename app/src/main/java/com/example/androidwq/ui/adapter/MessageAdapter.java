package com.example.androidwq.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.androidwq.R;
import com.example.androidwq.ui.data.ContactShowInfo;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<ContactShowInfo> contactInfos;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public MessageAdapter(List<ContactShowInfo> contactInfos){
        this.contactInfos = contactInfos;
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_recycler_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MessageAdapter.ViewHolder viewHolder, final int i) {
        ContactShowInfo contactInfo = contactInfos.get(i);
        final MotionEvent[] e = new MotionEvent[1];


        viewHolder.layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        e[0] = event;
                        break;
                }
                return false;
            }
        });

        //为item添加点击事件
        if(onItemClickListener!=null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(i);
                }
            });
        }
        if(onItemLongClickListener!=null){
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(viewHolder.itemView, e[0],i);
                    return false;
                }
            });
        }

        //初始化item控件
        viewHolder.headImg.setImageResource(contactInfo.getHeadImage());
        viewHolder.lastMsgTime.setText(contactInfo.getLastMsgTime());
        viewHolder.username.setText(contactInfo.getUsername());
        viewHolder.lastMsg.setText(contactInfo.getLastMsg());
        if (contactInfo.isMute())
            viewHolder.mute.setVisibility(View.VISIBLE);
        else
            viewHolder.mute.setVisibility(View.GONE);

        if (contactInfo.isRead()) {
            viewHolder.badge.setVisibility(View.GONE);
        } else {
            viewHolder.badge.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return contactInfos.size();
    }

    public void setOnClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener{
        void onClick( int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View childView, MotionEvent event, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout layout;
        private TextView username, lastMsg, lastMsgTime, badge;
        private ImageView headImg, mute;
        private MotionEvent e;

        public ViewHolder(View v){
            super(v);
            username = v.findViewById(R.id.item_wechat_main_tv_username);
            lastMsg = v.findViewById(R.id.item_wechat_main_tv_lastmsg);
            lastMsgTime = v.findViewById(R.id.item_wechat_main_tv_time);
            headImg = v.findViewById(R.id.item_wechat_main_iv_headimg);
            mute = v.findViewById(R.id.item_wechat_main_iv_mute);
            badge = v.findViewById(R.id.item_wechat_main_iv_unread);
            layout = v.findViewById(R.id.item_layout);
        }
    }
}
