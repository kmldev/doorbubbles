package com.laundry.bubbles.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.laundry.bubbles.ModelClass.TicketCommentDTO;
import com.laundry.bubbles.ModelClass.UserDTO;
import com.laundry.bubbles.R;
import com.laundry.bubbles.utils.CustomTextView;
import com.laundry.bubbles.utils.ProjectUtils;

import java.util.ArrayList;

/**
 * Created by VARUN on 01/01/19.
 */

public class AdapterViewCommentTicket extends BaseAdapter {
    private Context mContext;
    private ArrayList<TicketCommentDTO> ticketCommentDTOSList;
    private UserDTO userDTO;

    public AdapterViewCommentTicket(Context mContext, ArrayList<TicketCommentDTO> ticketCommentDTOSList, UserDTO userDTO) {
        this.mContext = mContext;
        this.ticketCommentDTOSList = ticketCommentDTOSList;
        this.userDTO = userDTO;

    }

    @Override
    public int getCount() {
        return ticketCommentDTOSList.size();
    }

    @Override
    public Object getItem(int postion) {
        return ticketCommentDTOSList.get(postion);
    }

    @Override
    public long getItemId(int postion) {
        return postion;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        //ViewHolder holder = new ViewHolder();
        if (ticketCommentDTOSList.get(position).getIs_admin().equalsIgnoreCase("1")) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_view_comment, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_view_comment_my, parent, false);


        }

        CustomTextView textViewMessage = (CustomTextView) view.findViewById(R.id.textViewMessage);
        CustomTextView textViewTime = (CustomTextView) view.findViewById(R.id.textViewTime);
        CustomTextView tvName = (CustomTextView) view.findViewById(R.id.tvName);

        ImageView ivView = (ImageView) view.findViewById(R.id.ivView);
        textViewMessage.setText(ticketCommentDTOSList.get(position).getMessage());
//        tvName.setText(ticketCommentDTOSList.get(position).get());

        try{
            Glide.with(mContext)
                    .load(R.color.white)
                    .into(ivView);
            textViewTime.setText(ProjectUtils.convertTimestampToTime(ProjectUtils.correctTimestamp(Long.parseLong(ticketCommentDTOSList.get(position).getCreated_at()))));

        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

}
