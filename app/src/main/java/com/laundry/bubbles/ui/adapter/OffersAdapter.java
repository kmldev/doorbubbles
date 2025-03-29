package com.laundry.bubbles.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.laundry.bubbles.ModelClass.OfferDTO;
import com.laundry.bubbles.ModelClass.SpecialOfferPkgDTO;
import com.laundry.bubbles.R;
import com.laundry.bubbles.databinding.OffersBinding;
import com.laundry.bubbles.databinding.SpecialOffersBinding;

import java.util.ArrayList;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyViewHolder> {

    LayoutInflater layoutInflater;
    SpecialOffersBinding binding;
    Context kContext;
    ArrayList<OfferDTO> specialOfferPkgDTOArrayList;

    public OffersAdapter(Context kContext, ArrayList<OfferDTO> specialOfferPkgDTOArrayList) {
        this.kContext = kContext;
        this.specialOfferPkgDTOArrayList = specialOfferPkgDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.special_offers, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.binding.offer.setText(specialOfferPkgDTOArrayList.get(position).getOffer_name());
    }

    @Override
    public int getItemCount() {
        return specialOfferPkgDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        SpecialOffersBinding binding;

        public MyViewHolder(@NonNull SpecialOffersBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
