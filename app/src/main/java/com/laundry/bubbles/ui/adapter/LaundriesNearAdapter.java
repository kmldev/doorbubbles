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
import com.laundry.bubbles.ModelClass.NearBYDTO;
import com.laundry.bubbles.R;
import com.laundry.bubbles.databinding.AdapterNearBinding;
import com.laundry.bubbles.databinding.AdapterPopularLaundriesBinding;
import com.laundry.bubbles.interfaces.Consts;
import com.laundry.bubbles.ui.activity.ServiceAcitivity;

import java.util.ArrayList;

public class LaundriesNearAdapter extends RecyclerView.Adapter<LaundriesNearAdapter.MyViewHolder> {

    LayoutInflater layoutInflater;
    AdapterNearBinding binding;
    Context kContext;
    ArrayList<NearBYDTO> nearBYDTOArrayList;

    public LaundriesNearAdapter(Context kContext, ArrayList<NearBYDTO> nearBYDTOArrayList) {
        this.kContext = kContext;
        this.nearBYDTOArrayList = nearBYDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_near, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        Glide.with(kContext)
                .load(nearBYDTOArrayList.get(position).getImage())
                .placeholder(R.drawable.drawable_tag)
                .into(holder.binding.image);


        binding.tvTitle.setText(nearBYDTOArrayList.get(position).getShop_name());
        binding.longDescription.setText(nearBYDTOArrayList.get(position).getDescription());

        holder.binding.cvCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(kContext, ServiceAcitivity.class);
                in.putExtra(Consts.NEARSHOPDTO,nearBYDTOArrayList.get(position));
                kContext.startActivity(in);
            }
        });

    }

    @Override
    public int getItemCount() {
        return nearBYDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterNearBinding binding;

        public MyViewHolder(@NonNull AdapterNearBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
