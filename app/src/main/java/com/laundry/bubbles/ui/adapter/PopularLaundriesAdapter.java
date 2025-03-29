package com.laundry.bubbles.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laundry.bubbles.ModelClass.PopLaundryDTO;
import com.laundry.bubbles.R;
import com.laundry.bubbles.databinding.AdapterPopularLaundriesBinding;
import com.laundry.bubbles.databinding.AdapterServicesBinding;
import com.laundry.bubbles.interfaces.Consts;
import com.laundry.bubbles.ui.activity.Schedule_Activity;
import com.laundry.bubbles.ui.activity.ServiceAcitivity;

import java.util.ArrayList;

public class PopularLaundriesAdapter extends RecyclerView.Adapter<PopularLaundriesAdapter.MyViewHolder> {

    LayoutInflater layoutInflater;
    AdapterPopularLaundriesBinding binding;
    Context kContext;
    ArrayList<PopLaundryDTO> popLaundryDTOArrayList;

    public PopularLaundriesAdapter(Context kContext, ArrayList<PopLaundryDTO> popLaundryDTOArrayList) {
        this.kContext = kContext;
        this.popLaundryDTOArrayList = popLaundryDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_popular_laundries, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        Glide.with(kContext).load(popLaundryDTOArrayList.get(position).getImage()).placeholder(R.drawable.laundryshop).into(holder.binding.laundrImage);

        holder.binding.laundryName.setText(popLaundryDTOArrayList.get(position).getShop_name());
        holder.binding.address.setText(popLaundryDTOArrayList.get(position).getAddress());

        holder.binding.cvCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(kContext, ServiceAcitivity.class);
                in.putExtra(Consts.SHOPDTO,popLaundryDTOArrayList.get(position));
                kContext.startActivity(in);
            }
        });
    }

    @Override
    public int getItemCount() {
        return popLaundryDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterPopularLaundriesBinding binding;

        public MyViewHolder(@NonNull AdapterPopularLaundriesBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
