package com.laundry.bubbles.ui.adapter;
/**
 * Created by VARUN on 01/01/19.
 */
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.laundry.bubbles.ModelClass.ChatListDTO;
import com.laundry.bubbles.R;
import com.laundry.bubbles.interfaces.Consts;
import com.laundry.bubbles.ui.activity.OneTwoOneChat;
import com.laundry.bubbles.utils.CustomTextView;
import com.laundry.bubbles.utils.CustomTextViewBold;
import com.laundry.bubbles.utils.ProjectUtils;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<ChatListDTO> chatList;


    public ChatListAdapter(Context mContext, ArrayList<ChatListDTO> chatList) {
        this.mContext = mContext;
        this.chatList = chatList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_chat_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tvTitle.setText(chatList.get(position).getUser_name());
        holder.tvMsg.setText(chatList.get(position).getMessage());
        try{
            holder.tvDay.setText(ProjectUtils.getDisplayableDay(ProjectUtils.correctTimestamp(Long.parseLong(chatList.get(position).getUpdated_at()))));
            holder.tvDate.setText(ProjectUtils.convertTimestampToTime(ProjectUtils.correctTimestamp(Long.parseLong(chatList.get(position).getUpdated_at()))));

        }catch (Exception e){
            e.printStackTrace();
        }


        Glide.with(mContext).
                load(chatList.get(position).getUser_image())
                .placeholder(R.drawable.ic_user_dummy)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.IVprofile);
        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, OneTwoOneChat.class);
                in.putExtra(Consts.TO_USER_ID, chatList.get(position).getUser_id());
                in.putExtra(Consts.NAME, chatList.get(position).getUser_name());
                in.putExtra(Consts.IMAGE, chatList.get(position).getUser_image());
                mContext.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {

        return chatList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CustomTextViewBold tvTitle;
        public CustomTextView tvDay, tvDate, tvMsg;
        public CircleImageView IVprofile;
        public CardView cardClick;

        public MyViewHolder(View view) {
            super(view);

            cardClick = view.findViewById(R.id.cardClick);
            IVprofile = view.findViewById(R.id.IVprofile);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvDay = view.findViewById(R.id.tvDay);
            tvDate = view.findViewById(R.id.tvDate);
            tvMsg = view.findViewById(R.id.tvMsg);

        }
    }


}
