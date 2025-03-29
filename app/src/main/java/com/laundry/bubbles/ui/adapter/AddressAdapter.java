package com.laundry.bubbles.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laundry.bubbles.ModelClass.AddressDTO;
import com.laundry.bubbles.R;
import com.laundry.bubbles.databinding.AdapterAddressBinding;
import com.laundry.bubbles.databinding.AdapterPopularLaundriesBinding;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.MyViewHolder> {

    LayoutInflater layoutInflater;
    AdapterAddressBinding binding;
    Context kContext;
    ArrayList<AddressDTO> popLaundryDTOArrayList;

    public AddressAdapter(Context kContext, ArrayList<AddressDTO> popLaundryDTOArrayList) {
        this.kContext = kContext;
        this.popLaundryDTOArrayList = popLaundryDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.adapter_address, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.binding.address.setText(popLaundryDTOArrayList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return popLaundryDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdapterAddressBinding binding;

        public MyViewHolder(@NonNull AdapterAddressBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
