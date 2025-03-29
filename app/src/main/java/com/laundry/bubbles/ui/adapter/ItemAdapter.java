package com.laundry.bubbles.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laundry.bubbles.GlobalState;
import com.laundry.bubbles.ModelClass.CurrencyDTO;
import com.laundry.bubbles.ModelClass.ItemBookingDTO;
import com.laundry.bubbles.ModelClass.ItemServiceDTO;
import com.laundry.bubbles.R;
import com.laundry.bubbles.databinding.AdapterItemBinding;
import com.laundry.bubbles.ui.activity.Schedule_Activity;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    String TAG = ItemAdapter.class.getSimpleName();
    LayoutInflater layoutInflater;
    AdapterItemBinding binding;
    Context kContext;
    ArrayList<ItemServiceDTO> servicesDTOArrayList;
    int i = -1,j=0;
        Schedule_Activity schedule_activity;
        CurrencyDTO currencyDTO;
        GlobalState globalState;
    public ItemAdapter(Context kContext, ArrayList<ItemServiceDTO> servicesDTOArrayList,Schedule_Activity schedule_activity,CurrencyDTO currencyDTO) {
        this.kContext = kContext;
        this.currencyDTO = currencyDTO;
        this.servicesDTOArrayList = servicesDTOArrayList;
        this.schedule_activity = schedule_activity;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_item, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {


        holder.binding.ctvItemName.setText(servicesDTOArrayList.get(position).getItem_name());
        holder.binding.ctvDeliverenum.setText(servicesDTOArrayList.get(position).getCount());
        Glide.with(kContext)
                .load(servicesDTOArrayList.get(position).getImage())
                .error(R.drawable.shirt)
                .into(holder.binding.ivItem);

        holder.binding.ctvPrice.setText(currencyDTO.getCurrency_symbol()+" "+servicesDTOArrayList.get(position).getPrice());

        holder.binding.ivPLus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                j= Integer.parseInt(holder.binding.ctvDeliverenum.getText().toString().trim());
                j=j+1;

            holder.binding.ctvDeliverenum.setText(String.valueOf(j));
            servicesDTOArrayList.get(position).setCount(String.valueOf(j));

                schedule_activity.addData(servicesDTOArrayList.get(position));


            }
        });

        holder.binding.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                j= Integer.parseInt(holder.binding.ctvDeliverenum.getText().toString().trim());
                if(j==0){}else {j=j-1;

                    holder.binding.ctvDeliverenum.setText(String.valueOf(j));
                    servicesDTOArrayList.get(position).setCount(String.valueOf(j));

                    schedule_activity.subData(servicesDTOArrayList.get(position));}



            }
        });
    }

    @Override
    public int getItemCount() {
        return servicesDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterItemBinding binding;

        public MyViewHolder(@NonNull AdapterItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }



}
