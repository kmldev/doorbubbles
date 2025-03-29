package com.laundry.bubbles.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.laundry.bubbles.ModelClass.OfferDTO;
import com.laundry.bubbles.R;
import com.laundry.bubbles.databinding.OffersBinding;

import java.util.ArrayList;

public class OffersOtherAdapter extends RecyclerView.Adapter<OffersOtherAdapter.MyViewHolder> {

    LayoutInflater layoutInflater;
    OffersBinding binding;
    Context kContext;
    ArrayList<OfferDTO> specialOfferPkgDTOArrayList;

    public OffersOtherAdapter(Context kContext, ArrayList<OfferDTO> specialOfferPkgDTOArrayList) {
        this.kContext = kContext;
        this.specialOfferPkgDTOArrayList = specialOfferPkgDTOArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.offers, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


        holder.binding.offer.setText(specialOfferPkgDTOArrayList.get(position).getDescription());
        holder.binding.ctvCode.setText(specialOfferPkgDTOArrayList.get(position).getPromocode());
        holder.binding.ctvOffer.setText(specialOfferPkgDTOArrayList.get(position).getDetail()+" "+specialOfferPkgDTOArrayList.get(position).getEnd_date());
        Glide.with(kContext)
                .load(specialOfferPkgDTOArrayList.get(position).getImage())
                .error(R.drawable.offernewpa)
                .into(holder.binding.acivOfferImage);


        holder.binding.titleoffer.setText(specialOfferPkgDTOArrayList.get(position).getOffer_name());
        holder.binding.ctvCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClipboard(kContext,specialOfferPkgDTOArrayList.get(position).getPromocode());
            }
        });

    }

    @Override
    public int getItemCount() {
        return specialOfferPkgDTOArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        OffersBinding binding;

        public MyViewHolder(@NonNull OffersBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }


    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Code", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, R.string.copied, Toast.LENGTH_SHORT).show();

        }
    }
}
