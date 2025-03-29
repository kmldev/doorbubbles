package com.laundry.bubbles.ui.adapter;

/**
 * Created by VARUN on 01/01/19.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.laundry.bubbles.ModelClass.TicketDTO;
import com.laundry.bubbles.ModelClass.UserDTO;
import com.laundry.bubbles.R;
import com.laundry.bubbles.interfaces.Consts;
import com.laundry.bubbles.ui.activity.CommentOneByOne;
import com.laundry.bubbles.ui.activity.TicketsActivity;
import com.laundry.bubbles.utils.CustomTextView;
import com.laundry.bubbles.utils.CustomTextViewBold;
import com.laundry.bubbles.utils.ProjectUtils;

import java.util.ArrayList;


public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    private Context mContext;
    private TicketsActivity tickets;
    private ArrayList<TicketDTO> ticketDTOSList;
    private UserDTO userDTO;


    public TicketAdapter(TicketsActivity tickets, Context mContext, ArrayList<TicketDTO> ticketDTOSList, UserDTO userDTO) {
        this.tickets = tickets;
        this.mContext = mContext;
        this.ticketDTOSList = ticketDTOSList;
        this.userDTO = userDTO;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_ticket, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {



        holder.tvTicket.setText(ticketDTOSList.get(position).getTitle());
        holder.ctvDiscription.setText(ticketDTOSList.get(position).getDescription());

        try {

            holder.tvDate.setText(ProjectUtils.convertTimestampDateToTime(Long.parseLong(ticketDTOSList.get(position).getCreated_at())));

        }catch (Exception e){
            e.printStackTrace();
        }

        if (ticketDTOSList.get(position).getStatus().equalsIgnoreCase("0")) {
            holder.tvStatus.setText(mContext.getResources().getString(R.string.pending));
            holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_round_corner_orange));
        } else if (ticketDTOSList.get(position).getStatus().equalsIgnoreCase("1")) {
            holder.tvStatus.setText(mContext.getResources().getString(R.string.inprogress));
            holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_round_corner_yello));
        } else if (ticketDTOSList.get(position).getStatus().equalsIgnoreCase("2")) {
            holder.tvStatus.setText(mContext.getResources().getString(R.string.solve));
            holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_round_corner_green));
        } else if (ticketDTOSList.get(position).getStatus().equalsIgnoreCase("3")) {
            holder.tvStatus.setText(mContext.getResources().getString(R.string.rejected));
            holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_round_corner_red));
        }

        holder.rlClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ticketDTOSList.get(position).getStatus().equalsIgnoreCase("2")) {
                  } else{ Intent in = new Intent(mContext, CommentOneByOne.class);
                    in.putExtra(Consts.TICKET_ID, ticketDTOSList.get(position).getTiket_id());
                    mContext.startActivity(in);}

            }
        });


    }

    @Override
    public int getItemCount() {

        return ticketDTOSList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CustomTextViewBold tvTicket;
        public CustomTextView tvDate,ctvDiscription, tvStatus;
        public LinearLayout llStatus;
        public RelativeLayout rlClick;

        public MyViewHolder(View view) {
            super(view);
            llStatus = view.findViewById(R.id.llStatus);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvTicket = view.findViewById(R.id.tvTicketHeading);
            tvDate = view.findViewById(R.id.tvDate);
            rlClick = view.findViewById(R.id.rlClick);
            ctvDiscription = view.findViewById(R.id.ctvDiscription);


        }
    }


}
